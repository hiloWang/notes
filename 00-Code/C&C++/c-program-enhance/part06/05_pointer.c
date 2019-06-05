/*
 ============================================================================

 Description : 结构体嵌套二级指针

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

//一个导师，有n个学生
typedef struct Teacher {
    int age;
    char **stu; //二维内存
} Teacher;

//n1老师个数，n2每个老师带的学生个数
int createTeacher(Teacher **tmp, int n1, int n2) {
    if (tmp == NULL) {
        return -1;
    }
    Teacher *q = (Teacher *) malloc(sizeof(Teacher) * n1);
    //Teacher q[3]
    int i = 0;
    int j = 0;
    for (i = 0; i < n1; i++) {
        //q[i].stu
        //(q+i)->stu

        q[i].stu = (char **) malloc(n2 * sizeof(char *));
        //char *stu[3]
        for (j = 0; j < n2; j++) {
            //char buf[30]
            q[i].stu[j] = (char *) malloc(30);
            char buf[30];
            sprintf(buf, "name%d%d%d%d", i, i, j, j);
            strcpy(q[i].stu[j], buf);
        }
        q[i].age = 20 + i;
    }

    //间接赋值
    *tmp = q;
    return 0;
}

void showTeacher(Teacher *q, int n1, int n2) {
    if (q == NULL) {
        return;
    }
    int i = 0;
    int j = 0;
    for (i = 0; i < n1; i++) {
        printf("[age=%d]\t", q[i].age);
        for (j = 0; j < n2; j++) {
            printf("%s, ", q[i].stu[j]);
        }
        printf("\n");
    }
    printf("\n");
}

void sortTeacher(Teacher *p, int n) {
    if (p == NULL) {
        return;
    }
    int i = 0;
    int j = 0;
    Teacher tmp;

    for (i = 0; i < n - 1; i++) {
        for (j = i + 1; j < n; j++) {
            if (p[i].age < p[j].age) {//降序
                //交换内存地址
                tmp = p[i];
                p[i] = p[j];
                p[j] = tmp;
            }
        }
    }
}

void freeTeacher(Teacher **tmp, int n1, int n2) {
    if (tmp == NULL) {
        return;
    }
    Teacher *q = *tmp;
    int i = 0;
    int j = 0;
    for (i = 0; i < n1; i++) {
        for (j = 0; j < n2; j++) {
            if (q[i].stu[j] != NULL) {
                free(q[i].stu[j]);
                q[i].stu[j] = NULL;
            }
        }

        if (q[i].stu != NULL) {
            free(q[i].stu);
            q[i].stu = NULL;
        }

    }

    if (q != NULL) {
        free(q);
        q = NULL;
        *tmp = NULL;
    }
}

int main(void) {
#if 0
    char **name = NULL;
    //char *name[3]
    int n = 3;
    int i = 0;
    name = (char **)malloc(n * sizeof(char *));
    //char buf[30]
    for (i = 0; i < n; i++)
    {
        name[i] = (char *)malloc(30);
        strcpy(name[i], "lily");
    }

    for (i = 0; i < n; i++)
    {
        printf("%s\n", name[i]);
    }

    for (i = 0; i < n; i++)
    {
        if (name[i] != NULL)
        {
            free(name[i]);
            name[i] = NULL;
        }
    }

    if (name != NULL)
    {
        free(name);
        name = NULL;
    }

    //1
    Teacher t;
    //t.stu[3]

    //char *t.stu[3]
    int n = 3;
    int i = 0;
    t.stu = (char **)malloc(n * sizeof(char *));
    //char buf[30]
    for (i = 0; i < n; i++)
    {
        t.stu[i] = (char *)malloc(30);
        strcpy(t.stu[i], "lily");
    }

    for (i = 0; i < n; i++)
    {
        printf("%s\n", t.stu[i]);
    }

    for (i = 0; i < n; i++)
    {
        if (t.stu[i] != NULL)
        {
            free(t.stu[i]);
            t.stu[i] = NULL;
        }
    }

    if (t.stu != NULL)
    {
        free(t.stu);
        t.stu = NULL;
    }


    //2
    Teacher *p = NULL;
    //p->stu[3]
    p = (Teacher *)malloc(sizeof(Teacher));

    //char *p->stu[3]
    int n = 3;
    int i = 0;
    p->stu = (char **)malloc(n * sizeof(char *));
    //char buf[30]
    for (i = 0; i < n; i++)
    {
        p->stu[i] = (char *)malloc(30);
        strcpy(p->stu[i], "lily");
    }

    for (i = 0; i < n; i++)
    {
        printf("%s\n", p->stu[i]);
    }

    for (i = 0; i < n; i++)
    {
        if (p->stu[i] != NULL)
        {
            free(p->stu[i]);
            p->stu[i] = NULL;
        }
    }

    if (p->stu != NULL)
    {
        free(p->stu);
        p->stu = NULL;
    }

    if (p != NULL)
    {
        free(p);
        p = NULL;
    }
#endif


    //3
    Teacher *q = NULL;
    int ret = 0;
    ret = createTeacher(&q, 3, 3);
    if (ret != 0) {
        return ret;
    }

    printf("排序前\n");
    showTeacher(q, 3, 3);
    sortTeacher(q, 3);
    printf("\n排序后\n");
    showTeacher(q, 3, 3);
    freeTeacher(&q, 3, 3);

    printf("\n");
    system("pause");
    return 0;
}