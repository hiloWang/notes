/*
 ============================================================================

 Description : 找到数组中指定字符串的位置

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define NUM(a) (sizeof(a)/sizeof(*a))

int searchKeyTable(const char *table[], const int size,
                    const char *key, int *pos) {

    if (table == NULL || key == NULL || pos == NULL) {
        return -1;
    }

    int i = 0;
    int tmp = -1;
    for (i = 0; i < size; i++) {
        if (strcmp(table[i], key) == 0) {
            tmp = i;
            break;
        }
    }

    if (tmp != -1) {
        *pos = tmp + 1;
    } else {
        return -2;
    }

    return 0;
}

int main() {
    //指针数组
    //它是数组，每个元素都是指针
    char *keywords[] = {
            "while",
            "case",
            "static",
            "do"
    };

    //求元素个数
    //#define NUM(a) (sizeof(a)/sizeof(*a))
    //int n = sizeof(keywords) / sizeof(*keywords);
    //n = NUM(keywords)
    //n = sizeof(keywords) / sizeof(*keywords)
    int pos = 0;
    int ret = searchKeyTable(keywords, NUM(keywords), "while", &pos);

    if (ret != 0) {
        return ret;
    }

    printf("pos == %d\n", pos);


    printf("\n");
    system("pause");
    return 0;
}