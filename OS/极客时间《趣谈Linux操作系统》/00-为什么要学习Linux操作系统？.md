# [为什么要学习Linux操作系统？](https://time.geekbang.org/column/article/87104)

无论是从个人的职业发展角度，还是从公司招聘候选人的角度来看，扎实的基础知识是很多人的诉求。

## 打开 Linux 操作系统这扇门，你才是合格的软件工程师

- 2018 年 W3Techs 的数据统计，对于服务器端，Unix-Like OS 占的比例近 70%，其中 Linux 可以称得上是中流砥柱。
- 随着移动互联网的发展，客户端基本上以 Android 和 iOS 为主。Android 是基于 Linux 内核的，因而客户端也进入了 Linux 阵营。
- 那些火得不行的技术，什么云计算、虚拟化、容器、大数据、人工智能，几乎都是基于 Linux 技术的。
- 那些牛得不行的系统，团购、电商、打车、快递，都是部署在服务端，也几乎都是基于 Linux 技术的。

**在编程世界中，Linux 就是主流，不会 Linux 你就会格格不入**。

## 研究 Linux 内核代码，你能学到数据结构与设计模式的落地实践

Linux 最大的优点就是开源。在 Linux 内核里，你会看到：

- 内核调度函数，看内存分配过程。
- 数据结构和算法的经典使用案例。
- 并发情况下的保护这种复杂场景
- 等等...

平时看起来最简单的文件操作，通过阅读 Linux 代码，你能学到从应用层、系统调用层、进程文件操作抽象层、虚拟文件系统层、具体文件系统层、缓存层、设备 I/O 层的完美分层机制，尤其是虚拟文件系统对于接入多种类型文件系统的抽象设计，在很多复杂的系统里面，这个思想都能用得上。

当你写代码的时候，大部分情况下都可以使用现成的数据结构和算法库，但是有些场景对于内存的使用需要限制到很小，对于搜索的时间需要限制到很小的时候，我们需要定制化一些数据结构，这个时候内核里面这些实现就很有参考意义了。

## 了解 Linux 操作系统生态，能让你事半功倍地学会新技术

Linux 是一个生态，里面丰富多彩。很多大牛都是基于 Linux 来开发各种各样的软件。可以这么说，只要你能想象到的技术领域，几乎都能在里面找到 Linux 的身影。

数据库 MySQL、PostgreSQL，消息队列 RabbitMQ、Kafka，大数据 Hadoop、Spark，虚拟化 KVM、Openvswitch，容器 Kubernetes、Docker，这些软件都会默认提供 Linux 下的安装、使用、运维手册，都会默认先适配 Linux。

如果不进入 Linux 世界，你恐怕很难享受到开源软件如此多的红利。

## “趣谈 Linux 操作系统”专栏的时候，我主要秉承两大原则

- 趣谈：通过故事化的方式，将枯燥的基础知识结合某个场景，给你生动、具象地讲述出来，帮你加深理解、巩固记忆、夯实基础。
- 图解：Linux 操作系统中的概念非常多，数据结构也很多，流程也复杂，一般人在学习的过程中很容易迷路。所谓“一图胜千言”，我希望能够通过图的方式，将这些复杂的概念、数据结构、流程表现出来，争取用一张图串起一篇文章的知识点。