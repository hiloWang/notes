/*
 ============================================================================
 
 Author      : Ztiany
 Description : 获取字符串

 ============================================================================
 */

#include <stdlib.h>
#include <stdio.h>

static void scanfSample() {
    char str[100];
    //scanf一次只能读取一个单词
    //输入 ab cd只能获取到ab
    scanf("%s", str);
    puts(str);
}

static void getsSample1() {
    char str[100];
    //gets是不安全的函数，如果用户输入超过100个，会发生缓冲区溢出
    gets(str);
    puts(str);
}

static void fgetsSample() {
    const int SIZE = 20;
    char worlds[SIZE];
    fgets(worlds, SIZE, stdin);
    printf("worlds = %s", worlds);
    fputs(worlds, stdout);
}

static void getsSample2() {
    char str[100];
    char *result;
    //如果gets读取到文件结尾会返回NULL，一般在标准输入中Ctrl+Z或Ctrl+D表示文件结尾信号
    while ((result = gets(str)) != NULL) {
        printf("your enter is = %s\n", result);
    }
}


int main() {
    //fgetsSample();
    //scanfSample();
    //getsSample1();
    getsSample2();
    return EXIT_SUCCESS;
}