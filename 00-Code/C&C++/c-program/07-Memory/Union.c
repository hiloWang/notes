/*
 ============================================================================

 Author      : Ztiany
 Description : C语言联合体

 ============================================================================
 */
#include <stdio.h>

union UA {
    int i;
    float j;
};

void unionParameter(union UA ua);
void unionSample();

int main() {
    unionSample();
    return 0;
}

void unionSample() {
    //联合体中的所以数据都放在同一个地址开始的内存单元中。每一瞬间，联合体只能存储其中一个成员
    union Date {
        int i;
        char ch;
        float f;
    };

    union Date date1;//联合体所占内存大小与其成员中类型最大的成员一样。
    date1.f = 3.4F;
    printf(" f = %f\n", date1.f);//结果正确
    date1.i = -10;
    printf(" f = %f\n", date1.f);//结果错误

    //定义联合体的其他方式
    union Date date2 = {3};//默认对第一个成员进行初始化
    union Date date3 = {.ch = 'd'};

    //C99 允许类型相同的联合体相互复制
    date1 = date2;
    //C99之前不允许联合体作为非指针函数的参数，C99后允许联合体作为函数的参数
    union UA ua = {3};
    unionParameter(ua);
}

void unionParameter(union UA ua) {
    printf(" %d ", ua.i);
}