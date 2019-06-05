/*
 ============================================================================

 Author      : Ztiany
 Description : Linux 文件与I/O：open/close/creat/unlink

 ============================================================================
 */

/*
open/close 系统调用。

open函数可以打开或创建一个文件。
    #include <sys/types.h>
    #include <sys/stat.h>
    #include <fcntl.h>

    int open(const char *pathname, int flags);
    int open(const char *pathname, int flags, mode_t mode);
    返回值：成功返回新分配的文件描述符，出错返回-1并设置errno

close函数关闭一个已打开的文件：
    #include <unistd.h>

    int close(int fd);
    返回值：成功返回0，出错返回-1并设置errno

具体参考：https://akaedu.github.io/book/ch28s03.html
*/

#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>

void openFile()
{
    int fd = open("test.txt", O_RDWR | O_CREAT);
    printf("new file fd = %d\n", fd);
    int ret = close(fd);
    printf("close new file ret = %d\n", ret);
}

void deleteFile()
{
    //成功则返回0, 失败返回-1, 错误原因存于errno
    int ret = unlink("test.txt");
    printf("delete new file ret = %d\n", ret);
    perror("delete file error");
}

int main(int argc, char const *argv[])
{
    openFile();
    // deleteFile();
    return EXIT_SUCCESS;
}