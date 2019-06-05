/*
 ============================================================================

 Description : 将字符串数组进行排序

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int sort(char **array1, int num1,
         char(*array2)[30], int num2,
         char ***myp3, int *num3) {

    if (array1 == NULL || array2 == NULL || myp3 == NULL || num3 == NULL) {
        return -1;
    }

    //char *buf[]
    int n = num1 + num2;
    char **p = malloc(n * sizeof(char *)); //sizeof(char *)
    if (p == NULL) {
        return -1;
    }

    int i = 0;
    for (i = 0; i < num1; i++) {
        p[i] = malloc(strlen(array1[i]) + 1); //加上字符串结束符字符‘\0’
        if (p[i] != NULL) {
            strcpy(p[i], array1[i]);
        }
    }

    int j = 0;
    for (i = num1, j = 0; i < n; i++, j++) {
        p[i] = malloc(strlen(array2[j]) + 1);
        if (p[i] != NULL) {
            strcpy(p[i], array2[j]);
        }
    }

    //选择法排序，交换的是指针变量的值，即交换的是指针的指向
    char *tmp = NULL;
    for (i = 0; i < n; i++) {
        for (j = i + 1; j < n; j++) {
            if (strcmp(p[i], p[j]) > 0) {
                tmp = p[i];
                p[i] = p[j];
                p[j] = tmp;
            }
        }
    }

    //间接赋值
    *myp3 = p;
    *num3 = n;

    return 0;
}

void printBuf(char **p, int n) {
    int i = 0;
    for (i = 0; i < n; i++) {
        printf("%s\n", p[i]);
    }
}

void freeBuf(char ***p, int n) {
    if (p == NULL) {
        return;
    }

    char **tmp = *p; //还原二级指针
    int i = 0;
    for (i = 0; i < n; i++) {
        if (tmp[i] != NULL) {
            free(tmp[i]);
            tmp[i] = NULL;
        }
    }

    if (tmp != NULL) {
        free(tmp);
        *p = NULL;
    }
}

int main() {
    int ret = 0;
    char *p1[] = {"aa", "ccccccc", "bbbbbb"};
    char buf2[10][30] = {"111111", "3333333", "222222"};
    char **p3 = NULL;
    int len1, len2, len3, i = 0;

    len1 = sizeof(p1) / sizeof(*p1);
    len2 = 3;

    //通过形参改变实参的值
    ret = sort(p1, len1, buf2, len2, &p3, &len3);
    if (ret != 0) {
        printf("sort err: %d\n", ret);
        return ret;
    }

    printBuf(p3, len3);

    freeBuf(&p3, len3);


    printf("\n");
    system("pause");
    return 0;
}