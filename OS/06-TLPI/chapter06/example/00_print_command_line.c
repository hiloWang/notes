/*
 =========================================================
 Author       ：ztiany 
 Date         ：19-8-17
 Description  ：打印命令行
 =========================================================
 */

#include "tlpi_hdr.h"

int main(int argc, char *argv[]) {
    int j;

    for (j = 0; j < argc; j++){
        printf("argv[%d] = %s\n", j, argv[j]);
    }

    exit(EXIT_SUCCESS);
}

 
