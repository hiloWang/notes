/*
 ============================================================================
 Author      : Ztiany
 Description : C语言的数据类型
 ============================================================================
 */

//声明头文件，即应用程序接口，类似Java的导包
#include <stdio.h>
#include <stdlib.h>

//支持布尔类型的头文件
#include <stdbool.h>

//定义常量
#define A 1.3

//const定义常量更灵活，可以定义数组
const int days[3] = {1, 2, 3};

//如果函数写在main后面，需要在main函数前面先声明这个函数
void dataType();
void memoryAddress();

//C程序一定是从主函数开始执行的，main函数的返回值为int类型.
int main() {
    //数据类型与数据类下大小
    dataType();
    //获取内存地址
    memoryAddress();
    return EXIT_SUCCESS;
}

void dataType() {

    //C语言中的数据类型
    char c = 'c';
    short hd = 1233;
    int d = 12312312;
    long sld = 432432;
    long long ld = 232423423;
    unsigned int ud = 323;
    unsigned long uld = 3333;
    float f = 1212.132121F;
    double lf = 232.222222222;
    bool flag = false;//布尔类型，c99添加
    _Bool b1 = 1;//c99添加
    _Bool b2 = 0;//c99添加

    //打印变量
    printf("%c\n", c);
    printf("%ld\n", sld);
    printf("%ld\n", uld);
    printf("%hd\n", hd);
    printf("%d\n", d);
    printf("%d\n", ud);
    printf("%lld\n", ld);
    printf("%d\n", flag);
    printf("%d\n", b1);
    printf("%d\n", b2);
    printf("%f\n", f);
    printf("%lf\n", lf);

    //打印数据类型的长度
    printf("字符的长度是:%zd\n", sizeof(char));
    printf("short的长度是:%zd\n", sizeof(short));
    printf("int的长度是:%zd\n", sizeof(int));
    printf("long的长度是:%zd\n", sizeof(long));
    printf("long long的长度是:%zd\n", sizeof(long long));
    printf("float的长度是:%zd\n", sizeof(float));
    printf("double的长度是:%zd\n", sizeof(double));
    printf("常量A=%f", A);
}

//获取与打印内存地址
void memoryAddress() {
    int cc = 3;
    printf("%p\n", &cc);
}