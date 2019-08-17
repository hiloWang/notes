/*
 =========================================================
 Author       ：ztiany 
 Date         ：19-8-17
 Description  ：访问环境
 =========================================================
 */


#include "tlpi_hdr.h"

int main(int argc, char *argv[], char *envp[]) {

    char **env = envp;

    for (; *env != NULL; env++) {
        puts(*env);
    }

    return EXIT_SUCCESS;
}