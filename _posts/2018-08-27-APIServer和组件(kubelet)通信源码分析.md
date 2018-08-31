---
layout:     post
title:      "APIServer和组件(kubelet)通信源码分析"
subtitle:   "APIServer <=> Kubelet(组件)"
date:       2018-08-27 10:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - kubernetes源码
---

# APIServer和组件(kubelet)通信源码分析

在前面两章源码分析中，第一章简单的介绍了kubectl从命令到给apiserver发送请求的过程，第二章则是介绍apiserver从启动到api注册完成路由信息的配置过程，这一张我们讲介绍APIServer是如何通过List-Watch机制和各组件进行通信的过程，这一章主要是针对kubelet的组件模块进行分析，其它组件可以类似分析。

## list-watch机制（发布／订阅模式）

list-watch,作为k9s系统中一种统一的异步消息传递方式，对系统的性能，数据一致性有关键性的作用，我们首先将这个机制的实现方式研究清楚。

以创建一个POD为例子：

* kubectl首先发送一个 **创建POD的请求**，APIServer通过路由找到对应的操作函数，然后APIServer是 **如何**通知kubelet来创建一个POD对象的
* 大致的过程就是APIServer创建了一个**cache结构**，所有到etcd的数据流都会经过该结构
* 每个组件初始化时都会给APIServer发送一个**watch请求**，APIServer 会根据这些请求在cache中生成一个个的**watcher**
* 当发生事件时，会给这些watcher中的通道推送，每个watcher都有自己的**Filter过滤**，找到自己想要监听的事件则通过管道的方式将该数据发送到相应的组件。
* 所以当创建一个POD时就会产生一个创建POD的**事件Event**通过管道发送给kubelet,kubelet通过**轮询监听管道**看是否有事件发生，有则调用对应的处理方法
* 下图可以简单的表示整个POD创建的过程

![pod_create](/img/pod_create.png)

## kubelet向APIServer发起监听

这部分以后再具体研究，主要是发送一个watch请求，建立一个ListWatch，然后监听来自apiserver的管道。

## APIServer监听组件watch请求

总过程描述：

1. APIServer在创建一个kubeAPIServer并注册各API路由信息时，其中就包括**对Watch请求的路由信息**
2. 当监听到各组件发送来的Watch请求时，首先进入ListResource函数（因为List和Watch为同样的请求格式）,该函数会分析它为Watch请求后调用**rw的Watch函数**来创建一个watcher结构添加到cache结构中
3. 其中的cacher结构的创建过程则是在Storage的创建过程中创建的，每个资源pod,service,rc,dp等都有对应的Storage
4. 对应POD的创建NewStorage函数则是通过调用CompleteWithOptions,来创建cacher,并将store.Storage.Storage字段设为cacher,建立Storage和Cacher的关联
5. 在创建Cacher的过程中也会调用**ListAndWatch**实现将远端数据不断同步到Cacher
6. 并且没创建一个watcher都会启动一个协程来**监听watcher.input管道**，当有事件发生时，则通过Filter进行过滤然后**通过wathcer.result管道发送给各组件**


在APIServer安装API的最后一步中，根据API信息生成一个wenservice安装器，安装器开始安装APIInstaller.Install()的过程中有一个函数要将所有的路由信息path/handler注册到webservice中去，该函数为registerResourceHandlers。这个函数非常复杂，但其中涉及到了对kubelet发起的监听请求的处理：

由于该部分代码非常复杂，所以我们只提取相关的部分来进行分析：

* 首先将rest.Storage结构转化为Lister和Watcher
* 提供list和watch是同一个入口，所以我们区分是**List请求还是Watch** 请求是通过GET /pods?watch = true来区分是list还是watch
* 设置list请求和watch请求的handler是在ListResource中实现的

```go
func (a *APIInstaller) registerResourceHandlers(path string, storage rest.Storage,...

...
lister, isLister := storage.(rest.Lister)//将storage结构转化为Lister,类型断言
watcher, isWatcher := storage.(rest.Watcher)//将storage结构转化为Watcher                     ...(1)
...    
    case "LIST": // List all resources of a kind.                ...(2)
        doc := "list objects of kind " + kind
        if hasSubresource {
            doc = "list " + subresource + " of objects of kind " + kind
        }
        handler := metrics.InstrumentRouteFunc(action.Verb, resource, ListResource(lister, watcher, reqScope, false, a.minRequestTimeout)) //结合Lister和Watcer来设置对watch请求的处理handler ...(3) 
```

所以接下来我们看看ListResource(在新版中是restfulListResource)中的实现：

* ListResource根据opts中的Watch区分是Watch请求还是List请求
* 是Watch请求都会调用rw.Watch()创建一个Watcher
* 然后使用serverWatch来处理该请求

```go
func ListResource(r rest.Lister, rw rest.Watcher, scope RequestScope, forceWatch bool, minRequestTimeout time.Duration) http.HandlerFunc {
if opts.Watch || forceWatch {
            if rw == nil {
                scope.err(errors.NewMethodNotSupported(scope.Resource.GroupResource(), "watch"), w, req)
                return
            }
            // TODO: Currently we explicitly ignore ?timeout= and use only ?timeoutSeconds=.
            timeout := time.Duration(0)
            if opts.TimeoutSeconds != nil {
                timeout = time.Duration(*opts.TimeoutSeconds) * time.Second
            }
            if timeout == 0 && minRequestTimeout > 0 {
                timeout = time.Duration(float64(minRequestTimeout) * (rand.Float64() + 1.0))
            }
            glog.V(3).Infof("Starting watch for %s, rv=%s labels=%s fields=%s timeout=%s", req.URL.Path, opts.ResourceVersion, opts.LabelSelector, opts.FieldSelector, timeout)

            watcher, err := rw.Watch(ctx, &opts)//创建一个watcher
            if err != nil {
                scope.err(err, w, req)
                return
            }
            requestInfo, _ := request.RequestInfoFrom(ctx)
            metrics.RecordLongRunning(req, requestInfo, func() {
                serveWatch(watcher, scope, req, w, timeout)//再处理该watch请求
            })
            return
        }

        ...//对List请求的处理
    }
```

## Watcher的创建过程

我们知道Watcher是由rw.Watch()创建得到的，rw是由rest.Storage转化的Watcher接口，相当于由rest.Storage结构实现的接口，**类型断言**。

/k8s.io/apiserver/pkg/registry/generic/registry/store.go

```go
//创建一个watcher接口
func (e *Store) Watch(ctx context.Context, options *metainternalversion.ListOptions) (watch.Interface, error) {
    label := labels.Everything()
    if options != nil && options.LabelSelector != nil {
        label = options.LabelSelector
    }
    field := fields.Everything()
    if options != nil && options.FieldSelector != nil {
        field = options.FieldSelector
    }
    predicate := e.PredicateFunc(label, field)

    resourceVersion := ""
    if options != nil {
        resourceVersion = options.ResourceVersion
        predicate.IncludeUninitialized = options.IncludeUninitialized
    }
    return e.WatchPredicate(ctx, predicate, resourceVersion)
}

func (e *Store) WatchPredicate(ctx context.Context, p storage.SelectionPredicate, resourceVersion string) (watch.Interface, error) {
    if name, ok := p.MatchesSingle(); ok {
        if key, err := e.KeyFunc(ctx, name); err == nil {
            w, err := e.Storage.Watch(ctx, key, resourceVersion, p)//这里就是生成watcher接口的具体的调用函数
            if err != nil {
                return nil, err
            }
            if e.Decorator != nil {
                return newDecoratedWatcher(w, e.Decorator), nil
            }
            return w, nil
        }
        // if we cannot extract a key based on the current context, the
        // optimization is skipped
    }

    w, err := e.Storage.WatchList(ctx, e.KeyRootFunc(ctx), resourceVersion, p)
    if err != nil {
        return nil, err
    }
    if e.Decorator != nil {
        return newDecoratedWatcher(w, e.Decorator), nil
    }
    return w, nil
}
```

## Storage创建过程
我们发现生成Watcher的具体的调用函数为restStorage.Watch（）来得到的，所以我们需要研究**Storage**的的生成过程及结构，而Storage是在API注册过程中添加的结构，不同的APIGroup对应不同的Storage,都被添加到RESTStorageProvider数组中去。并且对应的结构位于/kubernetes/pkg/registry/xxx(对应xxxrest)/rest/storage_xxx.go。

所以Storage的构造过程在/kubernetes/pkg/registry/xxx(对应xxxrest)/rest/storage_xxx.go中的NewStorage中完成。

* 比如对于创建POD请求对应的Storage为/kubernetes/pkg/registry/core/rest/storage_core.go的函数NewLegacyRESTStorage
* 而其中有路由关系："pods":             podStorage.Pod。
* 该podStorage.Pod中的podStorage的构造又是由/kubernetes/pkg/registry/core/rest/pod/storage/storage.go中的**NewStorage**完成构造
* 看一下该NewStorage的构造实现
    - 其中最关键的部分是store.CompleteWithOptions(options),该函数会经过GetRESTOptions => StorageWithCacher来生成一个**函数func Decorator**
    - 并可以调用该函数Decorator来得到 **cacher结构，赋值给store.Storage.Storage**(第一个Storage为DryRunStorage,第二个为interface)
    - 而cacher的生成部分在于StorageWithCacher中调用的NewCacherFromConfig来得到
* 经过上述过程我们就可以成功**将cacher结构和Storage**建立关联

```go
func NewStorage(optsGetter generic.RESTOptionsGetter, k client.ConnectionInfoGetter, proxyTransport http.RoundTripper, podDisruptionBudgetClient policyclient.PodDisruptionBudgetsGetter) PodStorage {

    store := &genericregistry.Store{
        NewFunc:                  func() runtime.Object { return &api.Pod{} },
        NewListFunc:              func() runtime.Object { return &api.PodList{} },
        PredicateFunc:            pod.MatchPod,
        DefaultQualifiedResource: api.Resource("pods"),

        CreateStrategy:      pod.Strategy,
        UpdateStrategy:      pod.Strategy,
        DeleteStrategy:      pod.Strategy,
        ReturnDeletedObject: true,

        TableConvertor: printerstorage.TableConvertor{TablePrinter: printers.NewTablePrinter().With(printersinternal.AddHandlers)},
    }
    options := &generic.StoreOptions{RESTOptions: optsGetter, AttrFunc: pod.GetAttrs, TriggerFunc: pod.NodeNameTriggerFunc}
    //构建store和cache的关联关系入口
    if err := store.CompleteWithOptions(options); err != nil {
        panic(err) // TODO: Propagate error up
    }

    statusStore := *store
    statusStore.UpdateStrategy = pod.StatusStrategy

    return PodStorage{
        Pod:         &REST{store, proxyTransport},
        Binding:     &BindingREST{store: store},
        Eviction:    newEvictionStorage(store, podDisruptionBudgetClient),
        Status:      &StatusREST{store: &statusStore},
        Log:         &podrest.LogREST{Store: store, KubeletConn: k},
        Proxy:       &podrest.ProxyREST{Store: store, ProxyTransport: proxyTransport},
        Exec:        &podrest.ExecREST{Store: store, KubeletConn: k},
        Attach:      &podrest.AttachREST{Store: store, KubeletConn: k},
        PortForward: &podrest.PortForwardREST{Store: store, KubeletConn: k},
    }
}
```


## Cacher创建过程
* 所以在WatchPredicate中e.Storage.Watch函数实际上调用的是**DryRunnableStorage.Watch**
* 该函数只是进行一层**转调用**进一步调用DryRunnableStorage.Storage.Watch。
* 而我们已经分析到了store.Storage.Storage就是一个cache结构，所以最后是调用 **cache.Watch**来创建一个Wathcer。
* 在分析cache结构如何创建一个Watcher结构之前，我们首先先看看Cache是如何被创建的，上面我们已经分析到CompleteConfig=>GetRESTOptions => StorageWithCacher,StorageWithCacher中有一个函数NewCacherFromConfig来完成Cache结构的创建：

```go
//创建一个携带Cacher的Storage接口
func StorageWithCacher(capacity int) generic.StorageDecorator {
    return func(
        storageConfig *storagebackend.Config,
        objectType runtime.Object,
        resourcePrefix string,
        keyFunc func(obj runtime.Object) (string, error),
        newListFunc func() runtime.Object,
        getAttrsFunc storage.AttrFunc,
        triggerFunc storage.TriggerPublisherFunc) (storage.Interface, factory.DestroyFunc) {

        s, d := generic.NewRawStorage(storageConfig)
        if capacity == 0 {
            glog.V(5).Infof("Storage caching is disabled for %T", objectType)
            return s, d
        }
        glog.V(5).Infof("Storage caching is enabled for %T with capacity %v", objectType, capacity)

        // TODO: we would change this later to make storage always have cacher and hide low level KV layer inside.
        // Currently it has two layers of same storage interface -- cacher and low level kv.
        cacherConfig := cacherstorage.Config{
            CacheCapacity:        capacity,
            Storage:              s,
            Versioner:            etcdstorage.APIObjectVersioner{},
            Type:                 objectType,
            ResourcePrefix:       resourcePrefix,
            KeyFunc:              keyFunc,
            NewListFunc:          newListFunc,
            GetAttrsFunc:         getAttrsFunc,
            TriggerPublisherFunc: triggerFunc,
            Codec:                storageConfig.Codec,
        }//配置缓存Cacher信息
        cacher := cacherstorage.NewCacherFromConfig(cacherConfig)
        destroyFunc := func() {
            cacher.Stop()
            d()
        }

        // TODO : Remove RegisterStorageCleanup below when PR
        // https://github.com/kubernetes/kubernetes/pull/50690
        // merges as that shuts down storage properly
        RegisterStorageCleanup(destroyFunc)

        return cacher, destroyFunc
    }
}
```

**NewCacherFromConfig**:创建一个缓存结构负责List和Watch功能的cache

* 创建一个缓存结构watchCache,可以保存所有数据，并维护一个滑动窗口
* 构建一个Cacher对象，其中比较关键的成员有
    - watchCache:保存所有数据和滑动窗口环形队列
    - reflector:反射器，暂时不清楚具体的作用，在这里主要是为了调用reflector.ListAndWatch()来将**远端数据源源不断同步到缓存结构中来**
    - watchers:该缓存结构针对每个组件发起的**Watch请求创建的watcher结构**

```go
//首先介绍几个结构：

type watchCache struct {
    sync.RWMutex  //同步锁
    cond *sync.Cond //条件变量
    capacity int//历史滑动窗口容量
    keyFunc func(runtime.Object) (string, error)//从storage中获取键值
    getAttrsFunc func(runtime.Object) (labels.Set, fields.Set, bool, error)//获取一个对象的field和label信息
    cache      []watchCacheElement//循环队列缓存
    startIndex int//循环队列的起始下标
    endIndex   int//循环队列的结束下标
    store cache.Store//
    resourceVersion uint64
    onReplace func()
    onEvent func(*watchCacheEvent)//在每次缓存中的数据发生Add/Update/Delete后都会调用该函数，来获取对象的之前版本的值
    clock clock.Clock
    versioner storage.Versioner
}

//每一个Watch请求对应一个watcher结构
type cacheWatcher struct {
    sync.Mutex//同步锁
    input     chan *watchCacheEvent//输入管道,apiserver都事件发生时都会通过广播的形式向input管道进行发送
    result    chan watch.Event//输出管道，输出到update管道中去
    done      chan struct{}
    filter    filterWithAttrsFunc//过滤器
    stopped   bool
    forget    func(bool)
    versioner storage.Versioner
}


func NewCacherFromConfig(config Config) *Cacher {
    watchCache := newWatchCache(config.CacheCapacity, config.KeyFunc, config.GetAttrsFunc, config.Versioner)//创建一个watchCache对象，包括所有数据和滑动窗口
    listerWatcher := newCacherListerWatcher(config.Storage, config.ResourcePrefix, config.NewListFunc)//创建一个cacherListerWatcher对象
    reflectorName := "storage/cacher.go:" + config.ResourcePrefix//反射的名称：storage/cacher.go

    // Give this error when it is constructed rather than when you get the
    // first watch item, because it's much easier to track down that way.
    if obj, ok := config.Type.(runtime.Object); ok {
        if err := runtime.CheckCodec(config.Codec, obj); err != nil {
            panic("storage codec doesn't seem to match given type: " + err.Error())
        }
    }

    stopCh := make(chan struct{})



    cacher := &Cacher{
        ready:       newReady(),
        storage:     config.Storage,//用于连接etcd
        objectType:  reflect.TypeOf(config.Type),
        watchCache:  watchCache,//里面存储了所有数据和滑动窗口（环形队列）
        reflector:   cache.NewNamedReflector(reflectorName, listerWatcher, config.Type, watchCache, 0),//反射器
        versioner:   config.Versioner,
        triggerFunc: config.TriggerPublisherFunc,
        watcherIdx:  0,//watcher的数量
        watchers: indexedWatchers{
            allWatchers:   make(map[int]*cacheWatcher),
            valueWatchers: make(map[string]watchersMap),
        },//wathcers是根据组件的每一个watch请求生成的watcher
        // TODO: Figure out the correct value for the buffer size.
        incoming:              make(chan watchCacheEvent, 100),
        dispatchTimeoutBudget: newTimeBudget(stopCh),
        // We need to (potentially) stop both:
        // - wait.Until go-routine
        // - reflector.ListAndWatch
        // and there are no guarantees on the order that they will stop.
        // So we will be simply closing the channel, and synchronizing on the WaitGroup.
        stopCh: stopCh,
    }//构建一个cacher 结构



    watchCache.SetOnEvent(cacher.processEvent)//设置watch Cache的onEvent为processEvent
    go cacher.dispatchEvents()//从incoming通道监听事件，分发event到每一个watcher

    cacher.stopWg.Add(1)
    go func() {
        defer cacher.stopWg.Done()
        wait.Until(
            func() {
                if !cacher.isStopped() {
                    cacher.startCaching(stopCh)//启动一个协程，该协程的主要任务就是调用c.reflector.ListAndWatch()实现将远端数据源源不断同步到cahce结构中来
                }
            }, time.Second, stopCh,
        )
    }()
    return cacher
}
```

## Watcher的创建实现

这里我们已经介绍完了试如何创建一个Cacher结构的，并且Cacher也调用startCaching来将远端的数据源源不断同步到该缓存结构，现在可以开始分析Cacher是如何创建一个Watcher结构的，也就是Cacher.Watch()函数的实现过程。

* 创建一个watcher结构，并新建用于和组件kubelet进行**通信的管道(应该就是update)**
* 将该watcher结构添加到Cache的**watcher列表**中去

```go
func (c *Cacher) Watch(ctx context.Context, key string, resourceVersion string, pred storage.SelectionPredicate) (watch.Interface, error) {
    watchRV, err := c.versioner.ParseResourceVersion(resourceVersion)
    if err != nil {
        return nil, err
    }

    c.ready.wait()
    c.watchCache.RLock()//在处理该事件时要对缓存数据进行上锁，防止有新的事件发生
    defer c.watchCache.RUnlock()
    initEvents, err := c.watchCache.GetAllEventsSinceThreadUnsafe(watchRV)
    if err != nil {
        return newErrWatcher(err), nil
    }

    triggerValue, triggerSupported := "", false
    if matchValues := pred.MatcherIndex(); len(matchValues) > 0 {
        triggerValue, triggerSupported = matchValues[0].Value, true
    }
    if c.triggerFunc != nil && !triggerSupported {
        // TODO: We should tune this value and ideally make it dependent on the
        // number of objects of a given type and/or their churn.
        chanSize = 1000
    }

    c.Lock()
    defer c.Unlock()
    forget := forgetWatcher(c, c.watcherIdx, triggerValue, triggerSupported)
    //创建一个新的watcher
    watcher := newCacheWatcher(watchRV, chanSize, initEvents, filterWithAttrsFunction(key, pred), forget, c.versioner)
    //将新建的watcher添加到cache的watcher列表中
    c.watchers.addWatcher(watcher, c.watcherIdx, triggerValue, triggerSupported)
    c.watcherIdx++
    return watcher, nil
}

//创建一个watcher结构
func newCacheWatcher(resourceVersion uint64, chanSize int, initEvents []*watchCacheEvent, filter filterWithAttrsFunc, forget func(bool), versioner storage.Versioner) *cacheWatcher {
    watcher := &cacheWatcher{
        input:     make(chan *watchCacheEvent, chanSize),//创建一个缓存管道，用于过滤
        result:    make(chan watch.Event, chanSize),//创建一个用于和组件通信的管道
        done:      make(chan struct{}),
        filter:    filter,
        stopped:   false,
        forget:    forget,
        versioner: versioner,
    }
    go watcher.process(initEvents, resourceVersion)//启动一个异步线程，不断从cacher.input监听到事件然后通过Filter进行过滤，最后发送到cacher.result
    return watcher
}
```


## ListAndWatch实现从远端将数据同步到本地端

![ListAndWatch](/img/ListAndWatch.png)


梳理完从组件kubelet发送Watch请求到APIServer完成整个watcher创建并添加到cache.cacheWatchers中的流程后，最后一个问题就是Cache是如何实现对远端数据的同步，并通过watcher.input广播事件过滤后最后通过cacher.result发送到组件的过程，而该步骤的实现函数为ListAndWatch：

* 首先调用Reflector.ListerWatcher.List方法获得所有数据
* 然后通过版本信息过滤掉不满足版本要求的数据
* 最后调用Reflector.ListerWatcher.Watch进行操作
* ListerWatcher实际是一个包含List和Watch的接口，这里的接口实际上是newCacherListerWatcher函数创建的cacherListerWatcher结构


```go
//简化的版本
func (r *Reflector) ListAndWatch(stopCh <-chan struct{}) error {
    list, err := r.listerWatcher.List(options)
    
    r.metrics.listDuration.Observe(time.Since(start).Seconds())
    listMetaInterface, err := meta.ListAccessor(list)
    if err != nil {
        return fmt.Errorf("%s: Unable to understand list result %#v: %v", r.name, list, err)
    }
    resourceVersion = listMetaInterface.GetResourceVersion()
 
    for {
        w, err := r.listerWatcher.Watch(options)
 
        if err := r.watchHandler(w, &resourceVersion, resyncerrc, stopCh); err != nil {
            
            return nil
        }
    }

```


cacherListerWatcher实现的List和Watch接口函数如下：

* lw.Storage实际上是StorageWithCacher中 s, d := generic.NewRawStorage(storageConfig)产生的Storage接口

```go
func (lw *cacherListerWatcher) List(options metav1.ListOptions) (runtime.Object, error) {
    list := lw.newListFunc()
    if err := lw.storage.List(context.TODO(), lw.resourcePrefix, "", storage.Everything, list); err != nil {
        return nil, err
    }
    return list, nil
}

// Implements cache.ListerWatcher interface.
func (lw *cacherListerWatcher) Watch(options metav1.ListOptions) (watch.Interface, error) {
    return lw.storage.WatchList(context.TODO(), lw.resourcePrefix, options.ResourceVersion, storage.Everything)
}
```

NewRawStorage的实现如下：就是生成了一个直接对接etcd3数据存储的实际对象storage,路径为vendor/k8s.io/apiserver/pkg/storage/storagebackend/factory/etcd3.go

```go

func NewRawStorage(config *storagebackend.Config) (storage.Interface, factory.DestroyFunc) {
    s, d, err := factory.Create(*config)
    
    return s, d
}
 
func Create(c storagebackend.Config) (storage.Interface, DestroyFunc, error) {
    switch c.Type {
    case storagebackend.StorageTypeETCD2:
        return newETCD2Storage(c)
    case storagebackend.StorageTypeUnset, storagebackend.StorageTypeETCD3:
        
        return newETCD3Storage(c)//生成一个对接etcd3的接口对象storage
    default:
        return nil, nil, fmt.Errorf("unknown storage type: %s", c.Type)
    }

```

这里生成的就是vendor/k8s.io/apiserver/pkg/storage/etcd3/store.go中的store结构，进入到了etcd3组件的内部源码，来与etcd3存储的数据进行直接的交互。在介绍该e.Storage.WatchList之前，先简要介绍一下etcd3的一些概念

## etcd3分布式存储

etcd3的一些介绍可以参考该[Blog](https://skyao.gitbooks.io/learning-etcd3/introduction/)

* etcd用于可靠存储不频繁更新的数据并提供可靠的观察查询，etcd会暴露键值对的先前版本来支持不昂贵的快速和观察历史事件
* etcd3使用 **多版本持久化**键值来存储数据，所以键值存储不会 **就地更新结构**,总是生成一个新的更新后的结构
* etcd3的键值由一个三元组构成：(major, sub, type)Major是持有key的存储修订版本。Sub区分同一个修订版本的不同key。Type是用于特别值，以b+树键值对的方式存储数据
* etcd3的键值API被定义为gRPC服务
    - 键值对KeyValue是键值API可以操作的最小单元。每个键值对有一些字段
    
    ```
    message KeyValue {
    bytes key = 1;//字节数组形式的key
    int64 create_revision = 2;//最后一次创建的版本
    int64 mod_revision = 3;//当前版本
    int64 version = 4;//版本
    bytes value = 5;//字节数组形式的value
    int64 lease = 6;//附加到key的租约的ID
    }

    ```
* 一些术语：
    - Node:节点
    - Member:成员，一个etcd的实例，承载一个node,并为一个client提供服务
    - Cluster：集群，由多个Member组成
    - Peer：同伴，同一个集群的其它成员
    - Client:客户端集群HTTP API调用者
* 一些API：
    - 读API:range,watch
    - 写API:put,delete
    - 联合，事务：txn
* revision修订颁布：从0开始递增，两个拥有相同修订版本的键值对是被一个操作同时修改。
* etcd3提供三个服务：
    - KV Service
    - Watch Service(监控服务)
    - Lease Service
* Watch API提供一个基于事件Event接口来异步监视键值的改变，每次改变都代表了一个Event事件
    
    ```
    Event{
        enum Eventtype{
            PUT = 0,
            DELETE = 1
        }
        EventType type ;
        KeyValue kv ;  //当前的键值
        KeyValue pre_kv ;//上一个版本的键值，可以关闭该字段节约流量
    }
    ```
    - Watch是一个长期运行的请求，并且使用**grpc流来传输事件Event数据，这个流是双向**的，客户通过该流来发起watch请求，并接受Event数据
    - watch请求为WatchRequest
    
    ```
    WatchRequest{
        bytes key;//要观察的监听的key的范围起点
        bytes range_end;//要观察监听的key的范围结尾
        int64 start_revision;//监听哪个版本以后的数据
        bool progress_notify;
        enum FilterType{
            NOPUT,
            NODELETE,
        }
        repeated FilterType filter;//要被过滤掉的Event请求
        bool pre_kv;//是否关闭pre_kv字段
    }
    ```
    - Watch响应,当有事件发生时，会通过grpc流返回一个WatchResponse
    
    ```
    WatchResponse{
        ResponseHeader header;
        int64 watch_id;//watch请求的id，对应对哪个watch的响应
        bool created;//响应创建一个watch是否成功
        bool canceled;//响应取消一个watch是否成功
        int64 compact_version;//最小可查的数据版本
        repeated mnccpb.Event events;//发生的事件列表

    }
    ```



## etcd3Storage.WatchList实现对etcd3的watch服务

在初步了解etcd 的初步原理后，我们开始接着源码分析，e.Storage.WatchList()来实现etcd的Watch服务，该部分的源码位于vendor/k8s.io/apiserver/pkg/storage/etcd3/store.go

```go
// WatchList implements storage.Interface.WatchList.
func (s *store) WatchList(ctx context.Context, key string, resourceVersion string, pred storage.SelectionPredicate) (watch.Interface, error) {
    return s.watch(ctx, key, resourceVersion, pred, true)
}

func (s *store) watch(ctx context.Context, key string, rv string, pred storage.SelectionPredicate, recursive bool) (watch.Interface, error) {
    rev, err := s.versioner.ParseResourceVersion(rv)
    if err != nil {
        return nil, err
    }
    key = path.Join(s.pathPrefix, key)
    return s.watcher.Watch(ctx, key, int64(rev), recursive, pred)
}
```

s.watcher.Watch()结构主要负责对一些watch之前的管道的准备，以及监听协程的处理

* 创建一个watchChan管道监听事务处理器
* 启动该watchChan事务处理器
    - 发起监听请求startWatching
    - 启动请求管道监听事件处理Handler协程processEvent



```go
func (w *watcher) Watch(ctx context.Context, key string, rev int64, recursive bool, pred storage.SelectionPredicate) (watch.Interface, error) {
    if recursive && !strings.HasSuffix(key, "/") {
        key += "/"
    }
    wc := w.createWatchChan(ctx, key, rev, recursive, pred)
    go wc.run()
    return wc, nil
}

func (w *watcher) createWatchChan(ctx context.Context, key string, rev int64, recursive bool, pred storage.SelectionPredicate) *watchChan {
    wc := &watchChan{//创建管道监听事务处理器
        watcher:           w,
        key:               key,
        initialRev:        rev,
        recursive:         recursive,
        internalPred:      pred,
        incomingEventChan: make(chan *event, incomingBufSize),
        resultChan:        make(chan watch.Event, outgoingBufSize),
        errChan:           make(chan error, 1),
    }
    if pred.Empty() {
        // The filter doesn't filter out any object.
        wc.internalPred = storage.Everything
    }
    wc.ctx, wc.cancel = context.WithCancel(ctx)
    return wc
}

func (wc *watchChan) run() {
    watchClosedCh := make(chan struct{})
    go wc.startWatching(watchClosedCh)//发起监听请求开始监听事件

    var resultChanWG sync.WaitGroup
    resultChanWG.Add(1)
    go wc.processEvent(&resultChanWG)//监听管道中的监听事件，调用处理Handler

    select {
    case err := <-wc.errChan:
        if err == context.Canceled {
            break
        }
        errResult := transformErrorToEvent(err)
        if errResult != nil {
            // error result is guaranteed to be received by user before closing ResultChan.
            select {
            case wc.resultChan <- *errResult:
            case <-wc.ctx.Done(): // user has given up all results
            }
        }
    case <-watchClosedCh:
    case <-wc.ctx.Done(): // user cancel
    }

    // We use wc.ctx to reap all goroutines. Under whatever condition, we should stop them all.
    // It's fine to double cancel.
    wc.cancel()

    // we need to wait until resultChan wouldn't be used anymore
    resultChanWG.Wait()//阻塞监听事件发送管道，知道该管道不再被使用
    close(wc.resultChan)
}
```
如何发起监听请求的startWatching：

* 主要是调用了/github.com/coreos/etcd/clientv3/watch.go进入到etcd3组件内部源码，然后结合etcd3的Watch服务来发起Watch请求
* wc.watcher.client.Watch(wc.ctx, wc.key, opts...)

```go
func (wc *watchChan) startWatching(watchClosedCh chan struct{}) {
    if wc.initialRev == 0 {
        if err := wc.sync(); err != nil {
            glog.Errorf("failed to sync with latest state: %v", err)
            wc.sendError(err)
            return
        }
    }
    opts := []clientv3.OpOption{clientv3.WithRev(wc.initialRev + 1), clientv3.WithPrevKV()}
    if wc.recursive {
        opts = append(opts, clientv3.WithPrefix())
    }
    wch := wc.watcher.client.Watch(wc.ctx, wc.key, opts...)//进入到ETCD3组件内部发起监听请求WatchRequest
    for wres := range wch {
        if wres.Err() != nil {
            err := wres.Err()
            // If there is an error on server (e.g. compaction), the channel will return it before closed.
            glog.Errorf("watch chan error: %v", err)
            wc.sendError(err)
            return
        }
        for _, e := range wres.Events {
            wc.sendEvent(parseEvent(e))//发送监听到的事件
        }
    }
    // When we come to this point, it's only possible that client side ends the watch.
    // e.g. cancel the context, close the client.
    // If this watch chan is broken and context isn't cancelled, other goroutines will still hang.
    // We should notify the main thread that this goroutine has exited.
    close(watchClosedCh)
}
```

再看etcd3内部是如何发送监听请求的：

* 创建一个watchRequest请求
* 通过grpc流发送请求,若grpc流没有则创建一个
* 监听grpc的对Watch请求的响应并返回 

```go

// Watch posts a watch request to run() and waits for a new watcher channel
func (w *watcher) Watch(ctx context.Context, key string, opts ...OpOption) WatchChan {
    ow := opWatch(key, opts...)

    var filters []pb.WatchCreateRequest_FilterType
    if ow.filterPut {
        filters = append(filters, pb.WatchCreateRequest_NOPUT)
    }
    if ow.filterDelete {
        filters = append(filters, pb.WatchCreateRequest_NODELETE)
    }

    wr := &watchRequest{//构建发送请求WatchRequest
        ctx:            ctx,
        createdNotify:  ow.createdNotify,
        key:            string(ow.key),
        end:            string(ow.end),
        rev:            ow.rev,
        progressNotify: ow.progressNotify,
        filters:        filters,
        prevKV:         ow.prevKV,
        retc:           make(chan chan WatchResponse, 1),
    }

    ok := false
    ctxKey := streamKeyFromCtx(ctx)

    // find or allocate appropriate grpc watch stream
    w.mu.Lock()
    if w.streams == nil {
        // closed
        w.mu.Unlock()
        ch := make(chan WatchResponse)
        close(ch)
        return ch
    }
    wgs := w.streams[ctxKey]//找到对应的grpc流
    if wgs == nil {
        wgs = w.newWatcherGrpcStream(ctx)//若该流为空，则新创建一个流结构
        w.streams[ctxKey] = wgs
    }
    donec := wgs.donec
    reqc := wgs.reqc
    w.mu.Unlock()

    // couldn't create channel; return closed channel
    closeCh := make(chan WatchResponse, 1)

    // submit request
    select {
    case reqc <- wr://通过grpc流发送请求
        ok = true
    case <-wr.ctx.Done():
    case <-donec:
        if wgs.closeErr != nil {
            closeCh <- WatchResponse{closeErr: wgs.closeErr}
            break
        }
        // retry; may have dropped stream from no ctxs
        return w.Watch(ctx, key, opts...)
    }

    // receive channel
    if ok {
        select {
        case ret := <-wr.retc://接受来自etcd对watch请求的响应
            return ret//返回响应
        case <-ctx.Done():
        case <-donec:
            if wgs.closeErr != nil {
                closeCh <- WatchResponse{closeErr: wgs.closeErr}
                break
            }
            // retry; may have dropped stream from no ctxs
            return w.Watch(ctx, key, opts...)
        }
    }

    close(closeCh)
    return closeCh
}
```


## 回到ListAndWatch的实现：watchHandler监听ETCD3事件并处理

在ListAndWatch中循环调用Watch和r.watchHandler,这里就讲介绍watchHandler的功能：


* 读取outgoing channel中的信息，更新watchCache
* select监听stopCh,errc,resultChannel
* 监听到event之后根据分类switch{"ADD","MODIFIED","DELETED"}分别进行处理
* 这里会调用不同的函数**ADD,Update,DELETE**。
* 更新WatchCache就是通过这三个函数来进行更新，这三个函数位于/apiserver/pkg/storage/cache/watch_cache.go中定义

```go
func (r *Reflector) watchHandler(w watch.Interface, resourceVersion *string, errc chan error, stopCh <-chan struct{}) error {
    start := r.clock.Now()
    eventCount := 0

    // Stopping the watcher should be idempotent and if we return from this function there's no way
    // we're coming back in with the same watch interface.
    defer w.Stop()
    // update metrics
    defer func() {
        r.metrics.numberOfItemsInWatch.Observe(float64(eventCount))
        r.metrics.watchDuration.Observe(time.Since(start).Seconds())
    }()

loop:
    for {
        select {
        case <-stopCh:
            return errorStopRequested
        case err := <-errc:
            return err
        case event, ok := <-w.ResultChan():
            if !ok {
                break loop
            }
            if event.Type == watch.Error {
                return apierrs.FromObject(event.Object)
            }
            if e, a := r.expectedType, reflect.TypeOf(event.Object); e != nil && e != a {
                utilruntime.HandleError(fmt.Errorf("%s: expected type %v, but watch event object had type %v", r.name, e, a))
                continue
            }
            meta, err := meta.Accessor(event.Object)
            if err != nil {
                utilruntime.HandleError(fmt.Errorf("%s: unable to understand watch event %#v", r.name, event))
                continue
            }
            newResourceVersion := meta.GetResourceVersion()
            switch event.Type {
            case watch.Added:
                err := r.store.Add(event.Object)
                if err != nil {
                    utilruntime.HandleError(fmt.Errorf("%s: unable to add watch event object (%#v) to store: %v", r.name, event.Object, err))
                }
            case watch.Modified:
                err := r.store.Update(event.Object)//调用UPDATE
                if err != nil {
                    utilruntime.HandleError(fmt.Errorf("%s: unable to update watch event object (%#v) to store: %v", r.name, event.Object, err))
                }
            case watch.Deleted:
                // TODO: Will any consumers need access to the "last known
                // state", which is passed in event.Object? If so, may need
                // to change this.
                err := r.store.Delete(event.Object)
                if err != nil {
                    utilruntime.HandleError(fmt.Errorf("%s: unable to delete watch event object (%#v) from store: %v", r.name, event.Object, err))
                }
            default:
                utilruntime.HandleError(fmt.Errorf("%s: unable to understand watch event %#v", r.name, event))
            }
            *resourceVersion = newResourceVersion
            r.setLastSyncResourceVersion(newResourceVersion)
            eventCount++
        }
    }

    watchDuration := r.clock.Now().Sub(start)
    if watchDuration < 1*time.Second && eventCount == 0 {
        r.metrics.numberOfShortWatches.Inc()
        return fmt.Errorf("very short watch: %s: Unexpected watch close - watch lasted less than a second and no items received", r.name)
    }
    glog.V(4).Infof("%s: Watch close - %v total %v items received", r.name, r.expectedType, eventCount)
    return nil
}
```

上面的r.store结构中，r代表reflector,store为watchCache结构，所以是对WatchCache进行更新，该代码位于k8s.io/apiserver/pkg/storage/cacher/watch_cache.go：

* 创建一个Event,然后调用processEvent

```go
func (w *watchCache) Add(obj interface{}) error {
    object, resourceVersion, err := w.objectToVersionedRuntimeObject(obj)
    if err != nil {
        return err
    }
    event := watch.Event{Type: watch.Added, Object: object}

    f := func(elem *storeElement) error { return w.store.Add(elem) }
    return w.processEvent(event, resourceVersion, f)
}

// Update takes runtime.Object as an argument.
func (w *watchCache) Update(obj interface{}) error {
    object, resourceVersion, err := w.objectToVersionedRuntimeObject(obj)
    if err != nil {
        return err
    }
    event := watch.Event{Type: watch.Modified, Object: object}

    f := func(elem *storeElement) error { return w.store.Update(elem) }
    return w.processEvent(event, resourceVersion, f)
}

// Delete takes runtime.Object as an argument.
func (w *watchCache) Delete(obj interface{}) error {
    object, resourceVersion, err := w.objectToVersionedRuntimeObject(obj)
    if err != nil {
        return err
    }
    event := watch.Event{Type: watch.Deleted, Object: object}

    f := func(elem *storeElement) error { return w.store.Delete(elem) }
    return w.processEvent(event, resourceVersion, f)
}
```

processEvent():

* 创建一个watchCacheEvent
* OnEnvent将该事件发送到Cacher结构的incoming管道
* 调用watchCache.updateCache(),更新cache
* Broadcast事件，触发条件变量cond.wait阻塞的部分

```go
func (w *watchCache) processEvent(event watch.Event, resourceVersion uint64, updateFunc func(*storeElement) error) error {
    key, err := w.keyFunc(event.Object)
    if err != nil {
        return fmt.Errorf("couldn't compute key: %v", err)
    }
    elem := &storeElement{Key: key, Object: event.Object}
    elem.Labels, elem.Fields, elem.Uninitialized, err = w.getAttrsFunc(event.Object)
    if err != nil {
        return err
    }

    watchCacheEvent := &watchCacheEvent{
        Type:             event.Type,
        Object:           elem.Object,
        ObjLabels:        elem.Labels,
        ObjFields:        elem.Fields,
        ObjUninitialized: elem.Uninitialized,
        Key:              key,
        ResourceVersion:  resourceVersion,
    }

    // TODO: We should consider moving this lock below after the watchCacheEvent
    // is created. In such situation, the only problematic scenario is Replace(
    // happening after getting object from store and before acquiring a lock.
    // Maybe introduce another lock for this purpose.
    w.Lock()
    defer w.Unlock()
    previous, exists, err := w.store.Get(elem)
    if err != nil {
        return err
    }
    if exists {
        previousElem := previous.(*storeElement)
        watchCacheEvent.PrevObject = previousElem.Object
        watchCacheEvent.PrevObjLabels = previousElem.Labels
        watchCacheEvent.PrevObjFields = previousElem.Fields
        watchCacheEvent.PrevObjUninitialized = previousElem.Uninitialized
    }

    if w.onEvent != nil {
        w.onEvent(watchCacheEvent)//将监听到的事件发送到Cacher的管道
    }
    w.updateCache(resourceVersion, watchCacheEvent)//更新cache数据
    w.resourceVersion = resourceVersion
    w.cond.Broadcast()
    return updateFunc(elem)
}
```
