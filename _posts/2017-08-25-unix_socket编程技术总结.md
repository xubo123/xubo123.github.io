---
layout:     post
title:      "Socket编程总结"
subtitle:   "socket通信"
date:       2017-08-25 11:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - 技术总结（Socket）
---

## Unix Socket编程技术总结

### 网络ipc:套接字

套接字是通信端点的抽象，访问套接字也需要用套接字描述符。

![socketCoding](/img/socketCoding.png)

#### 套接字的创建：

int socket(int domain,int type,int protocol);//成功返回套接字描述符，失败返回－1

* 参数domain确定通信特性，包括地址格式，特性包括（1.AF_INET,IPv4; 2.AF_INET6,IPv6; 3.AF_UNIX,UNIX域； 4.AF_UNSPEC,未指定）AF表示address family地址族。
* 参数type确定套接字的类型，进一步确定通信特征,包括:
  
  1.SOCK_DGRAM:长度固定的无连接的不可靠报文传输
  2.SOCK_RAW:IP协议的数据报接口（需要自己构造协议首部，会直接访问网络层，绕过TCP和UDP封装，所以需要超级用户权限来创建）
  3.SOCK_SEQPACKET：长度固定，有序，可靠的面向连接报文传递(基于数据报文)
  4.SOCK_STREAM:有序，可靠，双向连接字节流（基于数据字节流）
  
 
* 参数protocol通常是零，表示按给定的域和套接字类型选择默认协议。在AF_INET通信域中套接字类型为SOCK_STREAM的默认协议是TCP,在AF_INET通信域中套接字类型为SOCK_DGRAM的默认协议是UDP.

操作套接字与操作文件一致，大多数处理文件描述符的函数都可以用于处理套接字对象

套接字通信是双向的：利用shutdown函数可以禁止套接字上的输入和输出

shutdown（int sockfd,int how）//sockfd标示套接字端口，how表示关闭读还是关闭写还是关闭读写（SHUT_RD,SHUT_WR,SHUT_RDWR）

close套接字是当最后一个活动应用被关闭时才释放节点，shutdown则不管引用该套接字的对象数目多少，直接关闭套接字端口，有时使用shutdown只关闭其中的一个端口会方便一点

#### 套接字寻址

套接字寻址则确定了如何确定一个目标通信进程，进程的标示由两个部分：计算机的网络地址确定网络上哪一台计算机，服务则标示计算机上特定的进程

字节序：比如像整数这种大数据类型，4个字节中的内部顺序，该整数对外有一个字节地址cp[3],内部字节之间也有一个序号排列（从左到右由高到低排列）。大端字节序：最大字节地址（cp[3]）指向数字最低有效字节也就是数字最右端上，小端字节序正好相反：最小字节地址cp[0]指向数字最高有效字节即最左端。

地址格式：为了使不同格式的地址传入到套接字函数，地址被强制转换成通用的地址结构sockaddr

{%highlight c%}
struct sockaddr{
sa_family_t sa_family;/*address family*/
char sa_data[];/*可变长度的地址*/
}
{%endhighlight%}

在IPv4因特尔域（AF_INET）中套接字地址用

{%highlight c%}
struct sockaddr_in{
sa_family_t sin_family;
in_port_t sin_port;/*uint16_t*/
struct in_addr sin_addr;/*uint32_t*/
}
{%endhighlight%}

为了将地址转换成人可以理解的格式，可以使用inet_addr,inet_ntoa,inet_ntop都可以用于地址将二进制地址格式转换成点分十进制字符串表示（a.b.c.d）

#### 地址寻址

有很多函数可以通过sockaddr获取指定地址的各种信息，读取主机信息和服务信息：

{%highlight c%}
struct hostent *gethostent(void)//获取主机信息
struct protoent *getprotobyname(const char *name)//获取协议名字和协议号
struct servent *getservbyname(const char *name,const char *proto);//获取端口号对应的服务名
int getaddrinfo(const char *restrict host,const char *restrict service,const struct addrinfo *restrict hint,struct addrinfo **restrict res)//根据主机名和服务名获取地址信息
int getnameinfo(...)//通过地址获取对应的主机名和服务名
{%endhighlight%}

#### 将套接字与地址绑定
客户端可以通过该地址发送请求，利用指定套接字进行与特定服务的通信，所以我们需要给一个接收客户端请求的套接字绑定一个地址，利用bind函数：

{%highlight c%}
int bind(int socktfd,const struct sockaddr *addr,socklen_t len);
{%endhighlight%}

可以调用getsockname来获取绑定到一个套接字的地址

{%highlight c%}
int getsockname (int sockfd,struct sockaddr *restrict addr,socklen_t *restrict alenp)
{%endhighlight%}

若该套接字具有连接，则可以调用getpeername()来找到对方地址:
{%highlight c%}
int getpeername (int sockfd,struct sockaddr *restrict addr,socklen_t *restrict alenp)
{%endhighlight%}

#### 建立连接
如果处理的是面向连接的网络服务(SOCK_SREAM和SOCK_SEQPACKET)开始交换数据以前需要在请求服务的进程套接字和提供服务的进程套接字中建立连接：

{%highlight c%}
int connect(int sockfd,const struct sockaddr *addr,socklen_t len);
{%endhighlight%}
该函数会将指定sockfd端口与addr指定的服务器建立一个连接。当sockfd没有绑定一个地址的时候，connect会给调用者绑定一个默认地址

连接成功建立之后，服务器端会调用listen来宣告可以接受连接请求：
{%highlight c%}
int listen(int sockfd,int backlog)//backlog提供一个提示用于表示进程所要入队的连接请求数量。当队列满时系统会拒绝多余的连接请求。
{%endhighlight%}

当服务器调用listen后表明可以接受连接请求，再使用accept获得连接请求并建立连接

{%highlight c%}
int accept(int sockfd,struct sockaddr *restrict addr,socklen_t *restrict len);
{%endhighlight%}

参数sockfd为服务器端接受连接的套接字端口，addr在返回时会填充发出响应客户端的地址，len则返回地址的长度，也就是说服务器端的原sockfd只负责监听连接请求，当受到连接请求后会调用accept来获取一个新的sockfd来与服务器端进行数据接收传输，所有调用accept之后会返回一个新的sockfd文件描述符，用于接收客户端的服务请求，此时原sockfd会去监听并接收其他连接请求。

当accept当前没有连接请求时，服务器会阻塞直到一个请求的到来。另外可以使用poll或select来等待一个请求的到来

#### 数据传输
当accept已经建立好用于数据传输的连接后就可以通过调用read和write来通过套接字通信了。但这只能简单从两个套接字中交换数据，如果想要从多个客户端接收数据包或发送数据包，则需要采用六个传递数据的套接字函数，三个用于发送数据，三个用于接收数据。

##### 三个发送函数如下

1.最简单的是send函数：

ssize_t send(int sockfd,const void *buf,size_t nbytes,int flags);

使用send时套接字必须已经连接，参数buf和nbytes与write中的含义一致，主要第四个参数flags用于改变处理数据的方式。

flags有：

* MSG_DONTROUTE:勿将数据路由出本地网络
* MSG_DONTWAIT:允许非阻塞操作
* MSG_EOR:如果协议支持，此为记录结束
* MSG_OOB:如果协议支持，发送带外数据（见之后详解）

send成功返回不能保证目的端已经成功接收数据，只能保证将数据成功发送到网络

2.第二个传输函数为sendto函数，与send类似，但是sendto允许在无连接的套接字上指定一个目标地址，在对于面向连接的套接字目标地址可以忽略，而对于无连接的套接字不能使用send，需要使用sendto然后指定目标地址：

ssize_t send_to(int sockfd,const void *buf,size_t nbytes,int flags,const struct sockaddr *destaddr,socklen_t destlen);

3.第三个传输函数就是调用带有msghdr结构的sendmsg来指定多重缓冲区传输数据，这和writev很像：

ssize_t sendmsg(int sockfd,const struct msghdr *msg,int flags);

其中msghdr结构定义如下：

{% highlight c%}
struct msghdr{
  void *msg_name;//可选地址项
  socklen_t msg_namelen;//地址长度
  struct iovec *msg_iov;//传输数据缓冲区
  int msg_iovlen;//缓存数据长度
  void *msg_control;//辅助数据，数据副本
  socklen_t msg_controllen;//数据副本长度
  int msg_flags;//接收消息的标志位
}
{% endhighlight %}

##### 接收数据的三个函数：

1.recv函数

ssize_t recv(int sockfd,void *buf,size_t nbytes,int flags);

该函数同read很像，区别也是在于第四个标示位flags

flags有：

* MSG_OOB:如果协议支持，接收带外数据
* MSG_PEEK:返回报文内容而不真正取走报文,可以查看下一个要读的数据但不会真正取走，当再次调用recv的时候会返回刚才查看的数据
* MSG_TRUNC:即使报文被截断，要求返回的是报文的实际长度
* MSG_WAITALL:等到直到所有的数据可用，这是针对SOCK_STREAM，只有当所有字节流数据接收完毕后recv才会返回

2.若需要定位发送者，使用recvfrom()来得到数据发送者的源地址：

ssize_t recvfrom(int sockfd,void *restrict buf,size_t len,int flags,struct sockaddr *restrict addr,socklen_t *restrict addrlen);

如果addr非空，则包含数据发送者的套接字端点地址，addrlen将会返回该地址的实际字节大小，由于可以获得发送者的地址，所以recvfrom通常用于无连接套接字

3.为了将接收到的数据送入到多个缓冲区，或想接收辅助数据，可以使用recvmsg:
ssize_t recvmsg (int sockfd,struct msghdr *msg,int flags);

flags:

* MSG_OOB:接收带外数据
* MSG_CTRUNC:控制数据被截断
* MSG_EOR:接收到记录结束符
* MSG_DONTWAIT:recvmsg处于非阻塞模式
* MSG_TRUNC:一般数据被截断

#### 套接字选项
套接字机制提供两个套接字选项接口来控制套接字行为，一个借口用来设置选项，一个用于查询一个选项的状态。

int setsockopt 和 int getsockopt

#### 带外数据
带外数据就是紧急数据，优先级较高的数据，支持带外数据传输说明说明支持数据传输的优先级，TCP支持一个字节的紧急数据

如果安排发送套接字信号，当接收到紧急数据时，则发送信号SIGURG，使用fcntl()使用F_SETOWN命令来设置一个套接字的所有权

fcntl(sockfd,F_SETOWN,pid);//pid为正值指定一个进程id，若pid为负值，指定一个进程组id

获取一个套接字的所有权函数：

owner = fcntl(sockfd,F_GETOWN,0);//owner为正为进程id，为负为进程组id

#### 非阻塞和异步IO

recv函数没有数据接收的时候会阻塞等待，send发送消息时遇到输出队列满了之后会阻塞。但是在非阻塞模式下的行为会发生改变，这些函数不会阻塞而是失败，这时可以使用poll或select来判断何时能接受数据。

异步 I/O：当能够从套接字的异步I/O读取数据，或套接字写队列中的空间可用时，可以安排发送信号SIGIO。通过两个步骤使用异步I/O：

（1）.建立套接字拥有者关系（fcntl,ioctl）
（2）.通知套接字当I/O操作不会阻塞时发信号通知









