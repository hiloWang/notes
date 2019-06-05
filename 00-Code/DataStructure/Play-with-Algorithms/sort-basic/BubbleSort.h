/*
 ============================================================================
 
 Author      : Ztiany
 Description : 冒泡排序

 ============================================================================
 */


#ifndef PLAY_WITH_ALGORITHMS_BUBBLESORT_H
#define PLAY_WITH_ALGORITHMS_BUBBLESORT_H

#include <algorithm>

/**冒泡排序，O(n^2)*/
template<typename T>
void bubbleSort(T *arr, int size) {

    for (int i = 0; i < size; ++i) {

        //针对冒泡排序还有一个优化，如果再一次冒泡过程中，没有发生任何数组交换，那么该数组肯定是有序的，则可以提前结束冒泡了。
        bool hasSwap = false;

        /*每一次冒泡完毕，最大值出现在最后一个位置*/
        for (int j = 0; j < size - i - 1; ++j) {
            if (arr[j] > arr[j + 1]) {
                std::swap(arr[j], arr[j + 1]);
                if (!hasSwap) {
                    hasSwap = true;
                }
            }
        }
        if (!hasSwap) {
            break;
        }

    }

}

#endif
