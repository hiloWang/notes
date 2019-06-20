/*
 ============================================================================
 Author      : Ztiany
 Description : NA1:iso646.h函数库
 ============================================================================
 */

//如果键盘无法输入操作符，可以用iso646.h里面的宏定义代替

#include <iso646.h>
#include <stdlib.h>

int main() {
    int a = 4, b = 4;
    int rst = a bitand b;
    return EXIT_SUCCESS;
}