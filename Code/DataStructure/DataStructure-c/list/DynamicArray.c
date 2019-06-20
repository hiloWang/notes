/*
 ============================================================================

 Author      : Ztiany
 Description : 动态数组

 ============================================================================
 */

#include "DynamicArray.h"


//初始化
Dynamic_Array *Init_Array() {
    Dynamic_Array *dynamic_array = malloc(sizeof(Dynamic_Array));
    //初始化
    dynamic_array->size = 0;
    dynamic_array->capacity = 20;
    dynamic_array->pAddr = malloc(dynamic_array->capacity * sizeof(int));
    return dynamic_array;
}

//插入
void PushBack_Array(Dynamic_Array *arr, int value) {
    if (arr == NULL) {
        return;
    }

    if (arr->size == arr->capacity) {
        //扩容
        int *newSpace = malloc(arr->capacity * 2 * sizeof(int));
        //拷贝
        memcpy(newSpace, arr->pAddr, arr->capacity * sizeof(int));
        //释放
        free(arr->pAddr);
        //重新赋值
        arr->pAddr = newSpace;
        arr->capacity *= 2;
    }
    arr->pAddr[arr->size] = value;
    arr->size++;
}

//根据位置删除
void RemoveByPos_Array(Dynamic_Array *arr, int pos) {
    if (arr == NULL) {
        return;
    }
    if (pos < 0 || pos >= arr->size) {
        return;
    }
    for (int i = pos; i < arr->size; ++i) {
        arr->pAddr[pos] = arr->pAddr[pos + 1];
    }
    arr->size--;
}

//根据值删除
void RemoveByValue_Array(Dynamic_Array *arr, int value) {
    int pos = Find_Array(arr, value);
    if (pos != -1) {
        RemoveByPos_Array(arr, pos);
    }
}

//查找
int Find_Array(Dynamic_Array *arr, int value) {
    if (arr == NULL) {
        return -1;
    }
    int pos = -1;
    for (int i = 0; i < arr->size; ++i) {
        if (value == arr->pAddr[i]) {
            pos = i;
            break;
        }
    }
    return pos;
}

//打印
void Print_Array(Dynamic_Array *arr) {
    if (arr == NULL) {
        return;
    }
    for (int i = 0; i < arr->size; i++) {
        printf("%d ", arr->pAddr[i]);
    }
    printf("\n");
}

//释放动态数组的内存
void FreeSpace_Array(Dynamic_Array *arr) {
    if (arr == NULL) {
        return;
    }
    if (arr->pAddr != NULL) {
        free(arr->pAddr);
    }
    free(arr);
}

//清空数组
void Clear_Array(Dynamic_Array *arr) {
    if (arr == NULL) {
        return;
    }
    //pAddr -> 空间
    arr->size = 0;
}

//获得动态数组容量
int Capacity_Array(Dynamic_Array *arr) {
    if (arr == NULL) {
        return -1;
    }
    return arr->capacity;
}

//获得动态数据当前元素个数
int Size_Array(Dynamic_Array *arr) {
    if (arr == NULL) {
        return -1;
    }
    return arr->size;
}

//根据位置获得某个位置元素
int At_Array(Dynamic_Array *arr, int pos) {
    return arr->pAddr[pos];
}
