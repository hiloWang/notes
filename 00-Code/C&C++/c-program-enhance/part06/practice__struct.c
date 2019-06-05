/*
 ============================================================================

 Description : 重写结构体嵌套一级指针老师和二级指针学生的代码

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

//结构体类型，每个导师有三个学生
typedef struct Teacher {
    char *tName; //导师
    char **stu;  //三个学生
    int age;
} Teacher;

//在createTeacher中分配空间
int createTeacher(Teacher **p/*out*/, int n1, int n2) {
    if (p == NULL) {
        return -1;
    }

    //三个结构体，Teacher t[3]
    Teacher *t = (Teacher *) malloc(sizeof(Teacher) * n1);
    if (t == NULL) {
        return -2;
    }

    int i = 0;
    int j = 0;
    for (i = 0; i < n1; i++) //每个老师的导师，n2个学生分配空间
    {
        //导师名字
        t[i].tName = (char *) malloc(50 * sizeof(char));

        //每个导师有n2个学生
        char **tmp = (char **) malloc(n2 * sizeof(char *)); //char *tmp[i]
        for (j = 0; j < n2; j++) {
            tmp[j] = (char *) malloc(50);
        }

        t[i].stu = tmp; //重要

    }

    //间接赋值是指针存在最大意义
    *p = t;

    return 0;
}

//给成员赋值
void initTeacher(Teacher *p, int n1, int n2) {
    if (p == NULL) {
        return;
    }

    int i = 0;
    int j = 0;
    char buf[50] = {0};

    for (i = 0; i < n1; i++) {
        //导师名字
        sprintf(buf, "teacher%d%d%d", i, i, i);
        strcpy(p[i].tName, buf);

        //每个导师有n2个学生
        for (j = 0; j < n2; j++) {
            sprintf(buf, "stu%d%d%d%d", i, i, j, j);
            strcpy(p[i].stu[j], buf);
        }

        //年龄
        p[i].age = 20 + 2 * i;
    }

}

//打印结构体成员信息
void printTeacher(Teacher *p, int n1, int n2) {
    if (p == NULL) {
        return;
    }

    //先打印导师，再打印学生
    int i = 0;
    int j = 0;
    char buf[50] = {0};

    for (i = 0; i < n1; i++) {

        printf("%s[%d]\n", p[i].tName, p[i].age);

        //每个导师有n2个学生
        for (j = 0; j < n2; j++) {

            printf("\t%s", p[i].stu[j]);
        }
        printf("\n");
    }

}

//根据导师名字排序, 降序
void sortTeacher(Teacher *p, int n) {
    if (p == NULL) {
        return;
    }
    int i = 0;
    int j = 0;
    Teacher tmp;

    //选择法排序
    for (i = 0; i < n - 1; i++) {
        for (j = i + 1; j < n; j++) {
            if (strcmp(p[i].tName, p[j].tName) < 0) //降序
            {
                //交换成员变量的值
                tmp = p[i];
                p[i] = p[j];
                p[j] = tmp;
            }
        }
    }

}

//释放空间，在函数内部把p赋值为NULL
void freeTeacher(Teacher **p, int n1, int n2) {
    if (p == NULL) {
        return;
    }

    Teacher *t = *p;
    int i = 0;
    int j = 0;

    for (i = 0; i < n1; i++) {
        //先释放导师
        if (t[i].tName != NULL) {
            free(t[i].tName);
            t[i].tName = NULL;
        }

        //再释放导师名下的学生
        for (j = 0; j < n2; j++) {
            if (t[i].stu[j] != NULL) {
                free(t[i].stu[j]);
                t[i].stu[j] = NULL;
            }
        }

        if (t[i].stu != NULL) {
            free(t[i].stu);
            t[i].stu = NULL;
        }
    }

    if (t != NULL) {
        free(t);
        *p = NULL;
    }
}

int main(void) {
    int ret = 0;
    int n1 = 3; //导师个数
    int n2 = 3; //学生
    Teacher *p = NULL;

    ret = createTeacher(&p, n1, n2);
    if (ret != 0) {
        printf("createTeacher err:%d\n", ret);
        return ret;
    }

    initTeacher(p, n1, n2); //给成员赋值

    //打印成员，排序前
    printf("排序前：\n");
    printTeacher(p, n1, n2);


    //根据导师名字排序, 降序
    sortTeacher(p, n1);

    //打印成员，排序后
    printf("\n排序后：\n");
    printTeacher(p, n1, n2);

    //释放空间，在函数内部把p赋值为NULL
    freeTeacher(&p, n1, n2);

    printf("\n");
    system("pause");
    return 0;
}