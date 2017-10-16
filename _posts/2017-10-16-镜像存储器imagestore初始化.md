---
layout:     post
title:      "docker容器镜像存储器初始化过程"
subtitle:   "镜像存储器ImageStore初始化"
date:       2017-10-16 11:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码
---
## 镜像存储器imageStore初始化

镜像存储器结构体分析：
{% highlight go%}

type store struct {
	sync.Mutex
	ls        LayerGetReleaser
	images    map[ID]*imageMeta//存放镜像ID对应的layer及子镜像
	fs        StoreBackend//存储镜像存放的根目录/imagedb
	digestSet *digestset.Set//存放所有的镜像ID
}

接口实现：

type Store interface {
	Create(config []byte) (ID, error)
	Get(id ID) (*Image, error)
	Delete(id ID) ([]layer.Metadata, error)
	Search(partialID string) (ID, error)
	SetParent(id ID, parent ID) error
	GetParent(id ID) (ID, error)
	Children(id ID) []ID
	Map() map[ID]*Image
	Heads() map[ID]*Image
}

{% endhighlight %}

![](/img/ImageInit.png)

在daemon初始化过程中启动imageStore初始化：
{%highlight go %}

d.imageStore, err = image.NewImageStore(ifs, d.layerStore)//其中layerStore初始化在上一章节已做解释


// NewImageStore returns new store object for given layer store
func NewImageStore(fs StoreBackend, ls LayerGetReleaser) (Store, error) {
	is := &store{
		ls:        ls,
		images:    make(map[ID]*imageMeta),
		fs:        fs,
		digestSet: digestset.NewSet(),
	}//构建镜像存储器初始化对象

	// load all current images and retain layers
	//根据imagedb目录下的内容进行存储器的填充
	if err := is.restore(); err != nil {
		return nil, err
	}

	return is, nil
}

{% endhighlight %}

### 镜像存储器初始化恢复：读取imagedb/content/imageID文件数据中的diffIDs经过sha256加密获取对应layerdb/sha256/id中的id获取layer相关信息，其中ls.layerMaps即存储了该chainID和layer之间的映射关系，最后将得到的layer放入imageMeta对象中，并构建ImageID之间的父子关系得到store.images，存放到imageStore


{%highlight go %}
func (is *store) restore() error {
	err := is.fs.Walk(func(dgst digest.Digest) error {
	//Walk函数将会遍历docker/image/aufs/imagedb/content/sha256下的目录，并对每个目录名（id）进行sha256加密得到dgst对象，即加前缀sha256:id
		img, err := is.Get(IDFromDigest(dgst))//根据imagedb下content及metadata目录下的对应id的文件内容来构建Image结构体
		if err != nil {
			logrus.Errorf("invalid image %v, %v", dgst, err)
			return nil
		}
		var l layer.Layer
		if chainID := img.RootFS.ChainID();
		 chainID != "" {//这里相当于把content/id文件中的diff_ids通过某种算法连接起来成为chainID链,对应layerdb/sha256/id,来获取这一层只读镜像层数据
			l, err = is.ls.Get(chainID)
			if err != nil {
				return err
			}
		}
		if err := is.digestSet.Add(dgst); err != nil {//将该镜像id添加到digestSet
			return err
		}

		imageMeta := &imageMeta{
			layer:    l,
			children: make(map[ID]struct{}),
		}//构建镜像元数据信息

		is.images[IDFromDigest(dgst)] = imageMeta
       //构建镜像元数据和镜像id的映射
		return nil
	})
	if err != nil {
		return err
	}

	// Second pass to fill in children maps
	for id := range is.images {
		if parent, err := is.GetParent(id); err == nil {//构建ImageID中的父子关系
			if parentMeta := is.images[parent]; parentMeta != nil {
				parentMeta.children[id] = struct{}{}
			}
		}
	}

	return nil
}

{% endhighlight %}
### 根据digest加密的id获取镜像结构体：
```
type Image struct {
	V1Image
	Parent     ID        `json:"parent,omitempty"`
	RootFS     *RootFS   `json:"rootfs,omitempty"`
	History    []History `json:"history,omitempty"`
	OSVersion  string    `json:"os.version,omitempty"`
	OSFeatures []string  `json:"os.features,omitempty"`

	// rawJSON caches the immutable JSON associated with this image.
	rawJSON []byte

	// computedID is the ID computed from the hash of the image config.
	// Not to be confused with the legacy V1 ID in V1Image.
	computedID ID
}
```

根据imagedb下content及metadata目录下的对应id的文件内容来构建Image结构体
{% highlight go %}
func (is *store) Get(id ID) (*Image, error) {
	// todo: Check if image is in images
	// todo: Detect manual insertions and start using them
	config, err := is.fs.Get(id.Digest())//根据前缀id读取/imagedb/content/sha256/id文件中的内容到config
	if err != nil {
		return nil, err
	}

	img, err := NewFromJSON(config)//根据读取到的json数据构建镜像结构体img
	if err != nil {
		return nil, err
	}
	img.computedID = id

	img.Parent, err = is.GetParent(id)//根据id读取/imagedb/metadata/sha256/id下的文件内容
	if err != nil {
		img.Parent = ""
	}

	return img, nil
}
{% endhighlight %}