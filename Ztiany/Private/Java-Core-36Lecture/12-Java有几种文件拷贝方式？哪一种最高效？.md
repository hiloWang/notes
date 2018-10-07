# Java有几种文件拷贝方式？哪一种最高效？

---
## 1 典型回答

Java 有多种比较典型的文件拷贝实现方式：

- 利用 `java.io` 类库，直接为源文件构建一个 FileInputStream 读取，然后再为目标文件构建一个 FileOutputStream，完成写入工作。
- 利用 `java.nio` 类库提供的 transferTo 或 transferFrom 方法实现。
- Java 标准类库本身已经提供了几种 `Files.copy` 的实现。

对于 Copy 的效率，这个其实与**操作系统和配置等情况**相关，总体上来说，NIO transferTo/From 的方式可能更快，因为它更能利用现代操作系统底层机制，避免不必要拷贝和上下文切换。

---
## 2 考点分析

今天这个问题，从面试的角度来看，确实是一个面试考察的点，针对我上面的典型回答，面试官还可能会从实践角度，或者 IO 底层实现机制等方面进一步提问。这一讲的内容从面试题出发，主要还是为了让你进一步加深对 Java IO 类库设计和实现的了解。

从实践角度，我前面并没有明确说 NIO transfer 的方案一定最快，真实情况也确实未必如此。我们可以根据理论分析给出可行的推断，保持合理的怀疑，给出验证结论的思路，有时候面试官考察的就是如何将猜测变成可验证的结论，思考方式远比记住结论重要。

从技术角度展开，下面这些方面值得注意：

- 不同的 copy 方式，底层机制有什么区别？
- 为什么零拷贝（zero-copy）可能有性能优势？
- Buffer 分类与使用。
- Direct Buffer 对垃圾收集等方面的影响与实践选择。

---
## 3 知识扩展

### 3.1 拷贝实现机制分析

#### 用户态空间和内核态空间

用户态空间（User Space）和内核态空间（Kernel Space）：这是操作系统层面的基本概念，

- 内核态空间：操作系统内核、硬件驱动等运行在内核态空间，具有相对高的特权；运行在该模式的代码，可以无限制地对系统存储、外部设备进行访问。
- 用户态空间，则是给普通应用和服务使用

#### 普通 IO 的拷贝过程

当我们使用输入输出流进行读写时，实际上是进行了多次上下文切换，比如应用读取数据时，先在内核态将数据从磁盘读取到内核缓存，再切换到用户态将数据从内核缓存读取到用户缓存。写入操作也是类似，仅仅是步骤相反。参考下图：

![](index_files/io_copy.png)

由于数据至少需要拷贝两次，这种方式会带来一定的额外开销，可能会降低 IO 效率。

#### 基于 NIO transferTo 的实现方式

基于 NIO transferTo 的实现方式，在 Linux 和 Unix 上，则会使用到零拷贝技术，数据传输并不需要用户态参与，省去了上下文切换的开销和不必要的内存拷贝，进而可能提高应用拷贝性能。注意，transferTo 不仅仅是可以用在文件拷贝中，与其类似的，例如读取磁盘文件，然后进行 Socket 发送，同样可以享受这种机制带来的性能和扩展性提高。

![](index_files/nio_copy.png)


### 3.2  Java IO/NIO 源码结构

`java.nio.file.Files.copy` 提供了几个不同的重载方法：

```
public static Path copy(Path source, Path target, CopyOption... options) throws IOException
public static long copy(InputStream in, Path target, CopyOption... options) throws IOException
public static long copy(Path source, OutputStream out) throws IOException
```

copy 不仅仅是支持文件之间操作，没有人限定输入输出流一定是针对文件的，这是两个很实用的工具方法。

- 后面两种 copy 实现，能够在方法实现里直接看到使用的是 InputStream.transferTo()，其内部实现其实是 stream 在用户态的读写
- 第一种 copy 方法可以追溯到 native 层，根据不同的平台有不同的实现，通过追踪源码可以确定该方法不是利用 transferTo，而是本地技术实现的用户态拷贝

NIO 底层是和操作系统紧密相关的，所以每个平台都有自己的部分特有文件系统逻辑。

![](index_files/io_provider.png)

### 3.3 如何提高类似拷贝等 IO 操作的性能

- 在程序中，使用缓存等机制，合理减少 IO 次数（在网络通信中，如 TCP 传输，window 大小也可以看作是类似思路）。
- 使用 transferTo 等机制，减少上下文切换和额外 IO 操作。
- 尽量减少不必要的转换过程，比如编解码；对象序列化和反序列化，比如操作文本文件或者网络通信，如果不是过程中需要使用文本信息，可以考虑不要将二进制信息转换成字符串，直接传输二进制信息。

### 3.4 掌握 NIO Buffer

Buffer 是 NIO 操作数据的基本工具，Java 为每种原始数据类型都提供了相应的 Buffer 实现（布尔除外），所以掌握和使用 Buffer 是十分必要的，尤其是涉及 Direct Buffer 等使用，因为其在垃圾收集等方面的特殊性，更要重点掌握。

Buffer 有几个基本属性：

- capcity，它反映这个 Buffer 到底有多大，也就是数组的长度。
- position，要操作的数据起始位置。
- limit，相当于操作的限额。在读取或者写入时，limit 的意义很明显是不一样的。比如，读取操作时，很可能将 limit 设置到所容纳数据的上限；而在写入时，则会设置容量或容量以下的可写限度。
- mark，记录上一次 postion 的位置，默认是 0，算是一个便利性的考虑，往往不是必须的。

### 3.5 Direct Buffer 和垃圾收集

- **Direct Buffer**：如果我们看 Buffer 的方法定义，会发现它定义了 `isDirect()` 方法，返回当前 Buffer 是否是 Direct 类型。这是因为 Java 提供了堆内和堆外（Direct）Buffer，我们可以以它的 allocate 或者 allocateDirect 方法直接创建。
- **MappedByteBuffer**：它将文件按照指定大小直接映射为内存区域，当程序访问这个内存区域时将直接操作这块儿文件数据，省去了将数据从内核空间向用户空间传输的损耗。我们可以使用 `FileChannel.map` 创建 MappedByteBuffer，它本质上也是种 Direct Buffer。

Direct Buffer 的优势和适用场景：

- 在实际使用中，Java 会尽量对 Direct Buffer 仅做本地 IO 操作，对于**很多大数据量的 IO 密集操作**，可能会带来非常大的性能优势
- Direct Buffer 生命周期内内存地址都不会再发生更改，进而内核可以安全地对其进行访问，很多 IO 操作会很高效。
- 减少了堆内对象存储的可能额外维护工作，所以访问效率可能有所提高。
- Direct Buffer 创建和销毁过程中，都会比一般的堆内 Buffer 增加部分开销，所以通常都建议用于长期使用、数据较大的场景。

如何指定 Direct Buffer 的大小：

- Direct Buffer 不在堆上，所以 Xmx 之类参数，不能影响 Direct Buffer 等堆外成员所使用的内存额度
- 可以使用 `-XX:MaxDirectMemorySize=512M` 参数设置大小

如何处理对于 Direct Buffer 的 回收：

- 在计算 Java 可以使用的内存大小的时候，不能只考虑堆的需要，还有 Direct Buffer 等一系列堆外因素。如果出现内存不足，堆外内存占用也是一种可能性。
- 大多数垃圾收集过程中，都不会主动收集 Direct Buffer，它的垃圾收集过程，就是基于 Cleaner（一个内部实现）和幻象引用（PhantomReference）机制，其本身不是 public 类型，内部实现了一个 Deallocator 负责销毁的逻辑。对它的销毁往往要拖到 full GC 的时候，所以使用不当很容易导致 OutOfMemoryError。
- 一般建议在应用程序中，显式地调用 System.gc() 来强制触发。
- 尽可能的重复使用 Direct Buffer

### 3.6 如何跟踪和诊断 Direct Buffer 内存占用

在 JDK 8 之后的版本，可以使用 Native Memory Tracking（NMT）特性来进行诊断，开启 NMT 的参数为：

```
# 激活 NMT 通常都会导致 JVM 出现 5%~10% 的性能下降
-XX:NativeMemoryTracking={summary|detail}
```

在开启了 NMT 的 JVM 运行时，可以采用下面命令进行交互式对比：

```
# 打印 NMT 信息
jcmd <pid> VM.native_memory detail 

# 进行 baseline，以对比分配内存变化
jcmd <pid> VM.native_memory baseline

# 进行 baseline，以对比分配内存变化
jcmd <pid> VM.native_memory detail.diff
```

另外需要注意：JVM 的堆外内存远不止 Direct Buffer，NMT 输出的信息当然也远不止这些


---
## 4 总结

面对问题时，保持合理的怀疑，给出验证结论的思路，有时候面试官考察的就是如何将猜测变成可验证的结论，思考方式远比记住结论重要。

今天我分析了 Java IO/NIO 底层文件操作数据的机制，以及如何实现零拷贝的高性能操作，梳理了 Buffer 的使用和类型，并针对 Direct Buffer 的生命周期管理和诊断进行了较详细的分析。关于今天我们讨论的题目你做到心中有数了吗？你可以思考下，`如果我们需要在 channel 读取的过程中，将不同片段写入到相应的 Buffer 里面（类似二进制消息分拆成消息头、消息体等），可以采用 NIO 的什么机制做到呢`？