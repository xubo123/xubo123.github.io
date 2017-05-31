---
layout:     post
title:      "docker源码阅读笔记八"
subtitle:   "容器checkpoint create的实现四"
date:       2017-05-16 12:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码
---
### docker源码阅读笔记八－contaienrd服务器端请求处理

在笔记六中我们分析到docker源码通过sendRequest函数来向docker-contaienrd.sock发送请求数据，在笔记七中，我们解释了containerd是如何启用ContainerdDaemon和grpc服务器并且实现对docker-containerd.sock的监听&注册来API服务，在这一章中我们依然分析contaienrd的源码来了解当containerd的grpc服务器从指定的套接字接收到checkpoint json数据后，如何进行路由分发找到指定的方法handler来实现checkpointCreate。

### 1.docker发送请求
   在docker端，sendRequest()函数部分：
   
   stream, err := t.NewStream(ctx, callHdr)
   
   其中callHdr结构为：
   
   callHdr :Host:/var/run/docker/libcontainerd/docker-containerd.sock,Method:/types.API/CreateCheckpoint 
   
### 2.containerd服务器端对套接字的监听
  
```
func (s *Server) Serve(lis net.Listener) error
 {
 ...
for {
		rawConn, err := lis.Accept()//监听器接收请求连接
		if err != nil {
			if ne, ok := err.(interface {
				Temporary() bool
			}); ok && ne.Temporary() {
				if tempDelay == 0 {
					tempDelay = 5 * time.Millisecond
				} else {
					tempDelay *= 2
				}
				if max := 1 * time.Second; tempDelay > max {
					tempDelay = max
				}
				s.mu.Lock()
				s.printf("Accept error: %v; retrying in %v", err, tempDelay)
				s.mu.Unlock()
				select {
				case <-time.After(tempDelay):
				case <-s.ctx.Done():
				}
				continue
			}
			s.mu.Lock()
			s.printf("done serving; Accept = %v", err)
			s.mu.Unlock()
			return err
		}
		tempDelay = 0
		// Start a new goroutine to deal with rawConn
		// so we don't stall this Accept loop goroutine.
		go s.handleRawConn(rawConn)//根据连接建立goroutine处理请求
	}

}
```

请求处理实现handleRawConn(rawConn)：1.连接认证  2.处理请求

```
// handleRawConn is run in its own goroutine and handles a just-accepted
// connection that has not had any I/O performed on it yet.
func (s *Server) handleRawConn(rawConn net.Conn) {
	conn, authInfo, err := s.useTransportAuthenticator(rawConn)//传输连接认证
	if err != nil {
		s.mu.Lock()
		s.errorf("ServerHandshake(%q) failed: %v", rawConn.RemoteAddr(), err)
		s.mu.Unlock()
		grpclog.Printf("grpc: Server.Serve failed to complete security handshake from %q: %v", rawConn.RemoteAddr(), err)
		// If serverHandShake returns ErrConnDispatched, keep rawConn open.
		if err != credentials.ErrConnDispatched {
			rawConn.Close()
		}
		return
	}

	s.mu.Lock()
	if s.conns == nil {
		s.mu.Unlock()
		conn.Close()
		return
	}
	s.mu.Unlock()

	if s.opts.useHandlerImpl {//根据server配置opt来决定使用哪一种Handler处理请求
		s.serveUsingHandler(conn)
	} else {
		s.serveNewHTTP2Transport(conn, authInfo)//checkpoint create的实现主要在于使用该函数来处理请求
	}
}
```

serveNewHTTP2Transport(conn, authInfo)实现如下：新建一个serverTransport对象，服务该对象中的数据流

```
func (s *Server) serveNewHTTP2Transport(c net.Conn, authInfo credentials.AuthInfo) {
	st, err := transport.NewServerTransport("http2", c, s.opts.maxConcurrentStreams, authInfo)
	if err != nil {
		s.mu.Lock()
		s.errorf("NewServerTransport(%q) failed: %v", c.RemoteAddr(), err)
		s.mu.Unlock()
		c.Close()
		grpclog.Println("grpc: Server.Serve failed to create ServerTransport: ", err)
		return
	}
	if !s.addConn(st) {
		st.Close()
		return
	}
	s.serveStreams(st)//服务该对象中的数据流
}
------------------------------------------------------------
------------------------------------------------------------
func (s *Server) serveStreams(st transport.ServerTransport) {
	defer s.removeConn(st)
	defer st.Close()
	var wg sync.WaitGroup
	st.HandleStreams(func(stream *transport.Stream) {
		wg.Add(1)
		go func() {
			defer wg.Done()
			s.handleStream(st, stream, s.traceInfo(st, stream))//建立goroutine服务该数据流
		}()
	})
	wg.Wait()
}
```

对数据流提供服务函数handleStream(st, stream, s.traceInfo(st, stream))如下：

```
type service struct {
	server interface{} // the server for service methods
	md     map[string]*MethodDesc
	sd     map[string]*StreamDesc
	mdata  interface{}
}
// Server is a gRPC server to serve RPC requests.
type Server struct {
	opts options
	mu     sync.Mutex // guards following
	lis    map[net.Listener]bool
	conns  map[io.Closer]bool
	drain  bool
	ctx    context.Context
	cancel context.CancelFunc
	// A CondVar to let GracefulStop() blocks until all the pending RPCs are finished
	// and all the transport goes away.
	cv     *sync.Cond
	m      map[string]*service // service name -> service info
	events trace.EventLog
}
------------------------------------------------------------
------------------------------------------------------------
func (s *Server) handleStream(t transport.ServerTransport, stream *transport.Stream, trInfo *traceInfo) {
	sm := stream.Method()//获取数据流的请求命令路径
	if sm != "" && sm[0] == '/' {
		sm = sm[1:]
	}
	pos := strings.LastIndex(sm, "/")
	if pos == -1 {
		if trInfo != nil {
			trInfo.tr.LazyLog(&fmtStringer{"Malformed method name %q", []interface{}{sm}}, true)
			trInfo.tr.SetError()
		}
		if err := t.WriteStatus(stream, codes.InvalidArgument, fmt.Sprintf("malformed method name: %q", stream.Method())); err != nil {
			if trInfo != nil {
				trInfo.tr.LazyLog(&fmtStringer{"%v", []interface{}{err}}, true)
				trInfo.tr.SetError()
			}
			grpclog.Printf("grpc: Server.handleStream failed to write status: %v", err)
		}
		if trInfo != nil {
			trInfo.tr.Finish()
		}
		return
	}
	service := sm[:pos]//获取该命令所属服务类型
	method := sm[pos+1:]//获取该命令的方法名称
	srv, ok := s.m[service]//查找服务器中是否存在该服务
	if !ok {
		if trInfo != nil {
			trInfo.tr.LazyLog(&fmtStringer{"Unknown service %v", []interface{}{service}}, true)
			trInfo.tr.SetError()
		}
		if err := t.WriteStatus(stream, codes.Unimplemented, fmt.Sprintf("unknown service %v", service)); err != nil {
			if trInfo != nil {
				trInfo.tr.LazyLog(&fmtStringer{"%v", []interface{}{err}}, true)
				trInfo.tr.SetError()
			}
			grpclog.Printf("grpc: Server.handleStream failed to write status: %v", err)
		}
		if trInfo != nil {
			trInfo.tr.Finish()
		}
		return
	}
	// Unary RPC or Streaming RPC?
	if md, ok := srv.md[method]; ok {//根据server结构体srv.md是服务器的方法描述映射，当该请求的方法名称为方法时调用单次rpc请求处理函数
		s.processUnaryRPC(t, stream, srv, md, trInfo)
		return
	}
        if sd, ok := srv.sd[method]; ok {//同上，当s请求的方法名称为流时调用rpc流处理函数
		s.processStreamingRPC(t, stream, srv, sd, trInfo)
		return
	}
	if trInfo != nil {
		trInfo.tr.LazyLog(&fmtStringer{"Unknown method %v", []interface{}{method}}, true)
		trInfo.tr.SetError()
	}
	if err := t.WriteStatus(stream, codes.Unimplemented, fmt.Sprintf("unknown method %v", method)); err != nil {
		if trInfo != nil {
			trInfo.tr.LazyLog(&fmtStringer{"%v", []interface{}{err}}, true)
			trInfo.tr.SetError()
		}
		grpclog.Printf("grpc: Server.handleStream failed to write status: %v", err)
	}
	if trInfo != nil {
		trInfo.tr.Finish()
	}
}
```
handleStream关键部分在于获取服务名方法名称后，与对应的Server结构体方法描述成员以及流描述成员进行匹配，我们所研究的`checkpint create`命令属于方法，所以会使用 `processUnaryRPC(t, stream, srv, md, trInfo) `进行单次rpc请求处理函数。接下来我们将对该函数如何处理单次rpc请求进行研究：

```
type MethodDesc struct {
	MethodName string
	Handler    methodHandler
}
--------------------------------------------------
--------------------------------------------------
func (s *Server) processUnaryRPC(t transport.ServerTransport, stream *transport.Stream, srv *service, md *MethodDesc, trInfo *traceInfo) (err error) {
...
reply, appErr := md.Handler(srv.server, stream.Context(), df, s.opts.unaryInt)//调用Server方法描述映射MethodDesc中的Handler对象根据具体的方法执行命令
...
if err := s.sendResponse(t, stream, reply, s.opts.cp, opts); err != nil {//处理完成后发送响应
			switch err := err.(type) {
			case transport.ConnectionError:
				// Nothing to do here.
			case transport.StreamError:
				statusCode = err.Code
				statusDesc = err.Desc
			default:
				statusCode = codes.Unknown
				statusDesc = err.Error()
			}
			return err
		}
}
```
从以上对processUnaryRPC代码分析基本上containerd服务器端完成对请求数据的处理及请求到具体方法执行的路由分发，接下来我们就要进入具体方法的执行，及方法描述结构体映射中对应的Handler的具体处理情况。

### 3.checkpointCreate具体实现handler
   根据笔记七中的_API_serviceDesc的静态数据的关联映射可以知道CreateCheckpoint方法对应的handler为_API_CreateCheckpoint_Handler，该handler实现如下：
 
```   
   func _API_CreateCheckpoint_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(CreateCheckpointRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(APIServer).CreateCheckpoint(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/types.API/CreateCheckpoint",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(APIServer).CreateCheckpoint(ctx, req.(*CreateCheckpointRequest))//CreateCheckpoint的具体实现
	}
	return interceptor(ctx, in, info, handler)
}
```
(APIServer).CreateCheckpoint(ctx, req.(*CreateCheckpointRequest))实现如下：

```
type apiServer struct {
	sv *supervisor.Supervisor
}
func (s *apiServer) CreateCheckpoint(ctx context.Context, r *types.CreateCheckpointRequest) (*types.CreateCheckpointResponse, error) {
	e := &supervisor.CreateCheckpointTask{}
	e.ID = r.Id
	e.CheckpointDir = r.CheckpointDir
	e.Checkpoint = &runtime.Checkpoint{
		Name:        r.Checkpoint.Name,
		Exit:        r.Checkpoint.Exit,
		TCP:         r.Checkpoint.Tcp,
		UnixSockets: r.Checkpoint.UnixSockets,
		Shell:       r.Checkpoint.Shell,
		EmptyNS:     r.Checkpoint.EmptyNS,
	}//填充checkpoint相关的信息,并构建一个task对象
	s.sv.SendTask(e)//apiServer.Supervisor.sendTask(e)利用apiServer的管理器Supervisor发送一个任务并进行处理，而Supervisor对任务的监听在containerd启动过程的daemon（ctx）中的sv.Start()已经实现，实现部分下面将有描述
	if err := <-e.ErrorCh(); err != nil {
		return nil, err
	}
	return &types.CreateCheckpointResponse{}, nil
}
```
监管器的监听启动：

```
func (s *Supervisor) Start() error {
	logrus.WithFields(logrus.Fields{
		"stateDir":    s.stateDir,
		"runtime":     s.runtime,
		"runtimeArgs": s.runtimeArgs,
		"memory":      s.machine.Memory,
		"cpus":        s.machine.Cpus,
	}).Debug("containerd: supervisor running")
	go func() {
		for i := range s.tasks {
			s.handleTask(i)//很明显，这里就是新建一个goroutine来遍历s.tasks通道中的所有任务并处理
		}
	}()
	return nil
}
```
向任务通道发送任务的实现：
```
//很简单，任务计数器加一，向s.tasks通道发送任务
func (s *Supervisor) SendTask(evt Task) {
	TasksCounter.Inc(1)
	s.tasks <- evt
}
```
任务的处理handler实现：

```
func (s *Supervisor) handleTask(i Task) {
	var err error
	//通过任务类型识别调用具体的方法执行函数
	switch t := i.(type) {
	case *AddProcessTask:
		err = s.addProcess(t)
	case *CreateCheckpointTask:
		err = s.createCheckpoint(t)//创建检查点的进一步实现
	case *DeleteCheckpointTask:
		err = s.deleteCheckpoint(t)
	case *StartTask:
		err = s.start(t)
	case *DeleteTask:
		err = s.delete(t)
	case *ExitTask:
		err = s.exit(t)
	case *GetContainersTask:
		err = s.getContainers(t)
	case *SignalTask:
		err = s.signal(t)
	case *StatsTask:
		err = s.stats(t)
	case *UpdateTask:
		err = s.updateContainer(t)
	case *UpdateProcessTask:
		err = s.updateProcess(t)
	case *OOMTask:
		err = s.oom(t)
	default:
		err = ErrUnknownTask
	}
	if err != errDeferredResponse {
		i.ErrorCh() <- err
		close(i.ErrorCh())
	}
}
*********************************************************
func (s *Supervisor) createCheckpoint(t *CreateCheckpointTask) error {
	i, ok := s.containers[t.ID]
	if !ok {
		return ErrContainerNotFound
	}
	return i.container.Checkpoint(*t.Checkpoint, t.CheckpointDir)//调用具体容器的checkpoint方法
}
*********************************************************
func (c *container) Checkpoint(cpt Checkpoint, checkpointDir string) error {
	if checkpointDir == "" {
		checkpointDir = filepath.Join(c.bundle, "checkpoints")
	}

	if err := os.MkdirAll(checkpointDir, 0755); err != nil {
	//根据checkpintdir创建目录
		return err
	}

	path := filepath.Join(checkpointDir, cpt.Name)
	if err := os.Mkdir(path, 0755); err != nil {
		return err
	}
	f, err := os.Create(filepath.Join(path, "config.json"))
	if err != nil {
		return err
	}
	cpt.Created = time.Now()
	err = json.NewEncoder(f).Encode(cpt)
	f.Close()
	if err != nil {
		return err
	}
	args := []string{
		"checkpoint",
		"--image-path", path,
		"--work-path", filepath.Join(path, "criu.work"),
	}//填写checkpoint相关参数
	add := func(flags ...string) {
		args = append(args, flags...)
	}
	add(c.runtimeArgs...)
	if !cpt.Exit {
		add("--leave-running")
	}
	if cpt.Shell {
		add("--shell-job")
	}
	if cpt.TCP {
		add("--tcp-established")
	}
	if cpt.UnixSockets {
		add("--ext-unix-sk")
	}
	for _, ns := range cpt.EmptyNS {
		add("--empty-ns", ns)
	}
	add(c.id)
	out, err := exec.Command(c.runtime, args...).CombinedOutput()//执行命令docker－runc(因为启动docker－containerd时的run－time即为docker－runc)
	if err != nil {
		return fmt.Errorf("%s: %q", err.Error(), string(out))
	}
	return err
}
```
至此，containerd源码部分实现checkpointCreate的命令的使命完成，接下里就交付给runc实现具体checkpoint创建！

### 流程总结如下：
###### (1.containerd对请求的监听处理)（Server(lis)->handleRawConn->s.serveNewHTTP2Transport->serveStreams->handleStream->processUnaryRPC）->（2.路由分发找到了具体的方法执行handler实现）（_API_CreateCheckpoint_Handler->CreateCheckpoint->SendTask(e)->handleTask->createCheckpoint->Checkpoint）->（3.经过containerd模块的包装处理交付runc进一步实现容器检查点创建）exec.Command(c.runtime, args...)

### 下一章我们将分析runc源码部分如何实现docker容器检查点的创建!!!
