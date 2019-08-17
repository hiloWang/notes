/*
 =========================================================
 Author       ：ztiany 
 Date         ：19-8-17
 Description  ：访问环境
 =========================================================
 */


#include "tlpi_hdr.h"

//https://stackoverflow.com/questions/5582211/what-does-define-gnu-source-imply
#ifndef _GNU_SOURCE
#define _GNU_SOURCE
#endif

#include <unistd.h>

int main(int argc, char *argv[]) {

    char **env = __environ;

    for (; *env != NULL; env++) {
        puts(*env);
    }

    return EXIT_SUCCESS;
}