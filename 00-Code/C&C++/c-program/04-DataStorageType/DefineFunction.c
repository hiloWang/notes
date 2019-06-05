/*
 ============================================================================

 Author      : Ztiany
 Description : 函数存储类型，使用命令gcc DefineFunction.c LibFun1.c LibFun2.c编译

 ============================================================================
 */

#include <stdio.h>

static void delete_string(char str[], char ch);

int main() {

    extern void enter(char str[]); // 对函数的声明
    extern void print(char str[]); // 对函数的声明

    char c, str[100];
    enter(str);
    scanf("%c", &c);
    delete_string(str, c);
    print(str);
    system("pause");
    return 0;
}

//如果一个函数只能被本文件中其他函数所调用，它称为内部函数。在定义内部函数时，在函数名和函数类型的前面加 static
static void delete_string(char str[], char ch) {//内部函数
    int i, j;
    for (i = j = 0; str[i] != '\0'; i++)
        if (str[i] != ch)
            str[j++] = str[i];
    str[j] = '\0';
}