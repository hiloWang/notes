/*
 ============================================================================
 
 Author      : Ztiany
 Description : ctype.h系列函数

 ============================================================================
 */

//C标准库的 ctype.h 头文件提供了一些函数，可用于测试和映射字符。
//这些函数接受int作为参数，int类型的值必须是 EOF 或表示为一个无符号字符。
//如果参数c满足描述的条件，则这些函数返回非零（true）。如果参数c不满足描述的条件，则这些函数返回零。

#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <wctype.h>

int main() {
    char a = 'a', b = 'b', s = ' ';
    char n1 = '1', n2 = '2';

    //int isalpha(int c)，该函数检查所传的字符是否是字母。
    printf("a isalpha = %d\n", isalpha(a));

    //int isalnum(int c)，该函数检查所传的字符是否是字母和数字
    printf("isalnum = %d\n", isalnum(a));

    //int isdigit(int c)，该函数检查所传的字符是否是十进制数字。
    printf("isdigit = %d\n", isdigit(a));

    //int isspace(int c)，该函数检查所传的字符是否是空白字符。
    printf("isspace = %d\n", isspace(s));

    //int isupper(int c)，该函数检查所传的字符是否是大写字母。
    printf("isupper = %d\n", isupper(a));

    //int tolower(int c)，该函数把大写字母转换为小写字母。
    printf("tolower = %c\n", toupper(a));


    return EXIT_SUCCESS;
}