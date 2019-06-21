/*
 ============================================================================

 Description : 字符串拷贝

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main01(void) {
    char src[] = "abcedfdgds";
    char dst[100] = {0};
    int i = 0;
    
    for (i = 0; src[i] != '\0'; i++) {
        dst[i] = src[i];
    }
    //补结束符
    dst[i] = 0;

    printf("%s\n", dst);

    printf("\n");
    system("pause");
    return 0;
}

void my_strcpy(char *dst, char *src) {
    int i = 0;

    for (i = 0; *(src + i) != '\0'; i++) {
        *(dst + i) = *(src + i); //dst[i] = src[i]
    }
    //补结束符
    //dst[i] = 0;
    *(dst + i) = 0;
}

void my_strcpy2(char *dst, char *src) {
    while (*src != 0) {
        *dst = *src;
        dst++;
        src++;
    }
    *dst = 0;
}

void my_strcpy3(char *dst, char *src) {
    //*dst = *src
    //dst++， src++
    //判断 *dst是否为0, 为0跳出循环
    while (*dst++ = *src++) {
        NULL;
    }

}

//成功为0，失败非0
//1、判断形参指针是否为NULL
//2、最好不要直接使用形参
int my_strcpy4(char *dst, char *src) {

    if (dst == NULL || src == NULL) {
        return -1;
    }

    //辅助变量把形参接过来
    char *to = dst;
    char *from = src;

    //*dst = *src
    //dst++， src++
    //判断 *dst是否为0, 为0跳出循环
    while (*to++ = *from++) {
        NULL;
    }

    printf("my_strcpy4: dst = %s\n", dst);

    return 0;
}

int main(void) {
    char src[] = "abcedfdgds";
    char dst[100] = {0};
    int ret = 0;

    ret = my_strcpy4(dst, src);
    if (ret != 0) {
        printf("my_strcpy4 err:%d\n", ret);
        return ret;
    }
    printf("%s\n", dst);


    printf("\n");
    system("pause");
    return 0;
}

