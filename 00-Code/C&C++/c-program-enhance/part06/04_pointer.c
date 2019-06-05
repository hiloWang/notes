/*
 ============================================================================

 Description : 结构体嵌套一级指针

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Teacher {
    char *name;
    int age;
} Teacher;

Teacher *getMem(int n) {
    Teacher *p = (Teacher *) malloc(sizeof(Teacher) * 3);
    //Teacher q[3]
    int i = 0;
    char buf[30];
    for (i = 0; i < n; i++) {
        p[i].name = (char *) malloc(30);
        sprintf(buf, "name%d%d%d", i, i, i);
        strcpy(p[i].name, buf);
        p[i].age = 20 + 2 * i;
    }
    return p;
}

int getMem2(Teacher **tmp, int n) {

    if (tmp == NULL) {
        return -1;
    }
    Teacher *p = (Teacher *) malloc(sizeof(Teacher) * 3);
    //Teacher q[3]
    int i = 0;
    char buf[30];
    for (i = 0; i < n; i++) {
        p[i].name = (char *) malloc(30);
        sprintf(buf, "name%d%d%d", i, i, i);
        strcpy(p[i].name, buf);

        p[i].age = 20 + 2 * i;
    }
    *tmp = p;
    return 0;
}

void showTeacher(Teacher *p, int n) {
    int i = 0;
    for (i = 0; i < n; i++) {
        //printf("%s, %d\n", p[i].name, p[i].age);
        printf("%s, %d\n", (*(p + i)).name, p[i].age);
    }
}

void freeTeacher(Teacher *p, int n) {
    int i = 0;
    for (i = 0; i < n; i++) {
        if (p[i].name != NULL) {
            free(p[i].name);
            p[i].name = NULL;
        }
    }

    if (p != NULL) {
        free(p);
        p = NULL;
    }
}

int main(void) {
#if 0
    char *name = NULL;
    name = (char *)malloc(30);
    strcpy(name, "lily");
    printf("name = %s\n", name);

    if (name != NULL)
    {
        free(name);
        name = NULL;
    }


    //1
    Teacher t;
    t.name = (char *)malloc(30);
    strcpy(t.name, "lily");
    t.age = 22;
    printf("name = %s, age = %d\n", t.name, t.age);

    if(t.name != NULL)
    {
        free(t.name);
        t.name = NULL;
    }



    //2
    Teacher *p = NULL;
    p = (Teacher *)malloc(sizeof(Teacher));
    p->name = (char *)malloc(30);
    strcpy(p->name, "lilei");
    p->age = 22;
    printf("name = %s, age = %d\n", p->name, p->age);

    if (p->name != NULL)
    {
        free(p->name);
        p->name = NULL;
    }

    if (p != NULL)
    {
        free(p);
        p = NULL;
    }

#endif


    //3
    Teacher *p = NULL;
    //p = getMem(3);

    int ret = 0;
    ret = getMem2(&p, 3);
    if (ret != 0) {
        return ret;
    }

    showTeacher(p, 3);
    freeTeacher(p, 3);
    p = NULL;


    printf("\n");
    system("pause");
    return 0;
}