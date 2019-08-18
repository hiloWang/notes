#  Exception和Error有什么区别？

随着编程语言和软件的诞生，异常情况就如影随形地纠缠着我们，只有正确处理好意外情况，才能保证程序的可靠性。Java 语言在设计之初就提供了相对完善的异常处理机制，这种机制大大降低了编写和维护可靠程序的门槛。如今，**异常处理机制已经成为现代编程语言的标配**。那么 Java 中 Exception和Error有什么区别呢？

---
## 1 典型回答

- Exception 和 Error 都是继承了 Throwable 类，在 Java 中只有 Throwable 类型的实例才可以被抛出（throw）或者捕获（catch），它是异常处理机制的基本组成类型。
- Exception 和 Error 体现了 Java 平台设计者对不同异常情况的分类。
  - Exception 是程序正常运行中，可以预料的意外情况，可能并且应该被捕获，进行相应处理。
  - Error 是指在正常情况下，不大可能出现的情况，绝大部分的 Error 都会导致程序（比如 JVM 自身）处于非正常的、不可恢复状态。既然是非正常情况，所以不便于也不需要捕获，常见的比如 OutOfMemoryError 之类，都是 Error 的子类。
- Exception 又分为可检查（checked）异常和不检查（unchecked）异常：
  - 可检查异常在源代码里必须显式地进行捕获处理，这是编译期检查的一部分。
  - 不检查异常就是所谓的运行时异常，类似 `NullPointerException、ArrayIndexOutOfBoundsException `之类，通常是可以编码避免的逻辑错误，具体根据需要来判断是否需要捕获，并不会在编译期强制要求。

---
## 2 考点分析

在日常编程中，如何处理好异常是比较考验功底的，可以从下面两个方面

### 2.1 理解 Throwable、Exception、Error 的设计和分类

- 理解 Throwable、Exception、Error 的设计和分类。
- 掌握应用广泛的异常类型。
- 如何自定义异常。

下面是常见异常类图：

![](index_files/20180513233855.png)

### 2.2 懂得如何处理典型场景

- 理解 Java 语言中操作 Throwable 的元素和实践
- 掌握最基本的语法是必须的，如 `try-catch-finally` 块，throw、throws 关键字等。
- 懂得如何处理典型场景。
- 掌握 `try-with-resources` 应用和原理 和 `multiple catch`

关于 `try-with-resources` 和 `multiple catch`，具体可以参考下面的代码段。在编译时期，会自动生成相应的处理逻辑，比如，自动按照约定俗成 close 那些扩展了 AutoCloseable 或者 Closeable 的对象。

```
//// Try-with-resources
try (BufferedReader br = new BufferedReader(…);
     BufferedWriter writer = new BufferedWriter(…)) {
    // do something
catch ( IOException | XEception e) {// Multiple catch
       // Handle it
}
```

---
## 3 异常处理实践

### 案例 1：如何选择捕获异常的类型、生吞异常的弊端

参考下面代码
```
try {
  // 业务代码
  // …
  Thread.sleep(1000L);
} catch (Exception e) {
  // Ignore it
}
```

这段代码虽然很短，但是已经违反了异常处理的两个基本原则：

第一：**尽量不要捕获类似 Exception 这样的通用异常，而是应该捕获特定异常**。

- 在上面代码中 `Thread.sleep()` 抛出的 InterruptedException。
- 在日常的开发和合作中，我们读代码的机会往往超过写代码，软件工程是门协作的艺术，所以我们有义务让自己的代码能够直观地体现出尽量多的信息，对于异常处理，如果仅仅是泛泛的捕获 Exception 之类，恰恰隐藏了我们的目的。这在代码语义上表达就不够清晰。
- 另外，我们也要保证程序不会捕获到我们不希望捕获的异常。比如，可能更希望 RuntimeException 被扩散出来，而不是被捕获。

第二，**不要生吞（swallow）异常**。

- 生吞（swallow）异常是异常处理中要特别注意的事情，因为很可能会导致非常难以诊断的诡异情况。
- 生吞异常，往往是基于假设这段代码可能不会发生，或者感觉忽略异常是无所谓的，但是千万不要在产品代码做这种假设！
- 如果我们不把异常抛出来，或者也没有输出到日志（Logger）之类，程序可能在后续代码以不可控的方式结束。没人能够轻易判断究竟是哪里抛出了异常，以及是什么原因产生了异常。

### 案例 2：异常如何输出

参考下面代码：

```
try {
   // 业务代码
   // …
} catch (IOException e) {
    e.printStackTrace();
}
```

这段代码作为一段实验代码，它是没有任何问题的，但是在产品代码中，通常都不允许这样处理。关于`printStackTrace()`的文档，开头就是 `Prints this throwable and its backtrace to the standard error stream`。问题就在这里：

- 在稍微复杂一点的生产系统中，标准出错（STERR）不是个合适的输出选项，因为你很难判断出到底输出到哪里去了。
- 而对于分布式系统，如果发生异常，但是无法找到堆栈轨迹（stacktrace），这纯属是为诊断设置障碍。
- 所以，**最好使用产品日志，详细地输出到日志系统里。**


### 案例 3：Throw early, catch late 原则

参考下面代码：

```
public void readPreferences(String fileName){
     //...perform operations...
    InputStream in = new FileInputStream(fileName);
     //...read the preferences file...
}
```

#### throw early：尽早暴露异常信息

问题在于：如果 fileName 是 null，那么程序就会抛出 NullPointerException，但是由于没有第一时间暴露出问题，堆栈信息可能非常令人费解，往往需要相对复杂的定位。这个 NPE 只是作为例子，实际产品代码中，可能是各种情况，比如获取配置失败之类的。**在发现问题的时候，第一时间抛出，能够更加清晰地反映问题。**

修改一下，让问题“throw early”，对应的异常信息就非常直观了：
```
public void readPreferences(String filename) {
    Objects. requireNonNull(filename);
    //...perform other operations...
    InputStream in = new FileInputStream(filename);
     //...read the preferences file...
}
```

#### catch late：如何处理捕获后的异常

“catch late”，其实是我们经常苦恼的问题，捕获异常后，需要怎么处理呢？

1. 最差的处理方式，就是生吞异常，本质上其实是掩盖问题。
2. 如果实在不知道如何处理，可以选择保留原有异常的 cause 信息，直接再抛出或者构建新的异常抛出去。在更高层面，因为有了清晰的（业务）逻辑，往往会更清楚合适的处理方式是什么。
3. 自定义异常，有的时候，我们会根据需要自定义异常，这个时候除了保证提供足够的信息，还有两点需要考虑：
  - 是否需要定义成 Checked Exception，因为这种类型设计的初衷更是为了从异常情况恢复，作为异常设计者，我们往往有充足信息进行分类。
  - **在保证诊断信息足够的同时，也要考虑避免包含敏感信息**，因为那样可能导致潜在的安全问题。如果我们看 Java 的标准类库，你可能注意到类似 `java.net.ConnectException`，出错信息是类似“ Connection refused (Connection refused)”，而不包含具体的机器名、IP、端口等，一个重要考量就是信息安全。类似的情况在日志中也有，比如，用户数据一般是不可以输出到日志里面的。


---
## 4 关于受检测异常的业界争论

### 反面观点

业界有一种争论（甚至可以算是某种程度的共识），Java 语言的 Checked Exception 也许是个设计错误，反对者列举了几点：

- Checked Exception 的假设是我们捕获了异常，然后恢复程序。但是，其实我们大多数情况下，根本就不可能恢复。Checked Exception 的使用，已经大大偏离了最初的设计目的。
- Checked Exception 不兼容 functional 编程，如果你写过 Lambda/Stream 代码，相信深有体会。

### 正面观点

很多人也觉得没有必要矫枉过正，原因如下：

- 确实有一些异常，比如和环境相关的 IO、网络等，其实是存在可恢复性的，
- Java 已经通过业界的海量实践，证明了其构建高质量软件的能力。

---
## 5 从性能角度来审视 Java 的异常处理机制

从性能角度来审视一下 Java 的异常处理机制，这里有两个可能会相对昂贵的地方：

- try-catch 代码段会产生额外的性能开销，或者换个角度说，它往往会影响 JVM 对代码进行优化，所以**建议仅捕获有必要的代码**段，尽量不要一个大的 try 包住整段的代码
- 不建议利用异常控制代码流程，因为这远比我们通常意义上的条件语句（if/else、switch）要低效。
- Java 每实例化一个 Exception，都会对当时的栈进行快照，这是一个相对比较重的操作。如果发生的非常频繁，这个开销可就不能被忽略了。

所以，对于部分追求极致性能的底层类库，有种方式是尝试创建不进行栈快照的 Exception。这本身也存在争议，因为这样做的假设在于，我创建异常时知道未来是否需要堆栈。问题是，实际上可能吗？小范围或许可能，但是在大规模项目中，这么做可能不是个理智的选择。如果需要堆栈，但又没有收集这些信息，在复杂情况下，尤其是类似微服务这种分布式系统，这会大大增加诊断的难度。**当我们的服务出现反应变慢、吞吐量下降的时候，检查发生最频繁的 Exception 也是一种思路。**

---
## 6 响应式编程范式的异常处理

问题：对于异常处理编程，不同的编程范式也会影响到异常处理策略，比如，现在非常火热的反应式编程（Reactive Stream），因为其本身是异步、基于事件机制的，所以出现异常情况，决不能简单抛出去；另外，由于代码堆栈不再是同步调用那种垂直的结构，这里的异常处理和日志需要更加小心，我们看到的往往是特定 executor 的堆栈，而不是业务方法调用关系。对于这种情况，你有什么好的办法吗？

反应式编程链式编程一般是以流的形式处理事件，在流的尾端定义结果如何处理，比如有正常的结束的处理流程和异常的处理流程，如果在响应式流中发送了异常，不应该打断这个流，而应该按照规范把异常传递到流的尾端进行处理，对于异常信息处理，则需要通过适当的包装保留原有的异常信息，以方便定位异常。

---
## 7 总结与思考题

今天，我从一个常见的异常处理概念问题，简单总结了 Java 异常处理的机制。并结合代码，分析了一些普遍认可的最佳实践，以及业界最新的一些异常使用共识。最后，我分析了异常性能开销，希望对你有所帮助。

### NoClassDefFoundError 和 ClassNotFoundException 有什么区别

- NoClassDefFoundError：当 Java 虚拟机或 ClassLoader 实例试图在类的定义中加载（作为通常方法调用的一部分或者作为使用 new 表达式创建的新实例的一部分），但无法找到该类的定义时，抛出此异常。 **当前执行的类被编译时，所搜索的类定义存在，但无法再找到该定义。**
- ClassNotFoundException：当应用程序试图使用以下方法通过字符串名加载类，但是没有找到具有指定名称的类的定义时。抛出该异常：
 - Class 类中的 forName 方法。
 - ClassLoader 类中的 findSystemClass 方法。
 - ClassLoader 类中的 loadClass 方法。

### 异常处理总结

1. 不要推诿或延迟处理异常，就地解决最好，并且需要实实在在的进行处理，而不是只捕捉后忽略。
2. 一个函数尽管抛出了多个异常，但是只有一个异常可被传播到调用端。最后被抛出的异常时唯一被调用端接收的异常，其他异常都会被吞没掩盖。如果调用端要知道造成失败的最初原因，程序之中就绝不能掩盖任何异常。
3. 不要在finally代码块中处理返回值。
4. 按照我们程序员的惯性认知：当遇到return语句的时候，执行函数会立刻返回。但是，在Java语言中，如果存在finally就会有例外。除了return语句，try代码块中的break或continue语句也可能使控制权进入finally代码块。
5. 请勿在try代码块中调用return、break或continue语句。万一无法避免，一定要确保finally的存在不会改变函数的返回值。
6. 函数返回值有两种类型：值类型与对象引用。对于对象引用，要特别小心，如果在finally代码块中对函数返回的对象成员属性进行了修改，即使不在finally块中显式调用return语句，这个修改也会作用于返回值上。
7. 勿将异常用于控制流。、
8. 不要忽略try-catch代理的性能影响


