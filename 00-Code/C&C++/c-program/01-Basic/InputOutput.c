/*
 ============================================================================
 
 Author      : Ztiany
 Description : 输入输出

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>

//按下Ctrl+Z作为结束信号
static void getcharEOF() {
    int ch;
    //如果输入abc，则会连续调用3次
    //getchar读取到文件结尾返回EOF
    while ((ch = getchar()) != EOF) {
        putchar(ch);
        printf(" --%c-- \n", ch);
    }
}

static void scanfReturn(){
    int result;
    //当用户输入的不是数字，ret=0
    int ret = scanf("%d", &result);
    printf("ret = %d\n", ret);
    printf("result = %d\n", result);
}

static void printfSample1(){
    printf("%-20s\n", "a");//指定打印宽度，并左对齐
    printf("%20s\n", "a");//指定打印宽度，并右对齐
    printf("%+d\n", 32);//添加正负符号
    printf("%+d\n", -32);//添加正负符号
    printf("% d\n", 32);
    printf("% d\n", -32);
    printf("%o\n", 32);//转为八进制
}

static void scanfSample2(){
    char result [20];
    //指定值读取10个字符
    scanf("%10s", result);
    printf("%s\n", result);
}

int main() {
    //getcharEOF();
    //scanfReturn();
    //printfSample1();
    //scanfSample2();
    return EXIT_SUCCESS;
}