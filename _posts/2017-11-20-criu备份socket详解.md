---
layout:     post
title:      "CRIU备份socket详解"
subtitle:   "Internet_Socket迁移备份"
date:       2017-11-20 15:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码（CRIU）
---
## CRIU备份socket详解

在迁移过程中遇到对tcp连接检查点备份失败，为了了解底层失败原因，需要掌握criu对socketfd备份的过程。

这篇blog可以参考“criu dump重要步骤的深入研究六”，可以了解到criu备份socket的前期工作。

所以这篇blog将从（sockets.c）dump_socket展开分析：

![DumpTCPSocket](/img/DumpTCPSocket.png)

{% highlight c%}

int dump_socket(struct fd_parms *p, int lfd, struct cr_img *img)
{
	int family;
	const struct fdtype_ops *ops;

	if (dump_opt(lfd, SOL_SOCKET, SO_DOMAIN, &family))//根据lfd获取socket选项信息，family表示socket的集蔟
		return -1;

	switch (family) {
	case AF_UNIX:
		ops = &unix_dump_ops;
		break;
	case AF_INET:
		ops = &inet_dump_ops;//internet
		break;
	case AF_INET6:
		ops = &inet6_dump_ops;
		break;
	case AF_PACKET:
		ops = &packet_dump_ops;
		break;
	case AF_NETLINK:
		ops = &netlink_dump_ops;
		break;
	default:
		pr_err("BUG! Unknown socket collected (family %d)\n", family);
		return -1;
	}

	return do_dump_gen_file(p, lfd, ops, img);
}

{% endhighlight %}

我们这次主要分析AF_INET类型的socket备份：

{% highlight c %}

const struct fdtype_ops inet_dump_ops = {
	.type		= FD_TYPES__INETSK,
	.dump		= dump_one_inet_fd,
};

int do_dump_gen_file(struct fd_parms *p, int lfd,
		const struct fdtype_ops *ops, struct cr_img *img)
{
	FdinfoEntry e = FDINFO_ENTRY__INIT;
	int ret = -1;
    //将inet_dump_ops及fd的参数信息填充到fd信息单位结构体
	e.type	= ops->type;
	e.id	= make_gen_id(p);
	e.fd	= p->fd;
	e.flags = p->fd_flags;

	ret = fd_id_generate(p->pid, &e, p);//产生fd_id
	if (ret == 1) /* new ID generated */
		ret = ops->dump(lfd, e.id, p);//执行inet_dump_ops.dump函数dump_one_inet_fd，见后详解

	if (ret < 0)
		return ret;

	pr_info("fdinfo: type: %#2x flags: %#o/%#o pos: %#8"PRIx64" fd: %d\n",
		ops->type, p->flags, (int)p->fd_flags, p->pos, p->fd);

	return pb_write_one(img, &e, PB_FDINFO);//写入fdinfo镜像
}

{% endhighlight %}

### 对一个internet文件描述符的备份
{% highlight c %}

static int do_dump_one_inet_fd(int lfd, u32 id, const struct fd_parms *p, int family)
{
	struct inet_sk_desc *sk;//internet_socket描述对象结构体
	InetSkEntry ie = INET_SK_ENTRY__INIT;//internet_socket条目
	IpOptsEntry ipopts = IP_OPTS_ENTRY__INIT;//ip选项结构体条目
	SkOptsEntry skopts = SK_OPTS_ENTRY__INIT;//socket选项条目
	int ret = -1, err = -1, proto;

	ret = do_dump_opt(lfd, SOL_SOCKET, SO_PROTOCOL,
					&proto, sizeof(proto));//获取socket协议
	if (ret)
		goto err;

	if (!can_dump_ipproto(p->stat.st_ino, proto))//判断获取的协议是否是我们支持的类型
		goto err;

	sk = (struct inet_sk_desc *)lookup_socket(p->stat.st_ino, family, proto);//从全局变量sockets数组中根据st_ino查询是否存在该socket
	if (IS_ERR(sk))
		goto err;
	if (!sk) {
		sk = gen_uncon_sk(lfd, p, proto);
		if (!sk)
			goto err;
	}

	if (!can_dump_inet_sk(sk))
	//根据该socket的类型及状态来判断是否可以备份:
	//类型（SOCK_DGRAM,SOCKT_STREAM）,
	//状态（TCP_LISTEN,(TCP_ESTABLISHED,TCP_FIN_WAIT2,TCP_FIN_WAIT1,TCP_CLOSE_WAIT,TCP_LAST_ACK,TCP_CLOSING,TCP_SYN_SENT)小括号内的考虑参数--tcp-established,TCP_CLOSE）
		goto err;

	BUG_ON(sk->sd.already_dumped);

	ie.id		= id;
	ie.ino		= sk->sd.ino;
	ie.family	= family;
	ie.proto	= proto;
	ie.type		= sk->type;
	ie.src_port	= sk->src_port;
	ie.dst_port	= sk->dst_port;
	ie.backlog	= sk->wqlen;
	ie.flags	= p->flags;

	ie.fown		= (FownEntry *)&p->fown;
	ie.opts		= &skopts;
	ie.ip_opts	= &ipopts;

	ie.n_src_addr = PB_ALEN_INET;
	ie.n_dst_addr = PB_ALEN_INET;
    //填充internet_socket条目信息ie
	if (ie.family == AF_INET6) {
	//ipv6的部分先不具体分析
		int val;
		char device[IFNAMSIZ];
		socklen_t len = sizeof(device);

		ie.n_src_addr = PB_ALEN_INET6;
		ie.n_dst_addr = PB_ALEN_INET6;

		ret = dump_opt(lfd, SOL_IPV6, IPV6_V6ONLY, &val);
		if (ret < 0)
			goto err;

		ie.v6only = val ? true : false;
		ie.has_v6only = true;

		/* ifindex only matters on source ports for bind, so let's
		 * find only that ifindex. */
		if (sk->src_port && needs_scope_id(sk->src_addr)) {
			if (getsockopt(lfd, SOL_SOCKET, SO_BINDTODEVICE, device, &len) < 0) {
				pr_perror("can't get ifname");
				goto err;
			}

			if (len > 0) {
				ie.ifname = xstrdup(device);
				if (!ie.ifname)
					goto err;
			} else {
				pr_err("couldn't find ifname for %d, can't bind\n", id);
				goto err;
			}
		}
	}

	ie.src_addr = xmalloc(pb_repeated_size(&ie, src_addr));
	ie.dst_addr = xmalloc(pb_repeated_size(&ie, dst_addr));

	if (!ie.src_addr || !ie.dst_addr)
		goto err;

	memcpy(ie.src_addr, sk->src_addr, pb_repeated_size(&ie, src_addr));
	memcpy(ie.dst_addr, sk->dst_addr, pb_repeated_size(&ie, dst_addr));//将socket的源地址和目的地址拷贝到ie中去
     //备份ip选项及socket选项信息到ipopts及skopts
	if (dump_ip_opts(lfd, &ipopts))
		goto err;

	if (dump_socket_opts(lfd, &skopts))
		goto err;

	pr_info("Dumping inet socket at %d\n", p->fd);
	show_one_inet("Dumping", sk);
	show_one_inet_img("Dumped", &ie);
	sk->sd.already_dumped = 1;
	sk->cpt_reuseaddr = skopts.reuseaddr;

	switch (proto) {//根据协议来备份连接信息
	case IPPROTO_TCP:
		err = dump_one_tcp(lfd, sk);//备份tcp连接信息
		break;
	case IPPROTO_UDP:
	case IPPROTO_UDPLITE:
		sk_encode_shutdown(&ie, sk->shutdown);
		/* Fallthrough! */
	default:
		err = 0;
		break;
	}

	ie.state = sk->state;

	if (pb_write_one(img_from_set(glob_imgset, CR_FD_INETSK), &ie, PB_INET_SK))//写入internet_socket镜像
		goto err;
err:
	release_skopts(&skopts);
	xfree(ie.src_addr);
	xfree(ie.dst_addr);
	return err;
}
{% endhighlight %}

### 对tcp连接的备份操作:
{% highlight c %}

int dump_one_tcp(int fd, struct inet_sk_desc *sk)
{
	if (sk->dst_port == 0)
		return 0;

	pr_info("Dumping TCP connection\n");

	if (tcp_repair_establised(fd, sk))//启动tcp连接修复模式
		return -1;

	if (dump_tcp_conn_state(sk))//备份tcp的连接实时状态
		return -1;

	/*
	 * Socket is left in repair mode, so that at the end it's just
	 * closed and the connection is silently terminated
	 */
	return 0;
}
{% endhighlight %}

#### 启动tcp修复模式
{%highlight c%}

static int tcp_repair_establised(int fd, struct inet_sk_desc *sk)
{
	int ret;
	struct libsoccr_sk *socr;

	pr_info("\tTurning repair on for socket %x\n", sk->sd.ino);
	/*
	 * Keep the socket open in criu till the very end. In
	 * case we close this fd after one task fd dumping and
	 * fail we'll have to turn repair mode off
	 */
	sk->rfd = dup(fd);//首先复制该socket_fd
	if (sk->rfd < 0) {
		pr_perror("Can't save socket fd for repair");
		goto err1;
	}

	if (!(root_ns_mask & CLONE_NEWNET)) {
		ret = nf_lock_connection(sk);//通过调用网络过滤器中的连接开关关闭该socket连接
		if (ret < 0)
			goto err2;
	}

	socr = libsoccr_pause(sk->rfd);//打开该备份fd socket的的修复模式
	if (!socr)
		goto err3;

	sk->priv = socr;
	list_add_tail(&sk->rlist, &cpt_tcp_repair_sockets);//添加至ck_socket修复链表
	return 0;

err3:
	if (!(root_ns_mask & CLONE_NEWNET))
		nf_unlock_connection(sk);
err2:
	close(sk->rfd);
err1:
	return -1;
}

{%endhighlight%}

#### 备份tcp连接信息
{% highlight c%}

static int dump_tcp_conn_state(struct inet_sk_desc *sk)
{
	struct libsoccr_sk *socr = sk->priv;
	int ret, aux;
	struct cr_img *img;
	TcpStreamEntry tse = TCP_STREAM_ENTRY__INIT;
	char *buf;
	struct libsoccr_sk_data data;

	ret = libsoccr_save(socr, &data, sizeof(data));//从socr中获取信息（流选项，窗口信息，（接收和发送）队列信息），存放到data对象
	if (ret < 0)
		goto err_r;
	if (ret != sizeof(data)) {
		pr_err("This libsocr is not supported (%d vs %d)\n",
				ret, (int)sizeof(data));
		goto err_r;
	}
    //开始将tcp流连接获取到的信息data填充到tcp流条目中去tse
	sk->state = data.state;

	tse.inq_len = data.inq_len;
	tse.inq_seq = data.inq_seq;
	tse.outq_len = data.outq_len;
	tse.outq_seq = data.outq_seq;
	tse.unsq_len = data.unsq_len;
	tse.has_unsq_len = true;
	tse.mss_clamp = data.mss_clamp;
	tse.opt_mask = data.opt_mask;

	if (tse.opt_mask & TCPI_OPT_WSCALE) {
		tse.snd_wscale = data.snd_wscale;
		tse.rcv_wscale = data.rcv_wscale;
		tse.has_rcv_wscale = true;
	}
	if (tse.opt_mask & TCPI_OPT_TIMESTAMPS) {
		tse.timestamp = data.timestamp;
		tse.has_timestamp = true;
	}

	if (data.flags & SOCCR_FLAGS_WINDOW) {
		tse.has_snd_wl1		= true;
		tse.has_snd_wnd		= true;
		tse.has_max_window	= true;
		tse.has_rcv_wnd		= true;
		tse.has_rcv_wup		= true;
		tse.snd_wl1		= data.snd_wl1;
		tse.snd_wnd		= data.snd_wnd;
		tse.max_window		= data.max_window;
		tse.rcv_wnd		= data.rcv_wnd;
		tse.rcv_wup		= data.rcv_wup;
	}

	/*
	 * TCP socket options
	 */

	if (dump_opt(sk->rfd, SOL_TCP, TCP_NODELAY, &aux))//TCP_NODELAY和下面的TCP_CORK相对应，这个是有数据即发送，TCP_CORK则是累积数据后发送，获取该选项值备份到aux
		goto err_opt;

	if (aux) {
		tse.has_nodelay = true;
		tse.nodelay = true;
	}

	if (dump_opt(sk->rfd, SOL_TCP, TCP_CORK, &aux))//同上
		goto err_opt;

	if (aux) {
		tse.has_cork = true;
		tse.cork = true;
	}

	/*
	 * Push the stuff to image
	 */

	img = open_image(CR_FD_TCP_STREAM, O_DUMP, sk->sd.ino);
	if (!img)
		goto err_img;

	ret = pb_write_one(img, &tse, PB_TCP_STREAM);//写入tcp_stream镜像
	if (ret < 0)
		goto err_iw;

	buf = libsoccr_get_queue_bytes(socr, TCP_RECV_QUEUE, SOCCR_MEM_EXCL);//获取 socket的接受队列的缓存并写入镜像
	if (buf) {
		ret = write_img_buf(img, buf, tse.inq_len);
		if (ret < 0)
			goto err_iw;

		xfree(buf);
	}

	buf = libsoccr_get_queue_bytes(socr, TCP_SEND_QUEUE, SOCCR_MEM_EXCL);//获取socket的发送队列的缓存并写入镜像
	if (buf) {
		ret = write_img_buf(img, buf, tse.outq_len);
		if (ret < 0)
			goto err_iw;

		xfree(buf);
	}

	pr_info("Done\n");
err_iw:
	close_image(img);
err_img:
err_opt:
err_r:
	return ret;
}
{% endhighlight %}
