# Socket编程

---
## 1 Socket 编程简介

- 白话Socket 就是`插座`，端口就是插座上的孔，端口不能被其他进程占用，抽象理解 Socket 类似于操作某个IP地址上的某个端口达到点对点通信的目的, 需要绑定到某个具体的进程中和端口中。
- Socket 原本代表 Unix 上的原始套接字(RawSocket)，用于描述文件的内存镜像，因为Unix系统设计哲学是`一切都是文件` 所以后来的网络版的进程间通信就被冠名为`文件描述符` file desciptor，很多通讯协议的实现和源代码都能看到 `Socket* _fd` 这样的变量命名也正是因为如此。
- Socket811/1150/1151 是英特尔早期的指令集与硬件高速缓存的架构实现，同样也是基于`一切都是文件`的 Unix 系统设计思想。所以 Socket 也代表电子电气领域的硬件通信标准。

>TCP/IP 是 Socket 的一种实现。但 Socket 并不只有 TCP/IP。

以上摘自[socket编程到底是什么？ - 丁诚昊DCH的回答 - 知乎](
https://www.zhihu.com/question/29637351/answer/318125953)。


在一般的应用开发中，接触的最多是应用层协议 HTTP，HTTP 有很多局限性，比如无法双向通信，在遇到推送、即时通讯的需求时，HTTP 就无法满足了，这时就需要使用 Socket 编程，必要时还需要自定义协议来满足开发需求了。

---
## 2 如何保持连接

Java 中 Socket 编程：

- TCP/IP：`java.net.Socket/java.net.ServerSocket`
- UDP：`java.net.DatagramSocket`

以 TCP/IP 编程为例，步骤为：

1. 创建 ServerSocket 绑定一个端口并监听客户连接。
1. 使用 Socket 绑定 IP 和端口连接服务端。
1. 通过 Socket 获取输入输出流进行通信。

```java
private static final int PORT = 8099;
private static final String HOST = "localhost";

//Server
    mServerSocket = new ServerSocket(PORT);
        while (true) {
            //在tcp三次握手完成后，accept 返回一个用于服务端读写的 socket
            Socket client = mServerSocket.accept();
            handleClient(client);
    }
//Client
 Socket socket = new Socket(HOST, PORT);
```

在一次连接建立后，产生了三个 Socket 实例：

- 客户端的 Socket，`<client_ip:client_port, server_ip:server_port>`
- 服务端的 ServerSocket，`<*.*:**, server_ip:server_port>`
- 服务端的 Socket，`<client_ip:client_port, server_ip:server_port>`

---
## 2 如何保持连接

使用 Socket 的 `setKeepAlive(boolean on)` 启用 SO_KEEPALIVE。SO_KEEPALIVE 保持连接检测对方主机是否崩溃，避免（服务器）永远阻塞于 TCP 连接的输入。设置该选项后，如果2小时内在此套接口的任一方向都没有数据交换，TCP就自动给对方发一个保持存活探测分节(keepalive probe)。这是一个对方必须响应的TCP分节，但这意味着，如果连接断了，应用程序要经过两个小时才会知道。

当 socket 连接不正常，只有我们主动去读、写 socket，我们才能够知道。所以要确定我们的 socket 是保持连接的，必须自己实现心跳连接，客户端不断地给服务端写数据（心跳包），然后读取对方的数据，只要读写正常就说明 socket 是可用的。而写数据的时间间隔则需要根据实际的应用需求来决定。心跳包的内容可以与服务端商榷。

## 引用


- [玉刚说-手把手教你写 Socket 长连接](https://mp.weixin.qq.com/s?__biz=MzIwMTAzMTMxMg==&mid=2649492841&idx=1&sn=751872addc47d2464b8935be17d715d6&chksm=8eec8696b99b0f80b2ebb8e4c346adf177ad206401d83c17aca4047d883b0cc7c0788619df9d&mpshare=1&scene=1&srcid=06294w00jVbaCQYtqnGO04lI#rd)
- [Android微信智能心跳方案](https://mp.weixin.qq.com/s?__biz=MzAwNDY1ODY2OQ==&mid=207243549&idx=1&sn=4ebe4beb8123f1b5ab58810ac8bc5994&mpshare=1&scene=1&srcid=11089nSEwqTcmuPUezPU57Aa#rd)