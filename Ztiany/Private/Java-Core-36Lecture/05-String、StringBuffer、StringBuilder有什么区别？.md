# String、StringBuffer、StringBuilder有什么区别？

---
## 1 典型回答

**String**：

- String 是 Java 语言非常基础和重要的类，提供了构造和管理字符串的各种基本逻辑。
- String 是典型的 Immutable 类，被声明成为 final class，所有属性也都是 final 的。也由于它的不可变性，类似拼接、裁剪字符串等动作，都会产生新的 String 对象。
- 由于字符串操作的普遍性和字符串本身的不可变性，所以相关操作的效率往往对应用性能有明显影响。
- 由于 String 是不可变的，所以它是线程安全的。

**StringBuffer**：

- StringBuffer是 Java 1.5 中新增的，为解决 String 拼接产生太多中间对象的问题而提供的一个类。
- 可以用 append 或者 add 方法，把字符串添加到已有序列的末尾或者指定位置。
- StringBuffer 本质是一个线程安全的可修改字符序列，它保证了线程安全，也随之带来了额外的性能开销，所以除非有线程安全的需要，不然还是推荐使用它的后继者，也就是 StringBuilder。

**StringBuilder**：

- StringBuilder 在能力上和 StringBuffer 没有本质区别，但是它去掉了线程安全的部分，有效减小了开销，是绝大部分情况下进行字符串拼接的首选。


---
## 2 考点分析：String 底层内容

几乎所有的应用开发都离不开操作字符串，理解字符串的设计和实现以及相关工具如拼接类的使用，对写出高质量代码是非常有帮助的。关于 String，可以从以下几个方法深入探讨：

- String 是 Immutable 的，通过 String 和相关类，考察基本的线程安全设计与实现，各种基础编程实践。
- JVM 对象缓存机制的理解以及如何良好地使用。
- JVM 优化 Java 代码的一些技巧。
- String 相关类的演进，比如 Java 9 中实现的巨大变化。

---
## 3 字符串设计和实现考量

### String 是 Immutable 类

String 是 Immutable 类的典型实现，原生的保证了基础线程安全，因为你无法对它内部数据进行任何修改，这种便利甚至体现在拷贝构造函数中，由于不可变，Immutable 对象在拷贝时不需要额外复制数据。

### StringBuffer 细节

- **线程安全实现**：StringBuffer 的线程安全是通过把各种修改数据的方法都加上 synchronized 关键字实现的，非常直白。
- 一般情况下不必纠结于 synchronized 性能之类的，有人说**过早优化是万恶之源**，考虑可靠性、正确性和代码可读性才是大多数应用开发最重要的因素。
- **底层实现**：为了实现修改字符序列的目的，StringBuffer 和 StringBuilder 底层都是利用可修改的（char，JDK 9 以后是 byte）数组，二者都继承了 AbstractStringBuilder，里面包含了基本操作，区别仅在于最终的方法是否加了 synchronized。
- **优化**：StringBuffer 和 StringBuilder 内部数组的长度默认为 16，如果确定拼接会发生非常多次，而且大概是可预计的，那么就可以指定合适的大小，避免很多次扩容的开销。扩容会产生多重开销，因为要抛弃原有数组，创建新的（可以简单认为是倍数）数组，还要进行 arraycopy。

### Javac 对 String 拼接的优化

问题：

- 面对 String、StringBuffer、StringBuilder 三个可选的类，在具体的代码书写中，应该如何选择呢？
- 在没有线程安全问题的情况下，全部拼接操作是应该都用 StringBuider 实现吗？（要多敲很多字的，可读性也不理想）

代码验证：

```
//使用 StringBuilder 拼接
String strByBuilder  = new StringBuilder().append("aa").append("bb").append("cc").append("dd").toString();

//使用 String 拼接
String strByConcat = "aa" + "bb" + "cc" + "dd";
```

对于上面代码，可以在不同 JDK 版本下使用 javap 命令进行反编译，然后查看 class 字节码指令。于是我们会发现：

- 在通常情况下，没有必要过于担心，要相信 Java 还是非常智能的。
- 在 JDK 8 中，字符串拼接操作会自动被 javac 转换为 StringBuilder 操作，(这里的编译优化仅能针对字面量拼接进行优化)
- 在 JDK 9 中，是因为 Java 9 为了更加统一字符串操作优化，提供了 StringConcatFactory，作为一个统一的入口。
- 所以：javac 自动生成的代码，虽然未必是最优化的，但普通场景也足够了，可以酌情选择。

---
## 4 字符串缓存

把常见应用进行堆转储（Dump Heap），然后分析对象组成，会发现**平均 25% 的对象是字符串，并且其中约半数是重复**的。如果能避免创建重复字符串，可以有效降低内存消耗和对象创建开销。

### 字符串常量池

Java 中有 8 种基本类型和一种比较特殊的类型 String，对于这些类型的字面常量，JVM 会将它们直接存储在常量池中，这样可以在很大程度上节省内存和提高程序的运行效率。8 种基本类型的常量池都是系统协调的，String 类型的常量池比较特殊。它的主要使用方法有两种：

- 如果是直接使用双引号声明出来的 String 对象，即字符串字面常量，则会直接存储在常量池中。
- 如果不是用双引号声明的 String 对象，可以使用 String 提供的 intern 方法。intern 方法会从字符串常量池中查询当前字符串是否存在，若不存在就会将当前字符串放入常量池中


### `intern()` 方法

String 在 Java 6 以后提供了 `intern()` 方法，目的是提示 JVM 把相应字符串缓存起来，以备重复使用。在我们创建字符串对象并调用 `intern()` 方法的时候，如果已经有缓存的字符串，就会返回缓存里的实例，否则将其缓存起来。`intern()` 是一个 native 方法，其 API 文档说明为：“如果常量池中存在当前字符串, 就会直接返回当前字符串. 如果常量池中没有此字符串, 会将此字符串放入常量池中后, 再返回”

#### 不同 JDK 版本的字符串常量池

- **在 Java6 中**：一般使用 Java 6 这种历史版本，并不推荐大量使用 intern，为什么呢？因为被缓存的字符串是存在所谓 PermGen 里的，也就是臭名昭著的“永久代”，这个空间是很有限的，也基本不会被 FullGC 之外的垃圾收集照顾到。所以，如果使用不当，OOM 就会光顾。
- **在 Java7 中**：字符串常量池已经从 Perm 区移到正常的 Java Heap 区域了，因为 PermGen 太小，而且 GC 一般不回收 PermGen 区，而把字符串常量池放在堆中，就极大避免了永久代占满的问题。
- **在 Java8 中**：永久代在 JDK 8 中被 MetaSpace（元数据区）替代了。而且，默认缓存大小也在不断地扩大中，从最初的 1009，到 7u40 以后被修改为 60013。

>Perm 区是一个类静态的区域，主要存储一些加载类的信息，常量池，方法片段等内容，默认大小只有 4M，在 Java 6 中，一旦常量池中大量使用 intern 是会直接产生 `java.lang.OutOfMemoryError: PermGen space`错误的。

操作字符串常量池大小：

```
//下面的参数直接打印具体数字
-XX:+PrintStringTableStatistics

//下面的 JVM 参数手动调整大小
-XX:StringTableSize=N
```

#### 验证不同 JDK 版本的字符串常量池

```
    String s = new String("1");
    s.intern();
    String s2 = "1";
    System.out.println(s == s2);

    String s3 = new String("1") + new String("1");
    s3.intern();
    String s4 = "11";
    System.out.println(s3 == s4);
```

JDK6 中结果为 `false false`，原因是：因为 jdk6 中的常量池是放在 Perm 区中的，Perm 区和正常的 JAVA Heap 区域是完全分开的。所以利用 `==` 比较两个对象的内存地址肯定是 false，

JDK7 中结果为 `false true`，原因是：

- `String s = new String("1");`，创建了两个对象，s 执行new出来的String，在字符串常量池中还有一个 "1"
- `s.intern();` 无效，因为常量池中已有 "1" 了
- `String s3 = new String("1") + new String("1");`，此时 s3 的内容为 "11"
- `s3.intern();`，对象 s3 被加入到常量池中
- `String s4 = "11";`，s4 执行常量池中的 "11"，也就是 s3
- s3 和 s4 指向同一个对象

### 字符串排重

- Intern 是一种**显式地排重机制**，但是它也有一定的副作用，因为需要开发者写代码时明确调用，一是不方便，每一个都显式调用是非常麻烦的；另外就是我们很难保证效率，应用开发阶段很难清楚地预计字符串的重复情况，有人认为这是一种污染代码的实践。
- 在 Oracle JDK 8u20 之后，推出了一个新的特性，也就是 G1 GC 下的字符串排重。它是通过将相同数据的字符串指向同一份数据来做到的，是 JVM 底层的改变，并不需要 Java 类库做什么修改。这个功能目前是默认关闭的，需要使用 `-XX:+UseStringDeduplication` 参数开启，并且记得指定使用 G1 GC。

### Intrinsic(内在的；本身的) 机制

在运行时，字符串的一些基础操作会直接利用 JVM 内部的 Intrinsic 机制，往往运行的就是特殊优化的本地代码，而根本就不是 Java 代码生成的字节码。Intrinsic 可以简单理解为，是一种利用 native 方式 hard-coded 的逻辑，算是一种特别的内联，很多优化还是需要直接使用特定的 CPU 指令，具体可以看相关[源码](http://hg.openjdk.java.net/jdk/jdk/file/44b64fc0baa3/src/hotspot/share/classfile/vmSymbols.hpp)。

---
## 5 String 自身的演化

### 使用 char 数组

关于 Java 的字符串，在历史版本中，它是使用 char 数组来存数据的，这样非常直接。但是 Java 中的 char 是两个 bytes 大小，拉丁语系语言的字符，根本就不需要太宽的 char，这样无区别的实现就造成了一定的浪费。密度是编程语言平台永恒的话题，因为归根结底绝大部分任务是要来操作数据的。

### 使用 byte 数组

- **改用Compact Strings 设计**：在 Java 9 中，引入了 Compact Strings 的设计，对字符串进行了大刀阔斧的改进。将数据存储方式从 char 数组，改变为一个 byte 数组加上一个标识编码的所谓 coder，并且将相关字符串操作类都进行了修改。另外，所有相关的 Intrinsic 之类也都进行了重写，以保证没有任何性能损失。
- **可能的弊端**：在极端情况下，字符串也出现了一些能力退化，比如最大字符串的大小。原来 char 数组的实现，字符串的最大长度就是数组本身的长度限制，但是替换成 byte 数组，同样数组长度下，存储能力是退化了一倍的！还好这是存在于理论中的极限，还没有发现现实应用受此影响。

---
## 6 编码相关的问题

很多字符串操作，比如 `getBytes()/String(byte[] bytes)` 等都是隐含着使用平台默认编码，这是一种好的实践吗？是否有利于避免乱码？

`getBytes和String`相关的转换时根据业务需要**建议指定编码方式**，如果不指定则看看 JVM 参数里有没有指定 `file.encoding` 参数，如果 JVM 没有指定，那使用的默认编码就是运行的操作系统环境的编码了，那这个编码就变得不确定了。常见的编码`iso8859-1`是单字节编码，`UTF-8`是变长的编码。**建议指定编码方式可以不依赖于不确定因素**。

---
## 7 总结

今天我从 String、StringBuffer 和 StringBuilder 的主要设计和实现特点开始，分析了字符串缓存的 intern 机制、非代码侵入性的虚拟机层面排重、Java 9 中紧凑字符的改进，并且初步接触了 JVM 的底层优化机制 intrinsic。从实践的角度，不管是 Compact Strings 还是底层 intrinsic 优化，都说明了使用 Java 基础类库的优势，它们往往能够得到最大程度、最高质量的优化，而且只要升级 JDK 版本，就能零成本地享受这些益处。

### String的创建机理

由于 String 在Java世界中使用过于频繁，Java 为了避免在一个系统中产生大量的 String 对象，引入了字符串常量池。其运行机制是：**创建一个字符串时，首先检查池中是否有值相同的字符串对象，如果有则不需要创建直接从池中刚查找到的对象引用；如果没有则新建字符串对象，返回对象引用，并且将新创建的对象放入池中**。但是，通过 new 方法创建的 String 对象是不检查字符串池的，而是直接在堆区或栈区创建一个新的对象，也不会把对象放入池中。上述原则只适用于通过直接量给String对象引用赋值的情况。

```
String str1 = "123"; //通过直接量赋值方式，放入字符串常量池
String str2 = new String(“123”);//通过new方式赋值方式，不放入字符串常量池，123 放入常量池
```

String提供了 `intern()` 方法。调用该方法时，如果常量池中包括了一个等于此String对象的字符串（由equals方法确定），则返回池中的字符串。否则，将此String对象添加到池中，并且返回此池中对象的引用。

### 应用场景

- 在字符串内容不经常发生变化的业务场景优先使用 String 类。例如：常量声明、少量的字符串拼接操作等。如果有大量的字符串内容拼接，避免使用 String 与 String 之间的 `+` 操作，因为这样会产生大量无用的中间对象，耗费空间且执行效率低下（新建对象、回收对象花费大量时间）。
- 在频繁进行字符串的运算（如拼接、替换、删除等），并且运行在多线程环境下，建议使用 StringBuffer，例如XML 解析、HTTP 参数解析与封装。
- 在频繁进行字符串的运算（如拼接、替换、删除等），并且运行在单线程环境下，建议使用 StringBuilder ，例如 SQL 语句拼装、JSON 封装等。

---
## 引用

- [美团技术团队：深入解析String#intern](https://tech.meituan.com/in_depth_understanding_string_intern.html)