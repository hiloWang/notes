# Android AOP 编程

在 OOP 的实践中，发现有些问题是 OOP 无法解决的，OOP 强调的是封装、继承。但是有一类代码不属于特定的某个模块而又要参与到每个模块当中，这类代码不影响核心业务流程，但是在项目架构中又是必须的，比如日志、统计、性能监控、安全能代码。使用 AOP 可以把这些烦人的代码划分为切面模块，相对于纵向的业务流程来讲，它们横切入业务流程的各个流程之中，而又不影响具体的业务(业务代码对此毫无感知)，这就是 AOP 编程思想。

## 1 AOP 可选方案

在 Android 平台上，实施 AOP 编程的可选方案：

动态（运行时） AOP：

- Dexposed，Xposed等

静态（编译时） AOP：

- aspactJ
- javassist
- asm
- apt、ast(抽象语法树)

受限于 Android 平台的虚拟机实现，实施动态（运行时） AOP 是比较困难的，往往需要获取 root 权限，而且系统版本碎片化也比较严重，所以静态（编译时） AOP 是相对靠谱的选择。

## 2 静态（编译时） AOP 入口

Android 平台的虚拟机运行的是 dex 文件，dex 文件的生成需要下面两个步骤：

- `.java -> .class/.jar`
- `.class/.jar -> .dex`

可以在 `.class/.jar` 转换为 `.dex` 之前对字节码进行修改，插入切面代码，从而实现 AOP。

可选方案：

- Android Gradle Plugin 1.5 之前可能需要特定的 hook 手段，比如修改 dex 等构建工具 或 java 的 Instumentation 机制。（不过现在已不需要再考虑这种情况）
- Android Gradle Plugin 1.5 之后提供了 Transform API，我们可以利用 Transform API 对转换为 dex 之前的 class 做一些处理，比如插入/替换一些代码，基于这种方式，可供选择的字节码编辑库有：aspactJ、javassist、asm 等。这种方式的好处在于可以做到对业务代码完全无侵入。
- 使用 APT，使用 APT 可以生成一些模板代码，但是不能修改已有源码。
- 使用 AST，利用 AST 抽象语法树 API 可以修改源码。

## 3 AOP 应用

- 无痕埋点
- 敏感代码安全检测
- assert 替换
- 模板代码插入
- ...

## 4 AOP 相关学习资料

笔记：

- [Java-APT](../../Java/01-Java-Basic/注解02-APT.md)
- [Java-AST](../../Java/01-Java-Basic/注解03-AST.md)
- [AspectJ-01](../../Java/02-Advance-Java/AspectJ-01.md)
- [AspectJ-02](../../Java/02-Advance-Java/AspectJ-02.md)
- [ASM 入门](../../Java/02-Advance-Java/ASM入门.md)
- [Javassist 入门](../../Java/02-Advance-Java/Javassist入门.md)

AOP 概览：

- [安卓AOP三剑客:APT,AspectJ,Javassist](https://www.jianshu.com/p/dca3e2c8608a)
- [Android AOP编程的四种策略探讨：Aspectj，cglib+dexmaker，Javassist，epic+dexposed](https://blog.csdn.net/weelyy/article/details/78987087)

AOP 系列博客：

- [一文读懂 AOP | 你想要的最全面 AOP 方法探讨](https://www.jianshu.com/p/0799aa19ada1)
- [一文应用 AOP | 最全选型考量 + 边剖析经典开源库边实践，美滋滋](https://www.jianshu.com/p/42ce95450adb)
- [会用就行了？你知道 AOP 框架的原理吗？](https://www.jianshu.com/p/cfa16f4cf375)
- [AOP 最后一块拼图 | AST 抽象语法树 —— 最轻量级的AOP方法](https://juejin.im/post/5c45bce5f265da612c5e2d3f)

AST：

- [安卓AOP之AST:抽象语法树](https://www.jianshu.com/p/5514cf705666)
- [基于AST的组件化自动插桩方案](https://www.jianshu.com/p/a827a95fde17)

字节码:

- [一起玩转Android项目中的字节码](https://juejin.im/entry/5c0cc7c15188257d5e39647d)，基于 ASM
- [字节码插桩--你也可以轻松掌握](https://juejin.im/entry/5c886d786fb9a049f1550d65)，基于 ASM
- [Android动态编译技术 Plugin Transform Javassist操作Class文件](https://www.jianshu.com/p/a6be7cdcfc65)，基于 Javassist

无痕埋点：

- [Android AOP之字节码插桩](https://www.jianshu.com/p/c202853059b4)
- [应用于Android无埋点的Gradle插件解析](https://github.com/nailperry-zd/LazierTracker/wiki/%E5%BA%94%E7%94%A8%E4%BA%8EAndroid%E6%97%A0%E5%9F%8B%E7%82%B9%E7%9A%84Gradle%E6%8F%92%E4%BB%B6%E8%A7%A3%E6%9E%90)，基于 ASM

库：

- [capt 全称 Class Annotation Processor Tool，是作者基于 ASM 和 Android Transform API 打造的 Android 平台的字节码的注解处理工具。](https://mp.weixin.qq.com/s/8_88oUB2MJi27BJJOb-2_Q)
- [sdk-editor-plugin](https://github.com/iwhys/sdk-editor-plugin)
- [DroidAssist 是一个轻量级的 Android 字节码编辑插件，基于 Javassist 对字节码操作，根据 xml 配置处理 class 文件，以达到对 class 文件进行动态修改的效果](https://github.com/didi/DroidAssist)

运行时 AOP：

- [epic](https://github.com/tiann/epic)

## 5 相关学习路径

技术选型：

1. 动态 AOP
2. 静态 AOP

Android 静态 AOP 学习路径：

1. [Gradle-TransformAPI](../../Gradle/Android-TransformAPI.md)
2. [Java-APT](../../Java/01-Java-Basic/注解02-APT.md)
3. [Java-AST](../../Java/01-Java-Basic/注解03-AST.md)
4. AspectJ
5. Javassist
6. ASM
