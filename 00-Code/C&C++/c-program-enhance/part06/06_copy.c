/*
 ============================================================================

 Description : 深拷贝与浅拷贝

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Teacher {
    char *name;
    int age;
} Teacher;


int main(void) {
    //浅拷贝：结构体中嵌套指针，而且动态分配空间，同类型结构体变量赋值不同结构体成员指针变量指向同一块内存
    Teacher t1;
    t1.name = (char *) malloc(30);
    strcpy(t1.name, "lily");
    t1.age = 22;

    Teacher t2;
    t2 = t1;


    //深拷贝：人为增加内容，重新拷贝一下
    t2.name = (char *) malloc(30);
    strcpy(t2.name, t1.name);


    printf("[t2]%s, %d\n", t2.name, t2.age);

    if (t1.name != NULL) {
        free(t1.name);
        t1.name = NULL;
    }

    if (t2.name != NULL) {
        free(t2.name);
        t2.name = NULL;
    }


    printf("\n");
    system("pause");
    return 0;
}