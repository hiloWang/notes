/*
 ============================================================================

 Author      : Ztiany
 Description : 预处理

 ============================================================================
 */

// ============================================================================
//预处理：引入文件
#include <stdio.h>

// ============================================================================
//预处理：宏定义
#define EXIT_SUCCESS 0
#define EXIT_FAILURE 1
#define PI 3.14159//定义宏 PI
#undef PI//undef用于取消宏定义

#define M (n*n+3*n)//定义一个宏，n是谁，取决于在哪使用这个宏，如果上下文中没有n，那么编译错误
#define SQUARE(x) ((x)*(x)) // 定义一个带参数的表达式，求平方，SQUARE(x) 是宏名， ((x)*(x)) 是宏内容，宏内容和内部参数一定要用括号括起来。

// \符号是一个宏延续，一个宏通常写在一个单行上。但是如果宏太长，一个单行容纳不下，则使用宏延续运算符（\）
// 字符串常量化运算符（#）,在宏定义中，当需要把一个宏的参数转换为字符串常量时，则使用字符串常量化运算符（#）
#define  MESSAGE_FOR(a, b)  \
    printf(#a " and " #b " Together !\n")

//标记粘贴运算符（##），宏定义内的标记粘贴运算符（##）会合并两个参数。它允许在宏定义中两个独立的标记被合并为一个标记。
#define TOKEN_PASTER(n) printf ("token" #n " = %d\n", token##n)

//defined() 运算符：预处理器 defined 运算符是用在常量表达式中的，用来确定一个标识符是否已经使用 #define 定义过。
// 如果指定的标识符已定义，则值为真（非零）。如果指定的标识符未定义，则值为假（零）。
#if !defined(MY_NAME)
#define MY_NAME  "Ztiany"
#endif

#define PR(...) printf(__VA_ARGS__)//__VA_ARGS__是一个预定义宏，表示可变参数

#define NEWLINE printf("-------------------------------------------------------\n")

void printPredefinedMacros() {
    //ANSI C 定义了许多宏。在编程中您可以使用这些宏
    printf("File :%s\n", __FILE__);
    printf("Date :%s\n", __DATE__);
    printf("Time :%s\n", __TIME__);
    printf("Line :%d\n", __LINE__);
    printf("ANSI :%d\n", __STDC__);
}

int main() {
    //使用M
    int n = 10;
    int m = M;
    printf("n =10 , M =  %d\n", m);

    //使用SQUARE
    int a = SQUARE(10);
    printf("SQUARE(10) = %d\n", a);

    //使用MESSAGE_FOR
    MESSAGE_FOR("Ztiany", "Beautifully Grail");

    //使用TOKEN_PASTER
    int token100 = 10000;
    TOKEN_PASTER(100);

    //使用MY_NAME
    char *name = MY_NAME;
    printf("MY_NAME = %s\n", name);

    //使用NEWLINE
    NEWLINE;

    //使用PR
    PR("Hello PR\n");
    PR("name=%s,age=%d\n","Ztiany",27);

    //打印预定义宏
    printPredefinedMacros();
    return EXIT_SUCCESS;
}




