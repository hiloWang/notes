/*
 ============================================================================

 Author      : Ztiany
 Description : stdarg.h系列函数

 ============================================================================
 */

//stdarg.h 头文件定义了一个变量类型 va_list 和三个宏，这三个宏可用于在参数个数未知（即参数个数可变）时获取函数中的参数。

#include <stdlib.h>
#include <stdio.h>
#include <stdarg.h>

static int sum(int num_args, ...) {
    int val = 0;
    va_list ap;

    int i;

    va_start(ap, num_args);
    for (i = 0; i < num_args; i++) {
        val += va_arg(ap, int);
    }
    va_end(ap);

    return val;
}

int main() {
    printf("sum(10,20,30)=%d\n", sum(3, 10, 20, 30));
    printf("sum(4,20,25,30)=%d\n", sum(4, 4, 20, 25, 30));
}