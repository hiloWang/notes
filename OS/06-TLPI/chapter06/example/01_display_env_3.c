/*
 =========================================================
 Author       ：ztiany 
 Date         ：19-8-17
 Description  ：打印所有环境变量
 =========================================================
 */


#include "tlpi_hdr.h"

//define _GNU_SOURCE to get it from <unistd.h>
//https://stackoverflow.com/questions/5582211/what-does-define-gnu-source-imply
#ifndef _GNU_SOURCE
#define _GNU_SOURCE
#endif

extern char **environ;

int main(int argc, char *argv[]) {

    char **env = environ;

    for (; *env != NULL; env++) {
        puts(*env);
    }

    return EXIT_SUCCESS;
}