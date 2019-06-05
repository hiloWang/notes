/*
 ============================================================================
 
 Author      : Ztiany
 Description : 随机读写数据

 ============================================================================
 */
#include <stdio.h>
#include <stdlib.h>

#define FILE3 "H:\\codes\\C\\C-File\\file\\test3.txt"
#define FILE4 "H:\\codes\\C\\C-File\\file\\test4"
#define FILE5 "H:\\codes\\C\\C-File\\file\\test5.txt"

void randomSample1();

void randomSample2();

int main() {
//    randomSample1();
    randomSample2();
}

void randomSample2() {
    FILE *file = fopen(FILE4, "r");
    int result = fseek(file, 20, 0);//将文件位置移动到离文件开头20个字节的位置
    printf("%d--", result);
    long cur = ftell(file);
    printf("cur = %d", cur);
}

void randomSample1() {
    FILE *fi = fopen(FILE3, "r");
    FILE *fo = fopen(FILE5, "w");
    if (fi == NULL || fo == NULL) {
        printf("file open error");
        exit(EXIT_SUCCESS);
    }
    while (feof(fi)) {
        printf("%c", getc(fi));
    }
    putchar('\n');

    rewind(fi);//回到开始的位置

    while (feof(fi)) {
        putc(getc(fi), fo);
    }
    fclose(fi);
    fclose(fo);
}