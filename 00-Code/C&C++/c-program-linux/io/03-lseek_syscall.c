/*
 ============================================================================

 Author      : Ztiany
 Description : Linux 文件与I/O：lseek

 ============================================================================
 */

/*
每个打开的文件都记录着当前读写位置，打开文件时读写位置是0，表示文件开头，通常读写多少个字节就会将读写位置往后移多少个字节。
但是有一个例外，如果以O_APPEND方式打开，每次写操作都会在文件末尾追加数据，然后将读写位置移到新的文件末尾。lseek和标准I/O库的fseek函数类似，可以移动当前读写位置（或者叫偏移量）。

    #include <sys/types.h>
    #include <unistd.h>

    off_t lseek(int fd, off_t offset, int whence);

具体参考：https://akaedu.github.io/book/ch28s05.html
*/

#include <sys/types.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <stdio.h>

void crateTestFile()
{
    int fd = open("test.txt", O_RDWR | O_CREAT);

    char buff[30];

    strcpy(buff, "Hello");

    write(fd, buff, sizeof(buff));

    close(fd);
}

void testLseek()
{
    int fd = open("test.txt", O_RDWR);
    printf("fd = %d \n", fd);

    long offset = lseek(fd, 0L, SEEK_END);
    printf("offset = %ld \n", offset);
}

int main(int argc, char const *argv[])
{
    testLseek();
    return 0;
}
