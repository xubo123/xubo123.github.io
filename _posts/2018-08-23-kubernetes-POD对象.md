---
layout:     post
title:      "kubernetes POD对象"
subtitle:   "kubernetes基础"
date:       2018-08-23 10:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - kubernetes概念
---

# POD对象

POD是K8S最小的、管理、创建、计划的最小单元

## 什么是POD

一个POD相当于一个一个共享context的配置组，在同一个context下，应用可能还会有独立的cgroup隔离机制，一个Pod是一个容器环境下的"逻辑主机"，它可能包含一个或多个紧密相连的应用

*context可以理解为多个linux命名空间的联合，类似于一个独立的namespace环境
    - PID命名空间
    - 网络命名空间
    - IPC命名空间
    - UTS命名空间
* 同一个POD中的应用可以共享磁盘volume，磁盘volume是POD级别的，volume可以挂载到容器的文件系统
* 在Docker的架构中，一个POD类似于一组有着 **共享namespace和共享数据卷volume**的Docker容器
* pod是相对短暂的存在，不具有持久性，一个pod在一个node节点上，当一个节点down掉后，上面所有的pod都会被删除，该pod不会被转移到其他的节点，而是作为替代，用 **其他的相同功能的pod来替代(不同的UID)**。

## POD通信方式

在POD内部的容器共享IP地址和端口范围，可以通过localhost相互感知，内部容器的通信是通过IPC的方式进行通信的，包括 SystemV semphores 或Posix shared memory。不同POD上的容器有不同的IP地址，不能通过IPC通信，所以这些容器通过ip来相互通信

## POD的使用场景

POD可以作为**垂直应用**整合的载体，但是它的主要特点是支持同地协作，同地管理程序，例如：

* 内容管理系统，文件和数据加载，本地缓存等
* 日志和检查点备份，压缩，循环和快照
* 。。。

总体来说，独立的Pod不会加载多个相同的实例

* 为什么不在一个容器上运行所有的应用？
    - 透明：Pod中的容器对基础设施可见，使得基础设施可以给容器提供服务，例如线程管理和资源监控
    - 解耦：降低软件之间的依赖关系
    - 易用：用户不需要自己运行自己的线程管理器，也不需要关系程序的信号以及异常结束码
    - 高效：因为基础设施承载了更多责任，所以容器可以更加高效

## POD中容器的终止

* pod包含一个集群中节点上运行的进程，让这些进程不再被需要，优雅的退出很重要。用户可以请求删除，保证最后删除可以完成。
* 当用户请求删除pod,系统记录想要的优雅退出时间段，在这之前pod不能被强制的杀死，TERM信号会发送给容器主要的进程，一旦时间段过期，kill命令会送到这些进程。

## POD镜像拉取的策略

三种镜像拉取的策略：

* Always:不管镜像是否存在都会进行一次拉取
* Never:不管镜像是否存在都不会拉取
* IfNotPresent:只有镜像不存在时，才会进行镜像拉取

PS:

* 默认为IfNotPresent,但:latest标签的镜像默认为Always
* 拉取镜像时，docker会进行校验，如果镜像中的MD5没有变，则不会拉取镜像数据

## POD资源限制

Kubernetes通过cgroups限制容器的CPU和内存等计算资源，其中包括requests(请求，调度器保证调度到资源充足的Node上)和limits(上限)等

## 健康检查

为了确保容器在部署后确实处在正常运行的状态，K8S提供了两种探针来探测容器的状态:

1. LivenessProbe:探测应用是否处于健康状态，如果不健康则删除重建容器
2. ReadinessProbe:探测应用是否启动完成并处于正常服务状态，如果不正常则更新容器状态

## Init Container

InitContainer在所有容器运行之前执行，常用来初始化配置

## 容器生命周期钩子

容器生命周期钩子监听容器生命周期的特定事件，并在事件发生时执行已经注册的回调函数，支持两种钩子：

* postStart:容器启动后执行，注意由于是异步执行，所以无法保证一定在ENTRYPOINT之后运行，如果失败容器会被杀死，并根据RestartPolicy决定是否重启
* preStop:容器停止前执行，常用于资源清理，如果失败，容器同样会被杀死

钩子的回调函数支持两种：

* exec:在容器内执行命令
* httpGet:向指定URL发起GET请求

## Pod可以指定Node

通过nodeselector,可以指定它所想要运行的Node节点

## POD的几个阶段
一个POD的status字段中包含的是一个PodStatus对象，该对象中包含一个字段phase,下面是phase可能的值：

* Pending:说明Pod已经被K8S系统接收，但至少有一个容器的镜像还没有创建好
* Running:说明Pod已经被绑定到一个Node上，所有的容器都成功创建，至少有一个容器在运行，或在启动或重启的状态
* Succeeded:Pod中的所有容器都成功终止，并且不会被重启
* Failed:所有容器都被终止，但是至少有一个容器终止失败
* Unknown:因为某些原因，Pod的状态无法获取，通常是因为无法和主机上的Pod进行通信

POD的状况信息可以在PodStatus对象中的PodConditions数组得到，其中包括：

* lastProbeTime:上一次被探测状态的时间
* lastTransitionTime:上一次状态变化的时间
* message:可读的消息描述状态转换的细节信息
* reason:一个词表示状态转换的原因
* status:"True","False","Unknown"
* type
    - PodScheduled:该POD已经被调度到一个Node上
    - Ready:该POD可以对外提供服务
    - Initialiezd:所有初始化容器已经成功启动
    - Unschedulable:scheduler不能对该POD实现调度
    - ContainersReady:在POD所有容器都已经准备好

## 限制POD网络带宽
可以通过kubernetes.io/ingress-bandwidth和kubernetes.io/egress-bandwidth这两个注释annotation来限制Pod的网络带宽

kubernete的网络带宽限制其实是通过tc来实现的


