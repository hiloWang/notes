/*
 ============================================================================
 
 Author      : Ztiany
 Description :  读写二进制

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>

#define FILE4 "H:\\codes\\C\\C-File\\file\\test4"
#define SIZE 3

typedef struct {
    char name[20];
    int num;
    int age;
    char address[20];
} Student;

Student students[10];

int main() {
    //输入学生数据
    for (int i = 0; i < SIZE; ++i) {
        scanf("%s%d%d%s", students[i].name, &students[i].num, &students[i].age, students[i].address);
    }
    for (int j = 0; j < SIZE; ++j) {
        printf("%s %d %d %s \n", students[j].name, students[j].num, students[j].age, students[j].address);
    }

    //写学生数据
    FILE *fp = fopen(FILE4, "w");
    if (fp == NULL) {
        exit(0);
    }
    for (int k = 0; k < SIZE; ++k) {
        if (fwrite(&students[k], sizeof(Student), 1, fp) != 1) {
            printf("write file error...\n");
        }
    }
    fclose(fp);


    //读二进制学生数据
    FILE *file = fopen(FILE4, "r");
    if (file == NULL) {
        exit(0);
    }
    for (int l = 0; l < SIZE; ++l) {
        fread(&students[l], sizeof(Student), 1, file);
        printf("%-10s %4d %4d %-15s\n", students[l].name, students[l].num, students[l].age, students[l].address);
    }
    fclose(file);

}
