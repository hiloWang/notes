/*
 =========================================================
 Author       ：ztiany 
 Date         ：19-8-17
 Description  ：用于演示非原子的 append 可能导致的文件内容丢失问题

使用方式：./Code num-bytes [x]

示例：

- 使用 lseek 向文件追加内容
    ./Code f1 1000000 x & ./Code f1 1000000 x

- 使用 O_APPEND 标识向文件追加内容
    ./Code f2 1000000 & ./Code f2 1000000

 提示：& 用于让命令在后台运行，因此后面再接上一个命令的话，可以同时执行多个命令。

 结果：

-rw------- 1 ztiany ztiany 1013479 8月  17 18:38 f1  （出现文件内容丢失）
-rw------- 1 ztiany ztiany 2000000 8月  17 18:37 f2

 =========================================================
 */

#include <tlpi_hdr.h>
#include <sys/stat.h>
#include <fcntl.h>


int main(int argc, char *argv[]) {

    if (argc < 3) {
        usageErr("%s file num-bytes [x]\n"
                 " x means use lseek() instead of O_APPEND\n", argv[0]);
    }

    int numBytes, j, flags, fd;

    Boolean useLseek;

    useLseek = argc > 3;
    flags = useLseek ? 0 : O_APPEND;
    numBytes = getInt(argv[2], 0, "numBytes");

    fd = open(argv[1], O_RDWR | O_CREAT | flags, S_IRUSR | S_IWUSR);

    if (fd == -1) {
        errExit("open");
    }

    for (j = 0; j < numBytes; ++j) {
        if (useLseek) {
            if (lseek(fd, 0, SEEK_END) == -1) {
                errExit("lseek");
            }
        }
        if (write(fd, "x", 1) != 1) {
            errExit("write failed");
        }
    }

    printf("%ld done\n", (long) getpid());

    exit(EXIT_SUCCESS);
}