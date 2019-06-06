# Linux 进程

每个进程在内核中都有一个进程控制块（PCB）来维护进程相关的信息，Linux 内核的进程控制块是 task_struct 结构体。task_struct 中包含以下信息：

- 进程id。系统中每个进程有唯一的id，在C语言中用pid_t类型表示，其实就是一个非负整数。
- 进程的状态，有运行、挂起、停止、僵尸等状态。
- 进程切换时需要保存和恢复的一些 CPU 寄存器。
- 描述虚拟地址空间的信息。
- 描述控制终端的信息。
- 当前工作目录（Current Working Directory）。
- umask掩码。
- 文件描述符表，包含很多指向file结构体的指针。
- 和信号相关的信息。
- 用户 id 和组 id。
- 控制终端、Session 和进程组。
- 进程可以使用的资源上限（Resource Limit）。

两个重要的系统调用：`fork` 和 `exec`

- fork 的作用是根据一个现有的进程复制出一个新进程，原来的进程称为父进程（Parent Process），新进程称为子进程（Child Process）。系统中同时运行着很多进程，这些进程都是从最初只有一个进程开始一个一个复制出来的。在 Shell 下输入命令可以运行一个程序，是因为 Shell 进程在读取用户输入的命令之后会调用 fork 复制出一个新的 Shell 进程，然后新的 Shell 进程调用 exec 执行新的程序。
- exec 函数族的作用是执行参数所指定的可执行文件或脚本。这个函数会替换掉当前进程的内存镜像，也就是执行这个函数成功后，调用该函数的程序就不存在了。
- 一个程序可以多次加载到内存，成为同时运行的多个进程，例如可以同时开多个终端窗口运行 /bin/bash，另一方面，一个进程在调用exec前后也可以分别执行两个不同的程序，例如在 Shell 提示符下输入命令 ls，首先 fork 创建子进程，这时子进程仍在执行 /bin/bash 程序，然后子进程调用 exec 执行新的程序 /bin/ls。

具体参考[LinuxC一站式编程：第 28 章 第 30 章 进程](https://akaedu.github.io/book/ch30s01.html)

## 环境变量

- libc 中定义的全局变量 environ 指向环境变量表。
- getenv 函数
- setenv 函数
- unsetenv 函数

具体参考[LinuxC一站式编程：第 28 章 第 30 章 环境变量](https://akaedu.github.io/book/ch30s02.html)

## fork 函数

具体参考[LinuxC一站式编程：第 28 章 第 30 章 fork](https://akaedu.github.io/book/ch30s03.html#id2866212)

## exec 函数族

具体参考[LinuxC一站式编程：第 28 章 第 30 章 exec](https://akaedu.github.io/book/ch30s03.html#id2866732)