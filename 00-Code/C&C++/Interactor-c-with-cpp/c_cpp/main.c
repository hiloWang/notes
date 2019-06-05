/*
 ============================================================================
 
 Author      : Ztiany
 Description : C 调用 CPP

 ============================================================================
 */
#include <stdlib.h>
#include <stdio.h>
#include "CppLibrary.h"

//C中调用 C++ 函数：extern "C"需要放在 C++ 头文件中。

int main() {
    int a = 10;
    int b = 90;
    int ret = add(a, b);
    printf("c call cpp result = %d", ret);
    return EXIT_SUCCESS;
}