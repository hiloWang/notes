# AOP：AspectJ

---
## 1 AOP

OOP和AOP都是方法论。OOP是把一个个功能封装起来，问题或者功能都被划分到一个一个的模块里边。每个模块专心干自己的事情，模块之间通过设计好的接口交互。各个模块都隐藏内部的实现逻辑，简化了编程操作。

AOP(Aspect Oriented Programming)则是针对某一类问题形成一个切面，把涉及到众多模块的某一类问题进行统一管理。切面模块不会干扰到各个业务逻辑模块，但是又能把各个业务逻辑模块按照自己的切面功能切开来，形成各个细粒度的切入点。在切入点中插入自己的功能逻辑。

总结：面向对象编程提倡的是将功能模块化、对象化，AOP的思想则提倡针**对同一类问题进行统一处理**。

---
## 2 AspectJ

[AspectJ](https://eclipse.org/aspectj/) 是一种基于 Java 平台的面向切面编程的语言。Android 开源库 [hugo](https://github.com/JakeWharton/hugo) 就是使用 AspectJ 开发的。 在 `Spring Framework` 中也有使用 AspectJ 作为 AOP 工具，AspectJ 使用专用的 ajc 编译器对项目进行编译，根据AspectJ 语法（或 Java 语法）在项目源码插入新的代码。

编写 AspectJ 切面有两种方法：

- 完全使用 AspectJ 的语言。
- 使用纯 Java 语言开发，然后使用 AspectJ 注解。

AspectJ 支持的织入方式：

- 源代码织入：利用 ajc 编译器替代 javac 编译器，直接将源文件(java 或者 aspect 文件)编译成 class 文件并将切面织入进代码。
- 字节码织入：目标类可能已经被打成一个 jar 或已经是 class 了，此时也可以使用 ajc 命令指向织入。
- 加载时织入：
  - 不使用 ajc 编译器，利用 aspectjweaver.jar 工具，使用 java agent 代理在类加载期将切面织入进代码。
  - JVM 启动时使用参数替换默认的系统类加载器，(AppClassLoader -> WeavingURLClassLoader)，`DJava.systemloader=org.weaver.loadtime.WeavingURLCLassLoader`。
  - 自定义类加载器

---
## 3 Android 中使用 AspectJ

>运行期的注入一般是依赖 JVM 提供的 AttachAPI，因为 Android 的 JVM 是经过深度定制的，实际上 class 还会继续转换成 dex，因此 AspectJ 在 Android 平台是只能做到编译期注入。

在 Android 中使用 Aspectj 需要使用专门的 ajc 编译器，要在 class 转换为 dex 之前，使用 ajc 做一次注入操作，我们可以在 gradle 中加入 ajc 编译过程，此时需要自定义 gradle 插件，当然也可以使用一些开源的插件，比如 [gradle_plugin_android_aspectjx](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)

---
## 4 Ajc编译器参数

- `-inPath`：将被编织的的类或 jar 文件，这个参数将决定哪些类被 aspject 切入，输出将包含这些类，路径是一个参数包含一个 zip 文件或目录的路径列表，由平台特定的路径分隔符分隔
- `-d`：指定输出目录
- `-bootclasspath`：覆盖 VM 的引导类路径的位置，以便在编译时评估类型
- `-aspectPath`：将 jar 文件和目录中的二进制方法从路径整理到所有源文件中
- `-classpath`：指定在哪里可以找到用户类文件

可以看到，其中最重要的参数为：`-inPath`

---
## 5 AspectJ应用

权限、检测登录注册，性能统计，埋点统计等。

---
## 引用

文档：

- [aspectj user guide](https://www.eclipse.org/aspectj/doc/released/progguide/index.html)
- [ajc-ref](http://www.eclipse.org/aspectj/doc/next/devguide/ajc-ref.html)

博客：

- [关于AspectJ你可能不知道的事](https://juejin.im/entry/5a40abb16fb9a0451e400886)
- [看AspectJ在Android中的强势插入](http://www.jianshu.com/p/5c9f1e8894ec)
- [跟我学aspectj](http://blog.csdn.net/zl3450341/article/category/1169602)
- [深入理解AndroidA之OP](http://blog.csdn.net/innost/article/details/49387395)
- [AspectJ编程学习笔记](https://blog.gmem.cc/aspectj-study-note)
- [Aspect Oriented Programming in Android](https://fernandocejas.com/2014/08/03/aspect-oriented-programming-in-android/)（文件使用的方法已经不推荐）
- [android面向切面AOP编程精华总结](https://blog.csdn.net/qq_25943493/article/details/52524573)
- [AspectJ in Android （三），AspectJ 两种用法以及常见问题](http://johnnyshieh.me/posts/aspectj-in-android-usage/)

android gradle 插件

- [gradle_plugin_android_aspectjx](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)
  - 不支持 .aj
  - 支持增量编译
  - 按类路径进行过滤
- [tomato](https://github.com/PachyrhizusRen/tomato)
  - 支持 .aj 文件
  - 支持增量编译
  - 按 jar 进行过滤
- [android-gradle-aspectj](https://github.com/Archinamon/android-gradle-aspectj)
  - 支持 .aj 文件
  - 不支持增量编译
  - AS3 支持不够好
- [one-android-gradle-aspectj](https://github.com/thunderheadone/one-android-gradle-aspectj)
  - 支持 .aj 文件
  - 不支持增量编译
  - 支持 AndroidStudio 3x
