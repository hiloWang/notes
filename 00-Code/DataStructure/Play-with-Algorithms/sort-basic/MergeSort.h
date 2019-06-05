/*
 ============================================================================
 
 Author      : Ztiany
 Description : 归并排序（递归、迭代算法）

 ============================================================================
 */


#ifndef PLAY_WITH_ALGORITHMS_MERGESORT_H
#define PLAY_WITH_ALGORITHMS_MERGESORT_H

#include "InsertSort.h"

//将arr[l,middle] 和 arr[middle+1,r]两部分进行归并
template<typename T>
static void merge(T *arr, int l, int middle, int r) {
    //为什么是r - l + 1，因为都是闭区间
    T aux[r - l + 1];
    //复制原始区间的数据
    for (int i = l; i <= r; ++i) {
        aux[i - l] = arr[i];
    }

    //开始归并
    int i = l, j = middle + 1;
    for (int k = l; k <= r; ++k) {
        if (i > middle) { // 如果左半部分元素已经全部处理完毕
            arr[k] = aux[j - l];
            j++;
        } else if (j > r) { // 如果右半部分元素已经全部处理完毕
            arr[k] = aux[i - l];
            i++;
        } else if (aux[i - l] < aux[j - l]) { // 左半部分所指元素 < 右半部分所指元素
            arr[k] = aux[i - l];
            i++;
        } else { // 左半部分所指元素 >= 右半部分所指元素
            arr[k] = aux[j - l];
            j++;
        }
    }

}

template<typename T>
static void recursionMergeV1(T *arr, int l, int r) {
    //递归结束条件，分组的元素个数为 1
    if (l >= r) {
        return;
    }
    //还可以再继续分组就继续分
    int middle = l + ((r - l) / 2);
    //分组后，各组在进行归并排序
    recursionMergeV1(arr, l, middle);
    recursionMergeV1(arr, middle + 1, r);
    //两组排序之后，就要进行归并操作了
    if (arr[middle] > arr[middle + 1]) {
        merge(arr, l, middle, r);
    }
}

template<typename T>
static void recursionMergeV2(T *arr, int l, int r) {
    if (r - l <= 15/*递归到一定程度后，使用插入排序*/) {
        insertionSort(arr, l, r);
        return;
    }
    //还可以再继续分组就继续分
    int middle = l + ((r - l) / 2);
    //分组后，各组在进行归并排序
    recursionMergeV2(arr, l, middle);
    recursionMergeV2(arr, middle + 1, r);
    //两组排序之后，就要进行归并操作了
    if (arr[middle] > arr[middle + 1]) {
        merge(arr, l, middle, r);
    }
}

template<typename T>
void mergeSort(T *arr, int size) {
    //递归归并
    recursionMergeV2(arr, 0, size - 1);
}

/*自底向上的归并排序算法*/
template<typename T>
void mergeSortBU(T *arr, int size) {
    /*sz 表示每组元素的个数，初始个数为 1*/
    for (int sz = 1; sz < size; sz += sz) {/*对每组进行归并*/
        /* i += sz + sz 意思是每次跳过两组
            5 3 6 4 7 1 8 2
            5 3 6 4//第一个两组，0 - 3
            7 1 8 2//第二个两组，4 - 7
         */
        for (int i = 0; i + sz < size; i += sz + sz) {
            /* i += sz + sz 意思是每次跳过两组，
                假设size = 2
                    5 3 6 4 7 1 8 2
                    5 3 6 4//第一个两组，0 - 1 - 3
                    7 1 8 2//第二个两组，4 - 5 - 7
            */
            merge(arr, i, i + sz - 1, std::min(i + sz + sz - 1, size - 1));
        }
    }
}

// 使用自底向上的归并排序算法
template<typename T>
void mergeSortBUO(T arr[], int n) {

    // Merge Sort Bottom Up 优化
    // 对于小数组, 使用插入排序优化
    for (int i = 0; i < n; i += 16) {
        insertionSort(arr, i, std::min(i + 15, n - 1));
    }

    for (int sz = 16; sz < n; sz += sz) {
        for (int i = 0; i < n - sz; i += sz + sz) {
            // 对于arr[mid] <= arr[mid+1]的情况,不进行merge
            if (arr[i + sz - 1] > arr[i + sz]) {
                merge(arr, i, i + sz - 1, std::min(i + sz + sz - 1, n - 1));
            }
        }
    }

}


#endif //PLAY_WITH_ALGORITHMS_MERGESORT_H
