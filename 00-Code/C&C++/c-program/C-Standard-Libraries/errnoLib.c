/*
 ============================================================================

 Author      : Ztiany
 Description : errno.h系列函数

 ============================================================================
 */

//C 标准库的 errno.h 头文件定义了整数变量 errno，它是通过系统调用设置的，在错误事件中的某些库函数表明了什么发生了错误。

#include <errno.h>
#include <math.h>
#include <stdio.h>

int main() {
    errno = 0;
    double d = sqrt(-1);//负数开根号将导致错误
    printf("%lf\n", d);
    if (errno == EDOM) {
        //本函数产生两个输出，自己的字符串+错误原因(系统提供)。
        perror("EDOM error");
    }
}