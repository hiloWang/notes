/*
 =========================================================
 Author       ：ztiany 
 Date         ：19-8-17
 Description  ：通过 errno.h 获取第一个命令行参数
 =========================================================
 */


#include "tlpi_hdr.h"

#ifndef _GNU_SOURCE
#define _GNU_SOURCE
#endif

#include <errno.h>

extern char *program_invocation_name;
extern char *program_invocation_short_name;

int main(int argc, char *argv[]) {

    printf("program_invocation_name = %s\n",program_invocation_name);
    printf("program_invocation_short_name = %s\n",program_invocation_short_name);

    return EXIT_SUCCESS;
}