/*
 ============================================================================
 
 Author      : Ztiany
 Description : 插入排序

 ============================================================================
 */

#ifndef PLAY_WITH_ALGORITHMS_INSERTSORT_H
#define PLAY_WITH_ALGORITHMS_INSERTSORT_H

#include <algorithm>

/**插入排序，O(n^2)，插入排序每次有机会提前结束第二次循环*/
template<typename T>
void insertionSortV1(T *arr, int length) {
    int j;
    /*第一个for循环从1开始，因为认为第0个元素自己就是有序的*/
    for (int i = 1; i < length; ++i) {
        //不断与前面位置上的元素进行比较，如果小就往前移。
        for (j = i; j > 0 && arr[j] < arr[j - 1]; j--) {
            std::swap(arr[j], arr[j - 1]);
        }
    }
}

/**插入排序，O(n^2)，插入排序每次有机会提前结束第二次循环，优化：把swap操作简化为赋值操作。*/
template<typename T>
void insertionSortV2(T *arr, int length) {
    int j;
    /*第一个for循环从1开始，因为认为第0个元素自己就是有序的*/
    for (int i = 1; i < length; ++i) {
        //右边无序第一个数
        T temp = arr[i];
        //不断与前面位置上的元素进行比较。
        for (j = i; j > 0 && arr[j - 1] > temp; j--) {
            //有序序列往右边移
            arr[j] = arr[j - 1];
        }
        arr[j] = temp;
    }
}

/** 对arr[l...r]范围的数组进行插入排序 */
template<typename T>
void insertionSort(T arr[], int l, int r) {
    for (int i = l + 1; i <= r; i++) {
        T e = arr[i];
        int j;
        for (j = i; j > l && arr[j - 1] > e; j--) {
            arr[j] = arr[j - 1];
        }
        arr[j] = e;
    }
}

#endif //PLAY_WITH_ALGORITHMS_INSERTSORT_H
