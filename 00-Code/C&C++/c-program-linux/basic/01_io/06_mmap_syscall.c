/*
 ============================================================================

 Author      : Ztiany
 Description : Linux 文件与I/O：mmap 函数

 ============================================================================
 */

/*
mmap可以把磁盘文件的一部分直接映射到内存，这样文件中的位置直接就有对应的内存地址，对文件的读写可以直接用指针来做而不需要 read/write 函数。

    #include <sys/mman.h>

    void *mmap(void *addr, size_t len, int prot, int flag, int filedes, off_t off);
    int munmap(void *addr, size_t len);

具体参考：https://akaedu.github.io/book/ch28s08.html
*/

#include <stdlib.h>
#include <error.h>
#include <stdio.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <unistd.h>

/*
第一步：
    $ vi hello
    （编辑该文件的内容为“hello”）
    $ od -tx1 -tc hello 
    0000000 68 65 6c 6c 6f 0a
            h   e   l   l   o  \n
    0000006

第二步：执行下面函数：
    如下程序操作这个文件（注意，把fd关掉并不影响该文件已建立的映射，仍然可以对文件进行读写）。然后查看修改后的文件

第三步：
    $ od -tx1 -tc hello
    0000000 33 32 31 30 6f 0a
            3   2   1   0   o  \n
    0000006
*/
void mapFile()
{
    int *p;
    int fd = open("hello.txt", O_RDWR);
    if (fd < 0)
    {
        perror("open file: hello.txt");
        exit(EXIT_FAILURE);
    }

    p = mmap("hello.txt", 6, PROT_WRITE, MAP_SHARED, fd, 0);
    if (p == MAP_FAILED)
    {
        perror("mmap file: hello.txt");
        exit(1);
    }

    close(fd);
    p[0] = 0x30313233;
    munmap(p, 6);
}

int main(int argc, char const *argv[])
{
    mapFile();
    return 0;
}
