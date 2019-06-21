/*
 ============================================================================

 Author      : Ztiany
 Description : float.h系列函数

 ============================================================================
 */

/*
C 标准库的 float.h 头文件包含了一组与浮点值相关的依赖于平台的常量。这些常量是由 ANSI C 提出的，这让程序更具有可移植性。

    浮点数是由下面四个元素组成：
        S	符号 ( +/- )
        b	指数表示的基数，2 表示二进制，10 表示十进制，16 表示十六进制，等等...
        e	指数，一个介于最小值 e(min) 和最大值 e(max) 之间的整数。
        p	精度，基数 b 的有效位数

        宏命名
            FLT  是指类型 float
            DBL  是指类型 double
            LDBL 是指类型 long double。
 */

#include <float.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
    printf("max float value =%f", FLT_MAX);
    return EXIT_SUCCESS;
}