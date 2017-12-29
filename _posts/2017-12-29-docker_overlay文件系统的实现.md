---
layout:     post
title:      "docker OVERLAY文件系统的原理－－安装"
subtitle:   "第一章"
date:       2017-12-29 11:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码
---
## docker OVERLAY文件系统的实现

在了解虚拟文件系统vfs原理后，大致能够梳理出vfs的原理及vfs与各文件系统的关系，vfs是一种逻辑上的框架，每个文件系统在实现过程中为了支持vfs进行代码对该框架标准的适配，然后可以成功的和其它的文件系统进行转换。所以在与vfs交互这一层概念中来讲，overlay和ext4地位相同，但具体自身的实现不同，因为overlay是一种联合挂载文件系统，需要基于其它的文件系统进行安装。

正是因为vfs这一层转换，使得可以对各个文件系统进行统一和协调的的管理，使得文件系统之间可以共存并成功对文件进行读写的过程，对文件的打开，读写都是经过vfs层文件对象中**索引节点的载入**，然后初始化**文件对象**中的文件操作函数指针，进行各文件系统具体的读写层调用。

overlay和ext4之间的区别就在于，ext4这类文件系统的读写就是真正的读写操作，overlay则是寻找它所联合挂载的文件系统（比如ext4）的文件对象，然后再调用该文件对象的读写函数。**所以在调用具体的文件操作函数之前与vfs的交互overlay和ext4的地位相当，只不过具体的文件操作函数有自身的特点和区别。**

所以在有了虚拟文件系统概念的基础后，底层overlayfs的实现方式就可以进行深入分析，我们从docker这部分的overlay安装开始展开研究。

### docker OVERLAY的安装

在[docker启动容器过程分析](https://github.com/xubo123/xbblfz/edit/master/_posts/2017-07-26-docker_start容器恢复过程分析)这一章节中可以看到docker启动容器并安装文件系统的过程。我们分析到rl.layerStore.driver.Get()根据文件系统的驱动进行具体的安装过程，接下来我们将从这个入口开始深入理解overlay文件系统的安装及读写实现。

{% highlight go %}

// Get creates and mounts the required file system for the given id and returns the mount path.
func (d *Driver) Get(id string, mountLabel string) (s string, err error) {
	d.locker.Lock(id)
	defer d.locker.Unlock(id)
	dir := d.dir(id)//获取容器overlay文件系统下的上层根目录，／overlay/upper_id
	if _, err := os.Stat(dir); err != nil {
		return "", err
	}
	// If id has a root, just return it
	rootDir := path.Join(dir, "root")//如果有root子目录，说明是底层目录，没有upper层直接返回
	if _, err := os.Stat(rootDir); err == nil {
		return rootDir, nil
	}
	mergedDir := path.Join(dir, "merged")//获取upper层的merged子目录路径
	if count := d.ctr.Increment(mergedDir); count > 1 {//增加引用计数
		return mergedDir, nil
	}
	defer func() {
		if err != nil {
			if c := d.ctr.Decrement(mergedDir); c <= 0 {
				syscall.Unmount(mergedDir, 0)
			}
		}
	}()
	lowerID, err := ioutil.ReadFile(path.Join(dir, "lower-id"))//读取底层文件lower_id
	if err != nil {
		return "", err
	}
	var (
		lowerDir = path.Join(d.dir(string(lowerID)), "root")//获取底层文件的根目录路径
		upperDir = path.Join(dir, "upper")//获取上层文件的根目录路径
		workDir  = path.Join(dir, "work")//获取工作目录路径
		opts     = fmt.Sprintf("lowerdir=%s,upperdir=%s,workdir=%s", lowerDir, upperDir, workDir)
	)
	if err := syscall.Mount("overlay", mergedDir, "overlay", 0, label.FormatMountLabel(opts, mountLabel)); err != nil {
		return "", fmt.Errorf("error creating overlay mount to %s: %v", mergedDir, err)
	}//根据merged目录，upper目录路径，lower目录路径及工作目录路径安装容器overlay文件系统
	// chown "workdir/work" to the remapped root UID/GID. Overlay fs inside a
	// user namespace requires this to move a directory from lower to upper.
	rootUID, rootGID, err := idtools.GetRootUIDGID(d.uidMaps, d.gidMaps)
	if err != nil {
		return "", err
	}
	if err := os.Chown(path.Join(workDir, "work"), rootUID, rootGID); err != nil {
		return "", err
	}
	return mergedDir, nil//返回安装后的overlay文件系统的联合挂载目录merged
}
{% endhighlight %}

### Linux 安装overlay文件系统

#### 注册

首先，overlay文件系统是 作为模块动态的加载到内核，内核模块的特点就是不会被编译到内核中，可以尽量控制内核的大小，在内核运行期间，可以进行动态的加载和卸载。

模块加载和卸载相关函数：

    a)        module_init宏表示在加载模块的时候，自动调用这个宏所指示的函数。

    b)        module_exit宏表示在卸载模块的时候，自动调用这个宏所指示的函数。

代码中:

* module_init(funa);//加载该模块时调用函数funa
* module_exit(funb);//卸载该模块时调用函数funb

模块的相关命令：

* 加载模块：insmod modprobe（insmod一次只能加载一个模块，modprobe一次可以将指定模块的所依赖的所有模块全部加载）
* 卸载模块：rmmod
* 查看模块：lsmod

Overlay在作为模块被加载时，涉及到两个关键函数：

* module_init(ovl_init);
* module_exit(ovl_exit);

**ovl_init:**

{% highlight c %}
static int __init ovl_init(void)
{
	return register_filesystem(&ovl_fs_type);
}
{% endhighlight %}

**register_filesystem:将文件系统结构file_system_type添加到内核的文件系统链表file_systems中，以供挂载操作和其它系统调用**

{% highlight c%}
int register_filesystem(struct file_system_type * fs)
{
	int res = 0;
	struct file_system_type ** p;

	BUG_ON(strchr(fs->name, '.'));
	if (fs->next)
		return -EBUSY;//直接检查file_system_type的next是否有元素可以避免遍历全局文件系统链表的耗时
	write_lock(&file_systems_lock);
	p = find_filesystem(fs->name, strlen(fs->name));//查找内核文件系统的链表是否有该文件系统类型，若有则返回该file_system_type指针，否则返回链表尾部指针指向空。
	if (*p)
		res = -EBUSY;//已存在
	else
		*p = fs;//文件系统不存在该文件系统类型，添加到链表尾部
	write_unlock(&file_systems_lock);
	return res;
}
{% endhighlight%}

#### 挂载

首先，我们看一下将overlay文件系统挂载到merged目录的命令调用：
{%highlight%}
sudo mount -t overlay overlay -o lowerdir=lower,upperdir=upper,workdir=work merged
{% endhighlight %}

overlay底层联合的文件系统为ext4文件系统，所以lower层及upper层均属于ext4文件系统。而merged则属于overlay文件系统。

其中overlay的文件系统类型结构在fs/overlayfs/super.c中**ovl_fs_type**如下：

{%highlight c%}
static struct file_system_type ovl_fs_type = {
	.owner		= THIS_MODULE,
	.name		= "overlay",
	.mount		= ovl_mount,//挂载函数
	.kill_sb	= kill_anon_super,
};
{%endhighlight%}

mount字段指定了overlayfs的挂载函数：主要是分配并初始化一个指定文件系统的超级块对象，插入到该文件系统类型的超级块链表，文件系统类型file_system_type的fs_supers字段指向该链表的链表头

{%highlight c %}
static struct dentry *ovl_mount(struct file_system_type *fs_type, int flags,
				const char *dev_name, void *raw_data)
{
	return mount_nodev(fs_type, flags, raw_data, ovl_fill_super);
}

struct dentry *mount_nodev(struct file_system_type *fs_type,
	int flags, void *data,
	int (*fill_super)(struct super_block *, void *, int))
{
	int error;
	struct super_block *s = sget(fs_type, NULL, set_anon_super, flags, NULL);//分配超级块对象并初始化添加到**file_system_type->fs_supers**文件系统超级块对象链表和全局的已安装文件系统超级块链表**super_blocks**

	if (IS_ERR(s))
		return ERR_CAST(s);

	error = fill_super(s, data, flags & MS_SILENT ? 1 : 0);//填充超级块信息
	if (error) {
		deactivate_locked_super(s);
		return ERR_PTR(error);
	}
	s->s_flags |= MS_ACTIVE;
	return dget(s->s_root);
}
{%endhighlight%}

**填充超级块信息fill_super()**

在介绍填充超级块之前先介绍两个关键的结构体：

{%highlight c %}
//描述overlay文件系统的特有信息，包括overlayfs的upper层及lower层的已安装文件系统信息vfsmount及文件名以及work目录的目录项信息
struct ovl_fs {
	struct vfsmount *upper_mnt;
	unsigned numlower;
	struct vfsmount **lower_mnt;
	struct dentry *workdir;
	long lower_namelen;
	/* pathnames of lower and upper dirs, for show_options */
	struct ovl_config config;
};
//upper和lower及work路径名
struct ovl_config {
	char *lowerdir;
	char *upperdir;
	char *workdir;
};
//overlay特有的目录项对象
struct ovl_entry {
    struct dentry *__upperdentry;
    struct ovl_dir_cache *cache;
    union {
       struct {
           u64 version;
           bool opaque;
           };
       struct rcu_head rcu;
     }; 
    unsigned numlower;
    struct path lowerstack[];
};
{% endhighlight %}

填充超级块信息：
{%highlight c %}
static int ovl_fill_super(struct super_block *sb, void *data, int silent)
{
	struct path upperpath = { NULL, NULL };//upperpath定义结构体｛vfsmount(文件系统下子目录已安装的文件系统信息),dentry(目录项)｝
	struct path workpath = { NULL, NULL };//定义工作路径
	struct dentry *root_dentry;//根路径目录项
	struct ovl_entry *oe;//overlay的目录项结构
	struct ovl_fs *ufs;//ufs保存overlay文件系统特有的相关信息
	struct path *stack = NULL;//栈
	char *lowertmp;//底层临时目录路径
	char *lower;//底层目录路径
	unsigned int numlower;//底层次序
	unsigned int stacklen = 0;//栈长度
	unsigned int i;
	bool remote = false;
	int err;

	err = -ENOMEM;
	ufs = kzalloc(sizeof(struct ovl_fs), GFP_KERNEL);//分配ovl_fs结构体内存，GFP_KERNEL为内存分配标志位，调用函数是代表一个进程在执行一个系统调用. 使用 GFP_KENRL 意味着 kmalloc 能够使当前进程在少内存的情况下睡眠来等待一页
	if (!ufs)
		goto out;

	err = ovl_parse_opt((char *) data, &ufs->config);//将data（命令配置信息）数据填充到ufs->config
	if (err)
		goto out_free_config;

	err = -EINVAL;
	if (!ufs->config.lowerdir) {//检查是否有底层目录
		pr_err("overlayfs: missing 'lowerdir'\n");
		goto out_free_config;
	}

	sb->s_stack_depth = 0;
	sb->s_maxbytes = MAX_LFS_FILESIZE;
	if (ufs->config.upperdir) {
		if (!ufs->config.workdir) {//若有上层目录upperdir，则检查是否有工作目录
			pr_err("overlayfs: missing 'workdir'\n");
			goto out_free_config;
		}

		err = ovl_mount_dir(ufs->config.upperdir, &upperpath);//首先将路径进行处理，除去所有‘／’字符，其次对所得路径临时变量进行查询，将查询结果存放到upperpath变量，检查是否支持该文件系统，检查是否为一个目录，最后检查通过后将upperpath的相关引用计数减少
		if (err)
			goto out_free_config;

		/* Upper fs should not be r/o */
		if (upperpath.mnt->mnt_sb->s_flags & MS_RDONLY) {
			pr_err("overlayfs: upper fs is r/o, try multi-lower layers mount\n");
			err = -EINVAL;
			goto out_put_upperpath;
		}

		err = ovl_mount_dir(ufs->config.workdir, &workpath);//同上
		if (err)
			goto out_put_upperpath;

		err = -EINVAL;
		if (upperpath.mnt != workpath.mnt) {//上层目录的已安装文件系统信息vfsmount和工作目录的已安装文件系统的信息是否相同
			pr_err("overlayfs: workdir and upperdir must reside under the same mount\n");
			goto out_put_workpath;
		}
		if (!ovl_workdir_ok(workpath.dentry, upperpath.dentry))//工作目录和上层目录应该是分开的兄弟目录，而不是父子关系 {
			pr_err("overlayfs: workdir and upperdir must be separate subtrees\n");
			goto out_put_workpath;
		}
		sb->s_stack_depth = upperpath.mnt->mnt_sb->s_stack_depth;//设置文件系统超级块栈深
	}
	err = -ENOMEM;
	lowertmp = kstrdup(ufs->config.lowerdir, GFP_KERNEL);//复制底层目录
	if (!lowertmp)
		goto out_put_workpath;

	err = -EINVAL;
	stacklen = ovl_split_lowerdirs(lowertmp);//处理底层目录路径，包括去除'/'，及解析‘:’替换为'\0'，遇到":"说明底层目录栈更深一层，返回底层目录栈的深度。
		if (stacklen > OVL_MAX_STACK) {
		pr_err("overlayfs: too many lower directries, limit is %d\n",
		       OVL_MAX_STACK);
		goto out_free_lowertmp;
	} else if (!ufs->config.upperdir && stacklen == 1) {
		pr_err("overlayfs: at least 2 lowerdir are needed while upperdir nonexistent\n");
		goto out_free_lowertmp;
	}

	stack = kcalloc(stacklen, sizeof(struct path), GFP_KERNEL);//分配栈内存空间
	if (!stack)
		goto out_free_lowertmp;

	lower = lowertmp;
	for (numlower = 0; numlower < stacklen; numlower++) {
		err = ovl_lower_dir(lower, &stack[numlower],
				    &ufs->lower_namelen, &sb->s_stack_depth,
				    &remote);//检查是否支持该文件系统，检查是否为一个目录，最后检查通过后将upperpath的相关引用计数减少，然后获取虚拟文件系统的磁盘相关信息到statfs
		if (err)
			goto out_put_lowerpath;

		lower = strchr(lower, '\0') + 1;
	}

	err = -EINVAL;
	sb->s_stack_depth++;
	if (sb->s_stack_depth > FILESYSTEM_MAX_STACK_DEPTH) {
		pr_err("overlayfs: maximum fs stacking depth exceeded\n");
		goto out_put_lowerpath;
	}

	if (ufs->config.upperdir) {
		ufs->upper_mnt = clone_private_mount(&upperpath);//创建一个私有的路径clone,通过调用container_of来获取成员所在结构体的首地址
		err = PTR_ERR(ufs->upper_mnt);
		if (IS_ERR(ufs->upper_mnt)) {
			pr_err("overlayfs: failed to clone upperpath\n");
			goto out_put_lowerpath;
		}

		ufs->workdir = ovl_workdir_create(ufs->upper_mnt, workpath.dentry);//创建一个work目录，首先获取写权限，然后获得互斥体进入临界区，查询upper_mnt目录中是否有work目录，没有则调用ovl_create_real(),该函数进一步调用vfs_mkdir()调用父目录索引节点中的mkdir操作函数。
		
		err = PTR_ERR(ufs->workdir);
		if (IS_ERR(ufs->workdir)) {
			pr_warn("overlayfs: failed to create directory %s/%s (errno: %i); mounting read-only\n",
				ufs->config.workdir, OVL_WORKDIR_NAME, -err);
			sb->s_flags |= MS_RDONLY;
			ufs->workdir = NULL;
		}
	}

	err = -ENOMEM;
	ufs->lower_mnt = kcalloc(numlower, sizeof(struct vfsmount *), GFP_KERNEL);//分配底层目录内存
	if (ufs->lower_mnt == NULL)
		goto out_put_workdir;
	for (i = 0; i < numlower; i++) {
		struct vfsmount *mnt = clone_private_mount(&stack[i]);//对底层镜像栈逐个进行私有clone

		err = PTR_ERR(mnt);
		if (IS_ERR(mnt)) {
			pr_err("overlayfs: failed to clone lowerpath\n");
			goto out_put_lower_mnt;
		}
		/*
		 * Make lower_mnt R/O.  That way fchmod/fchown on lower file
		 * will fail instead of modifying lower fs.
		 */
		mnt->mnt_flags |= MNT_READONLY;

		ufs->lower_mnt[ufs->numlower] = mnt;//clone后填充到ufs->lower_mnt字段中
		ufs->numlower++;//并添加ufs的底层目录计数
	}

	/* If the upper fs is nonexistent, we mark overlayfs r/o too */
	if (!ufs->upper_mnt)
		sb->s_flags |= MS_RDONLY;

	if (remote)
		sb->s_d_op = &ovl_reval_dentry_operations;
	else
		sb->s_d_op = &ovl_dentry_operations;

	err = -ENOMEM;
	oe = ovl_alloc_entry(numlower);//根据栈的深度来确定ovl_entry（overlay所特有的目录项对象）的所需内存大小,调用offsetof(),然后分配内存返回首地址oe
	if (!oe)
		goto out_put_lower_mnt;

	root_dentry = d_make_root(ovl_new_inode(sb, S_IFDIR, oe));//ovl_new_inode创建新的索引节点作为根目录索引节点对象，然后d_make_root分配和构建根目录目录项缓存对象，最后根据目录项信息填充根目录索引节点信息
	if (!root_dentry)
		goto out_free_oe;

	mntput(upperpath.mnt);//释放上层目录挂载的引用计数
	for (i = 0; i < numlower; i++)
		mntput(stack[i].mnt);//释放底层目录引用计数
	path_put(&workpath);
	kfree(lowertmp);

	oe->__upperdentry = upperpath.dentry;
	for (i = 0; i < numlower; i++) {
		oe->lowerstack[i].dentry = stack[i].dentry;//设置底层目录的目录项信息
		oe->lowerstack[i].mnt = ufs->lower_mnt[i];//设置底层目录的文件系统安装挂载信息vfsmount
	}
	kfree(stack);

	root_dentry->d_fsdata = oe;//根目录目录项在overlay文件系统中特有的目录项结构体

	ovl_copyattr(ovl_dentry_real(root_dentry)->d_inode,
		     root_dentry->d_inode);

	sb->s_magic = OVERLAYFS_SUPER_MAGIC;
	sb->s_op = &ovl_super_operations;//设置超级块操作函数
	sb->s_root = root_dentry;//根目录目录项对象
	sb->s_fs_info = ufs;//设置超级块信息，s_fs_info指向overlay特有的文件系统信息

	return 0;

out_free_oe:
	kfree(oe);
out_put_lower_mnt:
	for (i = 0; i < ufs->numlower; i++)
		mntput(ufs->lower_mnt[i]);
	kfree(ufs->lower_mnt);
out_put_workdir:
	dput(ufs->workdir);
	mntput(ufs->upper_mnt);
out_put_lowerpath:
	for (i = 0; i < numlower; i++)
		path_put(&stack[i]);
	kfree(stack);
out_free_lowertmp:
	kfree(lowertmp);
out_put_workpath:
	path_put(&workpath);
out_put_upperpath:
	path_put(&upperpath);
out_free_config:
	kfree(ufs->config.lowerdir);
	kfree(ufs->config.upperdir);
	kfree(ufs->config.workdir);
	kfree(ufs);
out:
	return err;
}
{%endhighlight%}


填充超级块对象信息流程图如下：

![fill_super](/img/fill_super.png)


