
---
## lock-free

[Lock-Free 编程](https://www.cnblogs.com/gaochundong/p/lock_free_programming.html)

---
## CopyOnWrite

CopyOnWriteArrayList 官方文档：

- ArrayList 的一个线程安全的变体，其中所有可变操作（add、set 等等）都是通过对底层数组进行一次新的复制来实现的。
- 这一般需要很大的开销，但是当遍历操作的数量大大超过可变操作的数量时，这种方法可能比其他替代方法更 有效。
- 在不能或不想进行同步遍历，但又需要从并发线程中排除冲突时，它也很有用。
- 快照”风格的迭代器方法在创建迭代器时使用了对数组状态的引用。此数组在迭代器的生存期内不会更改，因此不可能发生冲突，并且迭代器保证不会抛出 ConcurrentModificationException。创建迭代器以后，迭代器就不会反映列表的添加、移除或者更改。在迭代器上进行的元素更改操作（remove、set 和 add）不受支持。这些方法将抛出 UnsupportedOperationException。
- 允许使用所有元素，包括 null。
- 内存一致性效果：当存在其他并发 collection 时，将对象放入 CopyOnWriteArrayList 之前的线程中的操作 happen-before 随后通过另一线程从 CopyOnWriteArrayList 中访问或移除该元素的操作。 

CopyOnWrite 并不是绝对安全的，使用的时候还是要按照规则使用，不然即使不会发生并发相关异常，但是会引发 ArrayIndexOutOfBoundsException 异常：

```
       CopyOnWriteArrayList<String> cow = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 100; i++) {
            cow.add(String.valueOf(i));
        }

        //不推荐使用这种方式遍历，这样会抛出 ArrayIndexOutOfBoundsException
       int size = cow.size();
        for (int i = 0; i < size; i++) {
            System.out.println(cow.get(i));
            cow.remove(i);
        }

        //利用迭代器的进行遍历，因为迭代器遍历的是快照，所以更安全
        System.out.println("---------------1");
        for (String s : cow) {
            cow.remove(s);
            System.out.println(s);
        }
        System.out.println("---------------2");
        for (String s : cow) {
            cow.remove(s);
            System.out.println(s);
        }
```

---
##  方法区、永久带、元数据区

- **关于方法区**：在 Java 虚拟机中，方法区（ Method Area） 是可供各条`线程共享`的`运行时内存区域`。方法区与传统语言中的编译代码储存区（ Storage Area Of Compiled Code）或者操作系统进程的正文段（ Text egment）的作用非常类似，它`存储了每一个类的结构信息`，例如运行时常量池（ Runtime Constant Pool）、字段和方法数据、构造函数和普通方法的字节码内容、还包括一些在类、实例、接口初始化时用到的特殊方法。
方法区在虚拟机启动的时候被创建，虽然`方法区是堆的逻辑组成部分`，但是简单的虚拟机实现可以选择在这个区域不实现垃圾收集。这个版本的 Java 虚拟机规范也不限定实现方法区的内存位置和编译代码的管理策略。方法区的容量可以是固定大小的，也可以随着程序执行的需求动态扩展，并在不需要过多空间时自动收缩。方法区在实际内存空间中可以是不连续的。
- 方法区别名：Non-Heap（非堆）
- **方法区与永久代（PermGen space）**：《Java虚拟机规范》规定了方法区的概念和作用，但没有规定应该如何实现它，所以不同的 JVM 所实现的方法区是不同的， Hotspot JVM 使用永久代来实现方法区，也就是说，方法区是一种规范，永久代是方法区的一种实现，调解参数：`初始值：-XX:PermSize；最大值：-XX:MaxPermSize`
- **元数据区（Metaspace）**：在 Oracle JDK8 中，已经被完全移除了，取而代之的是元数据区，调节参数：`初始值：XX:MetaspaceSize；最大值：-XX:MaxMetaspaceSize`
- 在 Oracle JDK7 中，其实方法区的部分数据就已经转移到堆或本地内存中，比如 `字符串常量池` 转移到了堆中

引用：

- 《Java虚拟机规范(JavaSE7)》
- [Java8内存模型—永久代(PermGen)和元空间(Metaspace)](https://www.cnblogs.com/paddix/p/5309550.html)


---
## 关于内存

### 操作系统内存

相关概念

- 主存中的每个字节都由一个 `物理地址` 整数所指定，物理地址的集合叫做物理 `地址空间`。它的范围通常为 `0到N-1` ，其中 N 是主存的大小。在带有 1GB 主存的的系统上，最高的有效地址是 `2 ** 30 - 1`，即 `1024 * 1024 * 1024`
- 许多操作系统提供 `虚拟内存`，也就是说程序永远不需要处理物理地址，也不需要知道有多少物理内存是有效的。
- 作为代替，程序处理虚拟地址，它被编码为从 `0到M-1` ，其中 M 是有效虚拟地址的大小。虚拟地址空间的大小取决于所处的操作系统和硬件。
- 比如32位和64位系统，在 32 位系统上，虚拟地址是 32 位的，大小为 `2 ** 32`，在64位系统上，虚拟地址空间大小为 `2 ** 64个字节`。
- 当一个程序读写内存中的值时，它使用虚拟地址。硬件在操作系统的帮助下，在访问主存之前将物理地址翻译成虚拟地址。翻译过程在进程层级上完成，所以即使两个进程访问相同的虚拟地址，它们所映射的物理地址可能不同。
- 虚拟内存是操作系统隔离进程的一种重要途径。通常，一个进程不能访问其他进程的数据，因为没有任何虚拟地址能映射到其他进程分配的物理内存。

地址翻译，虚拟地址（VA）如何翻译成物理地址（PA）？大多数处理器提供了内存管理单元（MMU），位于CPU和主存之间。MMU在VA和PA之间执行快速的翻译。

- 当程序读写变量时，CPU 会得到 VA。
- MMU 将 VA 分成两部分，称为`页码`和`偏移`。页是一个内存块，页的大小取决于操作系统和硬件，通常为 `1~4KB`。
- MMU在`页表`里查找页码，然后获取相应的物理页码。之后它将物理页码和偏移组合得到 PA。
- PA 传递给主存，用于读写指定地址。

### JVM 内存

公式：`Max memory = [-Xmx] + [-XX:MaxPermSize] + number_of_threads * [-Xss]`，Xmx表示最大堆，MaxPermSize表示永久代大小，Xss表示每个现在的栈大小

整个Java进程分为 heap 和 non-heap 两部分，每部分有以下几个概念：

- init：表示 Java 虚拟机在启动过程中从操作系统请求的用于启动期间的内存管理的初始内存量(以字节为单位)。 并且随着时间的推移也可能会向系统释放内存。初始化的值可能未定义。
- used：表示当前使用的内存量（以字节为单位）。
- committed：表示保证可供Java虚拟机使用的内存量（以字节为单位）。committed 内存量可能会随着时间的推移而变化（增加或减少）。Java虚拟机可能会向系统释放内存，并且 committed 内存可能少于 init。committed 将始终大于或等于 used。
- max：表示可用于内存管理的最大内存量（以字节为单位）。它的数值可能是不确定的。如果已定义，最大内存量可能会随时间而变化。如果定义了max，则 used 和 committed 的内存量将始终小于或等于 max。如果 `used > committed`，则尝试增加 used 内存会失败，即使 `used <= max` 仍然为true，例如，当系统虚拟内存不足时。

reserved 、committed、used：

- reserved memory 是指 JVM 通过 mmaped PROT_NONE 申请的虚拟地址空间，在页表中已经存在了记录（entries），保证了其他进程不会被占用，会`page faults`
- committed memory 是JVM向操作系统实际分配的内存（malloc/mmap）
- used memory 是 JVM 实际存储了数据（Java对象）的大小，当 `used~=committed` 的时候，heap 就会增加，而 `-Xmx` 设置了其上限。

内存耗用：VSS、RSS、PSS、USS

- VSS- Virtual Set Size 虚拟耗用内存（包含共享库占用的内存）
- RSS- Resident Set Size 实际使用物理内存（包含共享库占用的内存）
- PSS- Proportional Set Size 实际使用的物理内存（比例分配共享库占用的内存）
- USS- Unique Set Size 进程独自占用的物理内存（不包含共享库占用的内存）
- 一般来说内存占用大小有如下规律：`VSS >= RSS >= PSS >= USS`


引用：

- [JVM内存调优相关的一些笔记](http://zhanjindong.com/2016/03/02/jvm-memory-tunning-notes)
- [《操作系统思考》](http://greenteapress.com/thinkos/html/index.html)，[《操作系统思考》中文版](https://legacy.gitbook.com/book/wizardforcel/think-os/details)