/*
 ============================================================================
 
 Description : 函数指针

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

//指针函数
//()优先级比*高，它是函数，返回值是指针类型的函数
//返回指针类型的函数
int *fun2() {
    int *p = (int *) malloc(sizeof(int));
    return p;
}

int fun(int a) {
    printf("a ========== %d\n", a);
    return 0;
}

int main(void) {
    //函数指针，它是指针，指向函数的指针
    //定义函数指针变量有3种方式


    //1、先定义函数类型，根据类型定义指针变量（不常用）
    //有typedef是类型，没有事变量
    typedef int FUN(int a); //FUN函数类型
    FUN *p1 = NULL; //函数指针变量
    //p1 = &fun;
    p1 = fun; //p1 指向 fun函数
    fun(5); //传统调用
    p1(6); //函数指针变量调用方式

    //2、先定义函数指针类型，根据类型定义指针变量（常用）
    typedef int(*PFUN)(int a); //PFUN, 函数指针类型
    PFUN p2 = fun; //p2 指向 fun
    p2(7);

    //3、直接定义函数指针（常用）
    int (*p3)(int a) = fun;
    p3(8);

    int (*p4)(int a);
    p4 = fun;
    p4(9);

    printf("\n");
    system("pause");
    return 0;
}

