/*
 ============================================================================

 Author      : Ztiany
 Description : 打开关闭文件

 ============================================================================
 */


#include <stdio.h>
#include <stdlib.h>

/*注意，程序运行在build目录下,理清相对路径*/
//#define FILE1 "./test1.txt"
#define FILE1 ".\\test1.txt"

FILE *openFile(char *);
void readFile(FILE *file);
void closeFile(FILE *file);

int main() {
    FILE *fp = openFile(FILE1);
    //文件属于系统资源，用完需要关闭文件，其次，由于文件缓冲区的存在，不关闭文件直接退出程序有可能造成文件的数据丢失。
    readFile(fp);
    closeFile(fp);
    return 0;
}


FILE *openFile(char *fileName) {
    //fopen参数说明(文件名，打开的模式)
    FILE *fp;
    if ((fp = fopen(fileName, "r")) == NULL) {
        printf("open file error \n");
        //退出
        exit(0);
    } else {
        printf("open file success \n");
    }
    return fp;
}

void readFile(FILE *file) {
    printf("---------------file content start\n");
    //读取buff
    const int buffSize = 50;
    char buff[buffSize]; //缓冲
    while (fgets(buff, buffSize, file)) {
        printf("%s", buff);
    }
    printf("\n");
    printf("---------------file content end\n");
}


void closeFile(FILE *file) {
    int result = fclose(file);
    if (result == 0) {
        printf("file close success");
    } else if (result == EOF) {
        printf("file close error");
    }
}

