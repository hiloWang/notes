# AOP：AspectJ

---
## 1 AOP

OOP和AOP都是方法论。OOP是把一个个功能封装起来，问题或者功能都被划分到一个一个的模块里边。每个模块专心干自己的事情，模块之间通过设计好的接口交互。各个模块都隐藏内部的实现逻辑，简化了编程操作。

AOP(Aspect Oriented Programming)则是针对某一类问题形成一个切面，把涉及到众多模块的某一类问题进行统一管理。切面模块不会干扰到各个业务逻辑模块，但是又能把各个业务逻辑模块按照自己的切面功能切开来，形成各个细粒度的切入点。在切入点中插入自己的功能逻辑。

总结：面向对象编程提倡的是将功能模块化、对象化，AOP的思想则提倡针**对同一类问题进行统一处理。**

---
## 2 AspectJ

[AspectJ](https://eclipse.org/aspectj/)是一种基于Java平台的面向切面编程的语言。[hugo](https://github.com/JakeWharton/hugo)就是使用AspectJ开发的。 在`Spring Framework`中也是用了AspectJ作为AOP工具，AspectJ使用专用的ajc编译器对项目进行编译，根据AspectJ语法在项目源码插入新的代码。

使用AspectJ有两种方法：

- 完全使用AspectJ的语言。
- 使用纯Java语言开发，然后使用AspectJ注解

不论使用哪种方法，最后都是使用AspectJ的编译工具ajc来编译AspectJ语言或者Java代码。AspectJ编译器能识别任何普通的Java代码，可以使用ajc编译.java文件

AspectJ支持的织入方式

- 源代码织入：织入器作为编译器的一部分，处理源代码(在.java编译为.class的时候，进行代码注入)，支持经典语法和注解语法。生成的字节码符合JVM规范，需要使用ajc代替javac
- 字节码织入：传递给织入器的是字节码。使用这种方式时，包含编译普通Java类、编译切面，织入3个步骤。
- 加载时织入：传递给织入器的是Java类字节码、切面类，以及aop.xml配置文件。


---
## 3 Android中使用AspectJ

Aspectj需要使用专门的ajc编译器，所以要在gradle中加入ajc编译，当然，也可以使用沪江开源的[gradle_plugin_android_aspectjx](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)插件

---
## 4 Ajc编译器参数

- `-inPath`：将被编织的的类或jar文件，这个参数将决定哪些类被aspject切入，输出将包含这些类，路径是一个参数包含一个zip文件或目录的路径列表,由平台特定的路径分隔符分隔
- `-d`：指定输出目录
- `-bootclasspath`：覆盖VM的引导类路径的位置，以便在编译时评估类型。
- `-aspectPath`：将jar文件和目录中的二进制方法从路径整理到所有源文件中
- `-classpath`：指定在哪里可以找到用户类文件


可以看到，其中最重要的参数为：`-inPath`

---
## 5 AspectJ应用

权限、检测登录注册，性能统计，埋点统计等。


---
## 引用

文档：

- [user guide](https://www.eclipse.org/aspectj/doc/released/progguide/index.html)
- [ajc-ref](http://www.eclipse.org/aspectj/doc/next/devguide/ajc-ref.html)

博客：


- [看AspectJ在Android中的强势插入](http://www.jianshu.com/p/5c9f1e8894ec)
- [跟我学aspectj](http://blog.csdn.net/zl3450341/article/category/1169602)
- [深入理解AndroidAOP](http://blog.csdn.net/innost/article/details/49387395)
- [AspectJ编程学习笔记](https://blog.gmem.cc/aspectj-study-note)
- [Aspect Oriented Programming in Android](https://fernandocejas.com/2014/08/03/aspect-oriented-programming-in-android/)
- [android面向切面AOP编程精华总结](https://blog.csdn.net/qq_25943493/article/details/52524573)
- [AspectJ in Android （三），AspectJ 两种用法以及常见问题](http://johnnyshieh.me/posts/aspectj-in-android-usage/)