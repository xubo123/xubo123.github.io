---
layout:     post
title:      "docker源码阅读笔记六"
subtitle:   "容器checkpoint create的实现二"
date:       2017-05-12 12:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码
---

### docker源码阅读笔记六－grpc服务器端接收checkpoint请求实现
  前一章分析checkpoint create发送grpc请求过程中，分析到了sendRequest()这个部分，该部分代码有一个非常关键的步骤：建立一个与grpc服务器端的数据流如下
    stream, err := t.NewStream(ctx, callHdr)//首先建立一个与发送目的端发送请求数据的流数据
    经过调试可以得到该callHdr对象：
    
 ```
 
    type CallHdr struct {
	// Host specifies the peer's host.
	Host string
	// Method specifies the operation to perform.
	Method string

	// RecvCompress specifies the compression algorithm applied on
	// inbound messages.
	RecvCompress string

	// SendCompress specifies the compression algorithm applied on
	// outbound message.
	SendCompress string

	// Flush indicates whether a new stream command should be sent
	// to the peer without waiting for the first data. This is
	// only a hint. The transport may modify the flush decision
	// for performance purposes.
	Flush bool
}

```
   其中
   Host:/var/run/docker/libcontainerd/docker-containerd.sock</br>
   Method:/types.API/CreateCheckpoint </br>
   故 NewStream所实现的就是向该Host即/var/run/docker/ libcontainerd/docker-containerd.sock（套接字）发送方法请求/types.API/CreateCheckpoint数据至grpc服务器端。
   
 
###grpc服务器端对该套接字的监听实现

   实现对grpc请求套接字监听的部分自然是docker daemon初始化的时候就已经准备好了，因为当daemon启动后就可以提供一系列的API服务，时刻监听API grpc请求。所以我们需要从Daemon.Start()这个函数展开分析，
这个函数在docker源码阅读笔记三中有过初步研究，它包含所有Daemon启动需要的准备工作，其中第十个步骤根据DaemonCli创建containerd远程访问客户端对象：

```
containerdRemote, err := libcontainerd.New (cli.getLibcontainerdRoot(),cli.getPlatformRemoteOptions()...)
```

就创建了一个Containerd远程访问的客户端对象containerdRemote，通过该对象可以向grpc服务器发送请求，在这个对象的创建过程中则启动了Containerd后台进程并对docker-containerd.sock设置了监听。

从libcontainerd.New 展开分析：

```

     // New creates a fresh instance of libcontainerd remote.
  
     func New(stateDir string, options ...RemoteOption) (_ Remote, err error) {
	defer func() {
		if err != nil {
			err = fmt.Errorf("Failed to connect to containerd. Please make sure containerd is installed in your PATH or you have specified the correct address. Got error: %v", err)
		}
	}()
	r := &remote{
		stateDir:    stateDir,
		daemonPid:   -1,
		eventTsPath: filepath.Join(stateDir, eventTimestampFilename),
	}
	for _, option := range options {
		if err := option.Apply(r); err != nil {
			return nil, err
		}
	}
	if err := system.MkdirAll(stateDir, 0700); err != nil {
		return nil, err
	}

	if r.rpcAddr == "" {
		r.rpcAddr = filepath.Join(stateDir, containerdSockFilename)//这里的containerdSockFilename即docker-containerd.sock，为常量
	}

	if r.startDaemon {//设置好了remote相关参数属性后启动
		if err := r.runContainerdDaemon(); err != nil {
			return nil, err
		}
	}

	// don't output the grpc reconnect logging
	grpclog.SetLogger(log.New(ioutil.Discard, "", log.LstdFlags))
	dialOpts := append([]grpc.DialOption{grpc.WithInsecure()},
		grpc.WithDialer(func(addr string, timeout time.Duration) (net.Conn, error) {
			return net.DialTimeout("unix", addr, timeout)
		}),
	)
	conn, err := grpc.Dial(r.rpcAddr, dialOpts...)
	//若ContainerdDaemon启动成功，启动成功后会启用grpc.NewServer()即grpc服务器，调用grpc.Dial与该grpc服务器建立连接conn。
	if err != nil {
		return nil, fmt.Errorf("error connecting to containerd: %v", err)
	}

	r.rpcConn = conn
	r.apiClient = containerd.NewAPIClient(conn)
	//根据该连接建立api客户端对象，通过该对象就可以通过该连接发送请求json数据

	// Get the timestamp to restore from
	t := r.getLastEventTimestamp()
	tsp, err := ptypes.TimestampProto(t)
	if err != nil {
		logrus.Errorf("libcontainerd: failed to convert timestamp: %q", err)
	}
	r.restoreFromTimestamp = tsp

	go r.handleConnectionChange()
	//连接健康检查，定时0.5s访问连接检查一次

	if err := r.startEventsMonitor(); err != nil {
	//开启异常事件监听，包括kill，pause等命令的处理
		return nil, err
	}

	return r, nil
}
```

从containerd远程访问对象的创建代码来看，grpc的服务器端的启用及对套件字的监听是在r.runContainerdDaemon();实现的，所以接下来我们深入研究该函数看看grpc服务器的启用过程
   
```
   
    func (r *remote) runContainerdDaemon() error {
	pidFilename := filepath.Join(r.stateDir, containerdPidFilename)
	f, err := os.OpenFile(pidFilename, os.O_RDWR|os.O_CREATE, 0600)
	if err != nil {
		return err
	}
	defer f.Close()

	// File exist, check if the daemon is alive
	b := make([]byte, 8)
	n, err := f.Read(b)
	if err != nil && err != io.EOF {
		return err
	}

	if n > 0 {
		pid, err := strconv.ParseUint(string(b[:n]), 10, 64)
		if err != nil {
			return err
		}
		if system.IsProcessAlive(int(pid)) {
			logrus.Infof("libcontainerd: previous instance of containerd still alive (%d)", pid)
			r.daemonPid = int(pid)
			return nil
		}
	}

	// rewind the file
	_, err = f.Seek(0, os.SEEK_SET)
	if err != nil {
		return err
	}

	// Truncate it
	err = f.Truncate(0)
	if err != nil {
		return err
	}

	// Start a new instance
	args := []string{
		"-l", fmt.Sprintf("unix://%s", r.rpcAddr),
		"--metrics-interval=0",
		"--start-timeout", "2m",
		"--state-dir", filepath.Join(r.stateDir, containerdStateDir),
		//设置docker－containerd命令执行的参数，其中－l flag指定了Containerd服务需要监听的套接字文件。
	}
	if goruntime.GOOS == "solaris" {
		args = append(args, "--shim", "containerd-shim", "--runtime", "runc")
	} else {
		args = append(args, "--shim", "docker-containerd-shim")
		if r.runtime != "" {
			args = append(args, "--runtime")
			args = append(args, r.runtime)
		}
	}
	if r.debugLog {
		args = append(args, "--debug")
	}
	if len(r.runtimeArgs) > 0 {
		for _, v := range r.runtimeArgs {
			args = append(args, "--runtime-args")
			args = append(args, v)
		}
		logrus.Debugf("libcontainerd: runContainerdDaemon: runtimeArgs: %s", args)
	}

	cmd := exec.Command(containerdBinary, args...)
	
	//根据参数执行docker-contained命令：docker-containerd -l unix:///var/run/docker/libcontainerd/docker-containerd.sock --metrics-interval=0 --start-timeout 2m --state-dir /var/run/docker/libcontainerd/containerd --shim docker-containerd-shim --runtime docker-runc --debug,该docker-containerd为二进制文件，该二进制文件的生成是在Docker源码编译过程中hack/dockerfile/install-binaries.sh脚本文件中根据https://github.com/docker/containerd.git上的源码编译后产生的，所以当执行该docker-containerd命令后如何实现grpc服务器端端建立及对该套接字的监听则需要从github.com/docker/containerd这个项目的源码中分析。
	
	// redirect containerd logs to docker logs，这里是将containerd服务端的输入输出流信息重定向到dockerdaemon上来统一打印日志信息
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	cmd.SysProcAttr = setSysProcAttr(true)
	cmd.Env = nil
	// clear the NOTIFY_SOCKET from the env when starting containerd
	for _, e := range os.Environ() {
		if !strings.HasPrefix(e, "NOTIFY_SOCKET") {
			cmd.Env = append(cmd.Env, e)
		}
	}
	if err := cmd.Start(); err != nil {
	//这里开始执行上述docker-contianerd命令
		return err
	}
	logrus.Infof("libcontainerd: new containerd process, pid: %d", cmd.Process.Pid)
	if err := setOOMScore(cmd.Process.Pid, r.oomScore); err != nil {
		system.KillProcess(cmd.Process.Pid)
		return err
	}
	if _, err := f.WriteString(fmt.Sprintf("%d", cmd.Process.Pid)); err != nil {
		system.KillProcess(cmd.Process.Pid)
		return err
	}

	r.daemonWaitCh = make(chan struct{})
	go func() {
	//开启一个goroutine等待containerd的异常信号，若接收到异常信号关闭daemonwaitch
		cmd.Wait()
		close(r.daemonWaitCh)
	}() // Reap our child when needed
	r.daemonPid = cmd.Process.Pid
	return nil
    }
  
   ```
   
这一章主要讲述docker源码部分实现grpc服务器建立及对套接字的监听，在执行命令docker-containerd..之后将进入github.com/docker/containerd源码进行分析

###所以下一章将会解释containerd项目源码如何实现grpc的启用及套接字监听的部分内容
   






