# 四、文件 I/O：通用的 I/O 模型

## 1 文件描述符

- 所有执行 I/O 操作的系统调用都以文件描述符，一个非负整数（通常是小整数），来指代打开的文件。
- 文件描述符用以表示所有类型的已打开文件，包括`管道（pipe） 、FIFO、socket、终端、设备和普通文件`。

## 2 标准 I/O

- 标准IO包括标准输入，标准输出，标准错误，这三个文件描述符在进程中始终是打开的。
- 在程序中指代这些文件描述符时，可以使用数字（0、1、2）表示，或者采用 `<unistd.h>` 所定义的 POSIX 标准名称（推荐）。

![04_stdio](../images/04_stdio.png)

## 3 通用 I/O 概念

UNIX I/O 模型的显著特点之一是其输入/输出的通用性概念。这意味着使用 4 个同样的系统调用 `open()`、`read()`、`write()`和 `close()`可以对所有类型的文件执行 I/O 操作，包括终端之类的设备。因此，仅使用这些系统调用编写的程序，将对任何类型的文件有效。

## 4 标准 I/O 系统调用

- `fd = open(pathname, flags, mode)`：打开或创建文件。
- `numread = read(fd, buffer, count)`：从文件中读取数据。
- `numwritten = write(fd, buffer, count)`：向文件中写入数据。
- `status = close(fd)`：释放文件描述符 fd 以及与之相关的内核资源。
- `fd = create(pathname, mode)`：creat 系统调用根据 pathname 参数创建并打开一个文件，若文件已存在，则打开文件，并清空文件内容，将其长度清 0。由于 open 函数的功能更加完善，对 creat 的使用现在已不多见。
- `off_t leek(fd, offset, whence)`：改变文件偏移量。

### open 系统调用

```c
#include <sys/stat.h>
#include <fcntl.h>

// Returns file descriptor on success, or -1 on error
int open(const char *pathname, int flags, .../* mode_t mode */);
```

上述系统调用中，以 open 函数最为复杂，对于 open 函数，我们需要掌握：

#### 参数 flags 的取值以及相关作用

参数 flags 用于指定文件的访问模式，值可以是表中的单个值或者多个值的组合（通过位或 `|` 操作组合）

访问模式 | 描述
------- | -------
O_RDONLY | 以只读方式打开文件
O_WRONLY | 以只写方式打开文件
O_RDWR | 以读写方式打开文件
O_CLOEXEC | 设置close-on-exec标志
O_CREAT | 若文件不存在则创建之
O_DIRECT | 无缓冲的输入/输出
O_DIRECTORY | 若pathname不是目录，则失败
O_EXCL | 结合O_CREAT参数使用，专门用于创建文件
O_LARGEFILE | 在32位系统中使用此标志打开大文件
O_NOATIME | 调用read()时不更新文件最近访问时间
O_NOCTTY | 不要让pathname（所指向的是终端设备）成为控制终端
O_NOFOLLOW | 对符号链接不予解引用
O_TRUNC | 截断已有文件，使其长度为0
O_APPEND | 总在文件尾部追加数据
O_ASYNC | 当I/O操作可行时，产生信号通知进程
O_DSYNC | 提供同步的I/O数据完整性
O_NONBLOCK | 以非阻塞方式打开
O_SYNC | 以同步方式打开

#### 参数 mode 的取值以及相关作用

- mode 用于为新建文件时指定其权限
- 只有当 flags 指定为 O_CREAT（创建）访问模式时，才有必要设置这个参数
- 值可以是表中的单个值或者多个值的组合（通过位或 `|` 操作组合）

权限标识 | 描述
------- | -------
S_IRUSR | 所属主可读
S_IWUSR | 所属主可写
S_IXUSR | 所属主可执行
S_IRGRP | 所属组可读
S_IWGRP | 所属组可写
S_IXGRP | 所属组可执行
S_IROTH | 其他人可读
S_IWOTH | 其他人可写
S_IXOTH | 其他人可执行

#### open 函数的错误处理

若打开文件时发生错误，`open()` 将返回 -1，错误号 errno 标识错误原因。可能发生的错误如下：

errno | 描述
------- | -------
EACCES | 文件权限不允许调用进程以 flags 参数指定的方式打开文件。无法访问文件，其可能原因有目录权限的限制、文件不存在并且无法创建该文件。
EISDIR | 所指定的文件属于目录，而调用者企图打开该文件进行写操作。不允许这种用法。（另一方面，在某些场合中，打开目录进行读操作是有必要的。）
EMFILE | 进程已打开的文件描述符数量达到了进程资源限制所设定的上限（RLIMIT_NOFILE参数）
ENFILE | 文件打开数量已经达到系统允许的上限
ENOENT | 要么文件不存在且未指定 O_CREATE 标志，要么指定了 O_CREATE 标志，但 pathname 参数所指定路径的目录之一不存在，或者 pathname 参数为符号链接，而该链接指向的文件不存在
EROFS | 所指定的文件隶属于只读文件系统，而调用者企图以写方式打开文件
ETXTBSY | 所指定的文件为可执行文件，且正在运行中。系统不允许修改正在运行的程序。必须先终止程序运行，然后方可修改可执行文件。

### read 系统调用

```c
#include <unistd.h>

// Return number of bytes read, 0 on EOF, or -1 on error
ssize_t read(int fd, void *buffer, size_t count);
```

- 系统调用不会分配内存缓冲区用以返回信息给调用者。所以，必须预先分配大小合适的缓冲区并将缓冲区指针传递给系统调用。与此相反，有些库函数却会分配内存缓冲区用以返回信息给调用者。
- 如果 read() 调用成功，将返回实际读取的字节数，如果遇到文件结束（EOF）则返回 0，如果出现错误则返回-1。
- 当 read() 应用于其他文件类型时，比如管道、FIFO、socket 或者终端，在不同环境下也会 出现 read() 调用读取的字节数小于请求字节数的情况。例如，默认情况下从终端读取字符，一遇到换行符`（\n）`，read() 调用就会结束。

### write 系统调用

```c
#include <unistd.h>

// Return number of bytes written, or -1 on error
ssize_t write(int fd, void *buffer, size_t count);
```

对磁盘文件执行 I/O 操作时，`write()` 调用成功并不能保证数据已经写入磁盘。因为为了减少磁盘活动量和加快 `write()` 系统调用，内核会缓存磁盘的 I/O 操作。

### close 系统调用

```c
#include <unistd.h>

// Return 0 on success, or -1 on error
int close(int fd);
```

close 系统调用关闭一个打开的文件描述符，并将其释放回调用进程，供该进程继续使用。 当一进程终止时，将自动关闭其已打开的所有文件描述符。

- 显式关闭不再需要的文件描述符往往是良好的编程习惯，会使代码在后续修改时更具可读性。
- 文件描述符属于有限资源，因此文件描述符关闭失败可能会导致一个进程将文件描述符资源消耗殆尽。

在编写需要长期运行并处理大量文件的程序时，比如 shell 或者网络服务器软件，需要特别加以关注。 像其他所有系统调用一样，应对 `close()` 的调用进行错误检查，如下所示：

```c
if(close(fd) == -1){
    errExit("close");
}
```

### lseek 系统调用

```c
#include <unistd.h>

// Return new file offset if successful, or -1 on error
off_t lseek(int fd, off_t offset, int whence);
```

- `lseek()`调用只是调整内核中与文件描述符相关的文件偏移量记录，并没有引起对任何物理设备的访问。
- `lseek()`并不适用于所有类型的文件。不允许将 `lseek()` 应用于管道、FIFO、socket 或者终端。 一旦如此，调用将会失败，并将 errno 置为 ESPIPE。

whence 指定按照哪个基准点来解释 offset 参数，参数可选值如下：

参数 | 描述
------- | -------
SEEK_SET | 将文件偏移量设置为从文件头部起始点开始的offset个字节（offset非负）
SEEK_CUR | 相对于当前位置将文件偏移量调整offset个字节（offset可正可负）
SEEK_END | 起始文件偏移量设置在文件尾部，调整offset个字节（offset可正可负）

## 5 文件空洞

如果程序的文件偏移量已然跨越了文件结尾，然后再执行 I/O 操作，将会发生什么情况？

- `read()`调用将返回 0，表示文件结尾。
- **`write()` 函数可以在文件结尾后的任意位置写入数据**。

从文件结尾后到新写入数据间的这段空间被称为文件空洞。**空洞的存在意味着一个文件名义上的大小可能要比其占用的磁盘存储总量要大**（有时会大出许多）。向文件空洞中写入字节，内核需要为其分配存储单元，即使文件大小不变，系统的可用磁盘空间也将减少。

**限制文件空洞**：

- SUSv3 的函数 `posix_ fallocate(fd, offset, len)` 规定，针对文件描述符 fd 所指代的文件，能确保按照由 offset 参数和 len 参数所确定的字节范围为其在磁盘上分配存储空间。这样应用程序对文件的后续 `write()` 调用不会因磁盘空间耗尽而失败（否则，当文件中一个空洞被填满后，或者因其他应用程序消耗了磁盘空间时，都可能因磁盘空间耗尽而引发此类错误）。
- 在过去，glibc 库在实现 `posix_ fallocate()` 函数时，通过向指定范围内的每个块写入一个值为 0 的字节以达到预期结果。自内核版本 2.6.23 开始，Linux 系统提供了 `fallocate()`系统调用，能更为高效地确保所需存储空间的分配。当 `fallocate()` 调用可用时，glibc 库会利用其来实现 `posix_ fallocate()` 函数的功能。

## 6 通用 I/O 模型以外的操作：`ioctl()`

>大部分驱动需要除了读写设备的能力，还需要通过设备驱动进行各种硬件控制的能力. 大部分设备可进行超出简单的数据传输之外的操作; 用户空间必须常常能够请求, 例如, 设备锁上它的门, 弹出它的介质, 报告错误信息, 改变波特率, 或者自我销毁. 这些操作常常通过 ioctl 方法来支持, 它通过相同名子的系统调用来实现。参考[《Linux 设备驱动》第三版 6.1. ioctl 接口](https://www.kancloud.cn/kancloud/ldd3/60977)

对于未纳入标准 I/O 模型的所有设备和文件操作而言，可以选择`ioctl()` 系统调用。

```c
int ioctl(int fd, int request, ...);
```

ioctl 即 `input/output control`的缩写，`ioctl()` 系统调用为执行文件和设备操作提供了一种多用途机制。
