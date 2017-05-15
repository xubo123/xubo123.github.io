---
layout:     post
title:      "docker源码阅读笔记七"
subtitle:   "容器checkpoint create的实现三"
date:       2017-05-15 12:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码
---
### docker源码阅读笔记七－containerd中源码实现grpc服务器监听

上一章我们将docker部分的源码如何实现grpc的服务器监听，在执行函数runContainerdDaemon()过程中调用cmd := exec.Command(containerdBinary, args...)，相当于执行命令

```
docker-containerd -l unix:///var/run/docker/libcontainerd/ docker-containerd.sock --metrics-interval=0 --start-timeout 2m --state-dir /var/run/docker/libcontainerd/containerd --shim docker-containerd-shim --runtime docker-runc --debug
```	
docker-containerd命令则是通过编译containerd源码得到的二进制文件，所以执行docker－containerd命令时则需要通过分析containerd源码的主函数入口展开分析来研究对指定套接字的监听。

## 1.主函数入口

containerd进入主函数入口如下：

```
   
    func main() {	logrus.SetFormatter(&logrus.TextFormatter{TimestampFormat: time.RFC3339Nano})
	app := cli.NewApp()//新建一个app对象，在containerd架构中，contained的服务后台进程看做一个应用对象来提供服务
	app.Name = "containerd"
	if containerd.GitCommit != "" {
		app.Version = fmt.Sprintf("%s commit: %s", containerd.Version, containerd.GitCommit)
	} else {
		app.Version = containerd.Version
	}
	app.Usage = usage
	app.Flags = daemonFlags
	app.Before = func(context *cli.Context) error {
		setupDumpStacksTrap()
		if context.GlobalBool("debug") {
			logrus.SetLevel(logrus.DebugLevel)
			if context.GlobalDuration("metrics-interval") > 0 {
				if err := debugMetrics(context.GlobalDuration("metrics-interval"), context.GlobalString("graphite-address")); err != nil {
					return err
				}
			}
		}
		if logLevel := context.GlobalString("log-level"); logLevel != "" {
			lvl, err := logrus.ParseLevel(logLevel)
			if err != nil {
				lvl = logrus.InfoLevel
				fmt.Fprintf(os.Stderr, "Unable to parse logging level: %s\n, and being defaulted to info", logLevel)
			}
			logrus.SetLevel(lvl)
		}
		if p := context.GlobalString("pprof-address"); len(p) > 0 {
			pprof.Enable(p)
		}
		if err := checkLimits(); err != nil {
			return err
		}
		return nil
	}

	app.Action = func(context *cli.Context) {
		if err := daemon(context); err != nil {
			logrus.Fatal(err)
		}
	}
	//以上部分的代码都是给app这个对象来进行各种属性的设置
	if err := app.Run(os.Args); err != nil {
	    对app属性设置好后结合命令行中的指定参数开始运行应用，也就是启用containerd后台服务进程
		logrus.Fatal(err)
	}
}
```
## 2.containerd后台进程应用app启动

app.Run的实现部分如下：

```
    
    func (a *App) Run(arguments []string) (err error) {
	if a.Author != "" || a.Email != "" {
		a.Authors = append(a.Authors, Author{Name: a.Author, Email: a.Email})
	}

	newCmds := []Command{}
	for _, c := range a.Commands {
		if c.HelpName == "" {
			c.HelpName = fmt.Sprintf("%s %s", a.HelpName, c.Name)
		}
		newCmds = append(newCmds, c)
	}
	a.Commands = newCmds

	// append help to commands
	if a.Command(helpCommand.Name) == nil && !a.HideHelp {
		a.Commands = append(a.Commands, helpCommand)
		if (HelpFlag != BoolFlag{}) {
			a.appendFlag(HelpFlag)
		}
	}

	//append version/help flags
	if a.EnableBashCompletion {
		a.appendFlag(BashCompletionFlag)
	}

	if !a.HideVersion {
		a.appendFlag(VersionFlag)
	}

	// parse flags
	set := flagSet(a.Name, a.Flags)
	set.SetOutput(ioutil.Discard)
	err = set.Parse(arguments[1:])//解析参数，得到参数集合
	nerr := normalizeFlags(a.Flags, set)//检查参数是否合法
	context := NewContext(a, set, nil)
	if nerr != nil {
		fmt.Fprintln(a.Writer, nerr)
		ShowAppHelp(context)
		return nerr
	}

	if checkCompletions(context) {
		return nil
	}

	if err != nil {
		if a.OnUsageError != nil {
			err := a.OnUsageError(context, err, false)
			return err
		} else {
			fmt.Fprintf(a.Writer, "%s\n\n", "Incorrect Usage.")
			ShowAppHelp(context)
			return err
		}
	}

	if !a.HideHelp && checkHelp(context) {
		ShowAppHelp(context)
		return nil
	}

	if !a.HideVersion && checkVersion(context) {
		ShowVersion(context)
		return nil
	}

	if a.After != nil {
		defer func() {
			if afterErr := a.After(context); afterErr != nil {
				if err != nil {
					err = NewMultiError(err, afterErr)
				} else {
					err = afterErr
				}
			}
		}()
	}

	if a.Before != nil {
		err = a.Before(context)
		if err != nil {
			fmt.Fprintf(a.Writer, "%v\n\n", err)
			ShowAppHelp(context)
			return err
		}
	}

	args := context.Args()
	if args.Present() {
		name := args.First()
		c := a.Command(name)//获取当前app结构体中是否具有该指定命令。由于app是第一次启动所以Commands参数为空，执行a.Action默认函数，该默认函数就是启动containerdDaemon及grpc服务器的初次操作
		if c != nil {
			return c.Run(context)
		}
	}

	// Run default Action
	a.Action(context)//docker containerd第一次执行就是执行这个函数来启动grpc服务器
	return nil
}
```

根据上述代码分析，可以知道docker－containerd命令第一次执行的时候，app的Commands成员为空，因为海没有启动contaienrd daemon对app内的Commands进行初始化设置，故执行app.Action的默认操作来初始化containerddaemon及app.Command,在main（）函数中我们可以看到

```
app.Action = func(context *cli.Context) {
		if err := daemon(context); err != nil {
		//这里的daemon(context)就是非常关键的启动containerd服务daemon的入口，同时开启grpc服务器
			logrus.Fatal(err)
		}
```

## 3.daemon(context)函数启用Containerd Daemon


```
func daemon(context *cli.Context) error {
	stateDir := context.String("state-dir")//context包含来参数信息并且对参数名称进行正规化如：－l对应了listen。
	if err := os.MkdirAll(stateDir, 0755); err != nil {
		return err
	}
	s := make(chan os.Signal, 2048)
	signal.Notify(s, syscall.SIGTERM, syscall.SIGINT)
	// Split the listen string of the form proto://addr
	listenSpec := context.String("listen")//根据参数获取监听器，在我们的命令当中监听对象为docker-containerd.sock
	listenParts := strings.SplitN(listenSpec, "://", 2)
	if len(listenParts) != 2 {
		return fmt.Errorf("bad listen address format %s, expected proto://address", listenSpec)
	}
	// Register server early to allow healthcheck to be done
	server, err := startServer(listenParts[0], listenParts[1])//启动grpc服务器
	if err != nil {
		return err
	}
	sv, err := supervisor.New(
		stateDir,
		context.String("runtime"),
		context.String("shim"),
		context.StringSlice("runtime-args"),
		context.Duration("start-timeout"),
		context.Int("retain-count"))
	if err != nil {
		return err
	}
	types.RegisterAPIServer(server, grpcserver.NewServer(sv))//给grpc注册指定服务，相当于给json数据和处理handler建立关联关系。
	wg := &sync.WaitGroup{}
	for i := 0; i < 10; i++ {
		wg.Add(1)
		w := supervisor.NewWorker(sv, wg)
		go w.Start()
	}
	if err := sv.Start(); err != nil {
		return err
	}
	for ss := range s {
		switch ss {
		default:
			logrus.Infof("stopping containerd after receiving %s", ss)
			server.Stop()
			os.Exit(0)
		}
	}
	return nil
}
```
启动daemon这个函数做了两件事：

1）启动grpc服务器
2）为grpc服务器注册api服务

接下来来看这两个步骤事如何实现的。

### 1）启动grpc服务器

daemon(context *cli.Context)函数中的server, err := startServer(listenParts[0], listenParts[1])实现了grpc服务器的启动，但我们想具体看一下grpc的启动步骤，所以进入startServer函数部分如下：

```
func startServer(protocol, address string) (*grpc.Server, error) {
	// TODO: We should use TLS.
	// TODO: Add an option for the SocketGroup.
	sockets, err := listeners.Init(protocol, address, "", nil)//根据参数提供的监听对象信息来生成一个套接字sockets对象
	if err != nil {
		return nil, err
	}
	if len(sockets) != 1 {
		return nil, fmt.Errorf("incorrect number of listeners")
	}
	l := sockets[0]
	s := grpc.NewServer()//grpc.NewServer()正式启动，这里可以查看grpc的官方实现原理，就是通过调用NewSever来启动grpc服务器
	healthServer := health.NewServer()
	grpc_health_v1.RegisterHealthServer(s, healthServer)//对grpc注册健康检查服务，定期检查grpc服务器连接情况
	go func() {
	    //新建一个goroutine来响应监听的套接字对象
		logrus.Debugf("containerd: grpc api on %s", address)
		if err := s.Serve(l); err != nil {
		    //将grpc服务器与指定套接字建立关联关系来监听docker-containerd.sock
			logrus.WithField("error", err).Fatal("containerd: serve grpc")
		}
	}()
	return s, nil
}
```

### 2）为grpc服务器注册API服务

在daemon(context)中启动grpc服务器后，即通过调用types.RegisterAPIServer(server, grpcserver.NewServer(sv))来为grpc服务器注册服务，实现如下：

```
//_API_serviceDesc就是json数据与handler建立关联的静态数据，默认配置的关联关系
var _API_serviceDesc = grpc.ServiceDesc{
	ServiceName: "types.API",
	HandlerType: (*APIServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "GetServerVersion",
			Handler:    _API_GetServerVersion_Handler,
		},
		{
			MethodName: "CreateContainer",
			Handler:    _API_CreateContainer_Handler,
		},
		{
			MethodName: "UpdateContainer",
			Handler:    _API_UpdateContainer_Handler,
		},
		{
			MethodName: "Signal",
			Handler:    _API_Signal_Handler,
		},
		{
			MethodName: "UpdateProcess",
			Handler:    _API_UpdateProcess_Handler,
		},
		{
			MethodName: "AddProcess",
			Handler:    _API_AddProcess_Handler,
		},
		{
			MethodName: "CreateCheckpoint",
			Handler:    _API_CreateCheckpoint_Handler,
		},
		{
			MethodName: "DeleteCheckpoint",
			Handler:    _API_DeleteCheckpoint_Handler,
		},
		{
			MethodName: "ListCheckpoint",
			Handler:    _API_ListCheckpoint_Handler,
		},
		{
			MethodName: "State",
			Handler:    _API_State_Handler,
		},
		{
			MethodName: "Stats",
			Handler:    _API_Stats_Handler,
		},
	},
	Streams: []grpc.StreamDesc{
		{
			StreamName:    "Events",
			Handler:       _API_Events_Handler,
			ServerStreams: true,
		},
	},
	Metadata: "api.proto",
}
//每个方法具体执行函数对象抽象类型
type methodHandler func(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor UnaryServerInterceptor) (interface{}, error)
//方法信息描述对象将方法名与方法执行函数对应
// MethodDesc represents an RPC service's method specification.
type MethodDesc struct {
	MethodName string
	Handler    methodHandler
}
//服务信息描述对象，比如提供type.API为一个API服务，但服务里面又有具体的方法如CreateCheckpoint，DeleteCheckpoint，ListCheckpoint等方法对象，即包含了MethodDesc对象
// ServiceDesc represents an RPC service's specification.
type ServiceDesc struct {
	ServiceName string
	// The pointer to the service interface. Used to check whether the user
	// provided implementation satisfies the interface requirements.
	HandlerType interface{}
	Methods     []MethodDesc
	Streams     []StreamDesc
	Metadata    interface{}
}

//service构建服务器对象和该服务器所提供的服务进行关联然后对外暴露为一整个服务模式对象（相当于一个整体对象来提供各种服务）
// service consists of the information of the server serving this service and
// the methods in this service.
type service struct {
	server interface{} // the server for service methods
	md     map[string]*MethodDesc
	sd     map[string]*StreamDesc
	mdata  interface{}
}
//提供服务的服务器信息结构体
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
//首先服务器对象信息和服务方法对象信息是分开独立的，关联关系是通过service对象来提供。
***********************************************************************************

func RegisterAPIServer(s *grpc.Server, srv APIServer) {
	s.RegisterService(&_API_serviceDesc, srv)
}
***********************************************************************************
func (s *Server) RegisterService(sd *ServiceDesc, ss interface{}) {
	ht := reflect.TypeOf(sd.HandlerType).Elem()//这里的HandlerType就是_API_serviceDesc中的HandlerType: (*APIServer)(nil)，也就是APIServer类型的空方法
	st := reflect.TypeOf(ss)//这里的ss就是APIServer
	if !st.Implements(ht) {//Implements函数返回类型st（APISever）是否实现了接口ht（methodHandler），没有实现返回false，抛出异常：没有实现handler
		grpclog.Fatalf("grpc: Server.RegisterService found the handler of type %v that does not satisfy %v", st, ht)
	}
	s.register(sd, ss)
}
***********************************************************************************
func (s *Server) register(sd *ServiceDesc, ss interface{}) {
	s.mu.Lock()
	defer s.mu.Unlock()
	s.printf("RegisterService(%q)", sd.ServiceName)
	if _, ok := s.m[sd.ServiceName]; ok {//查看APIServer服务器是否已经注册了这些方法
		grpclog.Fatalf("grpc: Server.RegisterService found duplicate service registration for %q", sd.ServiceName)
	}
	//构造一个service对象来将服务与APIServer服务器关联，之前我们提到过服务器与服务对象是分开独立的
	srv := &service{
		server: ss,
		md:     make(map[string]*MethodDesc),
		sd:     make(map[string]*StreamDesc),
		mdata:  sd.Metadata,
	}
	//将所有的_API_serviceDesc中的MethodDesc中方法信息填充到service中与APIServer服务器建立关联关系
	for i := range sd.Methods {
		d := &sd.Methods[i]
		srv.md[d.MethodName] = d
	}
	//这里的流服务待分析，我们分析的checkpoint命令属于单次grpc请求服务，这里暂时不做考虑
	for i := range sd.Streams {
		d := &sd.Streams[i]
		srv.sd[d.StreamName] = d
	}
	s.m[sd.ServiceName] = srv//将该服务service对象（局部的服务器与服务之间的关系构成对外的服务体）作为整体填充到整体的containerd的Sever对象（包括所有类型的服务，不仅仅API服务还有健康检查服务器提供的服务等）中
}
```

研究思路整理：main（）－>app.Run()->a.Action(context)->daemon(context)->startServer()->grpc.NewServer()->types.RegisterAPIServer()构造好了service对象提供服务（包含服务器和提供的服务api信息）。这一张描述了containerd源码部分启动grpc服务器和对指定套接字的监听并且注册了apiserver服务器的所有api服务（建立json和处理handler的关联关系），得到整合服务器和其提供的服务对象的service对象，并填充到整体的containerd的Server对象。

### 下一章将分析当docker的containerd模块发送checkpoint Create Json数据到docker containerd监听的grpc服务器调用具体的方法处理handler实现checkpointcreate的流程。


