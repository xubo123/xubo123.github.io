---
layout:     post
title:      "kubernetes API Server源码分析"
subtitle:   "API Server源码分析"
date:       2018-08-24 11:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - kubernetes源码
---

# api-server 源码分析

API Server在k8s中有着非常重要的作用，API-Server负责和etcd进行交互，etcd感觉就像是保存集群整体结构的状态信息数据中心（POD,NODE的信息等），k8s中的其它组件都不会和etcd进行交互，只有API Server可以和etcd进行交互,api-server具有如下功能：

* 整个集群管理的API接口：所有对集群进行的查询和管理都是通过API进行
* 集群内部各个模块之间通信的枢纽：所有模块之间并不会互相调用，而是通过和API Server打交道完成这部分的工作
* 集群的安全控制:API Server提供的验证和授权保证了整个集群的安全

在介绍API Server源码之前，先介绍两个GO语言的框架：

1. GO语言的http包使用
2. 基于RESTful风格的go-restful包

## http包的使用

[http包详解](https://www.cnblogs.com/cynchanpin/p/6936307.html)

我们需要关注的是http中的Server结构，该结构中有一个Handler属性，我们通过设置该Handler就可以设置根据请求来获得指定的路由

```  
s := &http.Server{
     Addr:           ":8080",
     Handler:        myHandler,
     ReadTimeout:    10 * time.Second,
     WriteTimeout:   10 * time.Second,
     MaxHeaderBytes: 1 << 20,

 }
```

## gorestful包

restful一种软件架构风格、设计风格，而不是标准，只是提供了一组设计原则和约束条件。它主要用于客户端和服务器交互类的软件。基于这个风格设计的软件可以更简洁，更有层次，更易于实现缓存等机制。REST 指的是一组架构约束条件和原则。满足这些约束条件和原则的应用程序或设计就是 RESTful:

* Web 应用程序最重要的 REST 原则是，客户端和服务器之间的交互在请求之间是无状态的
* 另一个重要的 REST 原则是分层系统，这表示组件无法了解它与之交互的中间层以外的组件

架构：使用RPC样式架构

* 客户端将一个装满数据的信封(包括方法和参数信息),通过http发送给服务器端
* 服务器端打开信封并使用传递过来的参数指定的方法
* 方法执行后的结果同样打包到一个信封，发回给客户端

特点：

* 在REST样式的WEB服务中，每个资源都有一个地址，资源本身就是方法调用的目标
* 在RPC样式的架构中，关注点在于方法，REST架构中，关注点在于资源
* java中有可以帮助构建RESTful Web服务的框架，它实现对各种RESTful系统的资源、表示、连接器和媒体类型之类的概念，
    - 在RESTlet框架中，客户端和服务器端都是组件，组件通过连接器互相通信

REST原则描述网络中client-server的一种交互形式，即用**URL(地址)定位资源**，用**HTTP方法(post,get)描述操作的交互形式**。如果CS之间交互的网络接口满足REST风格，则称为RESTful API。以下是 理解RESTful架构 总结的REST原则：

* 网络上的资源**通过URI统一标示**。
* 客户端和服务器之间传递，这种资源的某种表现层。表现层可以是**json，文本，二进制或者图片**等。
* 客户端通过HTTP的四个动词，对服务端资源进行操作，实现表现层状态转化

### go-restful

[参考](https://www.jb51.net/article/137113.htm)

go-restful is a package for building REST-style Web Services using Google Go。go-restful定义了Container WebService和Route三个重要数据结构。

* **Route** 表示一条路由，包含 URL/HTTP method/输入输出类型/回调处理函数RouteFunction
* **WebService** 表示一个服务，由多个Route组成，他们共享同一个Root Path
* **Container** 表示一个服务器，由多个WebService和一个 http.ServerMux 组成，使用RouteSelector进行分发


#### route 构造路由信息

通过RouteBuilder构造Route信息，Path结合了rootPath和subPath。Function是路由Handler，即处理函数，它通过 ws.Get(subPath).To(function) 的方式加入。Filters实现了个类似gRPC拦截器的东西，也类似go-chassis的chain
```
type Route struct {
  Method  string
  Produces []string
  Consumes []string
  Path   string // 注意是webservice的根路径＋子路径webservice root path + described path
  Function RouteFunction//被路由到的方法
  Filters []FilterFunction//过滤器，被过滤的路径
  If    []RouteSelectionConditionFunction
  // cached values for dispatching
  relativePath string
  pathParts  []string
  pathExpr   *pathExpression
  // documentation
  Doc           string
  Notes          string
  Operation        string
  ParameterDocs      []*Parameter
  ResponseErrors     map[int]ResponseError
  ReadSample, WriteSample interface{} 
  Metadata map[string]interface{}
  Deprecated bool
}
```

#### webservice提供一组网络服务路由

每组webservice表示一个共享rootPath的服务，其中rootPath通过 ws.Path() 设置。

```
type WebService struct {
  rootPath    string//根路径
  pathExpr    *pathExpression 
  routes     []Route
  produces    []string
  consumes    []string
  pathParameters []*Parameter
  filters    []FilterFunction//拦截器
  documentation string
  apiVersion   string
 
  typeNameHandleFunc TypeNameHandleFunction
  dynamicRoutes bool
  routesLock sync.RWMutex
}
```

#### container提供多组网络服务的集合

container是根据标准库http的路由器ServeMux写的，并且它通过ServeMux的路由表实现了Handler接口，ServeMux就是一组路由关系，其中有一个映射map,根据路径调用对应结构中的Mehtod，往Container内添加WebService，内部维护的webServices**不能有重复的RootPath**

```
type Container struct {
  webServicesLock    sync.RWMutex
  webServices      []*WebService//多个网络服务
  ServeMux        *http.ServeMux//网络路由
  isRegisteredOnRoot   bool
  containerFilters    []FilterFunction
  doNotRecover      bool // default is true
  recoverHandleFunc   RecoverHandleFunction
  serviceErrorHandleFunc ServiceErrorHandleFunction
  router         RouteSelector // default is a CurlyRouter
  contentEncodingEnabled bool     // default is false
}

type ServeMux struct {
        mu    sync.RWMutex
        m     map[string]muxEntry
        hosts bool // whether any patterns contain hostnames
    }

type muxEntry struct {
        explicit bool
        h        Handler
        pattern  string
    }
```

#### dispatch实现路由分发

server侧的主要功能就是路由选择和分发。http包实现了一个 ServeMux ，go-restful在这个基础上封装了多个服务，如何在从container开始将路由分发给webservice，再由webservice分发给具体处理函数。这些都在 dispatch 中实现。

* SelectRoute根据Req在注册的WebService中选择匹配的WebService和匹配的Route。其中路由选择器默认是 CurlyRouter 。
* 解析pathParams，将wrap的请求和相应交给路由的处理函数处理。如果有filters定义，则链式处理

```go
func (c *Container)dispatch(httpWriter http.ResponseWriter, httpRequest *http.Request) {
  func() {
    c.webServicesLock.RLock()
    defer c.webServicesLock.RUnlock()
    webService, route, err = c.router.SelectRoute(
      c.webServices,
      httpRequest)//选择匹配request的webservice和route
  }()
 
  pathProcessor, routerProcessesPath := c.router.(PathProcessor)
  pathParams := pathProcessor.ExtractParameters(route, webService, httpRequest.URL.Path)//解析路径参数
  wrappedRequest, wrappedResponse := route.wrapRequestResponse(writer,
  httpRequest, pathParams)
 
  if len(c.containerFilters)+len(webService.filters)+len(route.Filters) > 0 {
    chain := FilterChain{Filters: allFilters, Target: func(req *Request, resp *Response) {
      // handle request by route after passing all filters
      route.Function(wrappedRequest, wrappedResponse)//调用路由对应的处理函数
    }}
    chain.ProcessFilter(wrappedRequest, wrappedResponse)
  } else {
    route.Function(wrappedRequest, wrappedResponse)
  }
}
```

## 源码总结

APIServer启动完成路由，主要使用go-restful包，构建route->webservice-> container的网络路由模块，启动服务器kubeApiServer, extensionApiServer, aggreatorServer三个服务器，每个服务器内部都构造一个接口提供器，接口提供器内部包含满足restful规范的路由信息的结构，然后将这些接口API路由信息都注册到server.Handler.GoRestfulContainer的container中去提供路由服务。

## API Server启动

同样，从cmd/kube-apiserver/apiserver.go的main()函数入口：

1. 构建APIServer命令
2. execute命令

构建的APIServerCommand中的RUN函数为：

```go

// Run runs the specified APIServer.  This should never exit.
func Run(completeOptions completedServerRunOptions, stopCh <-chan struct{}) error {
    // To help debugging, immediately log version
    glog.Infof("Version: %+v", version.Get())
    server, err := CreateServerChain(completeOptions, stopCh)
    if err != nil {
        return err
    }
    return server.PrepareRun().Run(stopCh)
}
```

其中创建createServerChain()创建一系列server

* kubeAPIServer:k8s的核心接口服务器
* apiExtensionsServer:一些可以扩展的API服务器
* aggregatorServer:聚合服务器，来提供对服务器访问的整合（包括一些webhook完成对安全的认证，或参数默认值的填充）


```go

// CreateServerChain creates the apiservers connected via delegation.
func CreateServerChain(completedOptions completedServerRunOptions, stopCh <-chan struct{}) (*genericapiserver.GenericAPIServer, error) {
  nodeTunneler, proxyTransport, err := CreateNodeDialer(completedOptions)
  if err != nil {
    return nil, err
  }

  kubeAPIServerConfig, insecureServingInfo, serviceResolver, pluginInitializer, admissionPostStartHook, err := CreateKubeAPIServerConfig(completedOptions, nodeTunneler, proxyTransport)//创建kubeAPIServer的配置文件
  if err != nil {
    return nil, err
  }

  // If additional API servers are added, they should be gated.
  apiExtensionsConfig, err := createAPIExtensionsConfig(*kubeAPIServerConfig.GenericConfig, kubeAPIServerConfig.ExtraConfig.VersionedInformers, pluginInitializer, completedOptions.ServerRunOptions, completedOptions.MasterCount)
  if err != nil {
    return nil, err
  }
  apiExtensionsServer, err := createAPIExtensionsServer(apiExtensionsConfig, genericapiserver.NewEmptyDelegate())
  if err != nil {
    return nil, err
  }

  kubeAPIServer, err := CreateKubeAPIServer(kubeAPIServerConfig, apiExtensionsServer.GenericAPIServer, admissionPostStartHook)//根据配置文件创建kubeAPIServer服务器对象,以及可扩展API服务器对象apiExtensionServer
  if err != nil {
    return nil, err
  }

  // otherwise go down the normal path of standing the aggregator up in front of the API server
  // this wires up openapi
  kubeAPIServer.GenericAPIServer.PrepareRun()//准备一些kubeAPIServer运行前的相关信息

  // This will wire up openapi for extension api server
  apiExtensionsServer.GenericAPIServer.PrepareRun()//准备一些apiExtensionsServer运行前的相关信息

  // aggregator comes last in the chain
  aggregatorConfig, err := createAggregatorConfig(*kubeAPIServerConfig.GenericConfig, completedOptions.ServerRunOptions, kubeAPIServerConfig.ExtraConfig.VersionedInformers, serviceResolver, proxyTransport, pluginInitializer)//创建聚合服务器的配置信息
  if err != nil {
    return nil, err
  }
  aggregatorServer, err := createAggregatorServer(aggregatorConfig, kubeAPIServer.GenericAPIServer, apiExtensionsServer.Informers)//创建聚合服务器对象
  if err != nil {
    // we don't need special handling for innerStopCh because the aggregator server doesn't create any go routines
    return nil, err
  }

  if insecureServingInfo != nil {
    insecureHandlerChain := kubeserver.BuildInsecureHandlerChain(aggregatorServer.GenericAPIServer.UnprotectedHandler(), kubeAPIServerConfig.GenericConfig)
    if err := insecureServingInfo.Serve(insecureHandlerChain, kubeAPIServerConfig.GenericConfig.RequestTimeout, stopCh); err != nil {
      return nil, err
    }
  }

  return aggregatorServer.GenericAPIServer, nil
}
```

我们先查看kubeAPIServer核心服务器的创建过程CreateKubeAPIServer，其中涉及到API的注册：

* 主要根据传进来的并完成填充的kubeAPIServerConfig信息来创建一个kubeapiServer对象
* 创建过程中就会涉及到对遗留API和标准restful API对象到Container的注册操作

```go
func CreateKubeAPIServer(kubeAPIServerConfig *master.Config, delegateAPIServer genericapiserver.DelegationTarget, admissionPostStartHook genericapiserver.PostStartHookFunc) (*master.Master, error) {
  kubeAPIServer, err := kubeAPIServerConfig.Complete().New(delegateAPIServer)//完成kubeAPIServer的配置信息的填充，然后根据该配置信息New一个kubeAPIServer核心服务器
  if err != nil {
    return nil, err
  }

  kubeAPIServer.GenericAPIServer.AddPostStartHookOrDie("start-kube-apiserver-admission-initializer", admissionPostStartHook)//添加启动后的钩子函数

  return kubeAPIServer, nil
}
```

进入到New函数内部，涉及到核心服务器kubeAPIServer的创建及API的注册过程：

* 创建一个kubeapiServer对象，将该server对象存放到Master结构中去
* 配置遗留的API接口并注册到server的Container结构中去
* 配置标准的restful API接口到server的Container中去

```go
//RESTStorageProvider是一个工厂模式中的类型
type RESTStorageProvider interface {
  GroupName() string
  NewRESTStorage(apiResourceConfigSource serverstorage.APIResourceConfigSource, restOptionsGetter generic.RESTOptionsGetter) (genericapiserver.APIGroupInfo, bool)
}
//开始创建kubeAPIServer
func (c completedConfig) New(delegationTarget genericapiserver.DelegationTarget) (*Master, error) {
  if reflect.DeepEqual(c.ExtraConfig.KubeletClientConfig, kubeletclient.KubeletClientConfig{}) {
    return nil, fmt.Errorf("Master.New() called with empty config.KubeletClientConfig")
  }

  s, err := c.GenericConfig.New("kube-apiserver", delegationTarget)//delegationTarget为可扩展服务器apiExtensiongServer对象来创建一个新的kubeapiserver服务器对象
  if err != nil {
    return nil, err
  }

  if c.ExtraConfig.EnableLogsSupport {
    routes.Logs{}.Install(s.Handler.GoRestfulContainer)
  }

  m := &Master{
    GenericAPIServer: s,
  }

  // install legacy rest storage
  if c.ExtraConfig.APIResourceConfigSource.VersionEnabled(apiv1.SchemeGroupVersion) {
    legacyRESTStorageProvider := corerest.LegacyRESTStorageProvider{
      StorageFactory:              c.ExtraConfig.StorageFactory,
      ProxyTransport:              c.ExtraConfig.ProxyTransport,
      KubeletClientConfig:         c.ExtraConfig.KubeletClientConfig,
      EventTTL:                    c.ExtraConfig.EventTTL,
      ServiceIPRange:              c.ExtraConfig.ServiceIPRange,
      ServiceNodePortRange:        c.ExtraConfig.ServiceNodePortRange,
      LoopbackClientConfig:        c.GenericConfig.LoopbackClientConfig,
      ServiceAccountIssuer:        c.ExtraConfig.ServiceAccountIssuer,
      ServiceAccountAPIAudiences:  c.ExtraConfig.ServiceAccountAPIAudiences,
      ServiceAccountMaxExpiration: c.ExtraConfig.ServiceAccountMaxExpiration,
    }//遗留的REST存储结构提供器
    m.InstallLegacyAPI(&c, c.GenericConfig.RESTOptionsGetter, legacyRESTStorageProvider)//根据接口提供器和配置信息来安装注册以前遗留的API
  }

  // The order here is preserved in discovery.
  // If resources with identical names exist in more than one of these groups (e.g. "deployments.apps"" and "deployments.extensions"),
  // the order of this list determines which group an unqualified resource name (e.g. "deployments") should prefer.
  // This priority order is used for local discovery, but it ends up aggregated in `k8s.io/kubernetes/cmd/kube-apiserver/app/aggregator.go
  // with specific priorities.
  // TODO: describe the priority all the way down in the RESTStorageProviders and plumb it back through the various discovery
  // handlers that we have.
  restStorageProviders := []RESTStorageProvider{
    //如下接口包括：认证、autoscaling、batch、证书、协调、扩展、网络、策略、rbacrest、调度、设置、存储等API接口
    //其中每个接口在/kubernetes/pkg/registry/extensions(对应extensionsrest)/rest/storage_extension.go中有相应的路由信息storage := map[string]rest.Storage{}
    authenticationrest.RESTStorageProvider{Authenticator: c.GenericConfig.Authentication.Authenticator},
    authorizationrest.RESTStorageProvider{Authorizer: c.GenericConfig.Authorization.Authorizer, RuleResolver: c.GenericConfig.RuleResolver},
    autoscalingrest.RESTStorageProvider{},
    batchrest.RESTStorageProvider{},
    certificatesrest.RESTStorageProvider{},
    coordinationrest.RESTStorageProvider{},
    extensionsrest.RESTStorageProvider{},
    networkingrest.RESTStorageProvider{},
    policyrest.RESTStorageProvider{},
    rbacrest.RESTStorageProvider{Authorizer: c.GenericConfig.Authorization.Authorizer},
    schedulingrest.RESTStorageProvider{},
    settingsrest.RESTStorageProvider{},
    storagerest.RESTStorageProvider{},
    // keep apps after extensions so legacy clients resolve the extensions versions of shared resource names.
    // See https://github.com/kubernetes/kubernetes/issues/42392
    appsrest.RESTStorageProvider{},
    admissionregistrationrest.RESTStorageProvider{},
    eventsrest.RESTStorageProvider{TTL: c.ExtraConfig.EventTTL},
  }//标准的rest存储接口提供器
  m.InstallAPIs(c.ExtraConfig.APIResourceConfigSource, c.GenericConfig.RESTOptionsGetter, restStorageProviders...)
//安装注册当前标准的API
  if c.ExtraConfig.Tunneler != nil {
    m.installTunneler(c.ExtraConfig.Tunneler, corev1client.NewForConfigOrDie(c.GenericConfig.LoopbackClientConfig).Nodes())
  }

  m.GenericAPIServer.AddPostStartHookOrDie("ca-registration", c.ExtraConfig.ClientCARegistrationHook.PostStartHook)
  m.GenericAPIServer.AddPostStartHookOrDie("start-kube-apiserver-informers", func(context genericapiserver.PostStartHookContext) error {
    if c.ExtraConfig.InternalInformers != nil {
      c.ExtraConfig.InternalInformers.Start(context.StopCh)
    }
    return nil
  })

  return m, nil
}

```



对应接口extensionsrest的映射信息如下，也就是说我们通过结构RESTStorageProvider可以调用v1beta1Storage获取到该接口的映射信息map[string]rest.Storage：

```go
//创建一个接口并返回API组的相关信息
func (p RESTStorageProvider) NewRESTStorage(apiResourceConfigSource serverstorage.APIResourceConfigSource, restOptionsGetter generic.RESTOptionsGetter) (genericapiserver.APIGroupInfo, bool) {
  apiGroupInfo := genericapiserver.NewDefaultAPIGroupInfo(extensions.GroupName, legacyscheme.Scheme, legacyscheme.ParameterCodec, legacyscheme.Codecs)
  // If you add a version here, be sure to add an entry in `k8s.io/kubernetes/cmd/kube-apiserver/app/aggregator.go with specific priorities.
  // TODO refactor the plumbing to provide the information in the APIGroupInfo

  if apiResourceConfigSource.VersionEnabled(extensionsapiv1beta1.SchemeGroupVersion) {
    apiGroupInfo.VersionedResourcesStorageMap[extensionsapiv1beta1.SchemeGroupVersion.Version] = p.v1beta1Storage(apiResourceConfigSource, restOptionsGetter)
  }

  return apiGroupInfo, true
}

//如下所示为对应/kubernetes/pkg/registry/extensions(对应extensionsrest)/rest/storage_extension.go的路由映射信息
func (p RESTStorageProvider) v1beta1Storage(apiResourceConfigSource serverstorage.APIResourceConfigSource, restOptionsGetter generic.RESTOptionsGetter) map[string]rest.Storage {
  storage := map[string]rest.Storage{}

  // This is a dummy replication controller for scale subresource purposes.
  // TODO: figure out how to enable this only if needed as a part of scale subresource GA.
  controllerStorage := expcontrollerstore.NewStorage(restOptionsGetter)
  storage["replicationcontrollers"] = controllerStorage.ReplicationController
  storage["replicationcontrollers/scale"] = controllerStorage.Scale

  // daemonsets
  daemonSetStorage, daemonSetStatusStorage := daemonstore.NewREST(restOptionsGetter)
  storage["daemonsets"] = daemonSetStorage.WithCategories(nil)
  storage["daemonsets/status"] = daemonSetStatusStorage

  //deployments
  deploymentStorage := deploymentstore.NewStorage(restOptionsGetter)
  storage["deployments"] = deploymentStorage.Deployment.WithCategories(nil)
  storage["deployments/status"] = deploymentStorage.Status
  storage["deployments/rollback"] = &RollbackREST{deploymentStorage.Rollback}
  storage["deployments/scale"] = deploymentStorage.Scale
  // ingresses
  ingressStorage, ingressStatusStorage := ingressstore.NewREST(restOptionsGetter)
  storage["ingresses"] = ingressStorage
  storage["ingresses/status"] = ingressStatusStorage

  // podsecuritypolicy
  podSecurityPolicyStorage := pspstore.NewREST(restOptionsGetter)
  storage["podSecurityPolicies"] = podSecurityPolicyStorage

  // replicasets
  replicaSetStorage := replicasetstore.NewStorage(restOptionsGetter)
  storage["replicasets"] = replicaSetStorage.ReplicaSet.WithCategories(nil)
  storage["replicasets/status"] = replicaSetStorage.Status
  storage["replicasets/scale"] = replicaSetStorage.Scale

  // networkpolicies
  networkExtensionsStorage := networkpolicystore.NewREST(restOptionsGetter)
  storage["networkpolicies"] = networkExtensionsStorage

  return storage
}
```


查看标准的API注册过程InstallAPIs，其实注册过程就是将这些映射信息通过route,webservice的形式添加到Container中去满足restful风格，完成整个路由过程：

* 遍历所有的服务提供接口Porvider结构，通过每个结构获取API组信息
* 整合所有组的api组信息到apiGroupsInfo
* 遍历所有的api组信息进行注册安装

```go
func (m *Master) InstallAPIs(apiResourceConfigSource serverstorage.APIResourceConfigSource, restOptionsGetter generic.RESTOptionsGetter, restStorageProviders ...RESTStorageProvider) {
  apiGroupsInfo := []genericapiserver.APIGroupInfo{}
  //遍历每个标准接口提供器
  for _, restStorageBuilder := range restStorageProviders {
    groupName := restStorageBuilder.GroupName()//获取该接口的API组名称
    if !apiResourceConfigSource.AnyVersionForGroupEnabled(groupName) {
      glog.V(1).Infof("Skipping disabled API group %q.", groupName)
      continue
    }
    apiGroupInfo, enabled := restStorageBuilder.NewRESTStorage(apiResourceConfigSource, restOptionsGetter)//获取该API组的信息apiGroupInfo
    if !enabled {
      glog.Warningf("Problem initializing API group %q, skipping.", groupName)
      continue
    }
    glog.V(1).Infof("Enabling API group %q.", groupName)

    if postHookProvider, ok := restStorageBuilder.(genericapiserver.PostStartHookProvider); ok {
      name, hook, err := postHookProvider.PostStartHook()
      if err != nil {
        glog.Fatalf("Error building PostStartHook: %v", err)
      }
      m.GenericAPIServer.AddPostStartHookOrDie(name, hook)
    }

    apiGroupsInfo = append(apiGroupsInfo, apiGroupInfo)//将该api组的的信息添加到总api组集合中去
  }

  for i := range apiGroupsInfo {//遍历所有的api组进行api安装注册
    if err := m.GenericAPIServer.InstallAPIGroup(&apiGroupsInfo[i]); err != nil {
      glog.Fatalf("Error in registering group versions: %v", err)
    }
  }
}
```


遍历所有API组进行注册InstallAPIGroup:
* 安装API资源，将api的网络服务注册到kubeapiserver.Handler.GoRestfulContainer中去
* 将API服务添加到服务发现管理器中去
  - 将服务发现的网络服务添加到GoRestfulContainer中去

```go
func (s *GenericAPIServer) InstallAPIGroup(apiGroupInfo *APIGroupInfo) error {
  if len(apiGroupInfo.PrioritizedVersions[0].Group) == 0 {
    return fmt.Errorf("cannot register handler with an empty group for %#v", *apiGroupInfo)
  }
  if len(apiGroupInfo.PrioritizedVersions[0].Version) == 0 {
    return fmt.Errorf("cannot register handler with an empty version for %#v", *apiGroupInfo)
  }

  if err := s.installAPIResources(APIGroupPrefix, apiGroupInfo); err != nil {
    return err
  }//开始安装API资源

  // setup discovery
  // Install the version handler.
  // Add a handler at /apis/<groupName> to enumerate all versions supported by this group.
  apiVersionsForDiscovery := []metav1.GroupVersionForDiscovery{}
  for _, groupVersion := range apiGroupInfo.PrioritizedVersions {
    // Check the config to make sure that we elide versions that don't have any resources
    if len(apiGroupInfo.VersionedResourcesStorageMap[groupVersion.Version]) == {
      continue
    }
    apiVersionsForDiscovery = append(apiVersionsForDiscovery, metav1.GroupVersionForDiscovery{
      GroupVersion: groupVersion.String(),
      Version:      groupVersion.Version,
    })
  }
  preferredVersionForDiscovery := metav1.GroupVersionForDiscovery{
    GroupVersion: apiGroupInfo.PrioritizedVersions[0].String(),
    Version:      apiGroupInfo.PrioritizedVersions[0].Version,
  }
  apiGroup := metav1.APIGroup{
    Name:             apiGroupInfo.PrioritizedVersions[0].Group,
    Versions:         apiVersionsForDiscovery,
    PreferredVersion: preferredVersionForDiscovery,
  }

  s.DiscoveryGroupManager.AddGroup(apiGroup)//将API服务添加到服务发现管理器中去
  s.Handler.GoRestfulContainer.Add(discovery.NewAPIGroupHandler(s.Serializer, apiGroup).WebService())//将服务发现的网络服务添加到container

  return nil
}

```


installAPIResources（）API资源的安装：

* 将API信息安装到gorestful的container结构中去
  - 根据API信息生成一个**webservice安装器**
  - 安装器开始安装，根据路由信息配置得到webservice
  - 将安装得到的wenbservice添加到container中去提供网络服务路由

```go
func (s *GenericAPIServer) installAPIResources(apiPrefix string, apiGroupInfo *APIGroupInfo) error {
  for _, groupVersion := range apiGroupInfo.PrioritizedVersions {
    if len(apiGroupInfo.VersionedResourcesStorageMap[groupVersion.Version]) == 0 {
      glog.Warningf("Skipping API %v because it has no resources.", groupVersion)
      continue
    }//检测是否有该API资源

    apiGroupVersion := s.getAPIGroupVersion(apiGroupInfo, groupVersion, apiPrefix)//获取api版本信息
    if apiGroupInfo.OptionsExternalVersion != nil {
      apiGroupVersion.OptionsExternalVersion = apiGroupInfo.OptionsExternalVersion
    }
    //将API信息安装到gorestful的container结构中去
    if err := apiGroupVersion.InstallREST(s.Handler.GoRestfulContainer); err != nil {
      return fmt.Errorf("unable to setup API %v: %v", apiGroupInfo, err)
    }
  }

  return nil
}

//安装到container结构中去
func (g *APIGroupVersion) InstallREST(container *restful.Container) error {
  prefix := path.Join(g.Root, g.GroupVersion.Group, g.GroupVersion.Version)
  installer := &APIInstaller{//生成一个API安装器，包括根路径，api组信息
    group:                        g,
    prefix:                       prefix,
    minRequestTimeout:            g.MinRequestTimeout,
    enableAPIResponseCompression: g.EnableAPIResponseCompression,
  }

  apiResources, ws, registrationErrors := installer.Install()//根据获得的安装器进行安装，获得webservice结构
  versionDiscoveryHandler := discovery.NewAPIVersionHandler(g.Serializer, g.GroupVersion, staticLister{apiResources})
  versionDiscoveryHandler.AddToWebService(ws)
  container.Add(ws)//将webservice结构添加到container结构中去
  return utilerrors.NewAggregate(registrationErrors)
}

//首先将路由信息逐条安装到webservice结构
func (a *APIInstaller) Install() ([]metav1.APIResource, *restful.WebService, []error) {
  var apiResources []metav1.APIResource
  var errors []error
  ws := a.newWebService()

  // Register the paths in a deterministic (sorted) order to get a deterministic swagger spec.
  paths := make([]string, len(a.group.Storage))
  var i int = 0
  for path := range a.group.Storage {
    paths[i] = path//根据路由信息获取所有路径
    i++
  }
  sort.Strings(paths)
  for _, path := range paths {
    apiResource, err := a.registerResourceHandlers(path, a.group.Storage[path], ws)//将路径和对应的方法注册到webservice
    if err != nil {
      errors = append(errors, fmt.Errorf("error in registering resource: %s, %v", path, err))
    }
    if apiResource != nil {
      apiResources = append(apiResources, *apiResource)
    }
  }
  return apiResources, ws, errors//返回得到的api资源和webservice
}
```

在路由信息安装的过程当中registerResourceHandlers将路由信息按动作进行分类，添加action,设置对应handler的路由route然后添加到webservice中去。这部分的代码非常复杂：／vendor/k8s.io/apiserver/pkg/endpoints/installer.go 170行处

* 根据类型断言判断接口的类型isGetter,isCreater...
* 根据不同类型的接口创建不同类型的action,"LIST","PUT","WATCH","CONNECT"...
* 遍历所有的action,根据不同类型action设置不同的handler,然后将path和对应的handler设置为一个route结构
* 最后将所有设置好的routes信息均添加到webservice结构中去

```go
func (a *APIInstaller) registerResourceHandlers(path string, storage rest.Storage, ws *restful.WebService) (*metav1.APIResource, error) {
  admit := a.group.Admit

  optionsExternalVersion := a.group.GroupVersion
  if a.group.OptionsExternalVersion != nil {
    optionsExternalVersion = *a.group.OptionsExternalVersion
  }

  resource, subresource, err := splitSubresource(path)
  if err != nil {
    return nil, err
  }

  fqKindToRegister, err := a.getResourceKind(path, storage)
  if err != nil {
    return nil, err
  }

  versionedPtr, err := a.group.Creater.New(fqKindToRegister)
  if err != nil {
    return nil, err
  }
  defaultVersionedObject := indirectArbitraryPointer(versionedPtr)
  kind := fqKindToRegister.Kind
  isSubresource := len(subresource) > 0

  // If there is a subresource, namespace scoping is defined by the parent resource
  namespaceScoped := true
  if isSubresource {
    parentStorage, ok := a.group.Storage[resource]
    if !ok {
      return nil, fmt.Errorf("missing parent storage: %q", resource)
    }
    scoper, ok := parentStorage.(rest.Scoper)
    if !ok {
      return nil, fmt.Errorf("%q must implement scoper", resource)
    }
    namespaceScoped = scoper.NamespaceScoped()

  } else {
    scoper, ok := storage.(rest.Scoper)
    if !ok {
      return nil, fmt.Errorf("%q must implement scoper", resource)
    }
    namespaceScoped = scoper.NamespaceScoped()
  }

  // what verbs are supported by the storage, used to know what verbs we support per path

  //对storage进行类型断言，判断是什么什么类型的接口
  creater, isCreater := storage.(rest.Creater)//创建资源类型的接口
  namedCreater, isNamedCreater := storage.(rest.NamedCreater)
  lister, isLister := storage.(rest.Lister)
  getter, isGetter := storage.(rest.Getter)
  getterWithOptions, isGetterWithOptions := storage.(rest.GetterWithOptions)
  gracefulDeleter, isGracefulDeleter := storage.(rest.GracefulDeleter)
  collectionDeleter, isCollectionDeleter := storage.(rest.CollectionDeleter)
  updater, isUpdater := storage.(rest.Updater)
  patcher, isPatcher := storage.(rest.Patcher)
  watcher, isWatcher := storage.(rest.Watcher)//对Watch请求的接口
  connecter, isConnecter := storage.(rest.Connecter)//Connecter类型对应exec这种直接与kubelet进行通信的操作
  storageMeta, isMetadata := storage.(rest.StorageMetadata)
  if !isMetadata {
    storageMeta = defaultStorageMetadata{}
  }
  exporter, isExporter := storage.(rest.Exporter)
  if !isExporter {
    exporter = nil
  }

  versionedExportOptions, err := a.group.Creater.New(optionsExternalVersion.WithKind("ExportOptions"))
  if err != nil {
    return nil, err
  }

  if isNamedCreater {
    isCreater = true
  }

  var versionedList interface{}
  if isLister {
    list := lister.NewList()
    listGVKs, _, err := a.group.Typer.ObjectKinds(list)
    if err != nil {
      return nil, err
    }
    versionedListPtr, err := a.group.Creater.New(a.group.GroupVersion.WithKind(listGVKs[0].Kind))
    if err != nil {
      return nil, err
    }
    versionedList = indirectArbitraryPointer(versionedListPtr)
  }

  versionedListOptions, err := a.group.Creater.New(optionsExternalVersion.WithKind("ListOptions"))
  if err != nil {
    return nil, err
  }

  var versionedDeleteOptions runtime.Object
  var versionedDeleterObject interface{}
  if isGracefulDeleter {
    versionedDeleteOptions, err = a.group.Creater.New(optionsExternalVersion.WithKind("DeleteOptions"))
    if err != nil {
      return nil, err
    }
    versionedDeleterObject = indirectArbitraryPointer(versionedDeleteOptions)
  }

  versionedStatusPtr, err := a.group.Creater.New(optionsExternalVersion.WithKind("Status"))
  if err != nil {
    return nil, err
  }
  versionedStatus := indirectArbitraryPointer(versionedStatusPtr)
  var (
    getOptions             runtime.Object
    versionedGetOptions    runtime.Object
    getOptionsInternalKind schema.GroupVersionKind
    getSubpath             bool
  )
  if isGetterWithOptions {
    getOptions, getSubpath, _ = getterWithOptions.NewGetOptions()
    getOptionsInternalKinds, _, err := a.group.Typer.ObjectKinds(getOptions)
    if err != nil {
      return nil, err
    }
    getOptionsInternalKind = getOptionsInternalKinds[0]
    versionedGetOptions, err = a.group.Creater.New(a.group.GroupVersion.WithKind(getOptionsInternalKind.Kind))
    if err != nil {
      versionedGetOptions, err = a.group.Creater.New(optionsExternalVersion.WithKind(getOptionsInternalKind.Kind))
      if err != nil {
        return nil, err
      }
    }
    isGetter = true
  }

  var versionedWatchEvent interface{}
  if isWatcher {
    versionedWatchEventPtr, err := a.group.Creater.New(a.group.GroupVersion.WithKind("WatchEvent"))
    if err != nil {
      return nil, err
    }
    versionedWatchEvent = indirectArbitraryPointer(versionedWatchEventPtr)
  }

  var (
    connectOptions             runtime.Object
    versionedConnectOptions    runtime.Object
    connectOptionsInternalKind schema.GroupVersionKind
    connectSubpath             bool
  )
  if isConnecter {//如果是Connecter类型的接口，获取相关的连接选项信息connectOptions
    connectOptions, connectSubpath, _ = connecter.NewConnectOptions()
    if connectOptions != nil {
      connectOptionsInternalKinds, _, err := a.group.Typer.ObjectKinds(connectOptions)
      if err != nil {
        return nil, err
      }

      connectOptionsInternalKind = connectOptionsInternalKinds[0]
      versionedConnectOptions, err = a.group.Creater.New(a.group.GroupVersion.WithKind(connectOptionsInternalKind.Kind))
      if err != nil {
        versionedConnectOptions, err = a.group.Creater.New(optionsExternalVersion.WithKind(connectOptionsInternalKind.Kind))
        if err != nil {
          return nil, err
        }
      }
    }
  }

  allowWatchList := isWatcher && isLister // watching on lists is allowed only for kinds that support both watch and list.
  nameParam := ws.PathParameter("name", "name of the "+kind).DataType("string")
  pathParam := ws.PathParameter("path", "path to the resource").DataType("string")

  params := []*restful.Parameter{}
  actions := []action{}

  var resourceKind string
  kindProvider, ok := storage.(rest.KindProvider)
  if ok {
    resourceKind = kindProvider.Kind()
  } else {
    resourceKind = kind
  }

  tableProvider, _ := storage.(rest.TableConvertor)

  var apiResource metav1.APIResource
  // Get the list of actions for the given scope.
  switch {
  case !namespaceScoped://没有namespace类型的接口
    // Handle non-namespace scoped resources like nodes.
    resourcePath := resource
    resourceParams := params
    itemPath := resourcePath + "/{name}"
    nameParams := append(params, nameParam)
    proxyParams := append(nameParams, pathParam)
    suffix := ""
    if isSubresource {
      suffix = "/" + subresource
      itemPath = itemPath + suffix
      resourcePath = itemPath
      resourceParams = nameParams
    }
    apiResource.Name = path
    apiResource.Namespaced = false
    apiResource.Kind = resourceKind
    namer := handlers.ContextBasedNaming{
      SelfLinker:         a.group.Linker,
      ClusterScoped:      true,
      SelfLinkPathPrefix: gpath.Join(a.prefix, resource) + "/",
      SelfLinkPathSuffix: suffix,
    }

    // Handler for standard REST verbs (GET, PUT, POST and DELETE).
    // Add actions at the resource path: /api/apiVersion/resource
    actions = appendIf(actions, action{"LIST", resourcePath, resourceParams, namer, false}, isLister)
    actions = appendIf(actions, action{"POST", resourcePath, resourceParams, namer, false}, isCreater)
    actions = appendIf(actions, action{"DELETECOLLECTION", resourcePath, resourceParams, namer, false}, isCollectionDeleter)
    // DEPRECATED in 1.11
    actions = appendIf(actions, action{"WATCHLIST", "watch/" + resourcePath, resourceParams, namer, false}, allowWatchList)

    // Add actions at the item path: /api/apiVersion/resource/{name}
    actions = appendIf(actions, action{"GET", itemPath, nameParams, namer, false}, isGetter)
    if getSubpath {
      actions = appendIf(actions, action{"GET", itemPath + "/{path:*}", proxyParams, namer, false}, isGetter)
    }
    actions = appendIf(actions, action{"PUT", itemPath, nameParams, namer, false}, isUpdater)
    actions = appendIf(actions, action{"PATCH", itemPath, nameParams, namer, false}, isPatcher)
    actions = appendIf(actions, action{"DELETE", itemPath, nameParams, namer, false}, isGracefulDeleter)
    // DEPRECATED in 1.11
    actions = appendIf(actions, action{"WATCH", "watch/" + itemPath, nameParams, namer, false}, isWatcher)
    actions = appendIf(actions, action{"CONNECT", itemPath, nameParams, namer, false}, isConnecter)
    actions = appendIf(actions, action{"CONNECT", itemPath + "/{path:*}", proxyParams, namer, false}, isConnecter && connectSubpath)
    break
  default:
    namespaceParamName := "namespaces"
    // Handler for standard REST verbs (GET, PUT, POST and DELETE).
    namespaceParam := ws.PathParameter("namespace", "object name and auth scope, such as for teams and projects").DataType("string")
    namespacedPath := namespaceParamName + "/{" + "namespace" + "}/" + resource
    namespaceParams := []*restful.Parameter{namespaceParam}

    resourcePath := namespacedPath
    resourceParams := namespaceParams
    itemPath := namespacedPath + "/{name}"
    nameParams := append(namespaceParams, nameParam)
    proxyParams := append(nameParams, pathParam)
    itemPathSuffix := ""
    if isSubresource {
      itemPathSuffix = "/" + subresource
      itemPath = itemPath + itemPathSuffix
      resourcePath = itemPath
      resourceParams = nameParams
    }
    apiResource.Name = path
    apiResource.Namespaced = true
    apiResource.Kind = resourceKind
    namer := handlers.ContextBasedNaming{
      SelfLinker:         a.group.Linker,
      ClusterScoped:      false,
      SelfLinkPathPrefix: gpath.Join(a.prefix, namespaceParamName) + "/",
      SelfLinkPathSuffix: itemPathSuffix,
    }
    //根据之前类型断言判断的接口类型，来添加actions操作
    actions = appendIf(actions, action{"LIST", resourcePath, resourceParams, namer, false}, isLister)
    actions = appendIf(actions, action{"POST", resourcePath, resourceParams, namer, false}, isCreater)
    actions = appendIf(actions, action{"DELETECOLLECTION", resourcePath, resourceParams, namer, false}, isCollectionDeleter)
    // DEPRECATED in 1.11
    actions = appendIf(actions, action{"WATCHLIST", "watch/" + resourcePath, resourceParams, namer, false}, allowWatchList)

    actions = appendIf(actions, action{"GET", itemPath, nameParams, namer, false}, isGetter)
    if getSubpath {
      actions = appendIf(actions, action{"GET", itemPath + "/{path:*}", proxyParams, namer, false}, isGetter)
    }
    actions = appendIf(actions, action{"PUT", itemPath, nameParams, namer, false}, isUpdater)
    actions = appendIf(actions, action{"PATCH", itemPath, nameParams, namer, false}, isPatcher)
    actions = appendIf(actions, action{"DELETE", itemPath, nameParams, namer, false}, isGracefulDeleter)
    // DEPRECATED in 1.11
    actions = appendIf(actions, action{"WATCH", "watch/" + itemPath, nameParams, namer, false}, isWatcher)
    actions = appendIf(actions, action{"CONNECT", itemPath, nameParams, namer, false}, isConnecter)
    actions = appendIf(actions, action{"CONNECT", itemPath + "/{path:*}", proxyParams, namer, false}, isConnecter && connectSubpath)

    // list or post across namespace.
    // For ex: LIST all pods in all namespaces by sending a LIST request at /api/apiVersion/pods.
    // TODO: more strongly type whether a resource allows these actions on "all namespaces" (bulk delete)
    if !isSubresource {
      actions = appendIf(actions, action{"LIST", resource, params, namer, true}, isLister)
      // DEPRECATED in 1.11
      actions = appendIf(actions, action{"WATCHLIST", "watch/" + resource, params, namer, true}, allowWatchList)
    }
    break
  }

  // Create Routes for the actions.
  // TODO: Add status documentation using Returns()
  // Errors (see api/errors/errors.go as well as go-restful router):
  // http.StatusNotFound, http.StatusMethodNotAllowed,
  // http.StatusUnsupportedMediaType, http.StatusNotAcceptable,
  // http.StatusBadRequest, http.StatusUnauthorized, http.StatusForbidden,
  // http.StatusRequestTimeout, http.StatusConflict, http.StatusPreconditionFailed,
  // http.StatusUnprocessableEntity, http.StatusInternalServerError,
  // http.StatusServiceUnavailable
  // and api error codes
  // Note that if we specify a versioned Status object here, we may need to
  // create one for the tests, also
  // Success:
  // http.StatusOK, http.StatusCreated, http.StatusAccepted, http.StatusNoContent
  //
  // test/integration/auth_test.go is currently the most comprehensive status code test

  mediaTypes, streamMediaTypes := negotiation.MediaTypesForSerializer(a.group.Serializer)
  allMediaTypes := append(mediaTypes, streamMediaTypes...)
  ws.Produces(allMediaTypes...)

  kubeVerbs := map[string]struct{}{}
  reqScope := handlers.RequestScope{
    Serializer:      a.group.Serializer,
    ParameterCodec:  a.group.ParameterCodec,
    Creater:         a.group.Creater,
    Convertor:       a.group.Convertor,
    Defaulter:       a.group.Defaulter,
    Typer:           a.group.Typer,
    UnsafeConvertor: a.group.UnsafeConvertor,
    Authorizer:      a.group.Authorizer,

    // TODO: Check for the interface on storage
    TableConvertor: tableProvider,

    // TODO: This seems wrong for cross-group subresources. It makes an assumption that a subresource and its parent are in the same group version. Revisit this.
    Resource:    a.group.GroupVersion.WithResource(resource),
    Subresource: subresource,
    Kind:        fqKindToRegister,

    MetaGroupVersion: metav1.SchemeGroupVersion,
  }
  if a.group.MetaGroupVersion != nil {
    reqScope.MetaGroupVersion = *a.group.MetaGroupVersion
  }
  reqScope.OpenAPISchema, err = a.getOpenAPISchema(ws.RootPath(), resource, fqKindToRegister, defaultVersionedObject)
  if err != nil {
    return nil, fmt.Errorf("unable to get openapi schema for %v: %v", fqKindToRegister, err)
  }
  for _, action := range actions {
    producedObject := storageMeta.ProducesObject(action.Verb)
    if producedObject == nil {
      producedObject = defaultVersionedObject
    }
    reqScope.Namer = action.Namer

    requestScope := "cluster"
    var namespaced string
    var operationSuffix string
    if apiResource.Namespaced {
      requestScope = "namespace"
      namespaced = "Namespaced"
    }
    if strings.HasSuffix(action.Path, "/{path:*}") {
      requestScope = "resource"
      operationSuffix = operationSuffix + "WithPath"
    }
    if action.AllNamespaces {
      requestScope = "cluster"
      operationSuffix = operationSuffix + "ForAllNamespaces"
      namespaced = ""
    }

    if kubeVerb, found := toDiscoveryKubeVerb[action.Verb]; found {
      if len(kubeVerb) != 0 {
        kubeVerbs[kubeVerb] = struct{}{}
      }
    } else {
      return nil, fmt.Errorf("unknown action verb for discovery: %s", action.Verb)
    }

    routes := []*restful.RouteBuilder{}

    // If there is a subresource, kind should be the parent's kind.
    if isSubresource {
      parentStorage, ok := a.group.Storage[resource]
      if !ok {
        return nil, fmt.Errorf("missing parent storage: %q", resource)
      }

      fqParentKind, err := a.getResourceKind(resource, parentStorage)
      if err != nil {
        return nil, err
      }
      kind = fqParentKind.Kind
    }

    verbOverrider, needOverride := storage.(StorageMetricsOverride)
    //遍历actons的每个action,根据action的动作"GET","SET","LIST","PUT","PATCH","DELETE","DELETECOLLECTION","WATCHLIST","CONNECT"的不同类型，来设置不同的handler的route信息，最后将所有的route信息添加到webservice中去
    switch action.Verb {
    case "GET": // Get a resource.
      var handler restful.RouteFunction
      if isGetterWithOptions {
        handler = restfulGetResourceWithOptions(getterWithOptions, reqScope, isSubresource)
      } else {
        handler = restfulGetResource(getter, exporter, reqScope)
      }

      if needOverride {
        // need change the reported verb
        handler = metrics.InstrumentRouteFunc(verbOverrider.OverrideMetricsVerb(action.Verb), resource, subresource, requestScope, handler)
      } else {
        handler = metrics.InstrumentRouteFunc(action.Verb, resource, subresource, requestScope, handler)
      }

      if a.enableAPIResponseCompression {
        handler = genericfilters.RestfulWithCompression(handler)
      }
      doc := "read the specified " + kind
      if isSubresource {
        doc = "read " + subresource + " of the specified " + kind
      }
      route := ws.GET(action.Path).To(handler).
        Doc(doc).
        Param(ws.QueryParameter("pretty", "If 'true', then the output is pretty printed.")).
        Operation("read"+namespaced+kind+strings.Title(subresource)+operationSuffix).
        Produces(append(storageMeta.ProducesMIMETypes(action.Verb), mediaTypes...)...).
        Returns(http.StatusOK, "OK", producedObject).
        Writes(producedObject)
      if isGetterWithOptions {
        if err := addObjectParams(ws, route, versionedGetOptions); err != nil {
          return nil, err
        }
      }
      if isExporter {
        if err := addObjectParams(ws, route, versionedExportOptions); err != nil {
          return nil, err
        }
      }
      addParams(route, action.Params)
      routes = append(routes, route)
    case "LIST": // List all resources of a kind.
      doc := "list objects of kind " + kind
      if isSubresource {
        doc = "list " + subresource + " of objects of kind " + kind
      }
      handler := metrics.InstrumentRouteFunc(action.Verb, resource, subresource, requestScope, restfulListResource(lister, watcher, reqScope, false, a.minRequestTimeout))
      if a.enableAPIResponseCompression {
        handler = genericfilters.RestfulWithCompression(handler)
      }
      route := ws.GET(action.Path).To(handler).
        Doc(doc).
        Param(ws.QueryParameter("pretty", "If 'true', then the output is pretty printed.")).
        Operation("list"+namespaced+kind+strings.Title(subresource)+operationSuffix).
        Produces(append(storageMeta.ProducesMIMETypes(action.Verb), allMediaTypes...)...).
        Returns(http.StatusOK, "OK", versionedList).
        Writes(versionedList)
      if err := addObjectParams(ws, route, versionedListOptions); err != nil {
        return nil, err
      }
      switch {
      case isLister && isWatcher:
        doc := "list or watch objects of kind " + kind
        if isSubresource {
          doc = "list or watch " + subresource + " of objects of kind " + kind
        }
        route.Doc(doc)
      case isWatcher:
        doc := "watch objects of kind " + kind
        if isSubresource {
          doc = "watch " + subresource + "of objects of kind " + kind
        }
        route.Doc(doc)
      }
      addParams(route, action.Params)
      routes = append(routes, route)
    case "PUT": // Update a resource.
      doc := "replace the specified " + kind
      if isSubresource {
        doc = "replace " + subresource + " of the specified " + kind
      }
      handler := metrics.InstrumentRouteFunc(action.Verb, resource, subresource, requestScope, restfulUpdateResource(updater, reqScope, admit))
      route := ws.PUT(action.Path).To(handler).
        Doc(doc).
        Param(ws.QueryParameter("pretty", "If 'true', then the output is pretty printed.")).
        Operation("replace"+namespaced+kind+strings.Title(subresource)+operationSuffix).
        Produces(append(storageMeta.ProducesMIMETypes(action.Verb), mediaTypes...)...).
        Returns(http.StatusOK, "OK", producedObject).
        // TODO: in some cases, the API may return a v1.Status instead of the versioned object
        // but currently go-restful can't handle multiple different objects being returned.
        Returns(http.StatusCreated, "Created", producedObject).
        Reads(defaultVersionedObject).
        Writes(producedObject)
      addParams(route, action.Params)
      routes = append(routes, route)
    case "PATCH": // Partially update a resource
      doc := "partially update the specified " + kind
      if isSubresource {
        doc = "partially update " + subresource + " of the specified " + kind
      }
      supportedTypes := []string{
        string(types.JSONPatchType),
        string(types.MergePatchType),
        string(types.StrategicMergePatchType),
      }
      handler := metrics.InstrumentRouteFunc(action.Verb, resource, subresource, requestScope, restfulPatchResource(patcher, reqScope, admit, supportedTypes))
      route := ws.PATCH(action.Path).To(handler).
        Doc(doc).
        Param(ws.QueryParameter("pretty", "If 'true', then the output is pretty printed.")).
        Consumes(string(types.JSONPatchType), string(types.MergePatchType), string(types.StrategicMergePatchType)).
        Operation("patch"+namespaced+kind+strings.Title(subresource)+operationSuffix).
        Produces(append(storageMeta.ProducesMIMETypes(action.Verb), mediaTypes...)...).
        Returns(http.StatusOK, "OK", producedObject).
        Reads(metav1.Patch{}).
        Writes(producedObject)
      addParams(route, action.Params)
      routes = append(routes, route)
    case "POST": // Create a resource.
      var handler restful.RouteFunction
      if isNamedCreater {
        handler = restfulCreateNamedResource(namedCreater, reqScope, admit)
      } else {
        handler = restfulCreateResource(creater, reqScope, admit)
      }
      handler = metrics.InstrumentRouteFunc(action.Verb, resource, subresource, requestScope, handler)
      article := getArticleForNoun(kind, " ")
      doc := "create" + article + kind
      if isSubresource {
        doc = "create " + subresource + " of" + article + kind
      }
      route := ws.POST(action.Path).To(handler).
        Doc(doc).
        Param(ws.QueryParameter("pretty", "If 'true', then the output is pretty printed.")).
        Operation("create"+namespaced+kind+strings.Title(subresource)+operationSuffix).
        Produces(append(storageMeta.ProducesMIMETypes(action.Verb), mediaTypes...)...).
        Returns(http.StatusOK, "OK", producedObject).
        // TODO: in some cases, the API may return a v1.Status instead of the versioned object
        // but currently go-restful can't handle multiple different objects being returned.
        Returns(http.StatusCreated, "Created", producedObject).
        Returns(http.StatusAccepted, "Accepted", producedObject).
        Reads(defaultVersionedObject).
        Writes(producedObject)
      addParams(route, action.Params)
      routes = append(routes, route)
    case "DELETE": // Delete a resource.
      article := getArticleForNoun(kind, " ")
      doc := "delete" + article + kind
      if isSubresource {
        doc = "delete " + subresource + " of" + article + kind
      }
      handler := metrics.InstrumentRouteFunc(action.Verb, resource, subresource, requestScope, restfulDeleteResource(gracefulDeleter, isGracefulDeleter, reqScope, admit))
      route := ws.DELETE(action.Path).To(handler).
        Doc(doc).
        Param(ws.QueryParameter("pretty", "If 'true', then the output is pretty printed.")).
        Operation("delete"+namespaced+kind+strings.Title(subresource)+operationSuffix).
        Produces(append(storageMeta.ProducesMIMETypes(action.Verb), mediaTypes...)...).
        Writes(versionedStatus).
        Returns(http.StatusOK, "OK", versionedStatus).
        Returns(http.StatusAccepted, "Accepted", versionedStatus)
      if isGracefulDeleter {
        route.Reads(versionedDeleterObject)
        if err := addObjectParams(ws, route, versionedDeleteOptions); err != nil {
          return nil, err
        }
      }
      addParams(route, action.Params)
      routes = append(routes, route)
    case "DELETECOLLECTION":
      doc := "delete collection of " + kind
      if isSubresource {
        doc = "delete collection of " + subresource + " of a " + kind
      }
      handler := metrics.InstrumentRouteFunc(action.Verb, resource, subresource, requestScope, restfulDeleteCollection(collectionDeleter, isCollectionDeleter, reqScope, admit))
      route := ws.DELETE(action.Path).To(handler).
        Doc(doc).
        Param(ws.QueryParameter("pretty", "If 'true', then the output is pretty printed.")).
        Operation("deletecollection"+namespaced+kind+strings.Title(subresource)+operationSuffix).
        Produces(append(storageMeta.ProducesMIMETypes(action.Verb), mediaTypes...)...).
        Writes(versionedStatus).
        Returns(http.StatusOK, "OK", versionedStatus)
      if err := addObjectParams(ws, route, versionedListOptions); err != nil {
        return nil, err
      }
      addParams(route, action.Params)
      routes = append(routes, route)
    // deprecated in 1.11
    case "WATCH": // Watch a resource.
      doc := "watch changes to an object of kind " + kind
      if isSubresource {
        doc = "watch changes to " + subresource + " of an object of kind " + kind
      }
      doc += ". deprecated: use the 'watch' parameter with a list operation instead, filtered to a single item with the 'fieldSelector' parameter."
      handler := metrics.InstrumentRouteFunc(action.Verb, resource, subresource, requestScope, restfulListResource(lister, watcher, reqScope, true, a.minRequestTimeout))
      route := ws.GET(action.Path).To(handler).
        Doc(doc).
        Param(ws.QueryParameter("pretty", "If 'true', then the output is pretty printed.")).
        Operation("watch"+namespaced+kind+strings.Title(subresource)+operationSuffix).
        Produces(allMediaTypes...).
        Returns(http.StatusOK, "OK", versionedWatchEvent).
        Writes(versionedWatchEvent)
      if err := addObjectParams(ws, route, versionedListOptions); err != nil {
        return nil, err
      }
      addParams(route, action.Params)
      routes = append(routes, route)
    // deprecated in 1.11
    case "WATCHLIST": // Watch all resources of a kind.
      doc := "watch individual changes to a list of " + kind
      if isSubresource {
        doc = "watch individual changes to a list of " + subresource + " of " + kind
      }
      doc += ". deprecated: use the 'watch' parameter with a list operation instead."
      handler := metrics.InstrumentRouteFunc(action.Verb, resource, subresource, requestScope, restfulListResource(lister, watcher, reqScope, true, a.minRequestTimeout))
      route := ws.GET(action.Path).To(handler).
        Doc(doc).
        Param(ws.QueryParameter("pretty", "If 'true', then the output is pretty printed.")).
        Operation("watch"+namespaced+kind+strings.Title(subresource)+"List"+operationSuffix).
        Produces(allMediaTypes...).
        Returns(http.StatusOK, "OK", versionedWatchEvent).
        Writes(versionedWatchEvent)
      if err := addObjectParams(ws, route, versionedListOptions); err != nil {
        return nil, err
      }
      addParams(route, action.Params)
      routes = append(routes, route)
    case "CONNECT":
      for _, method := range connecter.ConnectMethods() {
        connectProducedObject := storageMeta.ProducesObject(method)
        if connectProducedObject == nil {
          connectProducedObject = "string"
        }
        doc := "connect " + method + " requests to " + kind
        if isSubresource {
          doc = "connect " + method + " requests to " + subresource + " of " + kind
        }
        handler := metrics.InstrumentRouteFunc(action.Verb, resource, subresource, requestScope, restfulConnectResource(connecter, reqScope, admit, path, isSubresource))
        route := ws.Method(method).Path(action.Path).
          To(handler).
          Doc(doc).
          Operation("connect" + strings.Title(strings.ToLower(method)) + namespaced + kind + strings.Title(subresource) + operationSuffix).
          Produces("*/*").
          Consumes("*/*").
          Writes(connectProducedObject)
        if versionedConnectOptions != nil {
          if err := addObjectParams(ws, route, versionedConnectOptions); err != nil {
            return nil, err
          }
        }
        addParams(route, action.Params)
        routes = append(routes, route)

        // transform ConnectMethods to kube verbs
        if kubeVerb, found := toDiscoveryKubeVerb[method]; found {
          if len(kubeVerb) != 0 {
            kubeVerbs[kubeVerb] = struct{}{}
          }
        }
      }
    default:
      return nil, fmt.Errorf("unrecognized action verb: %s", action.Verb)
    }
    for _, route := range routes {
      route.Metadata(ROUTE_META_GVK, metav1.GroupVersionKind{
        Group:   reqScope.Kind.Group,
        Version: reqScope.Kind.Version,
        Kind:    reqScope.Kind.Kind,
      })
      route.Metadata(ROUTE_META_ACTION, strings.ToLower(action.Verb))
      ws.Route(route)
    }
    // Note: update GetAuthorizerAttributes() when adding a custom handler.
  }

  apiResource.Verbs = make([]string, 0, len(kubeVerbs))
  for kubeVerb := range kubeVerbs {
    apiResource.Verbs = append(apiResource.Verbs, kubeVerb)
  }
  sort.Strings(apiResource.Verbs)

  if shortNamesProvider, ok := storage.(rest.ShortNamesProvider); ok {
    apiResource.ShortNames = shortNamesProvider.ShortNames()
  }
  if categoriesProvider, ok := storage.(rest.CategoriesProvider); ok {
    apiResource.Categories = categoriesProvider.Categories()
  }
  if gvkProvider, ok := storage.(rest.GroupVersionKindProvider); ok {
    gvk := gvkProvider.GroupVersionKind(a.group.GroupVersion)
    apiResource.Group = gvk.Group
    apiResource.Version = gvk.Version
    apiResource.Kind = gvk.Kind
  }

  return &apiResource, nil
}
```