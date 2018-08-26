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
    go cacher.dispatchEvents()

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
*  ListerWatcher实际是一个包含List和Watch的接口，这里的接口实际上是newCacherListerWatcher函数创建的cacherListerWatcher结构
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

调用该storage.Watch实际上就是调用vendor/k8s.io/apiserver/pkg/storage/etcd3/watcher.go中的WatchList函数：

* 创建etcdWatcher对象,创建的同时，启动一个协程监听incomming channel,有数据则处理后通过outgoing channel发出
* 该etcdWatcher对象主要实现了Watch接口
* 该etcdWatcher对象主要的数据成员是etcdincoming channel和outgoing channel

```
func (h *etcdHelper) WatchList(ctx context.Context, key string, resourceVersion string, pred storage.SelectionPredicate) (watch.Interface, error) {
    if ctx == nil {
        glog.Errorf("Context is nil")
    }
    watchRV, err := h.versioner.ParseResourceVersion(resourceVersion)
    if err != nil {
        return nil, err
    }
    key = path.Join(h.pathPrefix, key)
    w := newEtcdWatcher(true, h.quorum, exceptKey(key), pred, h.codec, h.versioner, nil, h.transformer, h)
    go w.etcdWatch(ctx, h.etcdKeysAPI, key, watchRV)
    return w, nil
}

```

etcdWatcher.etcdWatch()函数的实现:

* 根据版本获取新的POD信息，将该信息的resp通过incoming channel发送
* 循环调用watcher.Next()应该是不断轮询所有的watcher?

```go
func (w *etcdWatcher) etcdWatch(ctx context.Context, client etcd.KeysAPI, key string, resourceVersion uint64) {
    defer utilruntime.HandleCrash()
    defer close(w.etcdError)
    defer close(w.etcdIncoming)

    // All calls to etcd are coming from this function - once it is finished
    // no other call to etcd should be generated by this watcher.
    done := func() {}

    // We need to be prepared, that Stop() can be called at any time.
    // It can potentially also be called, even before this function is called.
    // If that is the case, we simply skip all the code here.
    // See #18928 for more details.
    var watcher etcd.Watcher
    returned := func() bool {
        w.stopLock.Lock()
        defer w.stopLock.Unlock()
        if w.stopped {
            // Watcher has already been stopped - don't event initiate it here.
            return true
        }
        w.wg.Add(1)
        done = w.wg.Done
        // Perform initialization of watcher under lock - we want to avoid situation when
        // Stop() is called in the meantime (which in tests can cause etcd termination and
        // strange behavior here).
        if resourceVersion == 0 {
            latest, err := etcdGetInitialWatchState(ctx, client, key, w.list, w.quorum, w.etcdIncoming)//如果版本等于0，则获取所有的pods信息
            if err != nil {
                w.etcdError <- err
                return true
            }
            resourceVersion = latest
        }

        opts := etcd.WatcherOptions{
            Recursive:  w.list,
            AfterIndex: resourceVersion,
        }
        watcher = client.Watcher(key, &opts)
        w.ctx, w.cancel = context.WithCancel(ctx)
        return false
    }()
    defer done()
    if returned {
        return
    }

    for {//循环调用Watch.Next(),将结果放入到incomming channel，将所有的
        resp, err := watcher.Next(w.ctx)
        if err != nil {
            w.etcdError <- err
            return
        }
        w.etcdIncoming <- resp
    }
}


func (hw *httpWatcher) Next(ctx context.Context) (*Response, error) {
    for {
        httpresp, body, err := hw.client.Do(ctx, &hw.nextWait)
        if err != nil {
            return nil, err
        }

        resp, err := unmarshalHTTPResponse(httpresp.StatusCode, httpresp.Header, body)
        if err != nil {
            if err == ErrEmptyBody {
                continue
            }
            return nil, err
        }

        hw.nextWait.WaitIndex = resp.Node.ModifiedIndex + 1
        return resp, nil
    }
}
```

etcdWatcher.translate:该函数会在newEtcdWatcher中被调用来监听从incomming管道的数据并发送到outgoing管道的数据 

apiserver/pkg/storage/etcd/etcd_watcher.go translate()

1. 读取etcdIncoming channel信息；
2. 调用etcdWatcher.sendResult()进行转化;
3. 输入到outgoing channel；

```go
func (w *etcdWatcher) translate() {
    defer w.wg.Done()
    defer close(w.outgoing)
    defer utilruntime.HandleCrash()

    for {
        select {
        case err := <-w.etcdError:
            if err != nil {
                var status *metav1.Status
                switch {
                case etcdutil.IsEtcdWatchExpired(err):
                    status = &metav1.Status{
                        Status:  metav1.StatusFailure,
                        Message: err.Error(),
                        Code:    http.StatusGone, // Gone
                        Reason:  metav1.StatusReasonExpired,
                    }
                // TODO: need to generate errors using api/errors which has a circular dependency on this package
                //   no other way to inject errors
                // case etcdutil.IsEtcdUnreachable(err):
                //   status = errors.NewServerTimeout(...)
                default:
                    status = &metav1.Status{
                        Status:  metav1.StatusFailure,
                        Message: err.Error(),
                        Code:    http.StatusInternalServerError,
                        Reason:  metav1.StatusReasonInternalError,
                    }
                }
                w.emit(watch.Event{
                    Type:   watch.Error,
                    Object: status,
                })
            }
            return
        case <-w.userStop:
            return
        case res, ok := <-w.etcdIncoming://从incoming中读取数据
            if ok {
                if curLen := int64(len(w.etcdIncoming)); w.incomingHWM.Update(curLen) {
                    // Monitor if this gets backed up, and how much.
                    glog.V(1).Infof("watch: %v objects queued in incoming channel.", curLen)
                }
                w.sendResult(res)//通过outgoing channel发送
            }
            // If !ok, don't return here-- must wait for etcdError channel
            // to give an error or be closed.
        }
    }
}
```


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

对WatchCache进行更新：

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
* 调用watchCache.updateCache(),更新cache
* Broadcast事件给watcher? 

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
        w.onEvent(watchCacheEvent)
    }
    w.updateCache(resourceVersion, watchCacheEvent)
    w.resourceVersion = resourceVersion
    w.cond.Broadcast()
    return updateFunc(elem)
}
```
