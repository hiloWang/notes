# 二 Java内存模型：看Java如何解决可见性和有序性问题

可见性、原子性、有序性导致的问题是编程领域属于共性问题，所有的编程语言都会遇到。JVM 规范中定义了 **Java 内存模型** 来解决这些问题。

## Java 内存模型

导致可见性、原子性、有序性导致的问题是编译器优化和高速缓存。所以解决这些问题的方法是：

- 禁用一切缓存和编译优化，但是这会导致程序性能的急剧下降。
- **按需禁用缓存和编译优化**，只要不影响并发程序的正确性，依然允许高速缓存和编译优化。（当然怎么按需禁用缓存和编译优化，就是程序员的工作了。）

Java 内存模型规范了 JVM 如何提供**按需禁用缓存和编译优化**的方法。具体来说，这些方法包括：

- volatile、synchronized 和 final 三个关键字
- 六项 Happens-Before 规则

## volatile 关键字

volatile 的作用：告诉编译器，对使用 volatile 修饰的变量的读写，不能使用 CPU 缓存，必须从主存中读取修改后写入到主存中。

**JDK1.5 之前，volatile存在的问题**：

```java
class VolatileExample {

  int x = 0;
  volatile boolean v = false;

  public void writer() {
    x = 42;
    v = true;
  }

  public void reader() {
    if (v == true) {
      // 这里 x 会是多少呢？
    }
  }
}
```

这段代码运行的结果在不同 JDK 版本中有所不同：

- 如果在低于 1.5 版本上运行，x 可能是 42，也有可能是 0。
- 如果在 1.5 以上的版本上运行，x 就是等于 42。

造成在低于 1.5 版本上运行结果不确定的原因是，对 x 和 v 的赋值，可能有指令重排，而当时的 volatile 无法处理这种情况。因此，**Java 内存模型在 1.5 版本对 volatile 语义进行了增强**，具体体现在 Happens-Before 规则中。

## happens-before 规则

注意：Happens-Before 并不是说前面一个操作发生在后续操作的前面，它真正要表达的是：**前面一个操作的结果对后续操作是可见的**。

1. **程序的顺序性规则**：在一个线程中，按照程序顺序，前面的操作 Happens-Before 于后续的任意操作。
2. **volatile 变量规则**：对一个 volatile 变量的写操作， Happens-Before 于后续对这个 volatile 变量的读操作。
3. **happens-before 的传递性**：如果 A Happens-Before B，且 B Happens-Before C，那么 A Happens-Before C。（这就是 1.5 版本对 volatile 语义的增强，这个增强意义重大，1.5 版本的并发工具包（java.util.concurrent）就是靠 volatile 语义来搞定可见性的）
4. **管程中锁的规则**：对一个锁的解锁 Happens-Before 于后续对这个锁的加锁。（要理解这个规则，就首先要了解“管程指的是什么”。管程是一种通用的同步原语，在 Java 中指的就是 synchronized，synchronized 是 Java 里对管程的实现。）
5. **线程 start() 规则**：主线程 A 启动子线程 B 后，子线程 B 能够看到主线程在启动子线程 B 前的操作。
6. **线程 join() 规则**：主线程 A 等待子线程 B 完成（主线程 A 通过调用子线程 B 的 join() 方法实现），当子线程 B 完成后（主线程 A 中 join() 方法返回），主线程能够看到子线程的操作。

## final 关键字

final 修饰变量时，初衷是告诉编译器：这个变量生而不变，可以尽量优化。但**在 JDK1.5 之前，final 同样存在问题**，构造函数的错误重排导致线程可能看到 final 变量的值会变化。详细的案例可以参考 [JSR 133 (Java Memory Model) FAQ](http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html#finalWrong)。

在 1.5 以后 Java 内存模型对 final 类型变量的重排进行了约束。现在只要我们提供正确构造函数没有“逸出”，就不会出问题。

什么是**构造函数溢出**：在构造函数中，讲某个成本变量赋值给可以被外部直接访问的一个变量，此时的构造函数还没有执行完毕，内部成员可能还没有初始化完成。

```java
class FinalFieldExample{

    public static FinalFieldExample sFinalFieldExample;

    // 以下代码来源于【参考 1】
    final int x;

    // 错误的构造函数
    public FinalFieldExample() {
        x = 3;
        y = 4;
        // 此处 this 逸出了
        sFinalFieldExample.obj = this;
    }

}
```

## 总结

- Java 的内存模型是并发编程领域的一次重要创新，之后 C++、C#、Golang 等高级语言都开始支持内存模型。
- Java 内存模型里面，最晦涩的部分就是 Happens-Before 规则。
- Happens-Before 规则最初是在一篇叫做 [Time, Clocks, and the Ordering of Events in a Distributed System](https://lamport.azurewebsites.net/pubs/time-clocks.pdf) 的论文提出来的。
- Java 内存模型主要分为两部分，一部分面向你我这种编写并发程序的应用开发人员，另一部分是面向 JVM 的实现人员的。我们可以重点关注前者。

## 练习

有一个共享变量 abc，在一个线程里设置了 abc 的值 abc=3，有哪些办法可以让其他线程能够看到 `abc==3？`

## 扩展阅读

- [Java内存模型FAQ](http://ifeve.com/jmm-faq/)
- [JSR-133: JavaTM Memory Model and Thread Specification](https://www.cs.umd.edu/~pugh/java/memoryModel/jsr133.pdf)
- [JSR 133 (Java Memory Model) FAQ](http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html#finalWrong)
