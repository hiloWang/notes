#include <stdio.h>
#include "config.h"

#ifndef HAVE_POW
    #include <Lib.h>
#else
    #include <math.h>
#endif

int main() {
    printf("MathLib Version %d.%d\n",MATH_VERSION_MAJOR,MATH_VERSION_MINOR);
    printf("Enter tow number,like 4,4!\n");
    double a;
    int b;
    scanf("%lf,%d", &a, &b);
    double result;

#ifdef USE_MYMATH
    printf("use our math\n");
    result = power(a, b);
#else
    printf("use std math\n");
    result = pow(a,b);
#endif

    printf("pow(a,b) = %lf", result);
    return 0;
}