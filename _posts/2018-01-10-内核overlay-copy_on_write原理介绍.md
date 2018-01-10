---
layout:     post
title:      "内核OVERLAY--Copy_On_Write"
subtitle:   "写时复制原理"
date:       2018-01-10 12:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - Linux内核
---
## 内核OVERLAY-Copy_On_Write原理介绍

![COW](/img/COW.png)


### ovl_copy_up（）：

发起copy_on_write的检测和操作
* 首先检测该文件是否位于upper层，如果没有则发起copy_on_write操作
* 确定该文件只位于底层，则循环获取该文件及其父目录的锁结构，直到父目录位于upper层跳出循环
* 跳出内层循环后，获取该文件（或者是该文件的父目录）在lower层的路径结构，并获取其stat数据
* 将该文件（或者是该文件的父目录）从底层拷贝upper层ovl_copy_up_one（）
* 然后又从第一步开始检测，直到所有该文件及该文件的父目录均被拷贝到upper层跳出外层循环
{% highlight c%}
int ovl_copy_up(struct dentry *dentry)
{
	int err;

	err = 0;
	while (!err) {
		struct dentry *next;
		struct dentry *parent;
		struct path lowerpath;
		struct kstat stat;
		enum ovl_path_type type = ovl_path_type(dentry);//查询文件的类型

		if (OVL_TYPE_UPPER(type))//位于upper层则不需要copy_on_write
			break;

		next = dget(dentry);//获取该目录项的锁
		/* find the topmost dentry not yet copied up */
		for (;;) {
			parent = dget_parent(next);//获取父目录项的锁结构

			type = ovl_path_type(parent);//获取父目录项的类型
			if (OVL_TYPE_UPPER(type))//父目录项位于上层则跳出循环
				break;

			dput(next);//释放锁
			next = parent;
		}

		ovl_path_lower(next, &lowerpath);//next肯定位于底层，OVL_TYPE_UPPER验证不在upper层，循环过程中只要next的父目录位于upper层则跳出循环，获取底层目录路径结构体lowerpath。
		err = vfs_getattr(&lowerpath, &stat);//获取路径指定文件的状态
		if (!err)
			err = ovl_copy_up_one(parent, next, &lowerpath, &stat);//然后将next拷贝到upper层中去

		dput(parent);
		dput(next);
	}

	return err;
}
{% endhighlight%}

### ovl_copy_up_one():

负责将文件从底层lowerpath拷贝到父目录所在upper层的目录项结构下
* 获取工作目录下的该文件目录项结构
* 获取upper层中父目录的路径结构、目录项、状态信息
* 设置权限位
* 获取upper层和work目录的重命名锁
* 执行具体的拷贝操作ovl_copy_up_locked

{% highlight c%}
int ovl_copy_up_one(struct dentry *parent, struct dentry *dentry,
		    struct path *lowerpath, struct kstat *stat)
{
	struct dentry *workdir = ovl_workdir(dentry);
	int err;
	struct kstat pstat;
	struct path parentpath;
	struct dentry *upperdir;
	struct dentry *upperdentry;
	const struct cred *old_cred;
	struct cred *override_cred;
	char *link = NULL;

	if (WARN_ON(!workdir))
		return -EROFS;

	ovl_path_upper(parent, &parentpath);//获取upper层中父目录parent路径到parentpath
	upperdir = parentpath.dentry;//获取upper层的父目录的目录项

	err = vfs_getattr(&parentpath, &pstat);//获取upper层的父目录的状态
	if (err)
		return err;

	if (S_ISLNK(stat->mode)) {//是否为链接
		link = ovl_read_symlink(lowerpath->dentry);
		if (IS_ERR(link))
			return PTR_ERR(link);
	}

	err = -ENOMEM;
	override_cred = prepare_creds();//准备复写权限
	if (!override_cred)
		goto out_free_link;

	override_cred->fsuid = stat->uid;
	override_cred->fsgid = stat->gid;
	/*
	 * CAP_SYS_ADMIN for copying up extended attributes
	 * CAP_DAC_OVERRIDE for create
	 * CAP_FOWNER for chmod, timestamp update
	 * CAP_FSETID for chmod
	 * CAP_CHOWN for chown
	 * CAP_MKNOD for mknod
	 */
	cap_raise(override_cred->cap_effective, CAP_SYS_ADMIN);//设置权限，在位操作符中对位的操作，这里相当于将指定权限位设为1.还有cap_lower()清零，cap_raised(),检测
	cap_raise(override_cred->cap_effective, CAP_DAC_OVERRIDE);
	cap_raise(override_cred->cap_effective, CAP_FOWNER);
	cap_raise(override_cred->cap_effective, CAP_FSETID);
	cap_raise(override_cred->cap_effective, CAP_CHOWN);
	cap_raise(override_cred->cap_effective, CAP_MKNOD);
	old_cred = override_creds(override_cred);

	err = -EIO;
	if (lock_rename(workdir, upperdir) != NULL) {//获取重命名锁的机制，详见https://www.ibm.com/developerworks/cn/linux/l-cn-fsmeta/
		pr_err("overlayfs: failed to lock workdir+upperdir\n");
		goto out_unlock;
	}
	upperdentry = ovl_dentry_upper(dentry);//获取upper层目录项
	if (upperdentry) {
		/* Raced with another copy-up?  Nothing to do, then... */
		err = 0;
		goto out_unlock;
	}

	err = ovl_copy_up_locked(workdir, upperdir, dentry, lowerpath,
				 stat, link);//执行具体的拷贝操作
	if (!err) {
		/* Restore timestamps on parent (best effort) */
		ovl_set_timestamps(upperdir, &pstat);
	}
out_unlock:
	unlock_rename(workdir, upperdir);
	revert_creds(old_cred);
	put_cred(override_cred);

out_free_link:
	if (link)
		free_page((unsigned long) link);

	return err;
}
{% endhighlight%}

### ovl_copy_up_locked()

具体的文件拷贝过程： 将工作目录作为中间过渡层，来由底层拷贝数据到工作目录，然后通过rename操作将文件转移到upper层目录

* 在工作目录中"检索""该文件在相应路径临时文件的目录项结构newdentry
* 在upper层中""检索""该文件在相应路径的目录项结构upper
* 在工作目录下创建newdentry指定的文件或目录
* 对常规文件，获取其在upper层对应的路径结构体upperpath，并设置该路径结构体中的目录项对象为工作目录下的该文件的目录项
* 然后拷贝底层文件的数据到upperpath指定的目录下(此时为工作目录下的路径)，同时复制底层文件目录项中的attr及stat信息到upperpath->newdentry
* 最后通过rename操作将工作目录下的该文件复制到upper层目录下upper目录项
* 然后更新overlay文件系统中该文件的目录项dentry的层次信息中的__upperdentry结构
{% highlight c%}

static int ovl_copy_up_locked(struct dentry *workdir, struct dentry *upperdir,
			      struct dentry *dentry, struct path *lowerpath,
			      struct kstat *stat, const char *link)
{
	struct inode *wdir = workdir->d_inode;//获取工作目录的索引节点
	struct inode *udir = upperdir->d_inode;//获取upper层目录索引节点
	struct dentry *newdentry = NULL;
	struct dentry *upper = NULL;
	umode_t mode = stat->mode;
	int err;

	newdentry = ovl_lookup_temp(workdir, dentry);//查询工作目录临时文件，返回该临时文件的目录项
	err = PTR_ERR(newdentry);
	if (IS_ERR(newdentry))
		goto out;

	upper = lookup_one_len(dentry->d_name.name, upperdir,
			       dentry->d_name.len);//查询upper层中指定名称的组件目录项
	err = PTR_ERR(upper);
	if (IS_ERR(upper))
		goto out1;

	/* Can't properly set mode on creation because of the umask */
	stat->mode &= S_IFMT;
	err = ovl_create_real(wdir, newdentry, stat, link, NULL, true);//在工作目录创建newdentry指定的文件或目录
	stat->mode = mode;
	if (err)
		goto out2;

	if (S_ISREG(stat->mode)) {//是否是一个常规文件
		struct path upperpath;
		ovl_path_upper(dentry, &upperpath);//获取该文件在upper层目录的路径结构体，此时upperpath.dentry应该为空
		BUG_ON(upperpath.dentry != NULL);
		upperpath.dentry = newdentry;//设置upperpath.dentry为工作目录下该文件的目录项

		err = ovl_copy_up_data(lowerpath, &upperpath, stat->size);//拷贝底层目录下的文件到，upperpath指定的路径（其目录项指向的是工作目录下的文件）
		if (err)
			goto out_cleanup;
	}

	err = ovl_copy_xattr(lowerpath->dentry, newdentry);//复制一些底层目录项的属性到新的目录项
	if (err)
		goto out_cleanup;

	mutex_lock(&newdentry->d_inode->i_mutex);//获取新目录项的锁结构
	err = ovl_set_attr(newdentry, stat);//设置索引的stat
	mutex_unlock(&newdentry->d_inode->i_mutex);
	if (err)
		goto out_cleanup;

	err = ovl_do_rename(wdir, newdentry, udir, upper, 0);//然后将工作目录中的该文件重命名到upper层目录
	if (err)
		goto out_cleanup;

	ovl_dentry_update(dentry, newdentry);//更新dentry的层次信息中__upperdentry为newdentry
	newdentry = NULL;

	/*
	 * Non-directores become opaque when copied up.
	 */
	if (!S_ISDIR(stat->mode))
		ovl_dentry_set_opaque(dentry, true);
out2:
	dput(upper);
out1:
	dput(newdentry);
out:
	return err;

out_cleanup:
	ovl_cleanup(wdir, newdentry);
	goto out2;
}

{% endhighlight%}


### ovl_copy_up_data（）

具体的数据拷贝过程：这个函数的操作就是具体的拷贝数据的操作，将数据从底层lowerpath(old)拷贝到上层upperpath(new)，数据大小为len

{% highlight c%}
static int ovl_copy_up_data(struct path *old, struct path *new, loff_t len)
{
	struct file *old_file;
	struct file *new_file;
	loff_t old_pos = 0;
	loff_t new_pos = 0;
	int error = 0;

	if (len == 0)
		return 0;

	old_file = ovl_path_open(old, O_LARGEFILE | O_RDONLY);//只读的方式在底层目录下打开该文件，并获取文件对象
	if (IS_ERR(old_file))
		return PTR_ERR(old_file);

	new_file = ovl_path_open(new, O_LARGEFILE | O_WRONLY);//只写的方式在工作目录下打开，获取新文件的文件对象
	if (IS_ERR(new_file)) {
		error = PTR_ERR(new_file);
		goto out_fput;
	}

	/* FIXME: copy up sparse files efficiently */
	while (len) {
		size_t this_len = OVL_COPY_UP_CHUNK_SIZE;//一次拷贝块的大小
		long bytes;

		if (len < this_len)//如果文件大小小于一次拷贝的大小，则重设这一次拷贝数据的大小
			this_len = len;

		if (signal_pending_state(TASK_KILLABLE, current)) {
			error = -EINTR;
			break;
		}

		bytes = do_splice_direct(old_file, &old_pos,
					 new_file, &new_pos,
					 this_len, SPLICE_F_MOVE);//将this_len大小的数据从旧文件的指定偏移量处拷贝到新文件的指定偏移量处，splice是一种零拷贝数据发送函数，主要用于从管道发送，和从管道接受数据，传统的读写发送数据，会多出两次拷贝。这里我在下一章将作出具体的分析，如何减少拷贝次数
				 ，		if (bytes <= 0) {
			error = bytes;
			break;
		}
		WARN_ON(old_pos != new_pos);

		len -= bytes;//拷贝成功，则减少需要拷贝的数据量的大小
	}

	if (!error)
		error = vfs_fsync(new_file, 0);//将新文件的数据写回同步到磁盘
	fput(new_file);
out_fput:
	fput(old_file);
	return error;
}
{% endhighlight%}


---
