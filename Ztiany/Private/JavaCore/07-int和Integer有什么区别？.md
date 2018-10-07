# int 和 Integer 有什么区别？谈谈 Integer 的值缓存范围。

Java 虽然号称是面向对象的语言，但是原始数据类型仍然是重要的组成元素，那么 int 和 Integer 有什么区别？谈谈 Integer 的值缓存范围。

---
## 1 典型回答

- int 是整形数字，是 Java 的 8 个原始数据类型（Primitive Types，`boolean、byte 、short、char、int、float、double、long`）之一。Java 语言虽然号称一切都是对象，但原始数据类型是例外。
- Integer 是 int 对应的包装类，它有一个 int 类型的字段存储数据，并且提供了基本操作：
 - 数学运算、int 和字符串之间转换等。
 - 在 Java 5 中，引入了自动装箱和自动拆箱功能（boxing/unboxing），Java 可以根据上下文，自动进行转换，极大地简化了相关编程。


**关于 Integer 的值缓存**，Java 5 的改进。构建 Integer 对象的传统方式是直接调用构造器，直接 new 一个对象。但是根据实践，我们发现大部分数据操作都是集中在有限的、较小的数值范围，因而，在 Java 5 中新增了静态工厂方法 valueOf，在调用它的时候会利用一个缓存机制，带来了明显的性能改进。按照 Javadoc，这个值默认缓存是 -128 到 127 之间。

---
## 2 考点分析

今天这个问题涵盖了 Java 里的两个基础要素：原始数据类型、包装类。谈到这里，就可以非常自然地扩展到自动装箱、自动拆箱机制，进而考察封装类的一些设计和实践。坦白说，理解基本原理和用法已经足够日常工作需求了，但是要落实到具体场景，还是有很多问题需要仔细思考才能确定。

- Java 使用的不同阶段：编译阶段、运行时。那么自动装箱 / 自动拆箱是发生在什么阶段？
- 使用静态工厂方法 valueOf 会使用到缓存机制，那么自动装箱的时候，缓存机制起作用吗？
- 为什么我们需要原始数据类型，Java 的对象似乎也很高效，应用中具体会产生哪些差异？
- 阅读过 Integer 源码吗？分析下类或某些方法的设计要点。

---
## 3 理解自动装箱、拆箱

自动装箱实际上算是一种语法糖。什么是语法糖？可以简单理解为 Java 平台为我们自动进行了一些转换，保证不同的写法在运行时等价，它们发生在**编译阶段**，也就是生成的字节码是一致的。

javac 替我们自动把装箱转换为 `Integer.valueOf()`，把拆箱替换为 `Integer.intValue()`，但是注意 `new Integer()`不涉及到自动装箱，而是创建了新的 Integer 对象。

验证上面的结论：

```
Integer integer = 1;
int unboxing = integer ++;
```

javap 反编译结果：

```
1: invokestatic  #2                  // Method
java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
8: invokevirtual #3                  // Method
java/lang/Integer.intValue:()I
```

可以看到，反编译的结果是调用了 valueOf 和 intValue 方法。这种缓存机制并不是只有 Integer 才有，同样存在于其他的一些包装类，比如：

- Boolean，两个常量实例 `Boolean.TRUE/FALSE`。
- Short，同样是缓存了 -128 到 127 之间的数值。
- Byte，数值有限，所以全部都被缓存。
- Character，缓存范围 `'\u0000' 到 '\u007F'`。

### 自动装箱 / 自动拆箱编程实践

- 原则上，**建议避免无意中的装箱、拆箱行为**，尤其是在性能敏感的场合，创建 10 万个 Java 对象和 10 万个整数的开销可不是一个数量级的，不管是内存使用还是处理速度，光是对象头的空间占用就已经是数量级的差距了。
- 使用原始数据类型、数组甚至本地代码实现等，在性能极度敏感的场景往往具有比较大的优势，用其替换掉包装类、动态数组（如 ArrayList）等可以作为**性能优化的备选项**。


一个常见的线程安全计数器实现：

```
class Counter {
    private final AtomicLong counter = new AtomicLong();

    public void increase() {
        counter.incrementAndGet();
    }
}
```

如果利用原始数据类型，可以将其修改为

```
 class CompactCounter {
    //该变量必须是 volatile 和非 static 的
    private volatile long counter;

    private static final AtomicLongFieldUpdater<CompactCounter> updater = AtomicLongFieldUpdater.newUpdater(CompactCounter.class, "counter");

    public void increase() {
        updater.incrementAndGet(this);
    }
}
```

---
## 4 Integer 源码分析

阅读并实践高质量代码也是程序员成长的必经之路。

整体看一下 Integer 的职责：

- 各种基础的常量，比如最大值、最小值、位数等
- 各种静态工厂方法 valueOf()；获取环境变量数值的方法；各种转换方法，比如转换为不同进制的字符串，如 8 进制，或者反过来的解析方法等。

### 4.1 深挖缓存机制

Integer 的缓存范围虽然默认是 -128 到 127，但是在特别的应用场景，比如我们明确知道应用会频繁使用更大的数值，这时候应该怎么办呢？缓存上限值实际是可以根据需要调整的，JVM 提供了参数设置：

```
-XX:AutoBoxCacheMax=N
```

这些实现，都体现在 `java.lang.Integer` 内部的 IntegerCache 的静态初始化块里。

```
private static class IntegerCache {
        static final int low = -128;
        static final int high;
        static final Integer cache[];
        static {
            // high value may be configured by property
            int h = 127;
            String integerCacheHighPropValue = VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
            ...
            // range [-128, 127] must be interned (JLS7 5.1.7)
            assert IntegerCache.high >= 127;
        }
        ...
  }
```

### 4.2 不可变对象

Integer 内部的成员变量 value ，都被声明为“private final”，所以，它们同样是不可变类型。

### 4.3 SIZE 或者 BYTES 等常量设计

Integer 等包装类，定义了类似 SIZE 或者 BYTES 这样的常量，这反映了 Java 跨平台的特性，Java 中原始数据类型是不存在差异的，这些明确定义在Java 语言规范里面，不管是 32 位还是 64 位环境，开发者无需担心数据的位数差异。

### 4.4 原始类型线程安全

原始数据类型操作是不是线程安全的？

- 原始数据类型的变量，显然要使用并发相关手段，才能保证线程安全，比如 AtomicInteger、AtomicLong 这样的线程安全类。
- 部分比较宽的数据类型，比如 **float、double**，甚至不能保证更新操作的原子性，可能出现程序读取到只更新了一半数据位的数值！

### 4.5 Java 原始数据类型和引用类型局限性

- **原始数据类型和 Java 泛型并不能配合使用**：这是因为 Java 的泛型某种程度上可以算作伪泛型，它完全是一种编译期的技巧，Java 编译期会自动将类型转换为对应的特定类型，这就决定了使用泛型，必须保证相应类型可以转换为 Object。
- **无法高效地表达数据**，也不便于表达复杂的数据结构，比如 vector 和 tuple：Java 的对象都是引用类型，如果是一个原始数据类型数组，它在内存里是一段连续的内存，而对象数组则不然，数据存储的是引用，对象往往是分散地存储在堆的不同位置。这种设计虽然带来了极大灵活性，但是也导致了数据操作的低效，尤其是无法充分利用现代 CPU 缓存机制。


---
## 5 总结与思考题

今天，我梳理了原始数据类型及其包装类，从源码级别分析了缓存机制等设计和实现细节，并且针对构建极致性能的场景，分析了一些可以借鉴的实践。

前面提到了从空间角度，Java 对象要比原始数据类型开销大的多。你知道对象的内存结构是什么样的吗？比如，对象头的结构。如何计算或者获取某个 Java 对象的大小?

### 对象的结构与大小

对象头的结构。如何计算或者获取某个 Java 对象的大小?

在HotSpot虚拟机中，对象在内存中存储的布局可以分为 3 块区域：

- 对象头（Header）
- 实例数据（Instance Data）
- 对齐填充（Padding）

各部分信息解释：

- HotSpot虚拟机的对象头包括两部分信息，第一部分用于存储对象自身的运行时数据，如哈希码（HashCode）、GC分代年龄、锁状态标志、线程持有的锁、偏向线程ID、偏向时间戳等，这部分数据的长度在32位和64位的虚拟机（未开启压缩指针）中分别为32bit和64bit，官方称它为"Mark Word"。

- 对象头的另外一部分是类型指针，即对象指向它的类元数据的指针，虚拟机通过这个指针来确定这个对象是哪个类的实例。并不是所有的虚拟机实现都必须在对象数据上保留类型指针，换句话说，查找对象的元数据信息并不一定要经过对象本身。如果对象是一个Java数组，那在对象头中还必须有一块用于记录数组长度的数据，因为虚拟机可以通过普通Java对象的元数据信息确定Java对象的大小，但是从数组的元数据中却无法确定数组的大小。

- 实例数据部分是对象真正存储的有效信息，也是在程序代码中所定义的各种类型的字段内容。无论是从父类继承下来的，还是在子类中定义的，都需要记录起来。

- 对齐填充并不是必然存在的，也没有特别的含义，它仅仅起着占位符的作用。由于HotSpot VM的自动内存管理系统要求对象起始地址必须是8字节的整数倍，换句话说，就是对象的大小必须是8字节的整数倍。
