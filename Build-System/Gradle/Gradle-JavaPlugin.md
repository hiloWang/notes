# The Java Plugin

掌握 Gradle 构建机制，能帮助我们配置出更高效的构建脚本，同时在遇到构建错误时能快速定位和解决问题。

>翻译自[The Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html#sec:java_usage)。

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

### Known issues

- 如果由于编译错误导致了一个编译任务失败，那么在调用下一次编译任务时将会执行全量编译。
- 如果你使用了一个要读取资源配置文件的注解处理器，你需要生命这些资源作为编译任务的输入。
- 如果资源文件发生改变，将会触发一个全量编译。

### Incremental annotation processing

从 Gradle4.7 开始，增量编译同样支持增量注解处理器，所有的注解处理器都需要选择这个特性，否则它们将会触发全量编译。

作为一个使用者你可以使用 `--info` 选项来查看哪个注解处理器正在触发全量编译。如果一个自定义的 `executable(可执行文件)` 或者 `javaHome` 在编译任务中被配置，那么增量注解处理将被停用。

### Making an annotation processor incremental

首先请看一看 [incremental Java compilation](https://docs.gradle.org/current/userguide/java_plugin.html#sec:incremental_compile)，因为增量注解处理构建在 incremental Java compilation 之上。

Gradle 支持两个常见类别的注解处理器(`isolating隔离 和 aggregating聚集`)的增量编译。请参考下面的信息来决定哪中类别适合你的处理器。

然后你可以在 `META-INF` 使用一个文件为增量编译注册你的注解处理器，格式为每一行一个处理器，使用处理器的全限定类名，然后使用逗号隔开它的类别。

#### 示例：注册一个增量注解处理器

路径：`processor/src/main/resources/META-INF/gradle/incremental.annotation.processors`
```
EntityProcessor,isolating
ServiceRegistryProcessor,dynamic
```

如果你的处理器只能在运行时才能决定是否为增量的，你可以在 META-INF 中将其声明为`dynamic`，然后在运行时使用 ` Processor#getSupportedOptions()` 方法返回 true 类型。

#### 示例：动态注册一个增量注解处理器

路径：`processor/src/main/java/ServiceRegistryProcessor.java`

```java
@Override
public Set<String> getSupportedOptions() {
    return Collections.singleton("org.gradle.annotation.processing.aggregating");
}
```

两个类型都有以下限制

- 它们必须使用[Filer API](https://docs.oracle.com/javase/8/docs/api/javax/annotation/processing/Filer.html)生成特们的文件，使用其他任何方式写入文件都将导致延后的静默的失败，因为这些文件不会被正确的清理，如果你的处理器做了这样的事，将不能被增量处理。
- 它们不能依赖类似 `com.sun.source.util.Trees` 编译器特定的 API，Gradle 包装了处理 API，所以试图去转换编译器特定的类型会失败，如果你的处理器做了这样的事，将不能被增量处理。除非你有一些后备机制。
- 如果它们使用了 [Filer#createResource](https://docs.oracle.com/javase/8/docs/api/javax/annotation/processing/Filer.html#createResource(javax.tools.JavaFileManager.Location,java.lang.CharSequence,java.lang.CharSequence,javax.lang.model.element.Element...))，Gradle 将会重新编译所有的源文件，参考[gradle/issues/4702](https://github.com/gradle/gradle/issues/4702)


### "Isolating" annotation processors

最快的类别，它们隔离地查看每个带注释的元素，为它创建生成的文件或验证消息。例如，EntityProcessor 可以为每个带有 `@Entity`的类型生成一个 `<TypeName>Repository`

#### Example: An isolated annotation processor

路径：`processor/src/main/java/EntityProcessor.java`

```java
Set<? extends Element> entities = roundEnv.getElementsAnnotatedWith(entityAnnotation);
for (Element entity : entities) {
    createRepository((TypeElement) entity);
}
```

Isolating 类别处理器有以下限制

- 它们必须根据从 AST 获得的信息为注解类型做出所有决策(代码生成，验证消息)，这意味着你可以分析这些类型的超类、方法返回值、注解等，甚至是可传递的。但是你不能基于 RoundEnvironment 中不相关的元素做决策。这样做的话会导致静默失败，因为以后会重新编译太少的文件。如果你的处理器需要基于除此之外其他不相关的元素的组合来做决策，那使用请 `aggregating` 来代替 `isolating`。 
- 它们必须为每一个通过 Filer API 生成文件提供明确的原始元素，如果没有提供或者是提供了多个，那么 Gradle 将会重新编译所有的源文件。

当一个源文件被重新编译了，Gradle 会重新编译所有从这个源文件生成的文件。如果一个源文件被删除了，那么从这个源文件生成的文件也会被删除。

### "Aggregating" annotation processors

可以将多个源文件聚合成一个或多个输出文件或验证消息。例如，ServiceRegistryProcessor 可以为每个使用 `@Service` 注释的类型创建一个有一个方法的 ServiceRegistry。

#### Example: An aggregating annotation processor

路径：`processor/src/main/java/ServiceRegistryProcessor.java`

```java
JavaFileObject serviceRegistry = filer.createSourceFile("ServiceRegistry");
Writer writer = serviceRegistry.openWriter();
writer.write("public class ServiceRegistry {");
for (Element service : roundEnv.getElementsAnnotatedWith(serviceAnnotation)) {
    addServiceCreationMethod(writer, (TypeElement) service);
}
writer.write("}");
writer.close();
```

Aggregating 处理器有以下限制

- 它们只能读取在 CLASS 或 RUNTIME 期保留的注解
- 如果传递了 `-parameters ` 参数，那么它们只能读取参数名。

Gradle 总是会重新处理(不是重新编译)所有注解处理器注册的注解文件，Gradle 总会重新编译任何处理器生成的文件。

### Compile avoidance

如果一个 project 以 ABI-compatible 方式发生了改变(仅仅是它的私有 API 发生改变)，那么 Java 编译任务将会跳过它，这意味着如果 project A 依赖 project B，而 project B 中的一个类以 ABI-compatible 方式发生改变(通常，仅更改方法的主体)，Gradle 不会重新编译 project A。

某些不影响公共API的更改类型会被忽略：

- 改变一个方法体
- 改变一个注释
- 添加或异常或修改私有的方法、字段、或内部类
- 添加、移除或修改一个资源
- 改变类路径中 jar 或 目录的名词
- 参数重命名

因为实现细节对注解处理器很重要，它们必须在注解处理器路径上分别地声明，Gradle 会忽略在编译路径中的注解处理器。

Example 6. Declaring annotation processors

```groovy
dependencies {
    // The dagger compiler and its transitive dependencies will only be found on annotation processing classpath
    annotationProcessor 'com.google.dagger:dagger-compiler:2.8'

    // And we still need the Dagger library on the compile classpath itself
    implementation 'com.google.dagger:dagger:2.8'
}
```