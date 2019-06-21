/*
 ============================================================================
 
 Author      : Ztiany
 Description : 快速排序

 ============================================================================
 */

#ifndef PLAY_WITH_ALGORITHMS_QUICKSORT_H
#define PLAY_WITH_ALGORITHMS_QUICKSORT_H

#include <algorithm>
#include "InsertSort.h"

/**
对arr[left, right]部分进行 partition 操作，返回值 p，使得：
 arr[left , p -1] < arr[p] < arr[p+1, right]
 */
template<typename T>
static int partitionV1(T *arr, int left, int right) {
    T benchmark = arr[left];/*左边为基准*/

    int j = left;//j前面的都比 benchmark 小
    int i = j + 1;//i后面的都比 benchmark 大

    for (; i <= right; i++) {
        if (arr[i] < benchmark) {
            j++;
            std::swap(arr[j], arr[i]);
        }
    }
    //最后把基准和 j 调换位置
    std::swap(arr[left], arr[j]);
    return j;
}

/**
对arr[left, right]部分进行 partition 操作，返回值 p，使得：
 arr[left , p -1] < arr[p] < arr[p+1, right]
 */
template<typename T>
static int partitionV2(T *arr, int left, int right) {
    //随机选择基准，把随机选择的基准换到第一位
    std::swap(arr[left], arr[((rand() % (right - left + 1)) + left)]);

    T benchmark = arr[left];
    int j = left;//j前面的都比 benchmark 小
    int i = j + 1;//i后面的都比 benchmark 大

    for (; i <= right; i++) {
        if (arr[i] < benchmark) {
            j++;
            std::swap(arr[j], arr[i]);
        }
    }
    //最后把基准和 j 调换位置
    std::swap(arr[left], arr[j]);
    return j;
}

/**
对arr[left, right]部分进行 partition 操作，返回值 p，使得：
 arr[left , p -1] < arr[p] < arr[p+1, right]
 */
template<typename T>
static int partitionV3(T *arr, int left, int right) {
    //随机选择基准，把随机选择的基准换到第一位
    std::swap(arr[left], arr[((rand() % (right - left + 1)) + left)]);

    T benchmark = arr[left];
    //arr[left+1, i] <= benchmark <= arr[j, right]
    int j = right;
    int i = left + 1;

    while (true) {
        /*没有碰到比自己大的，就一直往右移动*/
        while (i <= right && arr[i] < benchmark) i++;
        /*没有碰到比自己小的，就一直往左移动*/
        while (j >= left + 1 && arr[j] > benchmark) j--;
        /*
            这里必须是i>j，而不能是 i >= j，比如：
                5 3 4 8 9 2 1 10 11 序列，5 为基准，最终 i 和 j 都会指向 10，这时跳出循环就会导致序列
                没有按照预期的顺序进行排列
         */
        if (i > j) {
            break;
        }
        //都不到了屏障就交换位置
        std::swap(arr[i], arr[j]);
        j--;
        i++;
    }
    //最后把基准和 j 调换位置
    std::swap(arr[left], arr[j]);
    return j;
}


template<typename T>
static void recursionQuickSort(T *arr, int left, int right) {
    //递归结束条件
    // 对于小规模数组, 使用插入排序进行优化
    if (right - left <= 15) {
        insertionSort(arr, left, right);
        return;
    }
    int middle = partitionV3(arr, left, right);
    recursionQuickSort(arr, left, middle);
    recursionQuickSort(arr, middle + 1, right);
}

template<typename T>
void quickSort(T *arr, int size) {
    std::srand(NULL);//随机种子
    recursionQuickSort(arr, 0, size - 1);
}

#endif
