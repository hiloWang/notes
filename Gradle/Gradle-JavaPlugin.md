# The Java Plugin

掌握 Gradle 构建机制，能帮助我们配置出更高效的构建脚本，同时在遇到构建错误时能快速定位和解决问题。

翻译自[The Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html#sec:java_usage)。


## CompileJava

Java Plugin 为项目中的每一个 source set 都添加了一个 JavaCompile 对象，最常见的配置如下所示：

### Compile properties

- 文件集合
    - classpath
    - 默认值：`sourceSet.compileClasspath`
- 文件树源：
    - source
    - 默认值: `sourceSet.java`，可以设置使用 [Understanding implicit conversion to file collections](https://docs.gradle.org/current/userguide/working_with_files.html#sec:specifying_multiple_files) 中描述的任何内容 
- 文件目标目录
    - 默认值: `sourceSet.java.outputDir`

默认情况下，Java 编译器运行在 gradle 进程中，设置 options.fork 为 true 可以使用编译运行在另一个的进程，在 Ant javac 任务情况下， 这意味着每一个编译任务都会 fork 出一个新的进程，这回导致编译速度减慢，反过来，Gradle 继承编译器会尽可能的重用编译器进程，在这两种情况下，所有设置的 fork 选型就会被兑现。

### Incremental Java compilation

Gradle 附带了一个复杂的增量 Java 编译器，默认情况下处于激活状态。这给你带来了以下好处：

- 增量编译更快
- 尽可能少的被修改类文件，那些类不需要被重新编译而是保留在输出目录中，这在 JRebel 中非常有用，被修改的输出类越少，JVM 可以越快地使用刷新的类。

为了帮助你理解增量编译的工作，下面提供了高级概述

- Gradle 会重新编译所有被修改影响的类。
- 如果一个类被修改了或者它的依赖被修改了，那么这个类就是被影响的类，不管这个依赖的类定义在相同的 project 中，还是其他的 project 中，甚至是在扩展的类库中。
- 一个类的依赖由其字节码中的类型引用所决定。
- 因为常量可以被内联，任何常量的改变都会导致 Gradle 重新编译所有源文件，你应该在可以的地方使用静态方法来代替常量以减少常量的使用。
- 因为 source-retention 的注解对字节码不可见，对 source-retention 的注解的改变会导致所有源被重新编译。
- 你可以通过应用像“低耦合”这样的优秀的软件设计原则来优化增量编译的性能，对于对象，如果在一个具体类和它的依赖之间设置一个接口层，那么被依赖的类只会在接口改变的情况下被重新编译，而实现类的改变不会会导致其重新编译。
- 类分析被缓存在项目目录中，所以在 clean 之后的第一次编译会比较慢，考虑在服务器上关闭增量编译。
