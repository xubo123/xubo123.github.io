---
layout:     post
title:      "containerd-shim启动容器过程分析"
subtitle:   "container-shim模块启动容器"
date:       2017-08-16 11:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码
---
## containerd-shim启动容器实现
昨天我们分析了contaienrd模块启动容器的过程，在其将一系列配置工作完成后通过调用containerd-shim id /var/run/libcontaienrd/ct_id docker-runc命令交付给containerd-shim子模块来启动容器，所以这一章节将进入containerd-shim模块入口来深入容器的启动过程。

### containerd-shim其实相当于containerd到下层runc的一层夹层，即一层接口来适应容器运行环境的实现

![containerd-shimStartContainer](/img/containerd-shimStartContainer.png)

### containerd-shim主函数入口：
{% highlight go %}
func main() {
	flag.Parse()//解析参数
	cwd, err := os.Getwd()//获取当前目录
	if err != nil {
		panic(err)
	}
	f, err := os.OpenFile(filepath.Join(cwd, "shim-log.json"), os.O_CREATE|os.O_WRONLY|os.O_APPEND|os.O_SYNC, 0666)//打开shim层日志文件
	if err != nil {
		panic(err)
	}
	//启动容器
	if err := start(f); err != nil {
		// this means that the runtime failed starting the container and will have the
		// proper error messages in the runtime log so we should to treat this as a
		// shim failure because the sim executed properly
		if err == errRuntime {
			f.Close()
			return
		}
		// log the error instead of writing to stderr because the shim will have
		// /dev/null as it's stdio because it is supposed to be reparented to system
		// init and will not have anyone to read from it
		writeMessage(f, "error", err)
		f.Close()
		os.Exit(1)
	}
}
{% endhighlight %}

### 配置各类信号通道，并创建进程
创建信号通道，退出信号通道，控制信息通道并监听，然后构建进程对象，并调用docker－runc创建进程
{% highlight go %}
func start(log *os.File) error {
	// start handling signals as soon as possible so that things are properly reaped
	// or if runtime exits before we hit the handler
	signals := make(chan os.Signal, 2048)//创建信号传输通道
	signal.Notify(signals)//监听所有的信号，并将信号放入signals通道
	// set the shim as the subreaper for all orphaned processes created by the container，将shim层作为所有容器创建的孤儿进程的回收者
	if err := osutils.SetSubreaper(1); err != nil {
		return err
	}
	// open the exit pipe
	f, err := os.OpenFile("exit", syscall.O_WRONLY, 0)//打开退出文件通道，可发送退出信号
	if err != nil {
		return err
	}
	defer f.Close()
	control, err := os.OpenFile("control", syscall.O_RDWR, 0)//打开控制信息通道，可发送接受容器控制信息
	if err != nil {
		return err
	}
	defer control.Close()
	p, err := newProcess(flag.Arg(0), flag.Arg(1), flag.Arg(2))//根据命令中的ct_id  bundle runtimeName构建一个进程对象
	if err != nil {
		return err
	}
	defer func() {
		if err := p.Close(); err != nil {
			writeMessage(log, "warn", err)
		}
	}()
	if err := p.create(); err != nil {
	//创建进程，调用docker-runc
		p.delete()
		return err
	}
	msgC := make(chan controlMessage, 32)//创建控制信息通道
	go func() {
		for {
			var m controlMessage
			if _, err := fmt.Fscanf(control, "%d %d %d\n", &m.Type, &m.Width, &m.Height); err != nil {
			//将control通道的信息格式化写入controlMessage
				continue
			}
			msgC <- m
		}
	}()
	if runtime.GOOS == "solaris" {
		return nil
	}
	var exitShim bool
	for {
		select {
		case s := <-signals://监听信号通道
			switch s {
			case syscall.SIGCHLD:
				exits, _ := osutils.Reap(false)
				for _, e := range exits {
					// check to see if runtime is one of the processes that has exited，检查运行环境是否是已经退出的进程之一
					if e.Pid == p.pid() {
						exitShim = true
						writeInt("exitStatus", e.Status)
					}
				}
			}
			// runtime has exited so the shim can also exit
			if exitShim {
				// kill all processes in the container incase it was not running in
				// its own PID namespace
				p.killAll()
				// wait for all the processes and IO to finish
				p.Wait()
				// delete the container from the runtime
				p.delete()
				// the close of the exit fifo will happen when the shim exits
				return nil
			}
		case msg := <-msgC://监听控制信息通道
			switch msg.Type {
			case 0:
				// close stdin，关闭输入端
				if p.stdinCloser != nil {
					p.stdinCloser.Close()
				}
			case 1:
				if p.console == nil {
					continue
				}
				ws := term.Winsize{
					Width:  uint16(msg.Width),
					Height: uint16(msg.Height),
				}
				term.SetWinsize(p.console.Fd(), &ws)
			}
		}
	}
	return nil
}
{% endhighlight %}

### 新建进程对象

读取进程状态信息及检查点信息后打开进程输入输出pipe端口，返回进程对象
{% highlight go %}
func newProcess(id, bundle, runtimeName string) (*process, error) {
	p := &process{
		id:      id,
		bundle:  bundle,
		runtime: runtimeName,
	}
	s, err := loadProcess()//根据process.json文件获取进程状态信息s
	if err != nil {
		return nil, err
	}
	p.state = s//将状态信息填充到进程对象中的state
	if s.CheckpointPath != "" {
		cpt, err := loadCheckpoint(s.CheckpointPath)//根据检查点路径加载检查点信息，读取检查点路径下的config.json文件信息到cpt对象
		if err != nil {
			return nil, err
		}
		p.checkpoint = cpt//同时填充检查点信息到进程对象中的checkpoint中去
		p.checkpointPath = s.CheckpointPath
	}
	if err := p.openIO(); err != nil {
	//打开进程的输入输出端pipe
		return nil, err
	}
	return p, nil
}

{% endhighlight %}

### 配置docker－runc参数，交付runc执行容器启动工作

配置docker-runc命令参数，包括checkpoint相关的参数，然后执行该命令等待该命令返回结果，最后配置容器的pid

{% highlight go %}
func (p *process) create() error {
	cwd, err := os.Getwd()//获取当前目录路径
	if err != nil {
		return err
	}
	logPath := filepath.Join(cwd, "log.json")//设置runc日志路径
	args := append([]string{
		"--log", logPath,
		"--log-format", "json",
	}, p.state.RuntimeArgs...)
	if p.state.Exec {
		args = append(args, "exec",
			"-d",
			"--process", filepath.Join(cwd, "process.json"),
			"--console", p.consolePath,
		)
	} else if p.checkpoint != nil {
	//配置检查点文件相关参数
		args = append(args, "restore",
			"-d",
			"--image-path", p.checkpointPath,
			"--work-path", filepath.Join(p.checkpointPath, "criu.work", "restore-"+time.Now().Format(time.RFC3339Nano)),
		)
		add := func(flags ...string) {
			args = append(args, flags...)
		}//定义添加参数函数add
		if p.checkpoint.Shell {
			add("--shell-job")
		}
		if p.checkpoint.TCP {
			add("--tcp-established")
		}
		if p.checkpoint.UnixSockets {
			add("--ext-unix-sk")
		}
		if p.state.NoPivotRoot {
			add("--no-pivot")
		}
		for _, ns := range p.checkpoint.EmptyNS {
			add("--empty-ns", ns)
		}
       //根据检查点文件添加参数
	} else {
		args = append(args, "create",
			"--bundle", p.bundle,
			"--console", p.consolePath,
		)
		if p.state.NoPivotRoot {
			args = append(args, "--no-pivot")
		}
	}
	args = append(args,
		"--pid-file", filepath.Join(cwd, "pid"),
		p.id,
	)
	cmd := exec.Command(p.runtime, args...)//构建命令docker-runc args
	cmd.Dir = p.bundle//定义命令执行工作路径
	cmd.Stdin = p.stdio.stdin
	cmd.Stdout = p.stdio.stdout
	cmd.Stderr = p.stdio.stderr
	// Call out to setPDeathSig to set SysProcAttr as elements are platform specific
	cmd.SysProcAttr = setPDeathSig()
   if err := cmd.Start(); err != nil {
	//执行docker-runc args命令
	
		if exErr, ok := err.(*exec.Error); ok {
			if exErr.Err == exec.ErrNotFound || exErr.Err == os.ErrNotExist {
				return fmt.Errorf("%s not installed on system", p.runtime)
			}
		}
		return err
	}
	if runtime.GOOS != "solaris" {
		// Since current logic dictates that we need a pid at the end of p.create
		// we need to call runtime start as well on Solaris hence we need the
		// pipes to stay open.
		p.stdio.stdout.Close()
		p.stdio.stderr.Close()
	}
	if err := cmd.Wait(); err != nil {
	//等待命令执行完毕
		if _, ok := err.(*exec.ExitError); ok {
			return errRuntime
		}
		return err
	}
	data, err := ioutil.ReadFile("pid")//读取pid文件信息，得到进程pid数据
	if err != nil {
		return err
	}
	pid, err := strconv.Atoi(string(data))
	if err != nil {
		return err
	}
	p.containerPid = pid//配置容器pid
	return nil
}

{% endhighlight %}