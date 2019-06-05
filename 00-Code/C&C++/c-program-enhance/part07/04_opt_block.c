/*
 ============================================================================
 
 Description : 按块读写

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Stu {
    char name[50];
    int id;
} Stu;

void my_fwrite(char *path) {
    FILE *fp = NULL;
    //读写方式打开，如果文件不存在，创建
    fp = fopen(path, "w+");
    if (fp == NULL) {
        perror("my_fwrite fopen");
        return;
    }

    Stu s[3];
    int i = 0;
    char buf[50];
    for (i = 0; i < 3; i++) {
        sprintf(buf, "stu%d%d%d", i, i, i);
        strcpy(s[i].name, buf);
        s[i].id = i + 1;
    }

    //写文件，按块的方式写
    //s, 写入文件内容的内存首地址
    //sizeof(Stu), 块数据的大小
    //3, 块数， 写文件数据的大小 sizeof(Stu) *3
    //fp, 文件指针
    //返回值，成功写入文件的块数目
    int ret = fwrite(s, sizeof(Stu), 3, fp);
    printf("ret = %d\n", ret);

    if (fp != NULL) {
        fclose(fp);
        fp = NULL;
    }
}

void my_fread(char *path) {
    FILE *fp = NULL;
    //读写方式打开，如果文件不存在，打开失败
    fp = fopen(path, "r+");
    if (fp == NULL) {
        perror("my_fwrite fopen");
        return;
    }

    Stu s[3];

    //读文件，按块的方式读
    //s, 放文件内容的首地址
    //sizeof(Stu), 块数据的大小
    //3, 块数， 读文件数据的大小 sizeof(Stu) *3
    //fp, 文件指针
    //返回值，成功读取文件内容的块数目
    int ret = fread(s, sizeof(Stu), 3, fp);
    printf("ret = %d\n", ret);

    int i = 0;
    for (i = 0; i < 3; i++) {
        printf("s === %s, %d\n", s[i].name, s[i].id);
    }

    if (fp != NULL) {
        fclose(fp);
        fp = NULL;
    }
}

int main(void) {
    my_fwrite("04.txt");
    my_fread("04.txt");

    printf("\n");
    system("pause");
    return 0;
}
