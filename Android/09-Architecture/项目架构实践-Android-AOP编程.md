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

可选方案：

- Android Gradle Plugin 1.5 之前可能需要特定的 hook 手段，比如修改 dex等构建工具 和 java 的 Instumentation 机制。（不过现在已不需要再考虑这种情况）
- Android Gradle Plugin 1.5 之后提供了 Transform API，我们可以利用 Transform API 对转换未 dex 之前的 class 做一些处理，比如插入/替换一些代码，基于这种方式，可供选择的字节码编辑库有：aspactJ、javassist、asm。这种方式的好处在于可以做到对业务代码完全无侵入。
- 利用 APT，单词地使用 APT 只能生成代码而不能修改已有源码，但可以利用 ast 抽象语法树 API 修改源码。

---
## AOP 应用

- 无痕埋点，相关资料参考 [Android AOP之字节码插桩](https://www.jianshu.com/p/c202853059b4) 或自行 Google 搜索 **Android无埋点数据收集SDK** 相关技术
- 敏感代码安全检测
- assert 替换
- 模板代码插入
- ...

---
## AOP 相关学习资料

博客：

- [AOP 最后一块拼图 | AST 抽象语法树 —— 最轻量级的AOP方法](https://juejin.im/post/5c45bce5f265da612c5e2d3f)
- [一文读懂 AOP | 你想要的最全面 AOP 方法探讨](https://www.jianshu.com/p/0799aa19ada1)
- [一文应用 AOP | 最全选型考量 + 边剖析经典开源库边实践，美滋滋](https://www.jianshu.com/p/42ce95450adb)
- [会用就行了？你知道 AOP 框架的原理吗？](https://www.jianshu.com/p/cfa16f4cf375)
- [一起玩转Android项目中的字节码](https://juejin.im/entry/5c0cc7c15188257d5e39647d)
- [Android AOP编程的四种策略探讨：Aspectj，cglib+dexmaker，Javassist，epic+dexposed](https://www.jianshu.com/p/524dbfc6a4e1)
- [字节码插桩](https://juejin.im/entry/5c886d786fb9a049f1550d65)
- [java注解处理器——在编译期修改语法树](https://blog.csdn.net/a_zhenzhen/article/details/86065063)

笔记：

- [Java-APT](../../Java/01-Java-Basic/注解02-APT.md)
- [Java-AST](../../Java/01-Java-Basic/注解03-AST.md)

库：

- [capt 全称 Class Annotation Processor Tool，是作者基于 ASM 和 Android Transform API 打造的 Android 平台的字节码的注解处理工具。](https://mp.weixin.qq.com/s/8_88oUB2MJi27BJJOb-2_Q)
- [sdk-editor-plugin](https://github.com/iwhys/sdk-editor-plugin)
