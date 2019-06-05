/*
 ============================================================================

 Description : 二级指针做输入：第二种内存模型——二维数组

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main01(void) {
    char a0[30] = "22222222222";
    char a1[30] = "11111111111";
    char a2[30] = "bbbbbbbbbbb";
    char a3[30] = "aaaaaaaaaaaa";

    //4个a[30]的一维数组组成二维数组
    //定义二维数组，不写第一个[]值有条件, 必须要初始化
    //a代表首行地址，首行地址和首行首元素地址有区别，但是他们的值是一样
    //区别：步长不一样
    char a[][30] = {"22222222222", "11111111111", "bbbbbbbbbbb", "aaaaaaaaaaaa"};
    printf("a:%d, a+1: %d\n", a, a + 1);//步长为30

    char b[30];
    printf("&b:%d, &b+1:%d\n", &b, &b + 1);
    printf("b:%d, b+1:%d\n", b, b + 1);

    int n = sizeof(a) / sizeof(a[0]);
    printf("n = %d\n", n);

    //a[0] = "22222222222"
    int i = 0;
    for (i = 0; i < 4; i++) {
        printf("%s\n", a[i]); //首行地址，和首行首元素地址的值是一样
        // a+i, *(a+i)
    }

    printf("\n");
    system("pause");
    return 0;
}

void print_array_err(char **a, int n) {
    printf("a: %d, a+1:%d\n", a, a + 1);
    int i = 0;
    for (i = 0; i < n; i++) {
        //printf("%s\n", a[i]); //首行地址，和首行首元素地址的值是一样
        // a+i, *(a+i)
    }
}

void print_array(char a[][30], int n) {
    //printf("a: %d, a+1:%d\n", a, a + 1);
    int i = 0;
    for (i = 0; i < n; i++) {
        printf("%s, ", a[i]); //首行地址，和首行首元素地址的值是一样

    }
    printf("\n");
}

//这里不能使用char **a，因为存在数组退化，char**a的步长为4
void sort_array(char a[][30], int n) {
    int i = 0;
    int j = 0;
    char tmp[30];

    for (i = 0; i < n - 1; i++) {
        for (j = i + 1; j < n; j++) {
            if (strcmp(a[i], a[j]) > 0) {
                //交换的内存块
                strcpy(tmp, a[i]);
                strcpy(a[i], a[j]);
                strcpy(a[j], tmp);
            }
        }
    }

}

int main(void) {
    //存在内存拷贝：在栈空间
    char a[][30] = {"22222222222", "11111111111", "bbbbbbbbbbb", "aaaaaaaaaaaa"};

    int n = sizeof(a) / sizeof(a[0]);

    printf("before sort:\n");
    print_array(a, n);

    sort_array(a, n);

    printf("\nafter sort:\n");
    print_array(a, n);

    printf("\n");
    system("pause");
    return 0;
}