/*
 ============================================================================
 
 Author      : Ztiany
 Description : 变量存储类型与作用域

 ============================================================================
 */

#include <stdio.h>
// #include <threads.h>

int global_a = 20;//声明全局变量(外部变量)、它是外链接静态变量
const int const_a = 32;//定义全局常量
static int static_a = 10;//内链接静态变量
// _Thread_local thread_a;//_Thread_local用于并发程序设计

//extern int extern_a = 30;不要使用extern来定义变量
//static extern int static_extern_a = 32;不能同时使用多个存储类别说明符

void defineVariable() {
    extern int static_b;//声明要使用一个定义在别处的变量
    printf("%d", static_b);
    int local_a = 33;//局部变量、自动变量
    static int local_static_a;//静态局部变量，默认初始化为0
    register int register_a = 33;//寄存器变量(请求把改变了存储在寄存器中，但是不一定会成功)，只允许定义局部寄存器变量，无法获取寄存器的内存地址
}

int static_b = 100;//在后面声明全局变量，它上门的函数需要使用的话，需要先使用extern声明

int main() {
    defineVariable();
    return 1;
}