/*
 ============================================================================

 Author      : Ztiany
 Description : math.h系列函数

 ============================================================================
 */

//math.h 头文件定义了各种数学函数和一个宏。在这个库中所有可用的功能都带有一个 double 类型的参数，且都返回 double 类型的结果。


#include <math.h>
#include <stdlib.h>
#include <stdio.h>

#define PI 3.14


static double toRadians(double angdeg) {
    return angdeg / 180.0 * PI;
}

static double toDegrees(double angrad) {
    return angrad * 180.0 / PI;
}


int main() {
    double a = toRadians(60);
    //返回弧度角 x 的正弦。
    double_t sin_a = sin(a);
    printf("san(30) =%lf", sin_a);
    return EXIT_SUCCESS;
}