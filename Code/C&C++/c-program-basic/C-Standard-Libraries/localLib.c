/*
 ============================================================================

 Author      : Ztiany
 Description : local.h系列函数

 ============================================================================
 */

// locale.h 头文件定义了特定地域的设置，比如日期格式和货币符号。库中的结构体 lconv 用于存储各种具体的值

#include <locale.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
    //读取地域化信息。
    struct lconv *env = localeconv();

    printf("decimal_point=%s\n", env->decimal_point);//用于非货币值的小数点字符。
    printf("mon_decimal_point=%s\n", env->mon_decimal_point);//用于货币值的小数点字符。
    printf("currency_symbol=%s\n", env->currency_symbol);//用于货币的本地符号。
    printf("positive_sign=%s\n", env->positive_sign);//用于正货币值的字符。
    printf("thousands_sep=%s\n", env->thousands_sep);//用于非货币值的千位分隔符。
}