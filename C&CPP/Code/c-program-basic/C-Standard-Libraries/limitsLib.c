/*
 ============================================================================

 Author      : Ztiany
 Description : limits.h系列函数

 ============================================================================
 */

//limits.h 头文件决定了各种变量类型的各种属性。定义在该头文件中的宏限制了各种变量类型（比如 char、int 和 long）的值。

#include <limits.h>
#include <stdlib.h>
#include <stdio.h>

int main() {
    printf("max int = %d\n", INT_MAX);
    printf("min int = %d\n", INT_MIN);
    return EXIT_SUCCESS;
}


