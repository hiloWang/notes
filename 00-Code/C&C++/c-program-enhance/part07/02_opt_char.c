/*
 ============================================================================

 Description : 按字符读写

 ============================================================================
 */


#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main01(void) {

    fputc('a', stdout); //stdout -> 屏幕, 打印普通信息

    char ch;
    ch = fgetc(stdin); //std -> 键盘
    printf("ch = %c\n", ch);

    //fprintf(stderr, "%c", ch ); //stderr -> 屏幕， 错误信息
    fputc(ch, stderr);

    printf("\n");
    system("pause");
    return 0;
}

int main02(void) {

    FILE *fp = NULL;

    //绝对路径：
    //下面两个等级
    //C:\\Users\\apple\\Documents\\C提高视频\\03.txt，  windows
    //C:/Users/apple/Documents/C提高视频/03.txt，  windows linux

    // "C:\\Users" windows的写法
    // "C:/Users" linux, windows都支持， 建议"/"

    //相对路径：  ./, ../(建议), linux, windows
    //vs: 编译代码时，路径相对于项目工程(当前代码)
    //直接运行可执行程序，路径相对于程序

    char *p = "1234353454364"\
        "lgkjfdljhlkfdjhlfdjk";
    printf("%s\n", p);

    fp = fopen("./03.txt", "r+");
    if (fp == NULL) {
        perror("fopen");
        system("pause");
        return -1;
    }

    if (fp != NULL) {
        fclose(fp);
        fp = NULL;
    }

    printf("\n");
    system("pause");
    return 0;
}

void my_fputc(char *path) {
    FILE *fp = NULL;

    //"w+", 写读方式打开，如果文件不存在，则创建，如果文件存在，清空内容，再写
    fp = fopen(path, "w+");
    if (fp == NULL) {
        //字符串
        perror("my_fputs fopen");
        return;
    }

    //写文件
    char buf[] = "this is a test for fputc";
    int i = 0;
    int n = strlen(buf);
    for (i = 0; i < n; i++) {
        //返回值，成功写入文件的字符
        int ch = fputc(buf[i], fp);
        printf("ch = %c\n", ch);
    }

    if (fp != NULL) {
        fclose(fp);
        fp = NULL;
    }
}

void my_fgetc(char *path) {
    FILE *fp = NULL;
    //读写方式打开，如果文件不存在，打开失败
    fp = fopen(path, "r+");
    if (fp == NULL) {
        perror("my_fgetc fopen");
        return;
    }

    char ch;

//使用feof函数和EOF判断有区别，具体参考 https://stackoverflow.com/questions/36164718/confusion-with-eof-vs-feof 和 http://blog.csdn.net/zangyuanan320/article/details/51582167
//feof（fp）用于测试fp所指向的文件的当前状态是否为“文件结束”。如果是，函数则返回的值是1（真），否则为0（假）。


#if 0//使用EOF判断
    while ((ch = (char) fgetc(fp)) != EOF) {
        printf("%c", ch);
    }
    printf("\n");
#endif

#if 1//使用feof判断
    ch = (char) fgetc(fp);
    while (!feof(fp)) { //文件没有结束
        printf("%c", ch);
        ch = (char) fgetc(fp);
    }
    printf("\n");
#endif

    if (fp != NULL) {
        fclose(fp);
        fp = NULL;
    }
}

int main(void) {
    my_fputc("02.txt");
    my_fgetc("02.txt");

    printf("\n");
    system("pause");
    return 0;
}
