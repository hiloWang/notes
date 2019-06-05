/*
 ============================================================================
 
 Author      : Ztiany
 Description : 数组遍历

 ============================================================================
 */


#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

#define RETURN_CODE 0

int main(void) {//主函数
    int intArr[5];
    intArr[0] = 4;
    int length = 5;
    //除了第一个元素固定打印为0外，其他元素的值都是不确定的，因为是自动变量
    for (; length >= 0; length--) {
        printf("数组第%d个元素的值是%d\n", length, intArr[length]);
    }
    return RETURN_CODE;
}

