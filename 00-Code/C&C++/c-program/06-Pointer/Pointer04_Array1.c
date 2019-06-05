/*
 ============================================================================
 
 Author      : Ztiany
 Description : 指针数组和多重指针

 ============================================================================
 */


#include <stdio.h>

#define NEWLINE printf("\n")

//指针数组
void pointerArray1() {
    //bookList是一个数组，数组里面的元素都是字符指针(字符串)
    char *bookList[4] = {"CoreJava", "EffectiveJava", "Android", "C ++"};
    for (int i = 0; i < 4; ++i) {
        printf("%s\n", bookList[i]);
    }
    //指向指针的指针：字符串本来就是字符数组，那么字符串名本身也是一个指针，可以理解为现在有一个由指针构成的数组
    //所以字符串数组名是本身是一个指针，同时它指向的也是指针
    char **p = bookList;
    printf("%s", *(p + 1));
}


void pointerArray2() {
    //定义一个整型数组
    int a[5] = {1, 2, 4, 5, 6};
    //定义一个整型指针数组
    int *pa[5] = {&a[0], &a[1], &a[2], &a[3], &a[4]};
    //定义一个指针，指向pa，p是指向指针的指针
    int **p = pa;
    for (int i = 0; i < 5; ++i) {
        printf("%d ", **p);
        p++;//移动的是一个指针的单位，指针的加减都是以指针指向的类型为单位的
    }
    NEWLINE;
}


void pointerArray3() {
    //一个字符指针数组
    char *arr[20];
    //arr本来就是一个指针，而且它指向的也是一个字符指针
    char **parr = arr;
}

//数组指针1
void arrayPointer1() {
    int array[4] = {1, 2, 3, 4};
    //这里int (*a)[4]是一个数组指针，即指针指向的是一个数组
    int (*a)[4] = &array;
    for (int i = 0; i < 4; ++i) {
        printf("array[%d]=%d\n", i, (*a)[i]);
    }
    printf("(*a)[4]的地址 = %p\n", a);
    printf("int array[4]的size = %zd \n", sizeof(array));//4*4=16
}

//数组指针2
void arrayPointer2() {
    int array[][4] = {{1, 2, 3, 4},
                      {5, 6, 7, 8}};
    int (*a)[4] = array;
    a++;//移动的单位是 sizeof(int [4])
    for (int i = 0; i < 4; ++i) {
        printf("array[%d]=%d\n", i, (*a)[i]);
    }
}

int main() {
    pointerArray1();
    pointerArray2();
    pointerArray3();
    arrayPointer1();
    arrayPointer2();
    return 0;
}