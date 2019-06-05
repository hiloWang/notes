/*
 ============================================================================

 Description : 从键盘输入3个学生的信息（姓名、学号、成绩），存入一个结构体数组中，计算平均分，并按成绩高低排序并输出

 ============================================================================
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Stu {
    char name[50];
    int id;
    double score;
} Stu;

//从键盘输入学生信息
void initStu(Stu *s, int n) {
    int i = 0;
    for (i = 0; i < n; i++) {
        printf("请输入第%d个学生信息：\n", i + 1);
        printf("请输入姓名：");
        scanf("%s", s[i].name);

        printf("请输入学号：");
        scanf("%d", &s[i].id);

        printf("请输入分数：");
        scanf("%lf", &s[i].score);
    }
}

//求平均分
double aveStu(Stu *s, int n) {
    int i = 0;
    double allScore = 0.0;

    for (i = 0; i < n; i++) {
        allScore += s[i].score; //分数累加
    }

    return allScore / (n * 1.0);
}

//分数排序，降序
void sortStu(Stu *s, int n) {
    int i = 0;
    int j = 0;
    Stu tmp;

    for (i = 0; i < n - 1; i++) {
        for (j = i + 1; j < n; j++) {
            if (s[i].score < s[j].score) //降序
            {
                tmp = s[i];
                s[i] = s[j];
                s[j] = tmp;
            }
        }
    }
}

//打印学生信息
void showStu(Stu *s, int n) {
    int i = 0;

    printf("\n学生信息如下：\n");
    printf("姓名\t学号\t分数\n");
    for (i = 0; i < n; i++) {
        printf("%s\t%d\t%lf\n", s[i].name, s[i].id, s[i].score);
    }

}

int main(void) {
    Stu s[3];
    int n = 3;

    //从键盘输入学生信息
    initStu(s, n);

    //aveStu(s, 3)：平均分
    printf("\n平均分为： %lf\n", aveStu(s, n));

    //分数排序，降序
    sortStu(s, n);

    //打印学生信息
    showStu(s, n);

    printf("\n");
    system("pause");
    return 0;
}