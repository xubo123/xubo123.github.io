---
layout:     post
title:      "criu Checkpoint流程分析"
subtitle:   "criu checkpoint一"
date:       2017-05-31 12:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - docker源码（CRIU）
---
## CRIU-Checkpoint／Restore备份流程分析

官网（https://criu.org/Checkpoint/Restore）描述如下：

checkpoint过程主要依赖于／proc文件系统（这是criu checkpoint获取所需要大部分数据的地方），从/proc获取的数据包括：</br>
1.文件描述符信息（/proc/$pid/fd和/proc/$pid/fdinfo）</br>
2.通道参数</br>
3.内存映射（/proc/$pid/maps和/proc/$pid/map_files）</br>
等。</br>

进程备份的步骤主要有如下几步：</br>
### 1.收集进程树并进行冻结</br>
  一个进程组父节点的$pid可以通过命令行（－－option参数）获取，通过使用$pid，备份器将会遍历/proc/$pid/task目录，收集线程信息，并遍历/proc/$pid/task/$tid/children迭代收集子进程信息，最后进行冻结</br>
 
### 2.收集任务的资源并备份</br>
   这个步骤，CRIU会读取所有与被收集的任务相关的信息并且把他们写到备份文件，资源的获取方式通过如下几种：</br>
   （1）虚拟存储区域通过/proc/$pid/smaps解析，并且映射文件通过 ／proc/$pid/map_files读取</br>
   （2）文件描述符的数量通过/proc/$pid/fd读取</br>
   （3）任务的内核参数通过ptrace接口并解析/proc/$pid/stat数据项来备份</br>
   CRIU通过ptrace接口给任务注入来parasitecode（寄生代码），这分为两个步骤：首先我们仅仅在任务所具有的 CS（代码段段地址）:IP（即将执行下一条指令的地址，偏移地址）给mmap系统调用注入少量字节，然后ptrace允许我们运行一个被注入的系统调用并且我们为寄生代码块分配来足够的内存来备份，此后寄生代码被拷贝到一个在被备份地址空间和CS:IP集中新的地方</br>

### 3.清理备份文件</br>
   在所有文件信息都完成备份后（例如内存页面，这只能从内部被备份的地址空间写出），我们再次使用ptrace工具来恢复被备份的内容，通过丢弃掉寄生代码并恢复原始的代码，然后CRIU解除和任务的绑定，让他们恢复运行。</br>
   
 
## Restore恢复步骤（4个步骤）：</br>

### 1.解析共享资源</br>
  这个步骤,CRIU读取镜像文件并且找出哪个进程共享哪个资源，之后共享的资源通过某一个进程被恢复并且所有其他共享的进程在第二阶段（比如会话）继承或通过某种其他的方式获取，例如带有SCM_CREDS消息的共享文件通过unix sockets发送，共享内存区域通过memfd文件描述符恢复。</br>

### 2.创建进程树</br>
  在这个步骤，CRIU多次调用fork（）来重建需要恢复的进程组。注明：线程不是在这个步骤恢复的，而是在第四个步骤恢复</br>

### 3.恢复基础的任务资源</br>
   CRIU恢复所有的资源除了：</br>
   （1）内存映射准确位置</br>
   （2）时钟</br>
   （3）credentials（信任证书文件？）</br>
   （4）线程</br>
   以上四个类型的资源被延迟到最后一个步骤恢复，在这个阶段CRIU打开文件，准备namespaces，映射（带有数据的文件）私有内存区域，创建套接字，调用chdir（）和chroot（）并且处理一些其它事务。</br>

### 4.转换到被恢复的上下文环境，恢复第三个步骤没有恢复的资源</br>

  
  
