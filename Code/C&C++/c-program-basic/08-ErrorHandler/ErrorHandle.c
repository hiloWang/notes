/*
 ============================================================================
 
 Author      : Ztiany
 Description : 错误处理

 ============================================================================
 */
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <limits.h>

static void testStrtol() {

    char *str = "12abc";
    //若 endPtr 不为NULL，则会将遇到的不符合条件而终止的字符指针由 endPtr 传回；若 endPtr 为 NULL，则表示该参数无效，或不使用该参数。
    char *endPtr;
    long val = strtol(str, &endPtr, 10);  //10 的意思是 10 进制

    //如果无法转换
    if (endPtr == str) {
        fprintf(stderr, "No digits were found\n");
        exit(EXIT_FAILURE);
    }

    // 如果整型溢出了
    if ((errno == ERANGE && (val == LONG_MAX || val == LONG_MIN))) {
        fprintf(stderr, "ERROR: number out of range for LONG\n");
        exit(EXIT_FAILURE);
    }

    // 如果是其它错误
    if (errno != 0 && val == 0) {
        perror("strtol");
        exit(EXIT_FAILURE);
    }

    printf("success, ret = %ld and endPrt = %s", val, endPtr);//success, ret = 12 and endPrt = abc
}

//像atoi(), atof(), atol() 或是 atoll() 这样的函数是不会设置 errno的，而且，还说了，如果结果无法计算的话，行为是 undefined。
static void testAtoi() {
    char *str = "abc";
    //'atoi' used to convert a string to an integer value, but function will not report conversion errors; consider using 'strtol' instead
    int ret = atoi(str);
    atoll("11");
    printf("atoi = %d\n", ret);
}

int main() {
    testAtoi();
    testStrtol();
    return EXIT_SUCCESS;
}