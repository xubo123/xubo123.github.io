---
layout:     post
title:      "docker容器镜像层数据初始化过程"
subtitle:   "docker容器镜像层初始化"
date:       2017-09-26 11:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码
---
## LayerStore初始化加载过程


![LayerInit](/img/LayerInit.png)

{% highlight go %}

d.layerStore, err = layer.NewStoreFromOptions(layer.StoreOptions{
		StorePath:                 config.Root,
		MetadataStorePathTemplate: filepath.Join(config.Root, "image", "%s", "layerdb"),
		GraphDriver:               driverName,
		GraphDriverOptions:        config.GraphOptions,
		UIDMaps:                   uidMaps,
		GIDMaps:                   gidMaps,
		PluginGetter:              d.PluginStore,
		ExperimentalEnabled:       config.Experimental,
	})
{% endhighlight%}

### 创建一个镜像层存储器对象
{% highlight go %}
func NewStoreFromOptions(options StoreOptions) (Store, error) {
	driver, err := graphdriver.New(options.GraphDriver, options.PluginGetter, graphdriver.Options{
		Root:                options.StorePath,
		DriverOptions:       options.GraphDriverOptions,
		UIDMaps:             options.UIDMaps,
		GIDMaps:             options.GIDMaps,
		ExperimentalEnabled: options.ExperimentalEnabled,
	})
	if err != nil {
		return nil, fmt.Errorf("error initializing graphdriver: %v", err)
	} //获取文件系统驱动，首先根据优先级priority来得到文件系统驱动，然后调用已经注册好的文件系统驱动初始化函数initFunc来初始化驱动。这里的driver和initFunc映射关系的构建是通过import->init()中的内容来建立的
	logrus.Debugf("Using graph driver %s", driver)

	fms, err := NewFSMetadataStore(fmt.Sprintf(options.MetadataStorePathTemplate, driver))//构建一个简单的fileMetadataStore对象结构fms，包含root目录。该目录应该为/var/lib/docker/image/aufs/layerdb/
	if err != nil {
		return nil, err
	}

	return NewStoreFromGraphDriver(fms, driver)
}
{% endhighlight%}

### 根据文件系统驱动获取镜像层存储器对象
{% highlight go%}

func NewStoreFromGraphDriver(store MetadataStore, driver graphdriver.Driver) (Store, error) {
	ls := &layerStore{
		store:    store,
		driver:   driver,
		layerMap: map[ChainID]*roLayer{},
		mounts:   map[string]*mountedLayer{},
	}//首先定义一个镜像层存储器对象

	ids, mounts, err := store.List()//根据fileMetadataStore中的root目录信息找到所有/var/lib/docker/image/aufs/layerdb下的mounts（mounts）目录及sha256目录（ids）下的数据
	if err != nil {
		return nil, err
	}

	for _, id := range ids {
		l, err := ls.loadLayer(id)//根据sha256下的id信息加载镜像层信息（包括diff_id（所有镜像层diffid是根据镜像内容使用sha256算法得到）,size,cache_id,parent,descriptor）然后存放到ls.layerMap[ChainID]中去。所以sha256中主要是存放着镜像id与镜像层diffid的映射关系，及diff之间的父子关系
		if err != nil {
			logrus.Debugf("Failed to load layer %s: %s", id, err)
			continue
		}
		if l.parent != nil {
			l.parent.referenceCount++
		}
	}

	for _, mount := range mounts {// mounts目录下主要记录各容器可读写层的相关信息，目录名一般为容器id，目录下有三份文件，分别是init-id,mounts-id,parent,分别对应可读写层初始化层id，可读写层id以及父镜像层diff－id。
		if err := ls.loadMount(mount); err != nil {//加载可读写层信息即init－id，mount－id，parent数据到ls.mounts[container_id]对象中去
			logrus.Debugf("Failed to load mount %s: %s", mount, err)
		}
	}

	return ls, nil
}

{% endhighlight%}