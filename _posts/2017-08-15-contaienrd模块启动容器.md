---
layout:     post
title:      "containerd启动容器过程分析"
subtitle:   "container模块启动容器"
date:       2017-08-15 11:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码
---
## contaienrd模块启动容器

在docker源码的学习中，我们曾经学到过docker与containerd模块的通信模式及containerd模块接受到数据后的处理流程，所以containerd如何实现对请求数据的路由这里不再重复描述，只从supervisor中的handleTask进入启动容器的路由函数start。contaienrd只负责管理容器的生命周期，不关心容器的镜像数据，所以containerd的管理对象只是运行中的容器，停止的容器containerd并不关心，所以只要containerd创建一个容器就相当于启动一个容器。
![ContaienrStart](/img/ContaienrStart.png)
### containerd启动容器：

{%highlight go %}
func (s *Supervisor) start(t *StartTask) error {
	start := time.Now()
	rt := s.runtime
	rtArgs := s.runtimeArgs
	if t.Runtime != "" {
		rt = t.Runtime
		rtArgs = t.RuntimeArgs
	}
	container, err := runtime.New(runtime.ContainerOpts{
		Root:        s.stateDir,
		ID:          t.ID,
		Bundle:      t.BundlePath,
		Runtime:     rt,
		RuntimeArgs: rtArgs,
		Shim:        s.shim,
		Labels:      t.Labels,
		NoPivotRoot: t.NoPivotRoot,
		Timeout:     s.timeout,
	})//配置一个容器启动的运行环境对象
	if err != nil {
		return err
	}
	s.containers[t.ID] = &containerInfo{
		container: container,
	}//添加到管理器中的运行中容器数组
	ContainersCounter.Inc(1)
	task := &startTask{
		Err:           t.ErrorCh(),
		Container:     container,
		StartResponse: t.StartResponse,
		Stdin:         t.Stdin,
		Stdout:        t.Stdout,
		Stderr:        t.Stderr,
		Ctx:           t.Ctx,
	}//配置容器启动任务对象
	if t.Checkpoint != nil {
		task.CheckpointPath = filepath.Join(t.CheckpointDir, t.Checkpoint.Name)
	}//配置容器启动过程中的检查点文件

	s.startTasks <- task//向超级管理器中startTask 通道发送任务
	ContainerCreateTimer.UpdateSince(start)
	return errDeferredResponse
}

{%endhighlight%}

### 在contaienrd的daemon启动过程中实现对通道的监听

{%highlight go %}
// Start runs a loop in charge of starting new containers
func (w *worker) Start() {
	defer w.wg.Done()
	for t := range w.s.startTasks {
		started := time.Now()
		process, err := t.Container.Start(t.Ctx, t.CheckpointPath, runtime.NewStdio(t.Stdin, t.Stdout, t.Stderr))//启动容器进程
		if err != nil {
			logrus.WithFields(logrus.Fields{
				"error": err,
				"id":    t.Container.ID(),
			}).Error("containerd: start container")
			t.Err <- err
			evt := &DeleteTask{
				ID:      t.Container.ID(),
				NoEvent: true,
				Process: process,
			}
			w.s.SendTask(evt)
			continue
		}
		if err := w.s.monitor.MonitorOOM(t.Container); err != nil && err != runtime.ErrContainerExited {
		//添加一个容器对象到OOM的监控列表中去
			if process.State() != runtime.Stopped {
				logrus.WithField("error", err).Error("containerd: notify OOM events")
			}
		}
		if err := w.s.monitorProcess(process); err != nil {
			logrus.WithField("error", err).Error("containerd: add process to monitor")
			t.Err <- err
			evt := &DeleteTask{
				ID:      t.Container.ID(),
				NoEvent: true,
				Process: process,
			}
			w.s.SendTask(evt)
			continue
		}
		// only call process start if we aren't restoring from a checkpoint
		// if we have restored from a checkpoint then the process is already started
		//如果没有检查点文件，则直接启动进程process.Start()
		if t.CheckpointPath == "" {
			if err := process.Start(); err != nil {
				logrus.WithField("error", err).Error("containerd: start init process")
				t.Err <- err
				evt := &DeleteTask{
					ID:      t.Container.ID(),
					NoEvent: true,
					Process: process,
				}
				w.s.SendTask(evt)
				continue
			}
		}
		ContainerStartTimer.UpdateSince(started)
		w.s.newExecSyncMap(t.Container.ID())
		t.Err <- nil
		t.StartResponse <- StartResponse{
			Container: t.Container,
		}
		w.s.notifySubscribers(Event{
			Timestamp: time.Now(),
			ID:        t.Container.ID(),
			Type:      StateStart,
		})
	}
}
{%endhighlight%}

### 新建命令对象cmd和进程对象process来执行contaienrd-shim启动容器
{%highlight go %}

func (c *container) Start(ctx context.Context, checkpointPath string, s Stdio) (Process, error) {
	processRoot := filepath.Join(c.root, c.id, InitProcessID)//容器根目录init
	if err := os.Mkdir(processRoot, 0755); err != nil {
		return nil, err
	}//创建该容器init根目录
	cmd := exec.Command(c.shim,
		c.id, c.bundle, c.runtime,
	)//新建命令对象docker-containerd-shim  container_id bundle（/var/run/docker/libcontaienr）,binary,shim就是containerd模块向下对接runc的接口标准，containerd向上对deamon提供标准的grpc接口,该命令的执行我们将在后一章节中具体描述。
	cmd.Dir = processRoot//指定命令执行的工作目录
	cmd.SysProcAttr = &syscall.SysProcAttr{
		Setpgid: true,
	}
	spec, err := c.readSpec()
	if err != nil {
		return nil, err
	}
	config := &processConfig{
		checkpoint:  checkpointPath,
		root:        processRoot,
		id:          InitProcessID,
		c:           c,
		stdio:       s,
		spec:        spec,
		processSpec: specs.ProcessSpec(spec.Process),
	}//构建进程配置文件
	p, err := newProcess(config)//根据配置文件新建进程对象
	if err != nil {
		return nil, err
	}
	if err := c.createCmd(ctx, InitProcessID, cmd, p); err != nil {
	//根据进程对象来执行命令并启动容器
		return nil, err
	}
	return p, nil
}
{%endhighlight%}

### 根据配置文件新建进程对象
{%highlight go %}
func newProcess(config *processConfig) (*process, error) {
	p := &process{
		root:      config.root,
		id:        config.id,
		container: config.c,
		spec:      config.processSpec,
		stdio:     config.stdio,
		cmdDoneCh: make(chan struct{}),
		state:     Running,
	}//构建进程对象
	uid, gid, err := getRootIDs(config.spec)//获取uid，gid信息
	if err != nil {
		return nil, err
	}
	f, err := os.Create(filepath.Join(config.root, "process.json"))//根据路径创建process.json文件
	if err != nil {
		return nil, err
	}
	defer f.Close()

	ps := ProcessState{
		ProcessSpec: config.processSpec,
		Exec:        config.exec,
		PlatformProcessState: PlatformProcessState{
			Checkpoint: config.checkpoint,
			RootUID:    uid,
			RootGID:    gid,
		},
		Stdin:       config.stdio.Stdin,
		Stdout:      config.stdio.Stdout,
		Stderr:      config.stdio.Stderr,
		RuntimeArgs: config.c.runtimeArgs,
		NoPivotRoot: config.c.noPivotRoot,
	}//构建进程状态文件

	if err := json.NewEncoder(f).Encode(ps); err != nil {
		return nil, err
	}//根据进程状态对象ps编码至process.json文件中去
	exit, err := getExitPipe(filepath.Join(config.root, ExitFile))//根据文件路径创建Mkfifo管道对象，并以O_NONBLOCK,O_RDONLY模式打开管道OpenFile等待写入的数据并读取。
	if err != nil {
		return nil, err
	}
	control, err := getControlPipe(filepath.Join(config.root, ControlFile))//同上，但是以O_NONBLOCK,O_RDWD模式打开管道OpenFile等待写入的数据并读取
	if err != nil {
		return nil, err
	}
	p.exitPipe = exit
	p.controlPipe = control
	return p, nil//返回进程对象
}
{%endhighlight%}


### 执行contaienrd-shim 命令进入容器启动的过程，并创建goroutine监听启动过程中的错误
{%highlight go %}
func (c *container) createCmd(ctx context.Context, pid string, cmd *exec.Cmd, p *process) error {
	p.cmd = cmd
	if err := cmd.Start(); err != nil {
	//执行之前创建的containerd-shim命令
		close(p.cmdDoneCh)
		if exErr, ok := err.(*exec.Error); ok {
			if exErr.Err == exec.ErrNotFound || exErr.Err == os.ErrNotExist {
				return fmt.Errorf("%s not installed on system", c.shim)
			}
		}
		return err
	}
	// We need the pid file to have been written to run
	defer func() {
		go func() {
		//创建goroutine等待命令执行返回
			err := p.cmd.Wait()
			if err == nil {
				p.cmdSuccess = true
			}

			if same, err := p.isSameProcess(); same && p.pid > 0 {
				// The process changed its PR_SET_PDEATHSIG, so force
				// kill it
				logrus.Infof("containerd: %s:%s (pid %v) has become an orphan, killing it", p.container.id, p.id, p.pid)
				err = unix.Kill(p.pid, syscall.SIGKILL)
				if err != nil && err != syscall.ESRCH {
					logrus.Errorf("containerd: unable to SIGKILL %s:%s (pid %v): %v", p.container.id, p.id, p.pid, err)
				} else {
					for {
						err = unix.Kill(p.pid, 0)
						if err != nil {
							break
						}
						time.Sleep(5 * time.Millisecond)
					}
				}
			}
			close(p.cmdDoneCh)
		}()
	}()

	ch := make(chan error)//创建一个传递error的管道
	go func() {
		if err := c.waitForCreate(p, cmd); err != nil {
		//新建一个goroutine对象来循环读取pidfile（pid文件其实就记录了一行表示该进程的pid）等待进程创建结束，并监听读取过程中发生的错误，如果读取失败则开始分析shim-log.json及log.json来获取错误原因并返回
			ch <- err
			return
		}
		c.processes[pid] = p
		ch <- nil
	}()
	select {
	case <-ctx.Done():
		cmd.Process.Kill()
		cmd.Wait()
		<-ch
		return ctx.Err()
	case err := <-ch:
		return err
	}
	return nil
}
{%endhighlight%}