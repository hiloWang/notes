/*
 ============================================================================

 Author      : Ztiany
 Description : signal.h系列函数

 ============================================================================
 */


//signal.h是C标准函数库中的信号处理部分， 定义了程序执行时如何处理不同的信号。
//信号用作进程间通信， 报告异常行为（如除零）、用户的一些按键组合（如同时按下Ctrl与C键，产生信号SIGINT）。
//参考：https://zh.wikipedia.org/wiki/Signal.h

//typedef void (*__p_sig_fn_t)(int); 用typedef定义一个类型void (*__p_sig_fn_t)(int);，这是一个函数指针，接收int，无返回值。

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <signal.h>

void sig_handler(int);

int main() {
    //设置一个函数来处理信号，即带有 sig 参数的信号处理程序,可以理解为回调
    //下面函数表示当发生SIGINT信号时，调用sig_handler函数。Ctrl+C可以发送SIGINT信号
    if (signal(SIGINT, sig_handler) == SIG_ERR) {
        fputs("An error occurred while setting a signal handler.\n", stderr);
        return EXIT_FAILURE;
    }


    int a = 1;
    while (1) {
        printf("start sleep 1 second...\n");
        a++;
        if (a == 5) {
            //raise用于发送一个信号
            int ret = raise(SIGINT);
            if (ret != 0) {
                fputs("Error raising the signal.\n", stderr);
                return EXIT_FAILURE;
            }
        }
        sleep(1);
    }

    return EXIT_SUCCESS;
}

void sig_handler(int signum) {
    printf("capture signal %d and break...\n", signum);
    exit(1);
}