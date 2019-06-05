/*
 ============================================================================

 Author      : Ztiany
 Description : 预处理——条件编译

 ============================================================================
 */

#include <stdio.h>

#define NUM 100
#define PI 3.14

int main() {
//#if、#elif、#else 和 #endif 都是预处理命令，整段代码的意思是：
// 如果宏 _WIN32 的值为真，就保留第 4、5 行代码，删除第 7、9 行代码；如果宏 __linux__ 的值为真，就保留第 7 行代码；如果所有的宏都为假，就保留第 9 行代码。
#if _WIN32//if 后面需要的是一个表达式，_WIN32已经是一个表达式了，所以直接使用
    printf("windows->http://c.biancheng.net\n");
#elif __linux__
    printf("linux->[22;31mhttp://c.biancheng.net");
#else
    printf("unknow->http://c.biancheng.net\n");
#endif

//使用if，要求的是表达式
#if NUM == 100 || NUM == 10
    printf("NUM right\n");
#else
    printf("NUM error\n");
#endif

//使用ifdef，后面跟的是宏名
#ifdef NUM
    printf("NUM has defined\n");
#else
    printf("NUM has not defined\n");
#endif

//条件流程：可以看到下面语句打印了三次
#define CA //如果定义CA，下面就不会执行
#ifndef CA
#define CA
    printf("CA has defined\n");
    printf("CA has defined\n");
    printf("CA has defined\n");
#endif

    return 0;
}