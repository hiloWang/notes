# Linux 信号

## 信号的基本概念

1. 用户输入命令，在Shell下启动一个前台进程。
2. 用户按下 Ctrl-C，这个键盘输入产生一个硬件中断。
3. 如果 CPU 当前正在执行这个进程的代码，则该进程的用户空间代码暂停执行，CPU 从用户态切换到内核态处理硬件中断。
4. 终端驱动程序将 Ctrl-C 解释成一个 SIGINT 信号，记在该进程的 PCB 中（也可以说发送了一个 SIGINT 信号给该进程）。
5. 当某个时刻要从内核返回到该进程的用户空间代码继续执行之前，首先处理 PCB 中记录的信号，发现有一个 SIGINT 信号待处理，而这个信号的默认处理动作是终止进程，所以直接终止进程而不再返回它的用户空间代码执行。

具体参考[LinuxC一站式编程：第 33 章 信号的基本概念](https://akaedu.github.io/book/ch33s01.html)

## 产生信号

具体参考[LinuxC一站式编程：第 33 章 产生信号](https://akaedu.github.io/book/ch33s02.html)

### 通过终端按键产生信号

- SIGINT的默认处理动作是终止进程
- SIGQUIT的默认处理动作是终止进程并且Core Dump

**Core Dump**：当一个进程要异常终止时，可以选择把进程的用户空间内存数据全部保存到磁盘上，文件名通常是core，这叫做Core Dump。

### 调用系统函数向进程发信号

- 用 kill 命令给它发 SIGSEGV 信号。
- kill 命令是调用 kill 函数实现的。
- raise 函数可以给当前进程发送指定的信号（自己给自己发信号）。
- abort 函数使当前进程接收到SIGABRT信号而异常终止。

### 由软件条件产生信号

SIGPIPE 是一种由软件条件产生的信号，调用 alarm 函数可以设定一个闹钟，也就是告诉内核在 seconds 秒之后给当前进程发 SIGALRM 信号，该信号的默认处理动作是终止当前进程。这个函数的返回值是 0 或者是以前设定的闹钟时间还余下的秒数。

## 阻塞信号

具体参考[LinuxC一站式编程：第 33 章 阻塞信号](https://akaedu.github.io/book/ch33s03.html)

### 信号在内核中的表示

### 信号集操作函数

- sigemptyset
- sigfillset
- sigaddset
- sigdelset
- sigprocmask
- sigpending

## 捕捉信号

如果信号的处理动作是用户自定义函数，在信号递达时就调用这个函数，这称为捕捉信号。

具体参考[LinuxC一站式编程：第 33 章 捕捉信号](https://akaedu.github.io/book/ch33s04.html)