/*
 ============================================================================
 
 Author      : Ztiany
 Description : 使用typedef声明新类型

 ============================================================================
 */


/*

可以使用typedef指定新的类型名来代替已有的类型名：

 定义一个新的类型的方法是：
    1. 先按定义变量的方法写出定义体(int a)
    2. 将变量名换成新的类型名(Count)
    3. 在最前面加上typedef(typedef Count)
    4. 然后可以用新的类型名去定义变量

 其他：
    1. 习惯把typedef声明的类型的第一次字母大写
    2. typedef与define表面上有相似之处，但是本质不一样，define在预编译时处理，而typedef在编译时处理。

 typedef和#define的区别：把typedef 看成一种彻底的“封装”类型，声明之后不能再往里面增加别的东西。

    1. 可以使用其他类型说明符对宏类型名进行扩展，但对 typedef 所定义的类型名却不能这样做。
            #define INTERGE int
            unsigned INTERGE n;  //没问题

            typedef int INTERGE;
            unsigned INTERGE n;  //错误，不能在 INTERGE 前面添加 unsigned

    2. 在连续定义几个变量的时候，typedef 能够保证定义的所有变量均为同一类型，而 #define 则无法保证。
            #define PTR_INT int *
            PTR_INT p1, p2;
            int *p1, p2;//经过宏替换以后

            typedef int * PTR_INT
            PTR_INT p1, p2//p1、p2 类型相同，它们都是指向 int 类型的指针。

 */

#include <stdio.h>
#include <stdlib.h>

//1:简单的使用新的类型名代替原有类型名
typedef int Integer;
typedef float Real;
typedef int Count;

//2:命名一个简单的名称代替复杂的类型表示方法
typedef struct {//给结构体定义一个Date的类型名称
    int year;
    int month;
    int day;
} Date;

Date date = {2009, 10, 10};

//3:声明Num为整型数组类型名
typedef int Num[100];
Num num = {1, 2, 3, 4, 5};

//4:声明String为字符指针名
typedef char *String;
String s = "abc";

//5:声明Pointer为指向函数指针的类型，该函数返回int类型
typedef int (*Pointer)();

Pointer pointer;

//6:定义函数指针
typedef void (*p_function)(int);

int get() {
    return 9;
}

int main() {
    Count a = 3;
    Real b = 3.0F;
    //结构体
    date.day = 10;
    //函数指针别名
    int get();
    pointer = get;
    printf("%d", pointer());
    return EXIT_SUCCESS;
}



