/*
 ============================================================================

 Author      : Ztiany
 Description : 指针的运算

 ============================================================================
 */

#include <stdio.h>

static void sample1() {
    char a[26] = "abcdefghijklmnopqretuvwxyz";
    for (int i = 0; i < 4; ++i) {
        printf("a[%d]=%c\n", i, a[i]);
    }
    //强制类型转换为int类型指针，此时
    int *ptr = (int *) a;
    printf("*ptr = %c\n", *ptr);//%c输出低8位
    ptr++;//一次跳过四个字节
    printf("*ptr = %c\n", *ptr);//%c输出低8位
}

static void sample2() {
    int array[20] = {1};
    int *pa = array;
    //每个元素自加1
    for (int i = 0; i < 20; ++i) {
        (*pa)++;//取值自加
        pa++;//移动到下一个位置
    }
    for (int j = 0; j < 20; ++j) {
        printf("%d ", array[j]);
    }
    printf("\n");
}

static void sample3() {
    char a[20] = "You_are_a_girl";
    char *p = a;
    char **ptr = &p;//读取p的位置，此时ptr是指针的指针
    printf("**ptr=%c\n", **ptr);
    //移动sizeof(char*)个单位
    ptr++;//这是不合理的操作，因为ptr指向的也是地址，操作ptr将导致指针指向未知的内存区域。
    printf("**ptr=%d\n", **ptr);//可能打印位置值也可能导致程序错误
}

int main() {
    sample1();
    sample2();
    sample3();
    return 0;
}