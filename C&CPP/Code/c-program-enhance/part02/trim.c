/*
 ============================================================================

 Description : 去掉字符串首位的空字符

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

int trimSample(void) {

    char *p = "      abcdefg      ";
    int begin = 0;
    int end = strlen(p) - 1;
    printf("first begin = %d \n", begin);
    printf("first end = %d \n", end);

    int n = 0;

    //从左边开始
    //如果当前字符为空，而且没有结束
    while (p[begin] == ' ' && p[begin] != 0) {
        begin++; //位置从右移动一位
    }
    printf("last begin = %d \n", begin);

    //如果当前字符为空，而且没有结束
    while (p[end] == ' ' && p[end] != 0) {
        end--; //往左移动
    }
    printf("last end = %d \n", end);

    n = end - begin + 1;
    printf("n = %d\n", n);

    system("pause");
    return 0;
}

int trimLength(char *p, int *n) {
    if (p == NULL || n == NULL) {
        return -1;
    }

    int begin = 0;
    int end = strlen(p) - 1;

    if (end < 0) {
        return -1;
    }

    //从左边开始
    //如果当前字符为空，而且没有结束
    while (isspace((p[begin]) && p[begin] != 0)) {
        begin++; //位置从右移动一位
    }

    //如果当前字符为空，而且没有结束
    while (isspace(p[end]) && p[end] != 0) {
        end--; //往左移动
    }

    *n = end - begin + 1;

    return 0;
}

int trim(char *p, char *buf) {
    if (p == NULL || buf == NULL) {
        return -1;
    }

    int begin = 0;
    int end = strlen(p) - 1;
    int n = 0;

    //从左边开始
    //如果当前字符为空，而且没有结束
    while (p[begin] == ' ' && p[begin] != 0) {
        begin++; //位置从右移动一位
    }

    //如果当前字符为空，而且没有结束
    while (p[end] == ' ' && p[end] != 0) {
        end--; //往左移动
    }

    n = end - begin + 1; //非空元素个数

    strncpy(buf, p + begin, n);
    buf[n] = 0;

    return 0;
}

int main(void) {
    trimSample();

    //调用 trim
    char *p = "      abcddsgadsgefg      ";
    int ret = 0;
    char buf[100] = {0};

    ret = trim(p, buf);
    if (ret != 0) {
        return ret;
    }
    printf("buf = %s\n", buf);

    system("pause");
    return 0;
}