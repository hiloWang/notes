/*
 ============================================================================

 Author      : Ztiany
 Description : assert.h系列函数

 ============================================================================
 */

//C标准库的 assert.h 头文件提供了一个名为 assert 的宏，它可用于验证程序做出的假设，并在假设为假时输出诊断消息。

#include <assert.h>

int main() {
    int a = 4;
    int b = 5;
    /*
     Expression: a > b

        This application has requested the Runtime to terminate it in an unusual way.
        Please contact the application's support team for more information.
     */
    assert(a > b);
}