---
layout:     post
title:      "docker启动容器过程分析"
subtitle:   "docker容器start"
date:       2017-07-26 11:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码
---
## Docker start容器恢复过程分析
  在实验的基础上，我们成功将容器文件系统迁移到目的机，并能够在目的机感知到被迁移容器的存在，但在目的端的恢复过程中，发现还有一些恢复过程中的数据没有迁移过来，所以今天我们开始了解容器恢复docker start的过程，总结恢复过程中所依赖的数据内容。
  同样在客户端到服务器通信这一段内容我不做描述，直接进入服务器端路由分发后的函数实现部分：
  
### 启动参数配置

{% highlight go%}
func (s *containerRouter) postContainersStart(ctx context.Context, w http.ResponseWriter, r *http.Request, vars map[string]string) error {
	// If contentLength is -1, we can assumed chunked encoding
	// or more technically that the length is unknown
	// https://golang.org/src/pkg/net/http/request.go#L139
	// net/http otherwise seems to swallow any headers related to chunked encoding
	// including r.TransferEncoding
	// allow a nil body for backwards compatibility

	version := httputils.VersionFromContext(ctx)
	var hostConfig *container.HostConfig
	// A non-nil json object is at least 7 characters.
	if r.ContentLength > 7 || r.ContentLength == -1 {
		if versions.GreaterThanOrEqualTo(version, "1.24") {
			return validationError{fmt.Errorf("starting container with non-empty request body was deprecated since v1.10 and removed in v1.12")}
		}

		if err := httputils.CheckForJSON(r); err != nil {
			return err
		}

		c, err := s.decoder.DecodeHostConfig(r.Body)
		if err != nil {
			return err
		}
		hostConfig = c
	}

	if err := httputils.ParseForm(r); err != nil {
		return err
	}//同样需要解析请求得到请求配置信息

	checkpoint := r.Form.Get("checkpoint")//获取是否有checkpoint选项
	checkpointDir := r.Form.Get("checkpoint-dir")//获取是否有checkpoint-dir选项
	if err := s.backend.ContainerStart(vars["name"], hostConfig, checkpoint, checkpointDir); err != nil {
	//根据获取到的checkpoint，checkpointDir信息调用启动容器的函数
		return err
	}

	w.WriteHeader(http.StatusNoContent)
	return nil
}

{% endhighlight%}

### 启动条件检测
启动容器函数：判断容器的启动是否达到一些必要条件
{% highlight go%}
// ContainerStart starts a container.
func (daemon *Daemon) ContainerStart(name string, hostConfig *containertypes.HostConfig, checkpoint string, checkpointDir string) error {
	if checkpoint != "" && !daemon.HasExperimental() {
	//判断是否处于experimental模式，只有该模式才支持checkpoint功能
		return apierrors.NewBadRequestError(fmt.Errorf("checkpoint is only supported in experimental mode"))
	}

	container, err := daemon.GetContainer(name)//根据名称获取容器对象，daemon.containers或daemon.idIndex中获取对应容器信息
	if err != nil {
		return err
	}

	if container.IsPaused() {
	//判断该容器是否处于paused状态
		return fmt.Errorf("Cannot start a paused container, try unpause instead.")
	}

	if container.IsRunning() {
	//判断该容器是否处于运行状态
		err := fmt.Errorf("Container already started")
		return apierrors.NewErrorWithStatusCode(err, http.StatusNotModified)
	}

	// Windows does not have the backwards compatibility issue here.
	//Windows平台
	if runtime.GOOS != "windows" {
		// This is kept for backward compatibility - hostconfig should be passed when
		// creating a container, not during start.
		if hostConfig != nil {
			logrus.Warn("DEPRECATED: Setting host configuration options when the container starts is deprecated and has been removed in Docker 1.12")
			oldNetworkMode := container.HostConfig.NetworkMode
			if err := daemon.setSecurityOptions(container, hostConfig); err != nil {
				return err
			}
			if err := daemon.mergeAndVerifyLogConfig(&hostConfig.LogConfig); err != nil {
				return err
			}
			if err := daemon.setHostConfig(container, hostConfig); err != nil {
				return err
			}
			newNetworkMode := container.HostConfig.NetworkMode
			if string(oldNetworkMode) != string(newNetworkMode) {
				// if user has change the network mode on starting, clean up the
				// old networks. It is a deprecated feature and has been removed in Docker 1.12
				container.NetworkSettings.Networks = nil
				if err := container.ToDisk(); err != nil {
					return err
				}
			}
			container.InitDNSHostConfig()
		}
	} else {
		if hostConfig != nil {
			return fmt.Errorf("Supplying a hostconfig on start is not supported. It should be supplied on create")
		}
	}

	// check if hostConfig is in line with the current system settings.
	// It may happen cgroups are umounted or the like.
	//验证配置文件数据
	if _, err = daemon.verifyContainerSettings(container.HostConfig, nil, false); err != nil {
		return err
	}
	// Adapt for old containers in case we have updates in this function and
	// old containers never have chance to call the new function in create stage.
	if hostConfig != nil {
		if err := daemon.adaptContainerSettings(container.HostConfig, false); err != nil {
			return err
		}
	}

	return daemon.containerStart(container, checkpoint, checkpointDir, true)//启动容器
}
{% endhighlight%}

### 以上都是属于启动容器的参数配置等静态监测，从这个步骤开始执行容器启动的具体操作，分为如下几个步骤：
* 1.文件系统挂载
* 2.网络初始化
* 3.createSpec
* 4.containerd.Create

这一个章节只研究第一个步骤：文件系统的挂载

### 启动容器全过程
该函数就是根据设置准备好容器启动所需要的数据，做好启动的准备，然后等待一个信号开始运行
{% highlight go%}
func (daemon *Daemon) containerStart(container *container.Container, checkpoint string, checkpointDir string, resetRestartManager bool) (err error) {
	start := time.Now()
	container.Lock()//给这个容器对象上锁
	defer container.Unlock()

	if resetRestartManager && container.Running { // skip this check if already in restarting step and resetRestartManager==false
		return nil
	}

	if container.RemovalInProgress || container.Dead {
		return fmt.Errorf("Container is marked for removal and cannot be started.")
	}

	// if we encounter an error during start we need to ensure that any other
	// setup has been cleaned up properly
	defer func() {
	//这里是考虑到如果容器启动失败了，所有的设置安装都需要被清除
		if err != nil {
			container.SetError(err)
			// if no one else has set it, make sure we don't leave it at zero
			if container.ExitCode() == 0 {
				container.SetExitCode(128)
			}
			container.ToDisk()

			container.Reset(false)

			daemon.Cleanup(container)
			// if containers AutoRemove flag is set, remove it after clean up
			if container.HostConfig.AutoRemove {
				container.Unlock()
				if err := daemon.ContainerRm(container.ID, &types.ContainerRmConfig{ForceRemove: true, RemoveVolume: true}); err != nil {
					logrus.Errorf("can't remove container %s: %v", container.ID, err)
				}
				container.Lock()
			}
		}
	}()

	if err := daemon.conditionalMountOnStart(container); err != nil {
	//给容器的可读写层挂载容器的基础文件系统
		return err
	}

	if err := daemon.initializeNetworking(container); err != nil {//初始化容器网络
		return err
	}

	spec, err := daemon.createSpec(container)
	if err != nil {
		return err
	}

	createOptions, err := daemon.getLibcontainerdCreateOptions(container)
	if err != nil {
		return err
	}

	if resetRestartManager {
		container.ResetRestartManager(true)
	}

	if checkpointDir == "" {
		checkpointDir = container.CheckpointDir()
	}

	if daemon.saveApparmorConfig(container); err != nil {
		return err
	}

	if err := daemon.containerd.Create(container.ID, checkpoint, checkpointDir, *spec, container.InitializeStdio, createOptions...); err != nil {
		errDesc := grpc.ErrorDesc(err)
		contains := func(s1, s2 string) bool {
			return strings.Contains(strings.ToLower(s1), s2)
		}
		logrus.Errorf("Create container failed with error: %s", errDesc)
		// if we receive an internal error from the initial start of a container then lets
		// return it instead of entering the restart loop
		// set to 127 for container cmd not found/does not exist)
		if contains(errDesc, container.Path) &&
			(contains(errDesc, "executable file not found") ||
				contains(errDesc, "no such file or directory") ||
				contains(errDesc, "system cannot find the file specified")) {
			container.SetExitCode(127)
		}
		// set to 126 for container cmd can't be invoked errors
		if contains(errDesc, syscall.EACCES.Error()) {
			container.SetExitCode(126)
		}

		// attempted to mount a file onto a directory, or a directory onto a file, maybe from user specified bind mounts
		if contains(errDesc, syscall.ENOTDIR.Error()) {
			errDesc += ": Are you trying to mount a directory onto a file (or vice-versa)? Check if the specified host path exists and is the expected type"
			container.SetExitCode(127)
		}

		return fmt.Errorf("%s", errDesc)
	}

	containerActions.WithValues("start").UpdateSince(start)

	return nil
}
{% endhighlight%}


### 第一个步骤：设置容器的基础镜像，容器文件系统的挂载，设置基础文件系统挂载点

#### 挂载文件系统并设置容器基础文件系统挂载点
![mountFS](/img/mountFS.png)

{% highlight go%}
func (daemon *Daemon) Mount(container *container.Container) error {
	dir, err := container.RWLayer.Mount(container.GetMountLabel())//RWLayer为一个接口实现对可读写层的操作接口，该接口中的Mount方法实现在可读写层对容器基础文件系统的挂载
	if err != nil {
		return err
	}
	logrus.Debugf("container mounted via layerStore: %v", dir)
    //默认第一次挂载时container.BaseFS应该为空，所以将container.BaseFS赋值为dir，容器文件系统的挂载点
	if container.BaseFS != dir {
		// The mount path reported by the graph driver should always be trusted on Windows, since the
		// volume path for a given mounted layer may change over time.  This should only be an error
		// on non-Windows operating systems.
		if container.BaseFS != "" && runtime.GOOS != "windows" {
			daemon.Unmount(container)
			return fmt.Errorf("Error: driver %s is returning inconsistent paths for container %s ('%s' then '%s')",
				daemon.GraphDriverName(), container.ID, container.BaseFS, dir)
		}
	}
	container.BaseFS = dir // TODO: combine these fields
	return nil
}
{% endhighlight%}

Mount：根据可读写层对象RWLayer的mountID及挂载标签得到挂载根路径，也就是该容器的整个文件系统的路径
{% highlight go%}
func (rl *referencedRWLayer) Mount(mountLabel string) (string, error) {
	return rl.layerStore.driver.Get(rl.mountedLayer.mountID, mountLabel)//driver为一个驱动接口对象，包含了对镜像层所有相关的操作，Get则是获取指定id和label的镜像层文件系统挂载点的路径
}


{% endhighlight%}

#### 根据是否有父镜像，返回可提供容器整体文件系统视角的挂载路径
{% highlight go%}
//aufs返回指定id的根文件系统路径，如果有父镜像则需要通过挂载得到整个容器的文件系统的视图，需要返回mnt／下的路径，如果没有父镜像，则diff层中的唯一一层镜像可直接提供整个容器文件系统视角，返回diff／下的镜像层路径即可
func (a *Driver) Get(id, mountLabel string) (string, error) {
	a.locker.Lock(id)
	defer a.locker.Unlock(id)
	parents, err := a.getParentLayerPaths(id)//得到父层镜像层
	if err != nil && !os.IsNotExist(err) {
		return "", err
	}

	a.pathCacheLock.Lock()
	m, exists := a.pathCache[id]//查看缓存中是否存在该镜像层id所对应的路径
	a.pathCacheLock.Unlock()

	if !exists {
		m = a.getDiffPath(id)//不存在，则获取该镜像层路径diff/id/路径，因为不存在父镜像，所以这一层镜像其实就相当于容器的整个文件系统，不需要综合提供mnt下的整个容器文件系统的视角
		if len(parents) > 0 {
			m = a.getMountpoint(id)//如果它有父镜像层则获取mnt/id/路径，也就是挂载文件系统，提供一个综合所有镜像层的容器文件系统的视角
		}
	}
	if count := a.ctr.Increment(m); count > 1 {
		return m, nil
	}

	// If a dir does not have a parent ( no layers )do not try to mount
	// just return the diff path to the data
	if len(parents) > 0 {
	//如果存在父镜像，则需要提供一个容器文件系统的整体视角，所以我们需要挂载到mnt目录下，如果不存在父镜像则diff中的可读写层可直接提供整个文件系统视角，不需要挂载，直接在diff中操作
		if err := a.mount(id, m, mountLabel, parents); err != nil {//如果该镜像层有父镜像层则挂载这一层镜像
			return "", err
		}
	}

	a.pathCacheLock.Lock()
	a.pathCache[id] = m
	a.pathCacheLock.Unlock()
	return m, nil
}
{% endhighlight%}
