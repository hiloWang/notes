/*
 =========================================================
 Author       ：ztiany 
 Date         ：19-8-17
 Description  ：打印所有环境变量
 =========================================================
 */


#include "tlpi_hdr.h"

extern char **environ;

int main(int argc, char *argv[]) {

    char **env = environ;

    for (; *env != NULL; env++) {
        puts(*env);
    }

    return EXIT_SUCCESS;
}