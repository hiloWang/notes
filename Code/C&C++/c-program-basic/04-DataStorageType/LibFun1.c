#include <stdio.h>

void enter(char str[100]) { // 定义外部函数 enter
    fgets(str, 100, stdin); // 向字符数组输入字符串
}