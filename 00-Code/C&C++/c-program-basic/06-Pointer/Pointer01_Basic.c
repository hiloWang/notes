/*
 ============================================================================

 Author      : Ztiany
 Description : 指针基本概念

 ============================================================================
 */

#include <stdio.h>

static void pointerSample();
static void errorSample();
static void findMax();
static void swapMax();
static void swap(int *, int *);
static void constPointer();
static void pointerSize();

int main() {
    pointerSample();
    pointerSize();
    errorSample();
    findMax();
    swapMax();
    constPointer();
    return 0;
}

//常量与指针
static void constPointer() {
    int a = 4;
    const int b = 5;

    //常量指针:指向常量的指针，不能通过*cpb1给b再次赋值
    int const *cpb1 = &b;
    const int *cpb2 = &b;

    //指针常量,pc不能再指向其他值
    int* const pc = &a;

    //指向常量的常指针：
    const int* const cpca = &a;
    const int* const cpcab = &b;
}

//获取最大值
static void swapMax() {
    int a, b, *p1, *p2;
    //https://stackoverflow.com/questions/3420629/what-is-the-difference-between-sscanf-or-atoi-to-convert-a-string-to-an-integer
    scanf("%d %d", &a, &b);
    p1 = &a;
    p2 = &b;
    if (a < b) {
        swap(p1, p2);
    }
    printf("较大的数为 %d, 较小的数为 %d\n", a, b);
}

//替换两个整数的值
static void swap(int *a, int *b) {
    int temp = *a;
    *a = *b;
    *b = temp;
}

//获取最大值
static void findMax() {
    int *p1, *p2, a, b;
    scanf("%d %d", &a, &b);
    p1 = &a;
    p2 = &b;
    if (a < b) {
        p1 = &b;
        p2 = &a;
    }
    printf("较大的数为 %d, 较小的数为 %d\n", *p1, *p2);
}


//指针的大小
static void pointerSize() {
    int *pi[10];
    char *pc[10];
    float *pf[10];
    double *pd[10];
    struct Student {
        int age;
        char *name;
        char *address;
        int id;
    };

    struct Student student = {1, "Ztiany", "China", 001};

    printf("int *p[10]的size = %d\n", sizeof(pi));//8*10=80
    printf("char *p[10]的size = %d\n", sizeof(pc));//8*10=80
    printf("float *p[10]的size = %d\n", sizeof(pf));//8*10=80
    printf("double *p[10]的size = %d\n", sizeof(pd));//8*10=80
    printf("student的size = %d\n", sizeof(student));//32
    printf("&student的size = %d\n", sizeof(&student));//8
}

//定义与使用指针
static void pointerSample() {
    int a = 4;
    //取a的地址赋值给指针aP
    int *pa = &a;
    printf("指针 pa 的地址= %p\n", pa);
    printf("指针 pa 的值 = %d\n", *pa);
    *pa = 7;//取 pa 的地址赋值，也就是给a赋值
    printf("变量 a = %d\n", a);
}
