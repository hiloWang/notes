
---
## 1 走进Java

Java不仅仅是一门编程语言，还是由一系列计算机软件和规范形成的**技术体系**，这个体系提供了完整的用于软件开发和跨平台部署的支持环境。

Java技术系统包括：

- Java程序设计语言
- 各种硬件平台上的java虚拟机实现
- Class文件格式
- Java API类库
- 来自商业机构和开源社区的第三方类库

Java程序设计语言、java虚拟机、Java API类库统称为**JDK**，Java SE API子集+java虚拟机这两部分统称为**JRE**


---
## 2 Java技术体系可以分为四个平台

- Java Card 用于支持一些Java小程序
- Java ME(Micro Edition)
- Java SM(Standard Edition)
- Java EE(Enterprise Edition)


---
## 3 Java虚拟机发展

虚拟机的实现由《Java虚拟机规范》指导。除了官方虚拟机外，其他组织和公司也研发了Java虚拟机实现，比如BEA和IBM公司。

- Sun Classic/Exact VM 第一款商用虚拟机
- **Sun HotSpot VM** 使用最广泛的虚拟机，也是Sun JDK和Open JDK中带的虚拟机
- BEA JRockit/IBM J9 Vm


---
## 4 64位虚拟机

Java程序运行在64位虚拟机上需要付出较大的代价，首先是内存问题，由于指针膨胀和各种数据类型对齐补白的原因，运行于64位系统上的Java应用需要消耗更多的内存，通讯需要比32位系统额外增加10%-30%的内存消耗；其次性能上不如32位虚拟机。

---
## 5 编译JDK

编译 Open JDK

