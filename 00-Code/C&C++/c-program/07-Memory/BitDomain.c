/*
 ============================================================================
 
 Author      : Ztiany
 Description : 结构体：位域

 ============================================================================
 */

#include <stdio.h>


struct {
    unsigned int widthValidated;
    unsigned int heightValidated;
} status1;//这种结构需要8字节的内存空间

//可以定义变量的宽度来告诉编译器，一个字节有8位
struct {
    unsigned int widthValidated : 2;//使用2位
    unsigned int heightValidated : 2;//使用2位
} status2;

int main() {

    printf("status1宽度 = %d \n", sizeof(status1));// 8
    printf("status2宽度 = %d \n", sizeof(status2));// 4

    //测试位域：Age
    struct {
        unsigned int age : 3;//3位，最大值为7。
    } Age;

    Age.age = 4;
    printf("Sizeof( Age ) : %d\n", sizeof(Age));
    printf("Age.age : %d\n", Age.age);

    Age.age = 7;
    printf("Age.age : %d\n", Age.age);

    Age.age = 8;//，如果您试图使用超过 3 位，则无法完成,此时age=0
    printf("Age.age : %d\n", Age.age);

    //测试位域：无名位域
    struct {
        int a :4;
        int b :3;
        int :1;//无名位域
        int d:8;
    };

    return 0;

}