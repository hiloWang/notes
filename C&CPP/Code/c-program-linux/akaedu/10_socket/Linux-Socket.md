# Linux Socket 编程

socket这个词可以表示很多概念：

- 在TCP/IP协议中，“IP地址+TCP或UDP端口号”唯一标识网络通讯中的一个进程，“IP地址+端口号”就称为socket。
- 在TCP协议中，建立连接的两个进程各自有一个socket来标识，那么这两个socket组成的socket pair就唯一标识一个连接。socket本身有“插座”的意思，因此用来描述网络连接的一对一关系。
- TCP/IP协议最早在BSD UNIX上实现，为TCP/IP协议设计的应用层编程接口称为socket API。

## 预备知识

### 网络字节序

- 内存中的多字节数据相对于内存地址有大端和小端之分。
- 磁盘文件中的多字节数据相对于文件中的偏移地址也有大端小端之分。
- 网络数据流同样有大端小端之分：发送主机通常将发送缓冲区中的数据按内存地址从低到高的顺序发出，接收主机把从网络上接到的字节依次保存在接收缓冲区中，也是按内存地址从低到高的顺序保存，因此，网络数据流的地址应这样规定：先发出的数据是低地址，后发出的数据是高地址。
  
具体参考

- [大端和小端](https://blog.csdn.net/lihao21/article/details/46311027)
- [Linux C编程一站式学习：第 37 章 预备知识](https://akaedu.github.io/book/ch37s01.html#id2902826)

### socket地址的数据类型及相关函数

socket API是一层抽象的网络编程接口，适用于各种底层网络协议，如IPv4、IPv6，以及后面要讲的UNIX Domain Socket。然而，各种网络协议的地址格式并不相同，所以需要通过系统函数各种格式进行转换。

## 基于TCP协议的网络程序

- 基于 TCP 协议的网络程序
- 基于 TCP Socket API 实现简单的服务器和客户端
- 错误处理与读写控制
- 使用 fork 并发处理多个 client 的请求
- setsockopt：允许创建端口号相同但IP地址不同的多个 socket 描述符
- select是网络程序中很常用的一个系统调用，它可以同时监听多个阻塞的文件描述符（例如多个网络连接），哪个有数据到达就处理哪个，这样，不需要fork和多进程就可以实现并发服务的server

具体参考 [Linux C编程一站式学习：第 37 章 基于TCP协议的网络程序](https://akaedu.github.io/book/ch37s02.html)

## 基于 UDP 协议的网络程序

UDP 面向无连接通信。

具体参考 [Linux C编程一站式学习：第 37 章 基于 UDP 协议的网络程序](https://akaedu.github.io/book/ch37s03.html)

## UNIX Domain Socket IPC

socket API原本是为网络通讯设计的，但后来在socket的框架上发展出一种IPC机制，就是UNIX Domain Socket。虽然网络socket也可用于同一台主机的进程间通讯（通过loopback地址127.0.0.1），但是UNIX Domain Socket用于IPC更有效率：不需要经过网络协议栈，不需要打包拆包、计算校验和、维护序号和应答等，只是将应用层数据从一个进程拷贝到另一个进程。这是因为，IPC机制本质上是可靠的通讯，而网络协议是为不可靠的通讯设计的。UNIX Domain Socket也提供面向流和面向数据包两种API接口，类似于TCP和UDP，但是面向消息的UNIX Domain Socket也是可靠的，消息既不会丢失也不会顺序错乱。

UNIX Domain Socket是全双工的，API接口语义丰富，相比其它IPC机制有明显的优越性，目前已成为使用最广泛的IPC机制，比如X Window服务器和GUI程序之间就是通过UNIX Domain Socket通讯的。

具体参考 [Linux C编程一站式学习：第 37 章 UNIX Domain Socket IPC](https://akaedu.github.io/book/ch37s04.html)
