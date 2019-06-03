# C 标准库

C标准主要由两部分组成，一部分描述C的语法，另一部分描述C标准库。C标准库定义了一组标准头文件，每个头文件中包含一些相关的函数、变量、类型声明和宏定义。要在一个平台上支持C语言，不仅要实现C编译器，还要实现C标准库，这样的实现才算符合C标准。不符合C标准的实现也是存在的，例如很多单片机的C语言开发工具中只有C编译器而没有完整的C标准库。

C语言是一种通用的、面向过程式的计算机程序设计语言。C语言不但提供了丰富的库函数，还允许用户定义自己的函数。每个函数都是一个可以重复使用的模块，通过模块间的相互调用，有条不紊地实现复杂的功能。可以说C程序的全部工作都是由各式各样的函数完成的。

C 标准函数库（C standard library，缩写：libc）是在C语言程序设计中，所有匹配标准的头文件（head file）的集合，以及常用的函数库实现程序（如 I/O 输入输出和字符串控制）。标准函数库通常会随附在编译器上。因为 C 编译器常会提供一些额外的非 ANSI C 函数功能，所以某个随附在特定编译器上的标准函数库，对其他不同的编译器来说，是不兼容的。

## C 标准函数库头文件发展

1. ANSI C共包括15个头文件。
2. 1995年，Normative Addendum 1 （NA1）批准了3个头文件（iso646.h、wchar.h和wctype.h）增加到C标准函数库中。
3. C99标准增加了6个头文件（complex.h、fenv.h、inttypes.h、stdbool.h、stdint.h和tgmath.h）。
4. C11标准中又新增了5个头文件（stdalign.h、stdatomic.h、stdnoreturn.h、threads.h和uchar.h）。

至此，C标准函数库共有29个头文件

**ANSI C**：

| 头文件文件 | 简介说明 |
|---|---|
| `<stdio.h>` | 标准I/O库 |
| `<ctype.h>` | 字符的判断和大小写转换 |
| `<stdlib.h>` | 标准工具库函数：定义数值转换函数，伪随机数生成函数，动态内存分配函数，过程控制函数。 |
| `<string.h>` | 字符串处理函数 |
| `<assert.h>` | 包含断言宏，被用来在程序的调试版本中帮助检测逻辑错误以及其他类型的bug。 |
| `<limits.h>` | 定义了整数类型的一些极限值 |
| `<stddef.h>` | 一些标准宏定义 |
| `<time.h>`| 时间相关 |
| `<float.h>`| 浮点运算与限制 |
| `<math.h>` | 数学函数 |
| `<errno.h>` | 用来测试由库函数报的错误代码。 |
| `<locale.h>` | 定义C语言本地化函数 |
| `<setjmp.h>` | 跨函数跳转 |
| `<signal.h>` | 信号|
| `<stdarg.h>` | 可变参处理 |

**NA1**：

| 头文件文件 | 简介说明 |
|---|---|
| `<iso646.h>` |定义了许多操作符宏定义|
| `<wchar.h>` | |
| `<wctype.h>` | |

**C99**：

| 头文件文件 | 简介说明 |
|---|---|
| `<stdint.h>和<inttypes.h>`| 增加了可移植的整型 |
| `<complex.h>` | 一组操作复数的函数|
| `<fenv.h>` | |
| `<stdbool.h>` | 新增了布尔类型 |
| `<tgmath.h>` | |

**C11**：

| 头文件文件 | 简介说明 |
|---|---|
| `<stdalign.h>` | |
| `<stdatomic.h>` | |
| `<stdnoreturn.h>` | |
| `<threads.h>` | 定义用于管理多个线程以及互斥和条件变量的函数 |
| `<uchar.h>` |  |

具体可以参考[维基百科：C标准函数库](https://zh.wikipedia.org/wiki/C%E6%A8%99%E6%BA%96%E5%87%BD%E5%BC%8F%E5%BA%AB)

## 不同平台的 C 标准实现

### Linux：C 标准库和 glibc

在 Linux 平台上最广泛使用的 C 函数库是 glibc，其中包括C标准库的实现，几乎所有 C 程序都要调用 glibc 的库函数，所以 glibc 是 Linux 平台C程序运行的基础。glibc提供一组头文件和一组库文件，最基本、最常用的C标准库函数和系统函数在 libc.so 库文件中，几乎所有C程序的运行都依赖于libc.so，有些做数学计算的C程序依赖于 libm.so，以后我们还会看到多线程的C程序依赖于 libpthread.so。

当然 glibc 并不是 Linux 平台唯一的基础 C 函数库，也有人在开发别的C函数库，比如适用于嵌入式系统的 uClibc。

### Man Page

Man Page 是 Linux 开发最常用的参考手册，由很多页面组成，每个页面描述一个主题，这些页面被组织成若干个 Section。FHS（Filesystem Hierarchy Standard）标准规定了 ManPage 各 Section 的含义如下：

1. 1 用户命令，例如`ls(1)`
2. 2 系统调用，例如`_exit(2)`
3. 3 库函数，例如`printf(3)`
4. 4 特殊文件，例如`null(4)`描述了设备文件`/dev/null、/dev/zero`的作用
5. 5 系统配置文件的格式，例如`passwd(5)`描述了系统配置文件`/etc/passwd`的格式
6. 6 游戏
7. 7 其它杂项，例如`bash-builtins(7`)描述了 bash 的各种内建命令
8. 8 系统管理命令，例如`ifconfig(8)`

使用 Man Page 查看 C 标准库中的函数文档。

```shell
man 3 printf
```

Man Page中有些页面有重名，比如敲 man printf 命令看到的并不是 C 函数 printf，而是位于第 1 个 Section 的系统命令 printf，要查看位于第 3 个 Section 的 printf 函数应该敲 `man 3 printf`，也可以敲`man -k printf`命令搜索哪些页面的主题包含 printf 关键字。

### Windows

- [Windows 编程之路](https://lellansin.wordpress.com/tutorials/windows-%E7%BC%96%E7%A8%8B%E4%B9%8B%E8%B7%AF/)

## 引用

- [docs.microsoft：c-runtime-library](https://docs.microsoft.com/en-us/cpp/c-runtime-library/c-run-time-library-reference?view=vs-2019)
- [C标准库研究](https://lellansin.wordpress.com/tutorials/c-standard-library/)
- [LinuxC一站式编程：第 25 章 C 标准库](https://akaedu.github.io/book/ch25.html)