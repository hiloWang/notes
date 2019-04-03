# 网络

---
## 1 网络协议

- **协议**：一系列相关协议的集合称为一个协议族的集合称为一个协议族，指定一个协议族的各种协议之间的相互关系并划分需要完成的任务的设计，称为协议族的体系结构或者参考模型。TCP/IP 是一个实现 Internet 体系结构的协议族。其来源于 ARPANET 参考模型。
- **应用程序编程接口**：无论是 P2P 或是客户机/服务器，都需要表述其所需的网络操作(比如建立一个连接，写入或读取数据)，这通常由主机操作系统使用一个网络应用程序编程接口(API)来实现，最流行的编程接口被称为套接字( Socket )或者 Berkeley 套接字，它最初由 LJFK93 开发。可以说 TCP/IP 是规范。而 Socket 是其在编程上的实现。

---
## 2 学习资料

### HTTP

- [IETF 文档](https://tools.ietf.org/html/)
- [HTTP / HTTP 2 专题](https://imququ.com/post/series.html)
- [实用 HTTP 专题](https://www.cnblogs.com/plokmju/tag/http/)
- [MDN-HTTP 专题](https://developer.mozilla.org/zh-CN/docs/Web/HTTP)
- [简单聊聊 GZIP 的压缩原理与日常应用](https://juejin.im/post/5b793126f265da43351d5125)

###  思科 PacketTracer 教程

- [CCNA图文笔记](https://www.qingsword.com/sitemap.html#ccna)
- [各版本下载](https://www.computernetworkingnotes.com/ccna-study-guide/download-packet-tracer-for-windows-and-linux.html)

### Books

- 《图解HTTP》
- 《图解TCP/IP》
- 《图解加密技术》
- 《TCP/IP 详解》
- 《Http 权威指南》
- 《Https 权威指南》
-  [《openssl攻略》](http://www.ituring.com.cn/book/download/338e1e55-fd94-4ac3-9e21-e0bf04984b3f)
- 《深入浅出 Https》

### RESTful

- [深入理解 RESTful Api 架构](http://mengkang.net/620.html)
- [你真的了解理解 RESTful吗？](http://mp.weixin.qq.com/s?__biz=MzA4MjU5NTY0NA==&mid=400698271&idx=1&sn=fdf440bb68dbbca6f422c5b82d0af25d&scene=23&srcid=1123oNzjlQD4AHMT8BLQrL8c#rd)
- [RESTful API 设计指南](http://www.ruanyifeng.com/blog/2014/05/restful_api.html)

---
## 3 笔记

### 网络基础

- [网络基础-概述](01-Basic/网络基础-概述.md)

### HTTP

- [HTTP 概述](01-Basic/HTTP_01_概述.md)
- [HTTP Content encoding 和 Transfer Encoding](01-Basic/HTTP_02_Content_encoding_Transfer_Encoding.md)
- [HTTP 范围请求](01-Basic/HTTP_03_范围请求.md)
- [HTTP URL&URI](01-Basic/HTTP_04_URL&URI.md)
- [HTTP 缓存](01-Basic/HTTP_05_缓存.md)
- [HTTP 客户端识别与 Cookie](01-Basic/HTTP_06_客户端识别与Cookie.md)

### HTTPS

- [《HTTPS权威指南》-TLS/SSL](01-Basic/HTTPS权威指南01-SSL&TLS.md)
- [《HTTPS权威指南》-密码学简介](01-Basic/HTTPS权威指南02-密码学简介.md)
- [《HTTPS权威指南》-TLS 协议](01-Basic/HTTPS权威指南03-TLS协议.md)
- [《HTTPS权威指南》-PKI、证书](01-Basic/HTTPS权威指南04-PKI.md)
- [《HTTPS权威指南》-OpenSSL&Keytool](01-Basic/HTTPS权威指南-OpenSSL&Keytool.md)

### 图解密码技术

- [《图解密码技术》第一部分](02-Encryption/图解密码技术-part01.md)