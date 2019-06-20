/*
 ============================================================================

 Description : 二级指针做输入：第一种内存模型——一维指针数组

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main01(void) {

    //每个类型都是char *
    char *p0 = "111111111";
    //printf("%s\n", p0);
    char *p1 = "000000000";
    char *p2 = "bbbbbbbbb";
    char *p3 = "aaaaaaaaa";

    //指针数组，指针的数组，它是一个数组，每一个元素都是指针char *
    char *p[] = {"111111111", "000000000", "bbbbbbbbb", "aaaaaaaaa"};
    //p[0] = "111111111"
    int n = sizeof(p) / sizeof(p[0]);
    printf("sizeof(p) = %d, sizeof(p[0]) = %d\n", sizeof(p), sizeof(p[0]));

    int i = 0;
    for (i = 0; i < n; i++) {
        printf("%s\n", p[i]);
    }

    char *q[10] = {"111111111", "000000000", "bbbbbbbbb", "aaaaaaaaa"};
    printf("sizeof(q) = %d, sizeof(q[0]) = %d\n", sizeof(q), sizeof(q[0]));


    printf("\n");
    system("pause");
    return 0;
}

int main02(void) {
    //指针数组，指针的数组，它是一个数组，每一个元素都是指针char *
    char *p[] = {"111111111", "000000000", "bbbbbbbbb", "aaaaaaaaa"};
    //char **q = { "111111111", "000000000", "bbbbbbbbb", "aaaaaaaaa" }; //err
    //p[0] = "111111111"
    int n = sizeof(p) / sizeof(p[0]);
    int i = 0;
    int j = 0;
    char *tmp = NULL;

    printf("排序前：\n");
    for (i = 0; i < n; i++) {
        printf("%s, ", p[i]);
    }
    printf("\n");

    //选择法排序
    for (i = 0; i < n - 1; i++) {
        for (j = i + 1; j < n; j++) {
            if (strcmp(p[i], p[j]) > 0) {
                tmp = p[i];
                p[i] = p[j];
                p[j] = tmp;
            }
        }
    }

    printf("\n排序后：\n");
    for (i = 0; i < n; i++) {
        printf("%s, ", p[i]);
    }
    printf("\n");


    printf("\n");
    system("pause");
    return 0;
}


//void print_array(char *p[], int n)
void print_array(char **p, int n) {
    int i = 0;
    for (i = 0; i < n; i++) {
        printf("%s, ", p[i]);
    }
    printf("\n");
}

void sort_array(char **p, int n) {
    int i, j;
    char *tmp;
    for (i = 0; i < n - 1; i++) {
        for (j = i + 1; j < n; j++) {
            if (strcmp(p[i], p[j]) > 0) {
                tmp = p[i];
                p[i] = p[j];
                p[j] = tmp;
            }
        }
    }
}

int main(void) {
    //指针数组，指针的数组，它是一个数组，每一个元素都是指针char *
    char *p[] = {"111111111", "000000000", "bbbbbbbbb", "aaaaaaaaa"};//不存在内存拷贝：在字符串常量区
    //*p[]本质上是数组，所以可以指向多块内存地址
    //**q本质上是一个指针，所以只能指向一个块内存，下面的写法的错误的
    //char **q = { "111111111", "000000000", "bbbbbbbbb", "aaaaaaaaa" }; //err
    char **q = p;//这是允许的

    //p[0] = "111111111"
    int n = sizeof(p) / sizeof(p[0]);//让数组自己填充时才使用这种方式哦

    printf("排序前:\n");
    print_array(p, n);

    sort_array(p, n);

    printf("\n排序后:\n");
    print_array(p, n);

    printf("\n");
    system("pause");
    return 0;
}