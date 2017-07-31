---
layout:     post
title:      "docker启动容器过程分析"
subtitle:   "docker调用container模块启动容器"
date:       2017-07-31 11:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码
---
## Dokcer的containerd模块启动容器
 我们已经知道容器的启动过程分为四个步骤，今天进入最后一个步骤的分析，即containerd模块的容器启动实现：
 
{% highlight go %}
err := daemon.containerd.Create(container.ID, checkpoint, checkpointDir, *spec, container.InitializeStdio, createOptions...);
{% endhighlight%}
在libcontainerd/client_unix.go中的实现如下：

### 创建启动容器所需的相关目录,启动容器
{%highlight go%}
func (clnt *client) Create(containerID string, checkpoint string, checkpointDir string, spec specs.Spec, attachStdio StdioCallback, options ...CreateOption) (err error) {
	clnt.lock(containerID)
	defer clnt.unlock(containerID)

	if _, err := clnt.getContainer(containerID); err == nil {
		return fmt.Errorf("Container %s is already active", containerID)//获取libcontainerd模块中的containers,这里面的容器应该包括正在运行的容器
	}

	uid, gid, err := getRootIDs(specs.Spec(spec))//获取gid和uid
	if err != nil {
		return err
	}
	dir, err := clnt.prepareBundleDir(uid, gid)//迭代创建statedir所需目录，该目录路径在daemon.start定义libcontainerd模块的时候已经指定
	if err != nil {
		return err
	}

	container := clnt.newContainer(filepath.Join(dir, containerID), options...)//创建一个containerCommon对象
	if err := container.clean(); err != nil {
		return err
	}

	defer func() {
		if err != nil {
			container.clean()
			clnt.deleteContainer(containerID)
		}
	}()

	if err := idtools.MkdirAllAs(container.dir, 0700, uid, gid); err != nil && !os.IsExist(err) {
	//创建容器目录
		return err
	}

	f, err := os.Create(filepath.Join(container.dir, configFilename))//创建配置文件路径，并根据spec创建配置文件
	if err != nil {
		return err
	}
	defer f.Close()
	if err := json.NewEncoder(f).Encode(spec); err != nil {
		return err
	}
//容器启动
	return container.start(checkpoint, checkpointDir, attachStdio)
}
{% endhighlight%}

### 配置containerd创建容器的标准输入输出端及请求数据的设置,并发送请求
{% highlight go %}
func (ctr *container) start(checkpoint string, checkpointDir string, attachStdio StdioCallback) (err error) {
	spec, err := ctr.spec()//根据读取的配置文件信息设置spec对象
	if err != nil {
		return nil
	}

	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	ready := make(chan struct{})

	fifoCtx, cancel := context.WithCancel(context.Background())
	defer func() {
		if err != nil {
			cancel()
		}
	}()

	iopipe, err := ctr.openFifos(fifoCtx, spec.Process.Terminal)//创建一个先进先出的io管道对象，包括可读端和可写端
	if err != nil {
		return err
	}

	var stdinOnce sync.Once

	// we need to delay stdin closure after container start or else "stdin close"
	// event will be rejected by containerd.
	// stdin closure happens in attachStdio
	stdin := iopipe.Stdin
	iopipe.Stdin = ioutils.NewWriteCloserWrapper(stdin, func() error {
		var err error
		stdinOnce.Do(func() { // on error from attach we don't know if stdin was already closed
			err = stdin.Close()
			go func() {
				select {
				case <-ready:
				case <-ctx.Done():
				}
				select {
				case <-ready:
					if err := ctr.sendCloseStdin(); err != nil {
						logrus.Warnf("failed to close stdin: %+v", err)
					}
				default:
				}
			}()
		})
		return err
	})

	r := &containerd.CreateContainerRequest{//定义对containerd模块的请求对象
		Id:            ctr.containerID,
		BundlePath:    ctr.dir,
		Stdin:         ctr.fifo(syscall.Stdin),
		Stdout:        ctr.fifo(syscall.Stdout),
		Stderr:        ctr.fifo(syscall.Stderr),
		Checkpoint:    checkpoint,
		CheckpointDir: checkpointDir,
		// check to see if we are running in ramdisk to disable pivot root
		NoPivotRoot: os.Getenv("DOCKER_RAMDISK") != "",
		Runtime:     ctr.runtime,
		RuntimeArgs: ctr.runtimeArgs,
	}
	ctr.client.appendContainer(ctr)

	if err := attachStdio(*iopipe); err != nil {
		ctr.closeFifos(iopipe)
		return err
	}

	resp, err := ctr.client.remote.apiClient.CreateContainer(context.Background(), r)//向containerd模块发送请求数据，实现容器创建
	if err != nil {
		ctr.closeFifos(iopipe)
		return err
	}
	ctr.systemPid = systemPid(resp.Container)
	close(ready)
       //启动成功后更新daemon中的容器状态
	return ctr.client.backend.StateChanged(ctr.containerID, StateInfo{
		CommonStateInfo: CommonStateInfo{
			State: StateStart,
			Pid:   ctr.systemPid,
		}})
}
{% endhighlight%}

### 下一章进入containerd代码模块，研究containerd模块启动容器的步骤