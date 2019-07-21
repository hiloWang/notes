//
// Created by ztiany on 19-7-21.
//


#include <sys/stat.h>
#include <fcntl.h>
#include <ctype.h>
#include "tlpi_hdr.h"


int main(int argc, char *argv[]) {
    size_t len;
    off_t offset;
    int fd, ap, j;
    char *buf;
    size_t numRead, numWritten;

    //check the args
    if (argc < 3 || strcmp(argv[1], "--help") == 0) {
        usageErr("%s file {r<length>|R<length>|w<string>|s<offset>}...\n", argv[0]);
    }

    //open file
    /*rw-rw-rw-*/
    mode_t perms = S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH;
    fd = open(argv[1], O_RDWR | O_CREAT, perms);
    if (fd == -1) {
        errExit("open");
    }

    //execute
    for (ap = 2; ap < argc; ++ap) {
        switch (argv[ap][0]) {

            /*读指令*/
            case 'r':
            case 'R': {
                /*获取r/R后面指定的长度，比如 r1000 会返回 1000*/
                len = getLong(&argv[ap][1], GN_ANY_BASE, argv[ap]);
                //申请对应长度的内存
                buf = malloc(len);

                //读取数据
                numRead = read(fd, buf, len);
                if (numRead == -1) {
                    errExit("read");
                }

                //按照参数打印读到的数据
                if (numRead == 0) {
                    printf("%s: end-of-file\n", argv[ap]);
                } else {
                    printf("%s: ", argv[ap]);
                    for (j = 0; j < numRead; ++j) {
                        if (argv[ap][0] == 'r') {
                            printf("%c", isprint((unsigned int) buf[j]) ? buf[j] : '?');
                        } else {
                            printf("%02x", (unsigned int) buf[j]);
                        }
                    }
                    printf("\n");
                }

                free(buf);
                break;
            }

                /*写指令*/
            case 'w': {
                /*第0ge是w，从第一个开始写*/
                numWritten = write(fd, &argv[ap][1]/*取第一个元素地址*/, strlen(&argv[ap][1]));
                if (numWritten == -1) {
                    errExit("write");
                }
                printf("%s: wrote %ld bytes\n", argv[ap], (long) numWritten);
                break;
            }

                /*seek 指令*/
            case 's': {
                offset = getLong(&argv[ap][1], GN_ANY_BASE, argv[ap]);
                if (lseek(fd, offset, SEEK_SET) == -1) {
                    errExit("lseek");
                }
                printf("%s: seek succeeded\n", argv[ap]);
                break;
            }

                /*默认分支*/
            default: {
                cmdLineErr("Argument must start with [rRws]: %s\n", argv[ap]);
            }

        }//end switch

    }//end for
}