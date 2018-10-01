# Android AOP 编程

在 OOP 的实践中，发现有些问题是 OOP 无法解决的，OOP 强调的是封装、继承。但是有一类代码不属于特定的某个模块而又要参与到每个模块当中，这类代码不影响核心业务流程，但是在项目架构中又是必须的，比如日志、统计、性能监控、安全能代码。使用 AOP 可以把这些烦人的代码划分为切面模块，相对于纵向的业务流程来讲，它们横切入业务流程的各个流程之中，而又不影响具体的业务(业务代码对此毫无感知)，这就是 AOP 编程思想。

---
##  AOP 可选方案

在 Android 平台上，实施 AOP 编程的可选方案：

动态（运行时） AOP

- Dexposed，Xposed等

静态（编译时） AOP

- aspactJ
- javassist
- asm
- apt、ast(抽象语法树)

受限于 Android 平台的虚拟机实现，实施动态（运行时） AOP 是比较困难的，往往需要获取 root 权限，而且系统版本碎片化也比较严重，所以静态（编译时） AOP 是相对靠谱的选择。

---
## 静态（编译时） AOP 入口

Android 平台的虚拟机运行的是 dex 文件，dex 文件的生成需要下面两个步骤：

- .java -> .class/.jar
- .class/.jar -> .dex

所以最好的时间是在.class/.jar 转换为 .dex 之前对字节码进行修改，从而实现AOP代码插入。

- Android Gradle Plugin 1.5 之后的 Transform API
- Android Gradle Plugin 1.5 之前可能需要特定的hook手段，比如修改 dex等构建工具 和 java 的 Instumentation 机制
- 利用 ast 抽象语法树 API 修改源码只需要 apt 插件

---
## AOP 应用

- 代码埋点
  - [Android AOP之字节码插桩](https://www.jianshu.com/p/c202853059b4)
  - Google 搜索 **Android无埋点数据收集SDK** 相关技术
- 敏感代码安全检测
- assert 替换