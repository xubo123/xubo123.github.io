---
layout:     post
title:      "docker源码阅读笔记五"
subtitle:   "容器checkpoint create的实现一"
date:       2017-04-24 12:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码
---

### docker源码阅读笔记五－容器checkpoint create的实现一

本章的内容我们将会就checkpoint create命令分析从daemon经过路由器分发调用containerd模块的grpc发出checkpoint create命令rpc请求数据的流程。

由上一章的内容我们了解到在apiServer路由器初始化的过程中需要对checkpoint命令的路由实现初始化：即将checkpoint命令的路由项添加到apiServer的路由表routes中去。在添加该路由项的过程中，构建checkpoint结构体如下：

{% highlight go %}
// checkpointRouter is a router to talk with the checkpoint controller
type checkpointRouter struct {
	backend Backend
	decoder httputils.ContainerDecoder
	routes  []router.Route
}
{% endhighlight %}

其中的Backend成员就是checkpoint子命令的实现接口：

{% highlight go %}
// Backend for Checkpoint
type Backend interface {
	CheckpointCreate(container string, config types.CheckpointCreateOptions) error
	CheckpointDelete(container string, config types.CheckpointDeleteOptions) error
	CheckpointList(container string, config types.CheckpointListOptions) ([]types.Checkpoint, error)
}
{% endhighlight %}

接口的实现如下：

{% highlight go %}
// CheckpointCreate checkpoints the process running in a container with CRIU
func (daemon *Daemon) CheckpointCreate(name string, config types.CheckpointCreateOptions) error {
	container, err := daemon.GetContainer(name)
	if err != nil {
		return err
	}

	if !container.IsRunning() {
		return fmt.Errorf("Container %s not running", name)
	}

	var checkpointDir string
	if config.CheckpointDir != "" {
		checkpointDir = config.CheckpointDir
	} else {
		checkpointDir = container.CheckpointDir()
	}

	if !validCheckpointNamePattern.MatchString(config.CheckpointID) {
		return fmt.Errorf("Invalid checkpoint ID (%s), only %s are allowed", config.CheckpointID, validCheckpointNameChars)
	}

	err = daemon.containerd.CreateCheckpoint(container.ID, config.CheckpointID, checkpointDir, config.Exit)
	if err != nil {
		return fmt.Errorf("Cannot checkpoint container %s: %s", name, err)
	}

	daemon.LogContainerEvent(container, "checkpoint")

	return nil
}
{% endhighlight %}

这部分daemon端的代码负责的内容有：

1.对容器的参数进行验证：包括容器名，容器是否在允许状态，检查点文件目录是否正确，检查点文件名称是否合法等

2.调用containerd模块的CreateCheckpoint(container.ID, config.CheckpointID, checkpointDir, config.Exit)命令实现检查点创建

由于daemon.containerd在Daemon结构体中的形式为：containerd libcontainerd.Client(一个interface接口),所以libcontainerd.Client.CreateCheckpoint（）接口的实现如下：

{% highlight go %}
func (clnt *client) CreateCheckpoint(containerID string, checkpointID string, checkpointDir string, exit bool) error {
	clnt.lock(containerID)
	defer clnt.unlock(containerID)
	if _, err := clnt.getContainer(containerID); err != nil {
		return err
	}

	_, err := clnt.remote.apiClient.CreateCheckpoint(context.Background(), &containerd.CreateCheckpointRequest{
		Id: containerID,
		Checkpoint: &containerd.Checkpoint{
			Name:        checkpointID,
			Exit:        exit,
			Tcp:         true,
			UnixSockets: true,
			Shell:       false,
			EmptyNS:     []string{"network"},
		},
		CheckpointDir: checkpointDir,
	})
	return err
}
{% endhighlight %}

其中clnt.remote.apiClient在client.remote结构体中的形式为：apiClient containerd.APIClient（同样为一个接口）该接口中的CreateCheckpoint实现如下：

{% highlight go %}
func (c *aPIClient) CreateCheckpoint(ctx context.Context, in *CreateCheckpointRequest, opts ...grpc.CallOption) (*CreateCheckpointResponse, error) {
	out := new(CreateCheckpointResponse)
	err := grpc.Invoke(ctx, "/types.API/CreateCheckpoint", in, out, c.cc, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}
{% endhighlight %}

很明显，这个函数只是负责根据参数调用grpc.Invoke(ctx, "/types.API/CreateCheckpoint", in, out, c.cc, opts...)来发出检查点创建请求，grpc.InVoke是负责发送RPC请求request,实现如下：

{% highlight go %}
func Invoke(ctx context.Context, method string, args, reply interface{}, cc *ClientConn, opts ...CallOption) error {
	if cc.dopts.unaryInt != nil {
		return cc.dopts.unaryInt(ctx, method, args, reply, cc, invoke, opts...)
	}
	return invoke(ctx, method, args, reply, cc, opts...)
}
---------------------------------------------------------
---------------------------------------------------------
func invoke(ctx context.Context, method string, args, reply interface{}, cc *ClientConn, opts ...CallOption) (err error) {
	//由于这个函数比较复杂，有许多检查及相关参数的配置操作，所以在这里我只列出该函数最关键的两行代码，其他的错误处理代码我就不展示了。
	stream, err = sendRequest(ctx, cc.dopts.codec, cc.dopts.cp, callHdr, t, args, topts)
    err = recvResponse(cc.dopts, t, &c, stream, reply)
}
{% endhighlight %}

根据grpc.Invoke代码的分析，我们看到发出RPC请求request的实现是通过sendRequest(ctx, cc.dopts.codec, cc.dopts.cp, callHdr, t, args, topts)来实现的，并且响应的接受是通过recvResponse(cc.dopts, t, &c, stream, reply)来处理响应

我们先了解发送请求的过程sendRequest（）：

{% highlight go %}
/ sendRequest writes out various information of an RPC such as Context and Message.
func sendRequest(ctx context.Context, codec Codec, compressor Compressor, callHdr *transport.CallHdr, t transport.ClientTransport, args interface{}, opts *transport.Options) (_ *transport.Stream, err error) {
	stream, err := t.NewStream(ctx, callHdr)//首先建立一个与发送目的端发送请求数据的流数据
	if err != nil {
		return nil, err
	}
	defer func() {
		if err != nil {
			// If err is connection error, t will be closed, no need to close stream here.
			if _, ok := err.(transport.ConnectionError); !ok {
				t.CloseStream(stream, err)//当该流不再使用时关闭该流
			}
		}
	}()
	var cbuf *bytes.Buffer
	if compressor != nil {
		cbuf = new(bytes.Buffer)
	}
	outBuf, err := encode(codec, args, compressor, cbuf)
	if err != nil {
		return nil, Errorf(codes.Internal, "grpc: %v", err)
	}
	err = t.Write(stream, outBuf, opts)//向该数据流写入请求数据
	// t.NewStream(...) could lead to an early rejection of the RPC (e.g., the service/method
	// does not exist.) so that t.Write could get io.EOF from wait(...). Leave the following
	// recvResponse to get the final status.
	if err != nil && err != io.EOF {
		return nil, err
	}
	// Sent successfully.
	return stream, nil
}
{% endhighlight %}

### sendRequest()步骤有三个步骤：<br>
1.与请求目的段建立数据传输流<br>
2.向建立好的数据流写入请求数据<br>
3.关闭数据流<br>

第一步中的NewStream(ctx, callHdr)是通过callHdr中的调用方法名或其他相关信息建立连接，根据http2Client中transports对象中的相关信息创建http协议请求头部文件及url路径，若contained的api中没有请求中所需要调用的命令则会报错流连接建立失败，若流成功建立则继续向流写入参数opts数据，t.Write(stream, outBuf, opts)传输该命令执行所需要的参数相关信息。

### 下一章我们将从containerd服务器端接收请求数据实现容器检查点文件创建开始分析。。。
