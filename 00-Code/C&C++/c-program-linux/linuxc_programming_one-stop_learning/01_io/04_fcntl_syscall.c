/*
 ============================================================================

 Author      : Ztiany
 Description : Linux 文件与I/O：fcntl 函数

 ============================================================================
 */

/*
可以用 fcntl函数改变一个已打开的文件的属性，可以重新设置读、写、追加、非阻塞等标志（这些标志称为File Status Flag），而不必重新open文件。

    #include <unistd.h>
    #include <fcntl.h>

    int fcntl(int fd, int cmd);
    int fcntl(int fd, int cmd, long arg);
    int fcntl(int fd, int cmd, struct flock *lock);

具体参考：https://akaedu.github.io/book/ch28s06.html
*/

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>

#define MSG_TRY "try again\n"

// 用 fcntl改变 File Status Flag
void changeFileStatusFlag()
{
    char buf[10];
    int n;
    int flags;
    flags = fcntl(STDIN_FILENO, F_GETFL);
    flags |= O_NONBLOCK;
    if (fcntl(STDIN_FILENO, F_SETFL, flags) == -1)
    {
        perror("fcntl");
        exit(1);
    }
tryagain:
    n = read(STDIN_FILENO, buf, 10);
    if (n < 0)
    {
        if (errno == EAGAIN)
        {
            sleep(1);
            write(STDOUT_FILENO, MSG_TRY, strlen(MSG_TRY));
            printf("polling...\n");
            goto tryagain;
        }
        perror("read stdin");
        exit(1);
    }
    write(STDOUT_FILENO, buf, n);
}

/*
以下程序通过命令行的第一个参数指定一个文件描述符，同时利用 Shell 的重定向功能在该描述符上打开文件，然后用 fcntl 的 F_GETFL命令取出 File Status Flag 并打印。

    示例 1：./a.out 0 < /dev/tty
        控制终端(/dev/tty，如果当前进程有控制终端(Controlling Terminal)的话，那么/dev/tty就是当前进程的控制终端的设备特殊文件。）
        Shell在执行a.out时将它的标准输入重定向到/dev/tty，并且是只读的。argv[1]是0，因此取出文件描述符0（也就是标准输入）的File Status Flag，
        用掩码O_ACCMODE取出它的读写位，结果是O_RDONLY。
        注意，Shell的重定向语法不属于程序的命令行参数，这个命行只有两个参数，argv[0]是"./a.out"，argv[1]是"0"，重定向由Shell解释，
        在启动程序时已经生效，程序在运行时并不知道标准输入被重定向了。

    示例 2：./a.out 1 > temp.foo
        Shell在执行a.out时将它的标准输出重定向到文件temp.foo，并且是只写的。程序取出文件描述符1的File Status Flag，发现是只写的，于是打印write only，
        但是打印不到屏幕上而是打印到temp.foo这个文件中了。
*/
void echoFileStatus(int argc, char const *argv[])
{

    for (size_t i = 0; i < argc; i++)
    {
        printf("args index %zd is %s \n", i, argv[i]);
    }

    int val;
    if (argc != 2)
    {
        fputs("usage: a.out <descriptor#>\n", stderr);
        exit(1);
    }
    if ((val = fcntl(atoi(argv[1]), F_GETFL)) < 0)
    {
        printf("fcntl error for fd %d\n", atoi(argv[1]));
        exit(1);
    }
    switch (val & O_ACCMODE)
    {
    case O_RDONLY:
        printf("read only");
        break;
    case O_WRONLY:
        printf("write only");
        break;
    case O_RDWR:
        printf("read write");
        break;
    default:
        fputs("invalid access mode\n", stderr);
        exit(1);
    }
    if (val & O_APPEND)
        printf(", append");
    if (val & O_NONBLOCK)
        printf(", nonblocking");
    putchar('\n');
}


int main(int argc, char const *argv[])
{
    // echoFileStatus(argc, argv);
    changeFileStatusFlag();
    return 0;
}
