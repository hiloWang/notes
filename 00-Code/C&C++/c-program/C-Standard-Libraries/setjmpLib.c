/*
 ============================================================================

 Author      : Ztiany
 Description : setjmp.h系列函数

 ============================================================================
 */

//setjmp.h是C标准函数库中提供“非本地跳转”的头文件：控制流偏离了通常的子程序调用与返回序列。互补的两个函数setjmp与longjmp提供了这种功能。

#include <stdio.h>
#include <setjmp.h>

//jmp_buf是库中定义的结构体
static jmp_buf buf;

void second() {
    printf("second\n");         // 打印
    longjmp(buf, 1);            // 跳回setjmp的调用处 - 使得setjmp返回值为1
}

void first() {
    second();
    printf("first\n");          // 不可能执行到此行
}

int main() {
    if (!setjmp(buf)) {
        first();                // 进入此行前，setjmp返回0
    } else {                    // 当longjmp跳转回，setjmp返回1，因此进入此行
        printf("main\n");       // 打印
    }
    return 0;
}