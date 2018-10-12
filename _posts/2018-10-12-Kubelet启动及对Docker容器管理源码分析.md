---
layout:     post
title:      "Kubelet启动源码分析"
subtitle:   "kubernetes"
date:       2018-10-12 09:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - kubernetes
---

# Kubelet启动及对Docker容器管理源码分析

## Kubelet启动流程分析


![Kubelet](/img/kubelet.png)


当执行kubelet...命令后，会调用到cmd/kubelet/kubelet.go的main()函数，该函数如下，主要借助cobra.Command工具构建一个Kubelet命令结构，然后执行Execute():

```go
func main() {
    rand.Seed(time.Now().UTC().UnixNano())

    command := app.NewKubeletCommand(server.SetupSignalHandler())
    logs.InitLogs()
    defer logs.FlushLogs()

    if err := command.Execute(); err != nil {
        fmt.Fprintf(os.Stderr, "%v\n", err)
        os.Exit(1)
    }
}
```

### NewKubeletCommand命令构建分析

app.NewKubeletCommand()主要的任务是：

* 验证kubelet启动参数
* kubelet对象生成
* kubelet维护pod所需要的服务

```go

// NewKubeletCommand creates a *cobra.Command object with default parameters
func NewKubeletCommand(stopCh <-chan struct{}) *cobra.Command {
    cleanFlagSet := pflag.NewFlagSet(componentKubelet, pflag.ContinueOnError)
    cleanFlagSet.SetNormalizeFunc(flag.WordSepNormalizeFunc)
    //设置默认的KubeletFlags的值,包括docker,证书路径，插件目录，包括CIDR等等信息
    kubeletFlags := options.NewKubeletFlags()

    //生成kubelet默认的配置文件
    kubeletConfig, err := options.NewKubeletConfiguration()
    // programmer error
    if err != nil {
        glog.Fatal(err)
    }

    cmd := &cobra.Command{
        Use: componentKubelet,
        Long: `The kubelet is the primary "node agent" that runs on each
node. The kubelet works in terms of a PodSpec. A PodSpec is a YAML or JSON object
that describes a pod. The kubelet takes a set of PodSpecs that are provided through
various mechanisms (primarily through the apiserver) and ensures that the containers
described in those PodSpecs are running and healthy. The kubelet doesn't manage
containers which were not created by Kubernetes.

Other than from an PodSpec from the apiserver, there are three ways that a container
manifest can be provided to the Kubelet.

File: Path passed as a flag on the command line. Files under this path will be monitored
periodically for updates. The monitoring period is 20s by default and is configurable
via a flag.

HTTP endpoint: HTTP endpoint passed as a parameter on the command line. This endpoint
is checked every 20 seconds (also configurable with a flag).

HTTP server: The kubelet can also listen for HTTP and respond to a simple API
(underspec'd currently) to submit a new manifest.`,
        // The Kubelet has special flag parsing requirements to enforce flag precedence rules,
        // so we do all our parsing manually in Run, below.
        // DisableFlagParsing=true provides the full set of flags passed to the kubelet in the
        // `args` arg to Run, without Cobra's interference.
        DisableFlagParsing: true,
        Run: func(cmd *cobra.Command, args []string) {
            // initial flag parse, since we disable cobra's flag parsing
            if err := cleanFlagSet.Parse(args); err != nil {
                cmd.Usage()
                glog.Fatal(err)
            }

            // check if there are non-flag arguments in the command line
         ... 
         ...

            // short-circuit on verflag
            verflag.PrintAndExitIfRequested()
            utilflag.PrintFlags(cleanFlagSet)

            // set feature gates from initial flags-based config
            if err := utilfeature.DefaultFeatureGate.SetFromMap(kubeletConfig.FeatureGates); err != nil {
                glog.Fatal(err)
            }

            // validate the initial KubeletFlags
            if err := options.ValidateKubeletFlags(kubeletFlags); err != nil {
                glog.Fatal(err)
            }
            //制定默认的pod运行所需要的基础镜像
            if kubeletFlags.ContainerRuntime == "remote" && cleanFlagSet.Changed("pod-infra-container-image") {
                glog.Warning("Warning: For remote container runtime, --pod-infra-container-image is ignored in kubelet, which should be set in that remote runtime instead")
            }

            // load kubelet config file, if provided
            if configFile := kubeletFlags.KubeletConfigFile; len(configFile) > 0 {
                kubeletConfig, err = loadConfigFile(configFile)
                if err != nil {
                    glog.Fatal(err)
                }
                // We must enforce flag precedence by re-parsing the command line into the new object.
                // This is necessary to preserve backwards-compatibility across binary upgrades.
                // See issue #56171 for more details.
                if err := kubeletConfigFlagPrecedence(kubeletConfig, args); err != nil {
                    glog.Fatal(err)
                }
                // update feature gates based on new config
                if err := utilfeature.DefaultFeatureGate.SetFromMap(kubeletConfig.FeatureGates); err != nil {
                    glog.Fatal(err)
                }
            }

            // We always validate the local configuration (command line + config file).
            // This is the default "last-known-good" config for dynamic config, and must always remain valid.
            if err := kubeletconfigvalidation.ValidateKubeletConfiguration(kubeletConfig); err != nil {
                glog.Fatal(err)
            }

            // use dynamic kubelet config, if enabled
            //动态修改kubelet的配置文件
            //BootstrapKubeletConfigController该函数就是为了发起对kubelet配置文件的Watch请求，来时刻监听配置的改变，若发生改变则重新加载kubelet的配置文件
            var kubeletConfigController *dynamickubeletconfig.Controller
            if dynamicConfigDir := kubeletFlags.DynamicConfigDir.Value(); len(dynamicConfigDir) > 0 {
                var dynamicKubeletConfig *kubeletconfiginternal.KubeletConfiguration
                dynamicKubeletConfig, kubeletConfigController, err = BootstrapKubeletConfigController(dynamicConfigDir,
                    func(kc *kubeletconfiginternal.KubeletConfiguration) error {
                        // Here, we enforce flag precedence inside the controller, prior to the controller's validation sequence,
                        // so that we get a complete validation at the same point where we can decide to reject dynamic config.
                        // This fixes the flag-precedence component of issue #63305.
                        // See issue #56171 for general details on flag precedence.
                        return kubeletConfigFlagPrecedence(kc, args)
                    })
                if err != nil {
                    glog.Fatal(err)
                }
                // If we should just use our existing, local config, the controller will return a nil config
                if dynamicKubeletConfig != nil {
                    kubeletConfig = dynamicKubeletConfig
                    // Note: flag precedence was already enforced in the controller, prior to validation,
                    // by our above transform function. Now we simply update feature gates from the new config.
                    if err := utilfeature.DefaultFeatureGate.SetFromMap(kubeletConfig.FeatureGates); err != nil {
                        glog.Fatal(err)
                    }
                }
            }

            // construct a KubeletServer from kubeletFlags and kubeletConfig
            kubeletServer := &options.KubeletServer{
                KubeletFlags:         *kubeletFlags,
                KubeletConfiguration: *kubeletConfig,
            }

            // use kubeletServer to construct the default KubeletDeps kubelet的缓存

            kubeletDeps, err := UnsecuredDependencies(kubeletServer)
            if err != nil {
                glog.Fatal(err)
            }

            // add the kubelet config controller to kubeletDeps
            kubeletDeps.KubeletConfigController = kubeletConfigController

            ...
            ...

            // run the kubelet 运行kuberlet,入口主要是这个函数
            glog.V(5).Infof("KubeletConfiguration: %#v", kubeletServer.KubeletConfiguration)
            if err := Run(kubeletServer, kubeletDeps, stopCh); err != nil {
                glog.Fatal(err)
            }
        },
    }

    ...
    ...


    return cmd
}
```


由上诉代码可以得知，NewKubeletCommand的Run函数的主要功能实现部分为Run(kubeletServer, kubeletDeps, stopCh)；具体实现见：k8s.io/kubernetes/cmd/kubelet/app/server.go.Run

```go

func Run(s *options.KubeletServer, kubeDeps *kubelet.Dependencies, stopCh <-chan struct{}) error {
    // To help debugging, immediately log version
    glog.Infof("Version: %+v", version.Get())
    if err := initForOS(s.KubeletFlags.WindowsService); err != nil {
        return fmt.Errorf("failed OS init: %v", err)
    }
    //主要启动函数
    if err := run(s, kubeDeps, stopCh); err != nil {
        return fmt.Errorf("failed to run Kubelet: %v", err)
    }
    return nil
}
```

### NewKubeletCommand 核心功能执行函数run()

run函数的具体实现为：

* SetFromMap配置函数主要是配置kubelet启用的功能特性
* UnsecuredDependencies依赖启动分析
    - 这个函数主要是生成dockerClientConfig 存储mounter OOMAdjuster 
    - 探针插件管理ProbeVolumePlugins 
    - 同步插件探针GetDynamicPluginProber以及证书相关tlsOptions等等
* clientCertificateManager启动证书管理客户端
* 获取连接kube-apiserver的kubeclient,用于和kube-apiserver通信
* 获取事件client客户端eventClient
* 获取心跳客户端heartbeatClient
* 获取cadvisor客户端
* 启动容器管理服务ContainerManager：这个功能包括 Cgroup管理 mount管理 容器运行时管理 Cgroup驱动 qos服务质量审计等
* 开启健康检查的端口：HealthzPort
* 核心功能就是运行kebelet：RunKubelet（）

```go

func run(s *options.KubeletServer, kubeDeps *kubelet.Dependencies, stopCh <-chan struct{}) (err error) {
    // Set global feature gates based on the value on the initial KubeletServer
    err = utilfeature.DefaultFeatureGate.SetFromMap(s.KubeletConfiguration.FeatureGates)
    if err != nil {
        return err
    }
    // validate the initial KubeletServer (we set feature gates first, because this validation depends on feature gates)
    if err := options.ValidateKubeletServer(s); err != nil {
        return err
    }
    // Obtain Kubelet Lock File
    if s.ExitOnLockContention && s.LockFilePath == "" {
        return errors.New("cannot exit on lock file contention: no lock file specified")
    }
    done := make(chan struct{})
    if s.LockFilePath != "" {
        glog.Infof("acquiring file lock on %q", s.LockFilePath)
        if err := flock.Acquire(s.LockFilePath); err != nil {
            return fmt.Errorf("unable to acquire file lock on %q: %v", s.LockFilePath, err)
        }
        if s.ExitOnLockContention {
            glog.Infof("watching for inotify events for: %v", s.LockFilePath)
            if err := watchForLockfileContention(s.LockFilePath, done); err != nil {
                return err
            }
        }
    }

    // Register current configuration with /configz endpoint
    err = initConfigz(&s.KubeletConfiguration)
    if err != nil {
        glog.Errorf("unable to register KubeletConfiguration with configz, error: %v", err)
    }

    // About to get clients and such, detect standaloneMode
    standaloneMode := true
    if len(s.KubeConfig) > 0 {
        standaloneMode = false
    }

    if kubeDeps == nil {
        kubeDeps, err = UnsecuredDependencies(s)
        if err != nil {
            return err
        }
    }

    if kubeDeps.Cloud == nil {
        if !cloudprovider.IsExternal(s.CloudProvider) {
            cloud, err := cloudprovider.InitCloudProvider(s.CloudProvider, s.CloudConfigFile)
            if err != nil {
                return err
            }
            if cloud == nil {
                glog.V(2).Infof("No cloud provider specified: %q from the config file: %q\n", s.CloudProvider, s.CloudConfigFile)
            } else {
                glog.V(2).Infof("Successfully initialized cloud provider: %q from the config file: %q\n", s.CloudProvider, s.CloudConfigFile)
            }
            kubeDeps.Cloud = cloud
        }
    }

    hostName, err := nodeutil.GetHostname(s.HostnameOverride)
    if err != nil {
        return err
    }
    nodeName, err := getNodeName(kubeDeps.Cloud, hostName)
    if err != nil {
        return err
    }

    if s.BootstrapKubeconfig != "" {
        if err := bootstrap.LoadClientCert(s.KubeConfig, s.BootstrapKubeconfig, s.CertDirectory, nodeName); err != nil {
            return err
        }
    }

    // if in standalone mode, indicate as much by setting all clients to nil
    if standaloneMode {
        kubeDeps.KubeClient = nil
        kubeDeps.ExternalKubeClient = nil
        kubeDeps.EventClient = nil
        kubeDeps.HeartbeatClient = nil
        glog.Warningf("standalone mode, no API client")
    } else if kubeDeps.KubeClient == nil || kubeDeps.ExternalKubeClient == nil || kubeDeps.EventClient == nil || kubeDeps.HeartbeatClient == nil {
        // initialize clients if not standalone mode and any of the clients are not provided
        var kubeClient clientset.Interface
        var eventClient v1core.EventsGetter
        var heartbeatClient v1core.CoreV1Interface
        var externalKubeClient clientset.Interface

        clientConfig, err := createAPIServerClientConfig(s)
        if err != nil {
            return fmt.Errorf("invalid kubeconfig: %v", err)
        }

        var clientCertificateManager certificate.Manager
        if s.RotateCertificates && utilfeature.DefaultFeatureGate.Enabled(features.RotateKubeletClientCertificate) {
            clientCertificateManager, err = kubeletcertificate.NewKubeletClientCertificateManager(s.CertDirectory, nodeName, clientConfig.CertData, clientConfig.KeyData, clientConfig.CertFile, clientConfig.KeyFile)
            if err != nil {
                return err
            }
        }
        // we set exitAfter to five minutes because we use this client configuration to request new certs - if we are unable
        // to request new certs, we will be unable to continue normal operation. Exiting the process allows a wrapper
        // or the bootstrapping credentials to potentially lay down new initial config.
        closeAllConns, err := kubeletcertificate.UpdateTransport(wait.NeverStop, clientConfig, clientCertificateManager, 5*time.Minute)
        if err != nil {
            return err
        }

        kubeClient, err = clientset.NewForConfig(clientConfig)
        if err != nil {
            glog.Warningf("New kubeClient from clientConfig error: %v", err)
        } else if kubeClient.CertificatesV1beta1() != nil && clientCertificateManager != nil {
            glog.V(2).Info("Starting client certificate rotation.")
            clientCertificateManager.SetCertificateSigningRequestClient(kubeClient.CertificatesV1beta1().CertificateSigningRequests())
            clientCertificateManager.Start()
        }
        externalKubeClient, err = clientset.NewForConfig(clientConfig)
        if err != nil {
            glog.Warningf("New kubeClient from clientConfig error: %v", err)
        }

        // make a separate client for events
        eventClientConfig := *clientConfig
        eventClientConfig.QPS = float32(s.EventRecordQPS)
        eventClientConfig.Burst = int(s.EventBurst)
        eventClient, err = v1core.NewForConfig(&eventClientConfig)
        if err != nil {
            glog.Warningf("Failed to create API Server client for Events: %v", err)
        }

        // make a separate client for heartbeat with throttling disabled and a timeout attached
        heartbeatClientConfig := *clientConfig
        heartbeatClientConfig.Timeout = s.KubeletConfiguration.NodeStatusUpdateFrequency.Duration
        heartbeatClientConfig.QPS = float32(-1)
        heartbeatClient, err = v1core.NewForConfig(&heartbeatClientConfig)
        if err != nil {
            glog.Warningf("Failed to create API Server client for heartbeat: %v", err)
        }

        kubeDeps.KubeClient = kubeClient
        kubeDeps.ExternalKubeClient = externalKubeClient
        if heartbeatClient != nil {
            kubeDeps.HeartbeatClient = heartbeatClient
            kubeDeps.OnHeartbeatFailure = closeAllConns
        }
        if eventClient != nil {
            kubeDeps.EventClient = eventClient
        }
    }

    // If the kubelet config controller is available, and dynamic config is enabled, start the config and status sync loops
    if utilfeature.DefaultFeatureGate.Enabled(features.DynamicKubeletConfig) && len(s.DynamicConfigDir.Value()) > 0 &&
        kubeDeps.KubeletConfigController != nil && !standaloneMode && !s.RunOnce {
        if err := kubeDeps.KubeletConfigController.StartSync(kubeDeps.KubeClient, kubeDeps.EventClient, string(nodeName)); err != nil {
            return err
        }
    }

    if kubeDeps.Auth == nil {
        auth, err := BuildAuth(nodeName, kubeDeps.ExternalKubeClient, s.KubeletConfiguration)
        if err != nil {
            return err
        }
        kubeDeps.Auth = auth
    }

    if kubeDeps.CAdvisorInterface == nil {
        imageFsInfoProvider := cadvisor.NewImageFsInfoProvider(s.ContainerRuntime, s.RemoteRuntimeEndpoint)
        kubeDeps.CAdvisorInterface, err = cadvisor.New(imageFsInfoProvider, s.RootDirectory, cadvisor.UsingLegacyCadvisorStats(s.ContainerRuntime, s.RemoteRuntimeEndpoint))
        if err != nil {
            return err
        }
    }

    // Setup event recorder if required.
    makeEventRecorder(kubeDeps, nodeName)

    if kubeDeps.ContainerManager == nil {
        if s.CgroupsPerQOS && s.CgroupRoot == "" {
            glog.Infof("--cgroups-per-qos enabled, but --cgroup-root was not specified.  defaulting to /")
            s.CgroupRoot = "/"
        }
        kubeReserved, err := parseResourceList(s.KubeReserved)
        if err != nil {
            return err
        }
        systemReserved, err := parseResourceList(s.SystemReserved)
        if err != nil {
            return err
        }
        var hardEvictionThresholds []evictionapi.Threshold
        // If the user requested to ignore eviction thresholds, then do not set valid values for hardEvictionThresholds here.
        if !s.ExperimentalNodeAllocatableIgnoreEvictionThreshold {
            hardEvictionThresholds, err = eviction.ParseThresholdConfig([]string{}, s.EvictionHard, nil, nil, nil)
            if err != nil {
                return err
            }
        }
        experimentalQOSReserved, err := cm.ParseQOSReserved(s.QOSReserved)
        if err != nil {
            return err
        }

        devicePluginEnabled := utilfeature.DefaultFeatureGate.Enabled(features.DevicePlugins)

        kubeDeps.ContainerManager, err = cm.NewContainerManager(
            kubeDeps.Mounter,
            kubeDeps.CAdvisorInterface,
            cm.NodeConfig{
                RuntimeCgroupsName:    s.RuntimeCgroups,
                SystemCgroupsName:     s.SystemCgroups,
                KubeletCgroupsName:    s.KubeletCgroups,
                ContainerRuntime:      s.ContainerRuntime,
                CgroupsPerQOS:         s.CgroupsPerQOS,
                CgroupRoot:            s.CgroupRoot,
                CgroupDriver:          s.CgroupDriver,
                KubeletRootDir:        s.RootDirectory,
                ProtectKernelDefaults: s.ProtectKernelDefaults,
                NodeAllocatableConfig: cm.NodeAllocatableConfig{
                    KubeReservedCgroupName:   s.KubeReservedCgroup,
                    SystemReservedCgroupName: s.SystemReservedCgroup,
                    EnforceNodeAllocatable:   sets.NewString(s.EnforceNodeAllocatable...),
                    KubeReserved:             kubeReserved,
                    SystemReserved:           systemReserved,
                    HardEvictionThresholds:   hardEvictionThresholds,
                },
                QOSReserved:                           *experimentalQOSReserved,
                ExperimentalCPUManagerPolicy:          s.CPUManagerPolicy,
                ExperimentalCPUManagerReconcilePeriod: s.CPUManagerReconcilePeriod.Duration,
                ExperimentalPodPidsLimit:              s.PodPidsLimit,
                EnforceCPULimits:                      s.CPUCFSQuota,
            },
            s.FailSwapOn,
            devicePluginEnabled,
            kubeDeps.Recorder)

        if err != nil {
            return err
        }
    }

    if err := checkPermissions(); err != nil {
        glog.Error(err)
    }

    utilruntime.ReallyCrash = s.ReallyCrashForTesting

    rand.Seed(time.Now().UTC().UnixNano())

    // TODO(vmarmol): Do this through container config.
    oomAdjuster := kubeDeps.OOMAdjuster
    if err := oomAdjuster.ApplyOOMScoreAdj(0, int(s.OOMScoreAdj)); err != nil {
        glog.Warning(err)
    }

    if err := RunKubelet(s, kubeDeps, s.RunOnce); err != nil {
        return err
    }

    if s.HealthzPort > 0 {
        healthz.DefaultHealthz()
        go wait.Until(func() {
            err := http.ListenAndServe(net.JoinHostPort(s.HealthzBindAddress, strconv.Itoa(int(s.HealthzPort))), nil)
            if err != nil {
                glog.Errorf("Starting health server failed: %v", err)
            }
        }, 5*time.Second, wait.NeverStop)
    }

    if s.RunOnce {
        return nil
    }

    // If systemd is used, notify it that we have started
    go daemon.SdNotify(false, "READY=1")

    select {
    case <-done:
        break
    case <-stopCh:
        break
    }

    return nil
}
```

### RunKubelet新建Kubelet并启动

RunKubelet主要有两个任务：

* 新建Kubelet结构体：CreateAndInitKubelet
    - NewMainKubelet:创建Kubelet
    - BirthCry():宣告出生
    - 启动垃圾回收GC服务
* 启动Kubelet:startKubelet

### startKubelet

主要做如下三件事情：

* 运行Kubelet:kubelet.Run()
* 开启服务端口10250
* 开启只读端口10255

```go
func startKubelet(k kubelet.Bootstrap, podCfg *config.PodConfig, kubeCfg *kubeletconfiginternal.KubeletConfiguration, kubeDeps *kubelet.Dependencies, enableServer bool) {
    wg := sync.WaitGroup{}

    // start the kubelet
    wg.Add(1)
    go wait.Until(func() {
        wg.Done()
        k.Run(podCfg.Updates())
    }, 0, wait.NeverStop)

    // start the kubelet server
    //开启服务端口 10250

    if enableServer {
        wg.Add(1)
        go wait.Until(func() {
            wg.Done()
            k.ListenAndServe(net.ParseIP(kubeCfg.Address), uint(kubeCfg.Port), kubeDeps.TLSOptions, kubeDeps.Auth, kubeCfg.EnableDebuggingHandlers, kubeCfg.EnableContentionProfiling)
        }, 0, wait.NeverStop)
    }

    //开启只读接口10255

    if kubeCfg.ReadOnlyPort > 0 {
        wg.Add(1)
        go wait.Until(func() {
            wg.Done()
            k.ListenAndServeReadOnly(net.ParseIP(kubeCfg.Address), uint(kubeCfg.ReadOnlyPort))
        }, 0, wait.NeverStop)
    }
    wg.Wait()
}
```

### Kubelet.Run()启动函数分析

启动pod管理所需要的一些服务程序：

* 初始化模块：volume,数据目录，容器日志
* 启动镜像管理，证书管理，OOM管理
* 启动资源分析器
* 启动网络工具Util
* 启动pod管理的删除机制
* 启动状态管理，探针管理
* 启动容器的生命周期PLEG
* 启动pod的同步进程syncLoop

```go
// Run starts the kubelet reacting to config updates
func (kl *Kubelet) Run(updates <-chan kubetypes.PodUpdate) {
    //启动日志服务
    if kl.logServer == nil {
        kl.logServer = http.StripPrefix("/logs/", http.FileServer(http.Dir("/var/log/")))
    }
    if kl.kubeClient == nil {
        glog.Warning("No api server defined - no node status update will be sent.")
    }
    //初始化模块,包括volume 数据目录 容器日志
    //启动镜像管理 启动证书管理 OOM管理
    //启动资源分析器
    if err := kl.initializeModules(); err != nil {
        kl.recorder.Eventf(kl.nodeRef, v1.EventTypeWarning, events.KubeletSetupFailed, err.Error())
        glog.Fatal(err)
    }

    // Start volume manager
    //启动volume管理
    go kl.volumeManager.Run(kl.sourcesReady, wait.NeverStop)

    if kl.kubeClient != nil {
        // Start syncing node status immediately, this may set up things the runtime needs to run.
        go wait.Until(kl.syncNodeStatus, kl.nodeStatusUpdateFrequency, wait.NeverStop)
    }
    go wait.Until(kl.updateRuntimeUp, 5*time.Second, wait.NeverStop)

    //启用网络util
    // Start loop to sync iptables util rules
    if kl.makeIPTablesUtilChains {
        go wait.Until(kl.syncNetworkUtil, 1*time.Minute, wait.NeverStop)
    }

    //启用pod的删除管理机制
    // Start a goroutine responsible for killing pods (that are not properly
    // handled by pod workers).
    go wait.Until(kl.podKiller, 1*time.Second, wait.NeverStop)

    // Start gorouting responsible for checking limits in resolv.conf
    if kl.dnsConfigurer.ResolverConfig != "" {
        go wait.Until(func() { kl.dnsConfigurer.CheckLimitsForResolvConf() }, 30*time.Second, wait.NeverStop)
    }

    // Start component sync loops.
    kl.statusManager.Start() //状态管理
    kl.probeManager.Start() //探针管理

    // Start the pod lifecycle event generator.
    kl.pleg.Start() //启动容器的生命周期
    kl.syncLoop(updates, kl) //循环同步
}
```

### syncLoop()

* 发起对PLEG（Pod生命周期）的监听
* syncLoopIteration来发起对POD的状态同步，及对POD状态改动时的相应处理

```go
func (kl *Kubelet) syncLoop(updates <-chan kubetypes.PodUpdate, handler SyncHandler) {
    glog.Info("Starting kubelet main sync loop.")
    // The resyncTicker wakes up kubelet to checks if there are any pod workers
    // that need to be sync'd. A one-second period is sufficient because the
    // sync interval is defaulted to 10s.
    syncTicker := time.NewTicker(time.Second)
    defer syncTicker.Stop()
    housekeepingTicker := time.NewTicker(housekeepingPeriod)
    defer housekeepingTicker.Stop()
    plegCh := kl.pleg.Watch()
    const (
        base   = 100 * time.Millisecond
        max    = 5 * time.Second
        factor = 2
    )
    duration := base
    for {
        if rs := kl.runtimeState.runtimeErrors(); len(rs) != 0 {
            glog.Infof("skipping pod synchronization - %v", rs)
            // exponential backoff
            time.Sleep(duration)
            duration = time.Duration(math.Min(float64(max), factor*float64(duration)))
            continue
        }
        // reset backoff if we have a success
        duration = base

        kl.syncLoopMonitor.Store(kl.clock.Now())
        if !kl.syncLoopIteration(updates, handler, syncTicker.C, housekeepingTicker.C, plegCh) {
            break
        }
        kl.syncLoopMonitor.Store(kl.clock.Now())
    }
}
```

### syncLoopIteration发起POD的状态同步及相应处理

syncLoopIteration对各管道进行监听：

* configCh用于传输Pod配置信息改动的事件PodUpdate,包括：ADD,UPDATE,REMOVE,RECONCILE,DELETE,RESTORE,SET,CHECKPOINT(源码修改)
* plegCh：用于更新运行时缓存
* syncCh:同步所有的Pod,如第一次拉取
* houseKeepingCh：trigger cleanup of pods

注意这里的configCh中的kubetypes与之前的blog[APIServer和组件kubelet通信源码分析](http://blog.xbblfz.site/2018/08/27/APIServer%E5%92%8C%E7%BB%84%E4%BB%B6(kubelet)%E9%80%9A%E4%BF%A1%E6%BA%90%E7%A0%81%E5%88%86%E6%9E%90/)中的Merge函数中的ADD,UPDATE...保持一致。


```
func (kl *Kubelet) syncLoopIteration(configCh <-chan kubetypes.PodUpdate, handler SyncHandler,
    syncCh <-chan time.Time, housekeepingCh <-chan time.Time, plegCh <-chan *pleg.PodLifecycleEvent) bool {
    select {
    case u, open := <-configCh:
        // Update from a config source; dispatch it to the right handler
        // callback.
        if !open {
            glog.Errorf("Update channel is closed. Exiting the sync loop.")
            return false
        }

        switch u.Op {
        case kubetypes.ADD:
            glog.V(2).Infof("SyncLoop (ADD, %q): %q", u.Source, format.Pods(u.Pods))
            // After restarting, kubelet will get all existing pods through
            // ADD as if they are new pods. These pods will then go through the
            // admission process and *may* be rejected. This can be resolved
            // once we have checkpointing.
            handler.HandlePodAdditions(u.Pods)
        case kubetypes.UPDATE:
            glog.V(2).Infof("SyncLoop (UPDATE, %q): %q", u.Source, format.PodsWithDeletionTimestamps(u.Pods))
            handler.HandlePodUpdates(u.Pods)
        case kubetypes.REMOVE:
            glog.V(2).Infof("SyncLoop (REMOVE, %q): %q", u.Source, format.Pods(u.Pods))
            handler.HandlePodRemoves(u.Pods)
        case kubetypes.RECONCILE:
            glog.V(4).Infof("SyncLoop (RECONCILE, %q): %q", u.Source, format.Pods(u.Pods))
            handler.HandlePodReconcile(u.Pods)
        case kubetypes.DELETE:
            glog.V(2).Infof("SyncLoop (DELETE, %q): %q", u.Source, format.Pods(u.Pods))
            // DELETE is treated as a UPDATE because of graceful deletion.
            handler.HandlePodUpdates(u.Pods)
        case kubetypes.RESTORE:
            glog.V(2).Infof("SyncLoop (RESTORE, %q): %q", u.Source, format.Pods(u.Pods))
            // These are pods restored from the checkpoint. Treat them as new
            // pods.
            handler.HandlePodAdditions(u.Pods)
        case kubetypes.SET:
            // TODO: Do we want to support this?
            glog.Errorf("Kubelet does not support snapshot update")
        case kubetypes.CHECKPOINT:
            glog.V(2).Infof("SyncLoop Checkpoint:%v", u.PodCheckpoint)
            fmt.Println("Entering syncLoopIteration")
            handler.HandlePodCheckpoint(u.Pods, u.PodCheckpoint)
        }

        if u.Op != kubetypes.RESTORE {
            // If the update type is RESTORE, it means that the update is from
            // the pod checkpoints and may be incomplete. Do not mark the
            // source as ready.

            // Mark the source ready after receiving at least one update from the
            // source. Once all the sources are marked ready, various cleanup
            // routines will start reclaiming resources. It is important that this
            // takes place only after kubelet calls the update handler to process
            // the update to ensure the internal pod cache is up-to-date.
            kl.sourcesReady.AddSource(u.Source)
        }
    case e := <-plegCh:
        if isSyncPodWorthy(e) {
            // PLEG event for a pod; sync it.
            if pod, ok := kl.podManager.GetPodByUID(e.ID); ok {
                glog.V(2).Infof("SyncLoop (PLEG): %q, event: %#v", format.Pod(pod), e)
                handler.HandlePodSyncs([]*v1.Pod{pod})
            } else {
                // If the pod no longer exists, ignore the event.
                glog.V(4).Infof("SyncLoop (PLEG): ignore irrelevant event: %#v", e)
            }
        }

        if e.Type == pleg.ContainerDied {
            if containerID, ok := e.Data.(string); ok {
                kl.cleanUpContainersInPod(e.ID, containerID)
            }
        }
    case <-syncCh:
        // Sync pods waiting for sync
        podsToSync := kl.getPodsToSync()
        if len(podsToSync) == 0 {
            break
        }
        glog.V(4).Infof("SyncLoop (SYNC): %d pods; %s", len(podsToSync), format.Pods(podsToSync))
        handler.HandlePodSyncs(podsToSync)
    case update := <-kl.livenessManager.Updates():
        if update.Result == proberesults.Failure {
            // The liveness manager detected a failure; sync the pod.

            // We should not use the pod from livenessManager, because it is never updated after
            // initialization.
            pod, ok := kl.podManager.GetPodByUID(update.PodUID)
            if !ok {
                // If the pod no longer exists, ignore the update.
                glog.V(4).Infof("SyncLoop (container unhealthy): ignore irrelevant update: %#v", update)
                break
            }
            glog.V(1).Infof("SyncLoop (container unhealthy): %q", format.Pod(pod))
            handler.HandlePodSyncs([]*v1.Pod{pod})
        }
    case <-housekeepingCh:
        if !kl.sourcesReady.AllReady() {
            // If the sources aren't ready or volume manager has not yet synced the states,
            // skip housekeeping, as we may accidentally delete pods from unready sources.
            glog.V(4).Infof("SyncLoop (housekeeping, skipped): sources aren't ready yet.")
        } else {
            glog.V(4).Infof("SyncLoop (housekeeping)")
            if err := handler.HandlePodCleanups(); err != nil {
                glog.Errorf("Failed cleaning pods: %v", err)
            }
        }
    }
    return true
}
```

### HandlePodAdditions Pod创建处理

* podManager添加Pod信息
* GetMirrorPodByPod（）获取镜像pod?
* 为**每个pod**都进行dispatchWork分发工作执行Pod具体的添加相关操作
* podManager再添加一次Pod信息？

```go
func (kl *Kubelet) HandlePodAdditions(pods []*v1.Pod) {
    start := kl.clock.Now()
    sort.Sort(sliceutils.PodsByCreationTime(pods))
    for _, pod := range pods {
        // Responsible for checking limits in resolv.conf
        if kl.dnsConfigurer != nil && kl.dnsConfigurer.ResolverConfig != "" {
            kl.dnsConfigurer.CheckLimitsForResolvConf()
        }
        existingPods := kl.podManager.GetPods()
        // Always add the pod to the pod manager. Kubelet relies on the pod
        // manager as the source of truth for the desired state. If a pod does
        // not exist in the pod manager, it means that it has been deleted in
        // the apiserver and no action (other than cleanup) is required.
        kl.podManager.AddPod(pod)

        if kubepod.IsMirrorPod(pod) {
            kl.handleMirrorPod(pod, start)
            continue
        }

        if !kl.podIsTerminated(pod) {
            // Only go through the admission process if the pod is not
            // terminated.

            // We failed pods that we rejected, so activePods include all admitted
            // pods that are alive.
            activePods := kl.filterOutTerminatedPods(existingPods)

            // Check if we can admit the pod; if not, reject it.
            if ok, reason, message := kl.canAdmitPod(activePods, pod); !ok {
                kl.rejectPod(pod, reason, message)
                continue
            }
        }
        mirrorPod, _ := kl.podManager.GetMirrorPodByPod(pod)
        kl.dispatchWork(pod, nil, kubetypes.SyncPodCreate, mirrorPod, start)
        kl.probeManager.AddPod(pod)
    }
}
```

### dispatchWork分发工作执行Pod变动的具体操作

核心函数和工作在kl.podWorkers.UpdatePod（UpdatePodOptions）中具体执行

```go

func (kl *Kubelet) dispatchWork(pod *v1.Pod, podcheckpoint *v1alpha.PodCheckpoint, syncType kubetypes.SyncPodType, mirrorPod *v1.Pod, start time.Time) {
    fmt.Println("Start dispatchWork!")
    if kl.podIsTerminated(pod) {
        if pod.DeletionTimestamp != nil {
            // If the pod is in a terminated state, there is no pod worker to
            // handle the work item. Check if the DeletionTimestamp has been
            // set, and force a status update to trigger a pod deletion request
            // to the apiserver.
            kl.statusManager.TerminatePod(pod)
        }
        return
    }
    // Run the sync in an async worker.
    kl.podWorkers.UpdatePod(&UpdatePodOptions{
        Pod:           pod,
        MirrorPod:     mirrorPod,
        UpdateType:    syncType,
        PodCheckpoint: podcheckpoint,
        OnCompleteFunc: func(err error) {
            if err != nil {
                metrics.PodWorkerLatency.WithLabelValues(syncType.String()).Observe(metrics.SinceInMicroseconds(start))
            }
        },
    })
    // Note the number of containers for new pods.
    if syncType == kubetypes.SyncPodCreate {
        metrics.ContainersPerPodCount.Observe(float64(len(pod.Spec.Containers)))
    }
}

```

UpdatePod实现如下：

* 为每一个pod都创建一个podUpdates管道，该管道只传输针对该POD信息的更新信息
* 创建一个线程并调用managePodLoop来对每个管道进行管理和监听



```
func (p *podWorkers) UpdatePod(options *UpdatePodOptions) {
    fmt.Println("Start UpdatePod:%v", options.PodCheckpoint)
    pod := options.Pod
    uid := pod.UID
    var podUpdates chan UpdatePodOptions
    var exists bool

    p.podLock.Lock()
    defer p.podLock.Unlock()
    if podUpdates, exists = p.podUpdates[uid]; !exists {
        // We need to have a buffer here, because checkForUpdates() method that
        // puts an update into channel is called from the same goroutine where
        // the channel is consumed. However, it is guaranteed that in such case
        // the channel is empty, so buffer of size 1 is enough.
        podUpdates = make(chan UpdatePodOptions, 1)
        p.podUpdates[uid] = podUpdates

        // Creating a new pod worker either means this is a new pod, or that the
        // kubelet just restarted. In either case the kubelet is willing to believe
        // the status of the pod for the first pod worker sync. See corresponding
        // comment in syncPod.
        go func() {
            defer runtime.HandleCrash()
            p.managePodLoop(podUpdates)//启动线程对每个管道都进行管理监听
        }()
    }
    if !p.isWorking[pod.UID] {
        p.isWorking[pod.UID] = true
        podUpdates <- *options
    } else {
        // if a request to kill a pod is pending, we do not let anything overwrite that request.
        update, found := p.lastUndeliveredWorkUpdate[pod.UID]
        if !found || update.UpdateType != kubetypes.SyncPodKill {
            p.lastUndeliveredWorkUpdate[pod.UID] = *options
        }
    }
}
```

managePodLoop来对每个管道进行管理实现：

* for range监听该管道传送过来的更新信息
* 对pod的每个更新的信息update进一步调用syncPodFn来具体执行pod变动之后的操作
* syncPodFn根据更新信息的类型type来执行相关操作，大部分工作是通过调用SyncPod来进行的

```go
func (p *podWorkers) managePodLoop(podUpdates <-chan UpdatePodOptions) {
    var lastSyncTime time.Time
    for update := range podUpdates {//监听该管道传送过来的更新信息
        err := func() error {
            podUID := update.Pod.UID
            // This is a blocking call that would return only if the cache
            // has an entry for the pod that is newer than minRuntimeCache
            // Time. This ensures the worker doesn't start syncing until
            // after the cache is at least newer than the finished time of
            // the previous sync.
            status, err := p.podCache.GetNewerThan(podUID, lastSyncTime)
            if err != nil {
                // This is the legacy event thrown by manage pod loop
                // all other events are now dispatched from syncPodFn
                p.recorder.Eventf(update.Pod, v1.EventTypeWarning, events.FailedSync, "error determining status: %v", err)
                return err
            }
            err = p.syncPodFn(syncPodOptions{
                mirrorPod:      update.MirrorPod,
                pod:            update.Pod,
                podcheckpoint:  update.PodCheckpoint,
                podStatus:      status,
                killPodOptions: update.KillPodOptions,
                updateType:     update.UpdateType,
            })
            lastSyncTime = time.Now()
            return err
        }()
        // notify the call-back function if the operation succeeded or not
        if update.OnCompleteFunc != nil {
            update.OnCompleteFunc(err)
        }
        if err != nil {
            // IMPORTANT: we do not log errors here, the syncPodFn is responsible for logging errors
            glog.Errorf("Error syncing pod %s (%q), skipping: %v", update.Pod.UID, format.Pod(update.Pod), err)
        }
        p.wrapUp(update.Pod.UID, err)
    }
}
```

### SyncPod执行具体的Pod更新操作

1. 计算并统计sandbox(pod的封闭运行环境信息)和pod内部的容器的变动情况
    ```go
    changes := podActions{
        KillPod:           createPodSandbox,//要删除的Pod
        CreateSandbox:     createPodSandbox,//要创建的Pod运行环境
        SandboxID:         sandboxID,
        Attempt:           attempt,
        ContainersToStart: []int{},//要启动的容器
        ContainersToKill:  make(map[kubecontainer.ContainerID] //containerToKillInfo),
    }
    ```
2. 删除应该删除的沙盒信息
3. 关闭不应该启动的容器
4. 创建需要创建的pod沙盒信息
5. 创建pod的初始化容器
6. 创建正常的容器

```go
//  1. Compute sandbox and container changes.
//  2. Kill pod sandbox if necessary.
//  3. Kill any containers that should not be running.
//  4. Create sandbox if necessary.
//  5. Create init containers.
//  6. Create normal containers.
func (m *kubeGenericRuntimeManager) SyncPod(pod *v1.Pod, _ v1.PodStatus, podStatus *kubecontainer.PodStatus, pullSecrets []v1.Secret, backOff *flowcontrol.Backoff) (result kubecontainer.PodSyncResult) {
    // Step 1: Compute sandbox and container changes.
    podContainerChanges := m.computePodActions(pod, podStatus)
    glog.V(3).Infof("computePodActions got %+v for pod %q", podContainerChanges, format.Pod(pod))
    if podContainerChanges.CreateSandbox {
        ref, err := ref.GetReference(legacyscheme.Scheme, pod)
        if err != nil {
            glog.Errorf("Couldn't make a ref to pod %q: '%v'", format.Pod(pod), err)
        }
        if podContainerChanges.SandboxID != "" {
            m.recorder.Eventf(ref, v1.EventTypeNormal, events.SandboxChanged, "Pod sandbox changed, it will be killed and re-created.")
        } else {
            glog.V(4).Infof("SyncPod received new pod %q, will create a sandbox for it", format.Pod(pod))
        }
    }

    // Step 2: Kill the pod if the sandbox has changed.
    if podContainerChanges.KillPod {
        if !podContainerChanges.CreateSandbox {
            glog.V(4).Infof("Stopping PodSandbox for %q because all other containers are dead.", format.Pod(pod))
        } else {
            glog.V(4).Infof("Stopping PodSandbox for %q, will start new one", format.Pod(pod))
        }

        killResult := m.killPodWithSyncResult(pod, kubecontainer.ConvertPodStatusToRunningPod(m.runtimeName, podStatus), nil)
        result.AddPodSyncResult(killResult)
        if killResult.Error() != nil {
            glog.Errorf("killPodWithSyncResult failed: %v", killResult.Error())
            return
        }

        if podContainerChanges.CreateSandbox {
            m.purgeInitContainers(pod, podStatus)
        }
    } else {
        // Step 3: kill any running containers in this pod which are not to keep.
        for containerID, containerInfo := range podContainerChanges.ContainersToKill {
            glog.V(3).Infof("Killing unwanted container %q(id=%q) for pod %q", containerInfo.name, containerID, format.Pod(pod))
            killContainerResult := kubecontainer.NewSyncResult(kubecontainer.KillContainer, containerInfo.name)
            result.AddSyncResult(killContainerResult)
            if err := m.killContainer(pod, containerID, containerInfo.name, containerInfo.message, nil); err != nil {
                killContainerResult.Fail(kubecontainer.ErrKillContainer, err.Error())
                glog.Errorf("killContainer %q(id=%q) for pod %q failed: %v", containerInfo.name, containerID, format.Pod(pod), err)
                return
            }
        }
    }

    // Keep terminated init containers fairly aggressively controlled
    // This is an optimization because container removals are typically handled
    // by container garbage collector.
    m.pruneInitContainersBeforeStart(pod, podStatus)

    // We pass the value of the podIP down to generatePodSandboxConfig and
    // generateContainerConfig, which in turn passes it to various other
    // functions, in order to facilitate functionality that requires this
    // value (hosts file and downward API) and avoid races determining
    // the pod IP in cases where a container requires restart but the
    // podIP isn't in the status manager yet.
    //
    // We default to the IP in the passed-in pod status, and overwrite it if the
    // sandbox needs to be (re)started.
    podIP := ""
    if podStatus != nil {
        podIP = podStatus.IP
    }

    // Step 4: Create a sandbox for the pod if necessary.
    podSandboxID := podContainerChanges.SandboxID
    if podContainerChanges.CreateSandbox {
        var msg string
        var err error

        glog.V(4).Infof("Creating sandbox for pod %q", format.Pod(pod))
        createSandboxResult := kubecontainer.NewSyncResult(kubecontainer.CreatePodSandbox, format.Pod(pod))
        result.AddSyncResult(createSandboxResult)
        podSandboxID, msg, err = m.createPodSandbox(pod, podContainerChanges.Attempt)
        if err != nil {
            createSandboxResult.Fail(kubecontainer.ErrCreatePodSandbox, msg)
            glog.Errorf("createPodSandbox for pod %q failed: %v", format.Pod(pod), err)
            ref, referr := ref.GetReference(legacyscheme.Scheme, pod)
            if referr != nil {
                glog.Errorf("Couldn't make a ref to pod %q: '%v'", format.Pod(pod), referr)
            }
            m.recorder.Eventf(ref, v1.EventTypeWarning, events.FailedCreatePodSandBox, "Failed create pod sandbox: %v", err)
            return
        }
        glog.V(4).Infof("Created PodSandbox %q for pod %q", podSandboxID, format.Pod(pod))

        podSandboxStatus, err := m.runtimeService.PodSandboxStatus(podSandboxID)
        if err != nil {
            ref, referr := ref.GetReference(legacyscheme.Scheme, pod)
            if referr != nil {
                glog.Errorf("Couldn't make a ref to pod %q: '%v'", format.Pod(pod), referr)
            }
            m.recorder.Eventf(ref, v1.EventTypeWarning, events.FailedStatusPodSandBox, "Unable to get pod sandbox status: %v", err)
            glog.Errorf("Failed to get pod sandbox status: %v; Skipping pod %q", err, format.Pod(pod))
            result.Fail(err)
            return
        }

        // If we ever allow updating a pod from non-host-network to
        // host-network, we may use a stale IP.
        if !kubecontainer.IsHostNetworkPod(pod) {
            // Overwrite the podIP passed in the pod status, since we just started the pod sandbox.
            podIP = m.determinePodSandboxIP(pod.Namespace, pod.Name, podSandboxStatus)
            glog.V(4).Infof("Determined the ip %q for pod %q after sandbox changed", podIP, format.Pod(pod))
        }
    }

    // Get podSandboxConfig for containers to start.
    configPodSandboxResult := kubecontainer.NewSyncResult(kubecontainer.ConfigPodSandbox, podSandboxID)
    result.AddSyncResult(configPodSandboxResult)
    podSandboxConfig, err := m.generatePodSandboxConfig(pod, podContainerChanges.Attempt)
    if err != nil {
        message := fmt.Sprintf("GeneratePodSandboxConfig for pod %q failed: %v", format.Pod(pod), err)
        glog.Error(message)
        configPodSandboxResult.Fail(kubecontainer.ErrConfigPodSandbox, message)
        return
    }

    // Step 5: start the init container.
    if container := podContainerChanges.NextInitContainerToStart; container != nil {
        // Start the next init container.
        startContainerResult := kubecontainer.NewSyncResult(kubecontainer.StartContainer, container.Name)
        result.AddSyncResult(startContainerResult)
        isInBackOff, msg, err := m.doBackOff(pod, container, podStatus, backOff)
        if isInBackOff {
            startContainerResult.Fail(err, msg)
            glog.V(4).Infof("Backing Off restarting init container %+v in pod %v", container, format.Pod(pod))
            return
        }

        glog.V(4).Infof("Creating init container %+v in pod %v", container, format.Pod(pod))
        if msg, err := m.startContainer(podSandboxID, podSandboxConfig, container, pod, podStatus, pullSecrets, podIP, kubecontainer.ContainerTypeInit); err != nil {
            startContainerResult.Fail(err, msg)
            utilruntime.HandleError(fmt.Errorf("init container start failed: %v: %s", err, msg))
            return
        }

        // Successfully started the container; clear the entry in the failure
        glog.V(4).Infof("Completed init container %q for pod %q", container.Name, format.Pod(pod))
    }

    // Step 6: start containers in podContainerChanges.ContainersToStart.
    for _, idx := range podContainerChanges.ContainersToStart {
        container := &pod.Spec.Containers[idx]
        startContainerResult := kubecontainer.NewSyncResult(kubecontainer.StartContainer, container.Name)
        result.AddSyncResult(startContainerResult)

        isInBackOff, msg, err := m.doBackOff(pod, container, podStatus, backOff)
        if isInBackOff {
            startContainerResult.Fail(err, msg)
            glog.V(4).Infof("Backing Off restarting container %+v in pod %v", container, format.Pod(pod))
            continue
        }

        glog.V(4).Infof("Creating container %+v in pod %v", container, format.Pod(pod))
        if msg, err := m.startContainer(podSandboxID, podSandboxConfig, container, pod, podStatus, pullSecrets, podIP, kubecontainer.ContainerTypeRegular); err != nil {
            startContainerResult.Fail(err, msg)
            // known errors that are logged in other places are logged at higher levels here to avoid
            // repetitive log spam
            switch {
            case err == images.ErrImagePullBackOff:
                glog.V(3).Infof("container start failed: %v: %s", err, msg)
            default:
                utilruntime.HandleError(fmt.Errorf("container start failed: %v: %s", err, msg))
            }
            continue
        }
    }

    return
}
```






























