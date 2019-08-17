/*
 =========================================================
 Author       ：ztiany 
 Date         ：19-8-17
 Description  ：修改环境变量
 =========================================================
 */


#include <stdlib.h>
#include "tlpi_hdr.h"

extern char **environ;

/**
 * 用法：./Code NAME1=VALUE1 NAME2=VALUE2 ...
 */
int main(int argc, char *argv[]) {
    int j;
    char **ep;

    clearenv();//清空环境

    /*添加命令行指定的环境变量*/
    for (j = 1; j < argc; j++) {
        if (putenv(argv[j]) != 0) {
            errExit("putenv: %s", argv[j]);
        }
    }

    //如果没有 GREET 就添加
    if (setenv("GREET", "Hello world", 0) == -1) {
        errExit("setenv");
    }

    //移除 BYE
    unsetenv("BYE");

    //打印所有环境变量
    for (ep = environ; *ep != NULL; ep++) {
        puts(*ep);
    }

    exit(EXIT_SUCCESS);
}

