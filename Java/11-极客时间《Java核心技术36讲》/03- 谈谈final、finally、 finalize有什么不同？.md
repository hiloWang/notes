# 谈谈final、finally、 finalize有什么不同？

---
## 1 典型回答

**final**

- 用来修饰类、方法、变量，分别有不同的意义
- final 修饰的 class 代表不可以继承扩展，
- final 的变量是不可以修改的
- final 的方法也是不可以重写的（override）

**finally**

- 是 Java 保证重点代码一定要被执行的一种机制。
- 可以使用 `try-finally` 或者 `try-catch-finally` 来进行类似关闭 JDBC 连接、保证 unlock 锁等动作。

**finalize**

- Java 基础类 `java.lang.Object` 的一个方法，它的设计目的是保证对象在被垃圾收集前完成特定资源的回收。
- finalize 机制现在已经不推荐使用，并且在 JDK 9 开始被标记为 deprecated。

再深入一层的话，可以从`性能、并发、对象生命周期`或`垃圾收集基本过程`等方面的理解。

---
## 2 关于 final

### 使用 final 关键字来明确表示代码的语义、逻辑意图

- 可以将方法或者类声明为 final，这样就可以明确告知别人，这些行为是不许修改的。
- 在设计 API 是，定义某些类为 final 的。可以有效避免 API 使用者更改基础功能，某种程度上，这是保证平台安全的必要手段。
- 使用 final 修饰参数或者变量，也可以清楚地避免意外赋值导致的编程错误。
- final 变量产生了某种程度的不可变（immutable）的效果，所以，可以用于保护只读数据，尤其是在并发编程中，因为明确地不能再赋值 final 变量，有利于减少额外的同步开销，也可以省去一些防御性拷贝的必要。

### final 是否会有性能的好处

很多文章或者书籍中都介绍了可在特定场景提高性能，比如，利用 final 可能有助于 JVM 将方法进行内联，可以改善编译器进行条件编译的能力等等。这样是否真的有必要？

- 很多类似这样的结论都是基于假设得出的。
- 现代高性能 JVM（如 HotSpot）判断内联未必依赖 final 的提示，要相信 JVM 还是非常智能的。
- final 字段对性能的影响，大部分情况下，并没有考虑的必要。
- 在日常开发中，除非有特别考虑，不然最好不要指望这种小技巧带来的所谓性能好处，**程序最好是体现它的语义目的**。

### final 不是 immutable

final 并不等同于 immutable，比如下面这段代码：

```
 final List<String> strList = new ArrayList<>();
 strList.add("Hello");
 strList.add("world");
 List<String> unmodifiableStrList = List.of("hello", "world");
 unmodifiableStrList.add("again");
```

final 只能约束 strList 这个引用不可以被赋值，但是 strList 对象行为不被 final 影响，添加元素等操作是完全正常的。如果我们真的希望对象本身是不可变的，那么需要相应的类支持不可变的行为。

### 创建 Immutable 类

Immutable 在很多场景是非常棒的选择，比如 Immutable 可以大大简化并发编程环境下的同步，Java 语言目前并没有原生的不可变支持，如果要实现 immutable 的类，我们需要做到：


- 将 class 自身声明为 final，这样别人就不能扩展来绕过限制了。
- 将所有成员变量定义为 private 和 final，并且 private 变量不要实现 setter 方法。
- 通常构造对象时，成员变量使用深度拷贝来初始化，而不是直接赋值，这是一种防御措施，因为无法确定输入对象不被其他人修改。
- 如果确实需要实现 getter 方法，或者其他可能会返回内部状态的方法，使用 copy-on-write 原则，创建私有的 copy。

---
## 3 关于 finally

对于 finally，明确知道怎么使用就足够了。需要关闭的连接等资源，更推荐使用 Java 7 中添加的 `try-with-resources` 语句，因为通常 Java 平台能够更好地处理异常情况。

有一种情况 finally 代码不会被执行，参考下面代码：

```
try {
  // do something
  System.exit(1);
} finally{
  System.out.println(“Print from finally”);
}
```

---
## 4 关于 finalize

### 4.1 finalize 方法是不被推荐使用的

- finalize 方法是不推荐使用的，业界实践一再证明它不是个好的办法
- 在 Java 9 中，已经明确将 Object.finalize() 标记为 deprecated
- 如果没有特别的原因，不要实现 finalize 方法，也不要指望利用它来进行资源回收。

### 4.2 finalize 为什么不被推荐使用

- 无法保证 finalize 什么时候执行，执行的是否符合预期。
- finalize 使用不当会影响性能，导致程序死锁、挂起等。
- finalize 的执行是和垃圾收集关联在一起的，一旦实现了非空的 finalize 方法，就会导致相应对象回收呈现数量级上的变慢，有人专门做过 benchmark（基准测试），大概是 40~50 倍的下降。
- finalize 被设计成在对象被垃圾收集前调用，这就意味着实现了 finalize 方法的对象是个“特殊公民”，JVM 要对它进行额外处理。finalize 本质上成为了快速回收的阻碍者，可能导致对象经过多个垃圾收集周期才能被回收。
- `System.runFinalization()`是不可预测、不能保证的，所以本质上还是不能指望。finalize 方法就会被执行。
- 实践中，因为 finalize 拖慢垃圾收集，导致大量对象堆积，也是一种典型的导致 OOM 的原因。即就算 finalize 方法被执行了，如果 finalize 中有耗时或不当的操作，反而会影响垃圾回收。

总结：我们要确保回收资源就是因为资源都是有限的，垃圾收集时间的不可预测，可能会极大加剧资源占用。这意味着对于消耗非常高频的资源，千万不要指望 finalize 去承担资源释放的主要职责。


### 4.3 有什么机制可以替换 finalize

#### Cleaner 机制

- Java 平台目前在逐步使用 `java.lang.ref.Cleaner` 来替换掉原有的 finalize 实现。Cleaner 的实现利用了幻象引用（PhantomReference），这是一种常见的所谓 post-mortem(事后的) 清理机制。
- 如果熟悉 Java 的各种引用，利用幻象引用和引用队列，可以保证对象被彻底销毁前做一些类似资源回收的工作，比如关闭文件描述符（操作系统有限的资源），它比 finalize 更加轻量、更加可靠。
- 吸取了 finalize 里的教训，每个 Cleaner 的操作都是独立的，它有自己的运行线程，所以可以避免意外死锁等问题。

`java.lang.ref.Cleaner`使用示例：

```
public class CleaningExample implements AutoCloseable {
        // A cleaner, preferably one shared within a library
        //最好在一个library里共享一个cleaner
        private static final Cleaner cleaner = <cleaner>;
        
        //State 是静态的，它不会持有外部类的引用
        static class State implements Runnable {
            State(...) {
                // initialize State needed for cleaning action
                // 初始化状态用于执行清理操作
            }
            public void run() {
                // cleanup action accessing State, executed at most once
                // 执行清理操作，最多被执行一次
            }
        }
        private final State;
        private final Cleaner.Cleanable cleanable

        public CleaningExample() {
            this.state = new State(...);
            //注册
            this.cleanable = cleaner.register(this, state);
        }

        public void close() {
            cleanable.clean();
        }
    }
```

#### Cleaner 或者幻象引用改善的程度仍然是有限的

如果由于种种原因导致**幻象引用堆积**，同样会出现问题。所以，Cleaner 适合作为一种最后的保证手段，而不是完全依赖 Cleaner 进行资源回收，不然我们就要再做一遍 finalize 的噩梦了。

很多第三方库自己直接利用幻象引用定制资源收集，比如广泛使用的 MySQL JDBC driver 之一的 mysql-connector-j，就利用了幻象引用机制。幻象引用也可以进行类似链条式依赖关系的动作，比如，进行总量控制的场景，保证只有连接被关闭，相应资源被回收，连接池才能创建新的连接。这种代码如果稍有不慎添加了对资源的强引用关系，就会导致循环引用关系，从而造成内存泄漏等问题。

---
## 5 copy-on-write 原则

Copy-On-Write 简称 COW，是一种用于程序设计中的优化策略。其基本思路是，**从一开始大家都在共享同一个内容，当某个人想要修改这个内容的时候，才会真正把内容Copy出去形成一个新的内容然后再改，这是一种延时懒惰策略**。从JDK1.5开始Java并发包里提供了两个使用 CopyOnWrite 机制实现的并发容器,它们是 CopyOnWriteArrayList 和 CopyOnWriteArraySet 。CopyOnWrite 容器非常有用，可以在非常多的并发场景中使用到。

---
## 6 总结与思考题

今天，我从语法角度分析了 final、finally、finalize，并从安全、性能、垃圾收集等方面逐步深入，探讨了实践中的注意事项，希望对你有所帮助。也许你已经注意到了，JDK 自身使用的 Cleaner 机制仍然是有缺陷的，你有什么更好的建议吗？