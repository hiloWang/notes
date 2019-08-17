/*
 =========================================================
 Author       ：ztiany 
 Date         ：19-8-17
 Description  ：获取某个环境变量
 =========================================================
 */


#include "tlpi_hdr.h"

int main(int argc, char *argv[]) {


    char *val;

    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        usageErr("%s environ-var\n", argv[0]);
    }

    val = getenv(argv[1]);

    printf("%s\n", (val != NULL) ? val : "No such variable");

    return EXIT_SUCCESS;
}