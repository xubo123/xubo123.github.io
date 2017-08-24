---
layout:     post
title:      "CRIU swrk模式提供容器恢复的rpc服务"
subtitle:   "swrk提供rpc服务"
date:       2017-08-24 11:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码（CRIU）
---

## CRIU swrk模式提供容器恢复的rpc服务

前一章节我们结束了runc模块对启动容器部分的处理，配置好criureq的容器启动请求后，启动criu swrk 3命令，并向客户端写入请求数据，成功交付criu第三方工具实现容器进程的恢复。在此之前我们曾经对criu dump命令有过深入的分析，从命令参数解析到，备份的实现步骤都有所提及，所以这里我们直接跳过参数解析部分的内容，进入swrk模式的实现：

### criu swrk 3指定监听的socketfd，接收到criureq数据放到msg后，判断msg类型执行对应功能函数，功能函数执行完毕后发送响应数据

{% highlight c %}
//参数解析后进入该函数解析请求
int cr_service_work(int sk)
{
	int ret = -1;
	CriuReq *msg = 0;

more:
	if (recv_criu_msg(sk, &msg) == -1) {
	//调用recv函数对指定sk接受数据，将接收到的数据存放到&msg对象
		pr_perror("Can't recv request");
		goto err;
	}

	if (chk_keepopen_req(msg))
		goto err;

	switch (msg->type) {//解析接收到的msg对象类型
	case CRIU_REQ_TYPE__DUMP:
		ret = dump_using_req(sk, msg->opts);
		break;
	case CRIU_REQ_TYPE__RESTORE:
	    //在我们分析的内容中，该req类型为CRIU_REQ_TYPE__RESTORE
		ret = restore_using_req(sk, msg->opts);//进入具体实现容器进程恢复的函数
		break;
	case CRIU_REQ_TYPE__CHECK:
		ret = check(sk);
		break;
	case CRIU_REQ_TYPE__PRE_DUMP:
		ret = pre_dump_loop(sk, msg);
		break;
	case CRIU_REQ_TYPE__PAGE_SERVER:
		ret =  start_page_server_req(sk, msg->opts);
		break;
	case CRIU_REQ_TYPE__CPUINFO_DUMP:
	case CRIU_REQ_TYPE__CPUINFO_CHECK:
		ret = handle_cpuinfo(sk, msg);
		break;
	case CRIU_REQ_TYPE__FEATURE_CHECK:
		ret = handle_feature_check(sk, msg);
		break;
	case CRIU_REQ_TYPE__VERSION:
		ret = handle_version(sk, msg);
		break;

	default:
		send_criu_err(sk, "Invalid req");
		break;
	}

	if (!ret && msg->keep_open) {
		criu_req__free_unpacked(msg, NULL);
		ret = -1;
		goto more;
	}

err:
	return ret;
}


{% endhighlight %}

### 完成criu对容器进程的恢复并发送响应数据
{% highlight c %}

static int restore_using_req(int sk, CriuOpts *req)
{
	bool success = false;

	/*
	 * We can't restore processes under arbitrary task yet.
	 * Thus for now we force the detached restore under the
	 * cr service task.
	 */

	opts.restore_detach = true;

	if (setup_opts_from_req(sk, req))//根据criureq请求内容设置criuopt对象
		goto exit;

	setproctitle("restore --rpc -D %s", images_dir);

	if (cr_restore_tasks())//进入容器进程恢复函数
		goto exit;

	success = true;
exit:
	if (send_criu_restore_resp(sk, success,
				   root_item ? root_item->pid->real : -1) == -1) {
		pr_perror("Can't send response");
		success = false;
	}

	if (success && opts.exec_cmd) {
		int logfd;

		logfd = log_get_fd();
		if (dup2(logfd, STDOUT_FILENO) == -1 || dup2(logfd, STDERR_FILENO) == -1) {
			pr_perror("Failed to redirect stdout and stderr to the logfile");
			return 1;
		}

		close_pid_proc();
		close(sk);

		execvp(opts.exec_cmd[0], opts.exec_cmd);
		pr_perror("Failed to exec cmd %s", opts.exec_cmd[0]);
		success = false;
	}

	return success ? 0 : 1;
}

{% endhighlight %}

### criu恢复容器进程
{% highlight c %}

int cr_restore_tasks(void)
{
	int ret = -1;

	if (cr_plugin_init(CR_PLUGIN_STAGE__RESTORE))
		return -1;

	if (check_img_inventory() < 0)
	//读取检查点文件中的inventory.img文件数据，读取后配置的数据包括ns_per_id，root_ids，root_cg_set，image_lsm，img_common_magic。
		goto err;

	if (init_stats(RESTORE_STATS))
		goto err;

	if (kerndat_init())
		goto err;

	timing_start(TIME_RESTORE);

	if (cpu_init() < 0)
		goto err;

	if (vdso_init())
		goto err;

	if (opts.cpu_cap & (CPU_CAP_INS | CPU_CAP_CPU)) {
		if (cpu_validate_cpuinfo())
			goto err;
	}

	if (prepare_task_entries() < 0)
		goto err;

	if (prepare_pstree() < 0)
		goto err;

	if (crtools_prepare_shared() < 0)
		goto err;

	if (criu_signals_setup() < 0)
		goto err;
//这之前的内容都能够成功执行，完成恢复前的准备工作，而主要恢复的过程在restore_root_task中，所以下一章节我们将就这个函数进入深入分析。
	ret = restore_root_task(root_item);
err:
	cr_plugin_fini(CR_PLUGIN_STAGE__RESTORE, ret);
	return ret;
}

{% endhighlight %}

