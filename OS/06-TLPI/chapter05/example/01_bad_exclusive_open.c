/*
 =========================================================
 Author       ：ztiany 
 Date         ：19-8-6
 Description  ：以独占的方式创建文件（错误方式）
 =========================================================
 */


#include <fcntl.h>
#include <sys/stat.h>
#include "tlpi_hdr.h"

/*
 * 使用步骤：
 *
 * 1. 第一个进程：./Code text_file sleep &
 * 2. 第二个进程：./Code text_file
 *
 * & 符号表示在后台运行命令
 *
 * 结果两个进程都会认为自己以独占的方式创建了新的文件。
 */
int main(int argc, char *argv[]) {


    int fd;

    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        usageErr("%s file\n", argv[0]);
    }

    fd = open(argv[1], O_WRONLY);

    if (fd != -1) {
        printf("PID %ld File \"%s\" already exist\n", (long) getpid(), argv[1]);
    } else {

        if (errno != ENOENT) {//文件已经存在，独占方式创建文件失败。
            errExit("open");
        } else {
            printf("PID %ld File \"%s\" doesn't exist\n", (long) getpid(), argv[1]);
            if (argc > 2) {//如果多余两个参数，让该进程睡眠5秒钟，这里是为了模拟独占方式创建文件失败。
                sleep(5);
                printf("PID %ld Done sleeping\n", (long) getpid());
            }
            fd = open(argv[1], O_CREAT | O_WRONLY, S_IRUSR | S_IWUSR);
            if (fd == -1) {
                errExit("open");
            }
            printf("PID %ld Created File \"%s\" exclusively\n", (long) getpid(), argv[1]);

        }

    }

    return EXIT_SUCCESS;
}
