/*
 ============================================================================

 Description : 按行读写

 ============================================================================
 */


#include <stdio.h>
#include <stdlib.h>

void my_fputs(char *path) {

    FILE *fp = NULL;

    //"w+", 写读方式打开，如果文件不存在，则创建
    //                     如果文件存在，清空内容，再写
    fp = fopen(path, "w+");
    if (fp == NULL) {
        //字符串
        perror("my_fputs fopen");
        return;
    }

    //写文件
    char *buf[] = {"123456\n", "bbbbbbbbbb\n", "ccccccccccc\n"};
    int i = 0;
    int n = 3;
    for (i = 0; i < n; i++) {
        //返回值，成功,和失败， 成功是0， 失败非0
        int result = fputs(buf[i], fp);
        printf("len = %d\n", result);
    }

    if (fp != NULL) {
        fclose(fp);
        fp = NULL;
    }
}

void my_fgets(char *path) {
    FILE *fp = NULL;
    //读写方式打开，如果文件不存在，打开失败
    fp = fopen(path, "r+");
    if (fp == NULL) {
        perror("my_fgets fopen");
        return;
    }

    char buf[100];

    while (!feof(fp)) { //文件没有结束
        //sizeof(buf), 最大值，放不下，只能放100， 不超过，实际大小存放
        //返回值，成功读取文件内容
        //把“\n”会读取，以“\n”作为换行的标志
        //fgets()读取完毕后，自动加字符串结束符0
        //memset(buf, 'a', sizeof(buf));
        char *p = fgets(buf, sizeof(buf), fp);
        if (p != NULL) {
            printf("buf = %s", buf);
            printf("p = %s", p);
        }
    }

    printf("\n");
    if (fp != NULL) {
        fclose(fp);
        fp = NULL;
    }
}

int main(void) {
    my_fputs("03.txt");
    my_fgets("03.txt");

    printf("\n");
    system("pause");
    return 0;
}