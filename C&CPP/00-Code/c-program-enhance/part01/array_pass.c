/*
 ============================================================================
 
 Description : 数组作为函数参数会退化为指针

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>

static void pass_array(int intArr[]) {
    int size = sizeof(intArr);//8
    printf("stack size = %d \n", size);
}

static void array() {
    int intArr[10];
    int size = sizeof(intArr);//40
    int length = size / sizeof(int);
    printf("stack size = %d \n", size);
    printf("length = %d \n", length);
    pass_array(intArr);
}

void sort_array(int a[10], int n) {
    int i, j, tmp;
    //选择法排序
    for (i = 0; i < n - 1; i++) {
        for (j = i + 1; j < n; j++) {
            if (a[i] > a[j]) {//升序
                tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }
        }
    }
}


int main() {
    //传递数组示例
    array();

    //数组选择排序
    int array[] = {1, 4, 52, 76, 888, 43, 4, 2, 9, 43, 8};
    int arrayLength = sizeof(array) / sizeof(array[0]);
    sort_array(array, arrayLength);

    for (int i = 0; i < arrayLength; ++i) {
        printf("%d , ", array[i]);
    }

    return EXIT_SUCCESS;
}