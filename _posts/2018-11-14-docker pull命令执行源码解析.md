---
layout:     post
title:      "Docker pull命令执行源码解析"
subtitle:   "Docker pull命令执行源码解析"
date:       2018-11-14 12:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码
---

# Docker pull命令执行源码解析

由于需要分析镜像层之间的组织结构方式，我们需要对Docker pull拉取镜像层及拉取过程中镜像层之间组织结构方式的构建过程要有一定了解。

## Docker根目录的分析

我们知道Docker的根目录为/var/lib/docker,其中和一个容器镜像相关的数据如下图
所示：
![docker directory](/img/docker_root1.png)

![docker directory](/img/docker_root2.png)

其中我们需要关系的信息主要有：

* **layer-DigestID**:镜像层数据压缩之后通过sha256计算得到的摘要值
* **layer-ID**:及cacheID,随机生成的ID,在本地与layer-DiffID一一对应，使得相同的镜像层拉取到不同的主机上时有不同的ID
* **layer-DiffID**:镜像层数据未压缩时通过sha256计算得到的摘要值
* **layer-ChainID**:根据所有父镜像层的layer-DiffID，计算sha256摘要值得到的ID
* **image-ID**: 根据该镜像的json 文件计算sha256得到的摘要值，这里一个image-ID只对应一个镜像，而非单个镜像层，json文件中会纪录该镜像中每个镜像层的相关信息，包括执行的Dockerfile命令信息，及环境变量和rootFS:diffIDs保存了每个镜像层的layer-ChainID,并非layer-DiffID

## Docker pull命令执行



### 拉取配置信息的解析得到imagePullConfig

客户端发送pull命令的过程跳过，直接研究服务器端实现Docker pull的流程，中间的服务器端对命令的路由过程也跳过，找到执行docker pull镜像功能的函数：

postImagesCreate:

* 解析来自客户端请求数据form
* 调用s.backend.PullImage()拉取镜像

```go
// Creates an image from Pull or from Import
func (s *imageRouter) postImagesCreate(ctx context.Context, w http.ResponseWriter, r *http.Request, vars map[string]string) error {
    if err := httputils.ParseForm(r); err != nil {
        return err
    }

    var (
        image   = r.Form.Get("fromImage")
        repo    = r.Form.Get("repo")
        tag     = r.Form.Get("tag")
        message = r.Form.Get("message")
        err     error
        output  = ioutils.NewWriteFlusher(w)
    )
    defer output.Close()

    w.Header().Set("Content-Type", "application/json")

    if image != "" { //pull
        metaHeaders := map[string][]string{}
        for k, v := range r.Header {
            if strings.HasPrefix(k, "X-Meta-") {
                metaHeaders[k] = v
            }
        }

        authEncoded := r.Header.Get("X-Registry-Auth")
        authConfig := &types.AuthConfig{}
        if authEncoded != "" {
            authJSON := base64.NewDecoder(base64.URLEncoding, strings.NewReader(authEncoded))
            if err := json.NewDecoder(authJSON).Decode(authConfig); err != nil {
                // for a pull it is not an error if no auth was given
                // to increase compatibility with the existing api it is defaulting to be empty
                authConfig = &types.AuthConfig{}
            }
        }

        err = s.backend.PullImage(ctx, image, tag, metaHeaders, authConfig, output)
    } else { //import
        src := r.Form.Get("fromSrc")
        // 'err' MUST NOT be defined within this block, we need any error
        // generated from the download to be available to the output
        // stream processing below
        err = s.backend.ImportImage(src, repo, tag, message, r.Body, output, r.Form["changes"])
    }
    if err != nil {
        if !output.Flushed() {
            return err
        }
        sf := streamformatter.NewJSONStreamFormatter()
        output.Write(sf.FormatError(err))
    }

    return nil
}
```


**PullImage**

* 对tag进行解析
* 调用daemon.pullImageWithReference拉取镜像

```go
func (daemon *Daemon) PullImage(ctx context.Context, image, tag string, metaHeaders map[string][]string, authConfig *types.AuthConfig, outStream io.Writer) error {
    // Special case: "pull -a" may send an image name with a
    // trailing :. This is ugly, but let's not break API
    // compatibility.
    image = strings.TrimSuffix(image, ":")

    ref, err := reference.ParseNormalizedNamed(image)
    if err != nil {
        return err
    }

    if tag != "" {
        // The "tag" could actually be a digest.
        var dgst digest.Digest
        dgst, err = digest.Parse(tag)
        if err == nil {
            ref, err = reference.WithDigest(reference.TrimNamed(ref), dgst)
        } else {
            ref, err = reference.WithTag(ref, tag)
        }
        if err != nil {
            return err
        }
    }

    return daemon.pullImageWithReference(ctx, ref, metaHeaders, authConfig, outStream)
}
```

**pullImageWithReference**

* 填充拉取的配置信息和接口方法imagePullConfig
* 调用distribution.Pull(ctx, ref, imagePullConfig)进行拉取

```go
func (daemon *Daemon) pullImageWithReference(ctx context.Context, ref reference.Named, metaHeaders map[string][]string, authConfig *types.AuthConfig, outStream io.Writer) error {
    // Include a buffer so that slow client connections don't affect
    // transfer performance.
    progressChan := make(chan progress.Progress, 100)

    writesDone := make(chan struct{})

    ctx, cancelFunc := context.WithCancel(ctx)

    go func() {
        progressutils.WriteDistributionProgress(cancelFunc, outStream, progressChan)
        close(writesDone)
    }()

    imagePullConfig := &distribution.ImagePullConfig{
        Config: distribution.Config{
            MetaHeaders:      metaHeaders,
            AuthConfig:       authConfig,
            ProgressOutput:   progress.ChanOutput(progressChan),
            RegistryService:  daemon.RegistryService,
            ImageEventLogger: daemon.LogImageEvent,
            MetadataStore:    daemon.distributionMetadataStore,
            ImageStore:       distribution.NewImageConfigStoreFromStore(daemon.imageStore),
            ReferenceStore:   daemon.referenceStore,
        },
        DownloadManager: daemon.downloadManager,
        Schema2Types:    distribution.ImageTypes,
    }

    err := distribution.Pull(ctx, ref, imagePullConfig)
    close(progressChan)
    <-writesDone
    return err
}
```

### Pull函数

这里的Pull函数位于distribution/pull.go中

* 解析repository的依赖信息，查询得到所有的镜像拉取接口endpoint
* 根据每个可能的endpoint尝试拉取镜像
    - 根据endpoint创建一个新的puller拉取器
    - puller.Pull()拉取成功则返回，拉取失败则换一个endpoint拉取

```go
func Pull(ctx context.Context, ref reference.Named, imagePullConfig *ImagePullConfig) error {
       // Resolve the Repository name from fqn to RepositoryInfo
       repoInfo, err := imagePullConfig.RegistryService.ResolveRepository(ref)
       
       // makes sure name is not `scratch`
       if err := ValidateRepoName(repoInfo.Name); err != nil {

       endpoints, err := imagePullConfig.RegistryService.LookupPullEndpoints(reference.Domain(repoInfo.Name))
       
       for _, endpoint := range endpoints {
              puller, err := newPuller(endpoint, repoInfo, imagePullConfig)
              
              if err := puller.Pull(ctx, ref); err != nil {
                ...//拉取失败，则换一个Endpoint尝试拉取
              }

              imagePullConfig.ImageEventLogger(reference.FamiliarString(ref), reference.FamiliarName(repoInfo.Name), "pull")
              return nil//拉取成功则直接返回
       }
       
       return TranslatePullError(lastErr, ref)
}
```

这里的puller.Pull函数使用的是v2版本的拉取方式，位于distribution/pull_v2.go

* NewV2Repository用于创建一个提供身份验证的方式 http通道，并返回一个repository接口，验证该endpoint可以ping通
* p.pullV2Repository()进行镜像拉取

```go
func (p *v2Puller) Pull(ctx context.Context, ref reference.Named) (err error) {
    // TODO(tiborvass): was ReceiveTimeout
    p.repo, p.confirmedV2, err = NewV2Repository(ctx, p.repoInfo, p.endpoint, p.config.MetaHeaders, p.config.AuthConfig, "pull")
    if err != nil {
        logrus.Warnf("Error getting v2 registry: %v", err)
        return err
    }

    if err = p.pullV2Repository(ctx, ref); err != nil {
        if _, ok := err.(fallbackError); ok {
            return err
        }
        if continueOnError(err) {
            logrus.Errorf("Error trying v2 registry: %v", err)
            return fallbackError{
                err:         err,
                confirmedV2: p.confirmedV2,
                transportOK: true,
            }
        }
    }
    return err
}
```
pullV2Repository（）主要是调用pullV2Tag()进行拉取，pullV2Tag才是真正执行镜像下载的函数：

* 首先拉取一个manifest清单文件,根据Digest获取manifest接口
* 调用pullSchema2进行拉取

```go
func (p *v2Puller) pullV2Tag(ctx context.Context, ref reference.Named) (tagUpdated bool, err error) {
    manSvc, err := p.repo.Manifests(ctx)
    if err != nil {
        return false, err
    }

    var (
        manifest    distribution.Manifest
        tagOrDigest string // Used for logging/progress only
    )
    if tagged, isTagged := ref.(reference.NamedTagged); isTagged {
        manifest, err = manSvc.Get(ctx, "", distribution.WithTag(tagged.Tag()))//拉取清单文件manifest
        if err != nil {
            return false, allowV1Fallback(err)
        }
        tagOrDigest = tagged.Tag()
    } else if digested, isDigested := ref.(reference.Canonical); isDigested {
        manifest, err = manSvc.Get(ctx, digested.Digest())
        if err != nil {
            return false, err
        }
        tagOrDigest = digested.Digest().String()
    } else {
        return false, fmt.Errorf("internal error: reference has neither a tag nor a digest: %s", reference.FamiliarString(ref))
    }

    if manifest == nil {
        return false, fmt.Errorf("image manifest does not exist for tag or digest %q", tagOrDigest)
    }

    if m, ok := manifest.(*schema2.DeserializedManifest); ok {
        var allowedMediatype bool
        for _, t := range p.config.Schema2Types {
            if m.Manifest.Config.MediaType == t {
                allowedMediatype = true
                break
            }
        }
        if !allowedMediatype {
            configClass := mediaTypeClasses[m.Manifest.Config.MediaType]
            if configClass == "" {
                configClass = "unknown"
            }
            return false, fmt.Errorf("target is %s", configClass)
        }
    }

    // If manSvc.Get succeeded, we can be confident that the registry on
    // the other side speaks the v2 protocol.
    p.confirmedV2 = true

    logrus.Debugf("Pulling ref from V2 registry: %s", reference.FamiliarString(ref))
    progress.Message(p.config.ProgressOutput, tagOrDigest, "Pulling from "+reference.FamiliarName(p.repo.Named()))

    var (
        id             digest.Digest
        manifestDigest digest.Digest
    )

    switch v := manifest.(type) {
    case *schema1.SignedManifest:
        if p.config.RequireSchema2 {
            return false, fmt.Errorf("invalid manifest: not schema2")
        }
        id, manifestDigest, err = p.pullSchema1(ctx, ref, v)
        if err != nil {
            return false, err
        }
    case *schema2.DeserializedManifest:
        id, manifestDigest, err = p.pullSchema2(ctx, ref, v)
        if err != nil {
            return false, err
        }
    case *manifestlist.DeserializedManifestList:
        id, manifestDigest, err = p.pullManifestList(ctx, ref, v)
        if err != nil {
            return false, err
        }
    default:
        return false, errors.New("unsupported manifest format")
    }

    progress.Message(p.config.ProgressOutput, "", "Digest: "+manifestDigest.String())

    if p.config.ReferenceStore != nil {
        oldTagID, err := p.config.ReferenceStore.Get(ref)
        if err == nil {
            if oldTagID == id {
                return false, addDigestReference(p.config.ReferenceStore, ref, manifestDigest, id)
            }
        } else if err != refstore.ErrDoesNotExist {
            return false, err
        }

        if canonical, ok := ref.(reference.Canonical); ok {
            if err = p.config.ReferenceStore.AddDigest(canonical, id, true); err != nil {
                return false, err
            }
        } else {
            if err = addDigestReference(p.config.ReferenceStore, ref, manifestDigest, id); err != nil {
                return false, err
            }
            if err = p.config.ReferenceStore.AddTag(ref, id, true); err != nil {
                return false, err
            }
        }
    }
    return true, nil
}
```

### pullSchema2函数:

* 首先获取Digest:manifestDigest
* 在ImageStore中查询该Digest是否已经存在，存在则直接返回无需重复下载，ImageStore中主要读取image.json中文件的内容
* 如果ImageStore中没有，则首先下载该镜像的image.json：调用pullSchema2Config（）函数下载得到**configJSON**
* 根据**mfst**中的每个镜像层中的信息，添加到镜像层描述对象**数组descriptors**
* 调用DownloadManager.Download下载descriptors中的所有镜像层
* Download下载返回rootFS信息，匹配rootFS和原先下载的configJSON中的diffIDs（layer-DiffID）进行**匹配**，匹配失败报错，否则镜像层下载成功


```go
func (p *v2Puller) pullSchema2(ctx context.Context, ref reference.Named, mfst *schema2.DeserializedManifest) (id digest.Digest, manifestDigest digest.Digest, err error) {
    manifestDigest, err = schema2ManifestDigest(ref, mfst)
    if err != nil {
        return "", "", err
    }

    target := mfst.Target()//获取该镜像的Digest,并查询本地的imageStore是否已经存在
    if _, err := p.config.ImageStore.Get(target.Digest); err == nil {
        // If the image already exists locally, no need to pull
        // anything.
        return target.Digest, manifestDigest, nil
    }

    var descriptors []xfer.DownloadDescriptor

    // Note that the order of this loop is in the direction of bottom-most
    // to top-most, so that the downloads slice gets ordered correctly.
    for _, d := range mfst.Layers {//将manifest中的镜像层信息添加到镜像层描述对象数组descriptors中
        layerDescriptor := &v2LayerDescriptor{
            digest:            d.Digest,
            repo:              p.repo,
            repoInfo:          p.repoInfo,
            V2MetadataService: p.V2MetadataService,
            src:               d,
        }

        descriptors = append(descriptors, layerDescriptor)
    }

    configChan := make(chan []byte, 1)
    configErrChan := make(chan error, 1)
    layerErrChan := make(chan error, 1)
    downloadsDone := make(chan struct{})
    var cancel func()
    ctx, cancel = context.WithCancel(ctx)
    defer cancel()

    // Pull the image config
    go func() {
        configJSON, err := p.pullSchema2Config(ctx, target.Digest)//首先拉取整个镜像的image.json数据
        if err != nil {
            configErrChan <- ImageConfigPullError{Err: err}
            cancel()
            return
        }
        configChan <- configJSON
    }()

    var (
        configJSON       []byte        // raw serialized image config
        downloadedRootFS *image.RootFS // rootFS from registered layers
        configRootFS     *image.RootFS // rootFS from configuration
        release          func()        // release resources from rootFS download
    )

    // https://github.com/docker/docker/issues/24766 - Err on the side of caution,
    // explicitly blocking images intended for linux from the Windows daemon. On
    // Windows, we do this before the attempt to download, effectively serialising
    // the download slightly slowing it down. We have to do it this way, as
    // chances are the download of layers itself would fail due to file names
    // which aren't suitable for NTFS. At some point in the future, if a similar
    // check to block Windows images being pulled on Linux is implemented, it
    // may be necessary to perform the same type of serialisation.
    if runtime.GOOS == "windows" {
        configJSON, configRootFS, err = receiveConfig(p.config.ImageStore, configChan, configErrChan)
        if err != nil {
            return "", "", err
        }

        if configRootFS == nil {
            return "", "", errRootFSInvalid
        }
    }

    if p.config.DownloadManager != nil {
        //开始进行异步数据下载
        go func() {
            var (
                err    error
                rootFS image.RootFS
            )
            downloadRootFS := *image.NewRootFS()
            rootFS, release, err = p.config.DownloadManager.Download(ctx, downloadRootFS, descriptors, p.config.ProgressOutput)//根据descriptors下载镜像层数据
            if err != nil {
                // Intentionally do not cancel the config download here
                // as the error from config download (if there is one)
                // is more interesting than the layer download error
                layerErrChan <- err
                return
            }

            downloadedRootFS = &rootFS
            close(downloadsDone)
        }()
    } else {
        // We have nothing to download
        close(downloadsDone)
    }

    if configJSON == nil {
        configJSON, configRootFS, err = receiveConfig(p.config.ImageStore, configChan, configErrChan)
        if err == nil && configRootFS == nil {
            err = errRootFSInvalid
        }
        if err != nil {
            cancel()
            select {
            case <-downloadsDone:
            case <-layerErrChan:
            }
            return "", "", err
        }
    }
    //等待下载结束
    select {
    case <-downloadsDone:
    case err = <-layerErrChan:
        return "", "", err
    }

    if release != nil {
        defer release()
    }
    //匹配下载的到的RootFS和之前下载的configJSON中的diffIDs是否匹配
    if downloadedRootFS != nil {
        // The DiffIDs returned in rootFS MUST match those in the config.
        // Otherwise the image config could be referencing layers that aren't
        // included in the manifest.
        if len(downloadedRootFS.DiffIDs) != len(configRootFS.DiffIDs) {
            return "", "", errRootFSMismatch
        }

        for i := range downloadedRootFS.DiffIDs {
            if downloadedRootFS.DiffIDs[i] != configRootFS.DiffIDs[i] {
                return "", "", errRootFSMismatch
            }
        }
    }

    imageID, err := p.config.ImageStore.Put(configJSON)
    if err != nil {
        return "", "", err
    }

    return imageID, manifestDigest, nil
}
```


### 镜像层下载Download（包含镜像层注册即相关目录创建）

 p.config.DownloadManager.Download 函数执行下载镜像层的步骤，函数位于distribution/xfer/download.go

 * 首先和本地的镜像根据ChainID进行匹配,如果匹配失败，则之后的镜像层不能继续复用本地缓存，之后也无需进行匹配的过程，匹配成功则可以服用本地缓存镜像层，下一轮可以继续匹配
 * 匹配失败，则开始准备进行下载，首先根据镜像层描述对象的key判断是否存在正在执行的同layer的download任务，防止一个镜像层多次重复下载
     - 有正在执行的同layer下载任务，则调用makeDownloadFuncFromDownload，等待镜像层和其父镜像的下载完成后完成该镜像层的注册过程。
     - 没有正在执行的同layer下载任务，则调用makeDownloadFunc执行镜像层的下载，并等待该镜像层及其父镜像层下载完成再完成注册的过程
 * makeDownloadFuncFromDownload和makeDownloadFunc都是调用layer/layer_store.go的Register完成镜像层的注册


```go
func (ldm *LayerDownloadManager) Download(ctx context.Context, initialRootFS image.RootFS, layers []DownloadDescriptor, progressOutput progress.Output) (image.RootFS, func(), error) {
    var (
        topLayer       layer.Layer
        topDownload    *downloadTransfer
        watcher        *Watcher
        missingLayer   bool
        transferKey    = ""
        downloadsByKey = make(map[string]*downloadTransfer)
    )

    rootFS := initialRootFS
    for _, descriptor := range layers {
        key := descriptor.Key()
        transferKey += key

        if !missingLayer {//只要有一次和本地的镜像层匹配失败，则之后的镜像层都不能继续匹配
            missingLayer = true
            diffID, err := descriptor.DiffID()
            if err == nil {
                getRootFS := rootFS
                getRootFS.Append(diffID)//获取该层的layer-DiffID
                l, err := ldm.layerStore.Get(getRootFS.ChainID())//根据该层的ChainID搜索本地是否已经有这一层镜像层，有直接复用本地缓存
                if err == nil {
                    //说明该层镜像层已经存在，直接复用
                    // Layer already exists.
                    logrus.Debugf("Layer already exists: %s", descriptor.ID())
                    progress.Update(progressOutput, descriptor.ID(), "Already exists")
                    if topLayer != nil {
                        layer.ReleaseAndLog(ldm.layerStore, topLayer)
                    }
                    topLayer = l
                    missingLayer = false
                    rootFS.Append(diffID)
                    // Register this repository as a source of this layer.
                    withRegistered, hasRegistered := descriptor.(DownloadDescriptorWithRegistered)
                    if hasRegistered {
                        withRegistered.Registered(diffID)//注册该镜像层的layer-DiffID
                    }
                    continue
                }
            }
        }

        // Does this layer have the same data as a previous layer in
        // the stack? If so, avoid downloading it more than once.
        var topDownloadUncasted Transfer
        //先看下有没有存在正在执行的同layer的download任务
        if existingDownload, ok := downloadsByKey[key]; ok {
            //存在正在执行的同layer的download任务
            xferFunc := ldm.makeDownloadFuncFromDownload(descriptor, existingDownload, topDownload)//创建下载函数xferFunc，这个匿名函数的工作是，等待自己这一层和祖先层的下载完毕，然后注册本层；执行是通过Transfer()来执行的，Transfer先查看是否有同样的协程在做同样的工作，如果没有才调用上面那个匿名函数
            defer topDownload.Transfer.Release(watcher)
            topDownloadUncasted, watcher = ldm.tm.Transfer(transferKey, xferFunc, progressOutput)//先查看是否有同样的协程在做同样的工作，如果没有才调用匿名函数xferFunc
            topDownload = topDownloadUncasted.(*downloadTransfer)
            continue
        }

        // Layer is not known to exist - download and register it.
        progress.Update(progressOutput, descriptor.ID(), "Pulling fs layer")

        var xferFunc DoFunc
        //没有正在执行的同layer的download任务，则执行 makeDownloadFunc()返回的匿名函数，这个匿名函数的工作是，执行下载和注册工作，如果祖先层也在下载，需要等待祖先层下载完毕才能注册
        if topDownload != nil {
            xferFunc = ldm.makeDownloadFunc(descriptor, "", topDownload)创建下载函数xferFunc
            defer topDownload.Transfer.Release(watcher)
        } else {
            xferFunc = ldm.makeDownloadFunc(descriptor, rootFS.ChainID(), nil)
        }
        topDownloadUncasted, watcher = ldm.tm.Transfer(transferKey, xferFunc, progressOutput)//先查看是否有同样的协程在做同样的工作，如果没有才调用匿名函数xferFunc
        topDownload = topDownloadUncasted.(*downloadTransfer)
        downloadsByKey[key] = topDownload
    }

    if topDownload == nil {
        return rootFS, func() {
            if topLayer != nil {
                layer.ReleaseAndLog(ldm.layerStore, topLayer)
            }
        }, nil
    }

    // Won't be using the list built up so far - will generate it
    // from downloaded layers instead.
    rootFS.DiffIDs = []layer.DiffID{}

    defer func() {
        if topLayer != nil {
            layer.ReleaseAndLog(ldm.layerStore, topLayer)
        }
    }()

    select {
    case <-ctx.Done():
        topDownload.Transfer.Release(watcher)
        return rootFS, func() {}, ctx.Err()
    case <-topDownload.Done():
        break
    }

    l, err := topDownload.result()
    if err != nil {
        topDownload.Transfer.Release(watcher)
        return rootFS, func() {}, err
    }

    // Must do this exactly len(layers) times, so we don't include the
    // base layer on Windows.
    for range layers {
        if l == nil {
            topDownload.Transfer.Release(watcher)
            return rootFS, func() {}, errors.New("internal error: too few parent layers")
        }
        rootFS.DiffIDs = append([]layer.DiffID{l.DiffID()}, rootFS.DiffIDs...)
        l = l.Parent()
    }
    return rootFS, func() { topDownload.Transfer.Release(watcher) }, err
}
```


### 镜像层的注册registerWithDescriptor：

* 生成该镜像层的cacheID,chainID,在layerStore中完成注册过程
* registerWithDescriptor中的ts为下载数据的reader,根据这个reader我们可以把**下载的实际的数据存放到指定的cacheID目录**下，即diff目录下（对于aufs来说）
* 函数中ls.driver.Create**创建不同文件系统驱动的不同目录**
* ls.applyTar将下载的压缩镜像层数据**解压到创建的cacheID即diff目录下。**
* storeLayer将size,diffID,cacheID**写入到对应目录的对应文件**中去
* 最后注册layerStore.layerMap[chainID] = layer对象

```go
func (ls *layerStore) registerWithDescriptor(ts io.Reader, parent ChainID, descriptor distribution.Descriptor) (Layer, error) {
    // err is used to hold the error which will always trigger
    // cleanup of creates sources but may not be an error returned
    // to the caller (already exists).
    var err error
    var pid string
    var p *roLayer
    if string(parent) != "" {
        p = ls.get(parent)
        if p == nil {
            return nil, ErrLayerDoesNotExist
        }
        pid = p.cacheID
        // Release parent chain if error
        defer func() {
            if err != nil {
                ls.layerL.Lock()
                ls.releaseLayer(p)
                ls.layerL.Unlock()
            }
        }()
        if p.depth() >= maxLayerDepth {
            err = ErrMaxDepthExceeded
            return nil, err
        }
    }

    // Create new roLayer
    layer := &roLayer{
        parent:         p,
        cacheID:        stringid.GenerateRandomID(),
        referenceCount: 1,
        layerStore:     ls,
        references:     map[Layer]struct{}{},
        descriptor:     descriptor,
    }

    if err = ls.driver.Create(layer.cacheID, pid, nil); err != nil {
        return nil, err
    }//根据文件系统驱动创建相关目录

    tx, err := ls.store.StartTransaction()
    if err != nil {
        return nil, err
    }

    defer func() {
        if err != nil {
            logrus.Debugf("Cleaning up layer %s: %v", layer.cacheID, err)
            if err := ls.driver.Remove(layer.cacheID); err != nil {
                logrus.Errorf("Error cleaning up cache layer %s: %v", layer.cacheID, err)
            }
            if err := tx.Cancel(); err != nil {
                logrus.Errorf("Error canceling metadata transaction %q: %s", tx.String(), err)
            }
        }
    }()

    if err = ls.applyTar(tx, ts, pid, layer); err != nil {//将下载的镜像层tar包数据存放到对应目录下去
        return nil, err
    }

    if layer.parent == nil {
        layer.chainID = ChainID(layer.diffID)
    } else {
        layer.chainID = createChainIDFromParent(layer.parent.chainID, layer.diffID)
    }

    if err = storeLayer(tx, layer); err != nil {//存放镜像层size,diffID,cacheID等信息到对应文件
        return nil, err
    }

    ls.layerL.Lock()
    defer ls.layerL.Unlock()

    if existingLayer := ls.getWithoutLock(layer.chainID); existingLayer != nil {
        // Set error for cleanup, but do not return the error
        err = errors.New("layer already exists")
        return existingLayer.getReference(), nil
    }

    if err = tx.Commit(layer.chainID); err != nil {
        return nil, err
    }

    ls.layerMap[layer.chainID] = layer//注册layerMap

    return layer.getReference(), nil
}
```





