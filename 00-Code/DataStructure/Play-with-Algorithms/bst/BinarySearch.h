/*
 ============================================================================
 
 Author      : Ztiany
 Description : 二分查找法

 ============================================================================
 */


#ifndef PLAY_WITH_ALGORITHMS_BINARYSEARCH_H
#define PLAY_WITH_ALGORITHMS_BINARYSEARCH_H

#include <cassert>

/**二分查找法, 在有序数组 arr 中, 查找 target，复杂度 O(log n)*/
template<typename T>
int binarySearch(T *arr, int size, T target) {
    int left = 0;
    int right = size - 1;
    int mid{};
    while (left <= right) {
        mid = left + (right - left) / 2;
        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] > target) {
            left = mid + 1;
        } else if (arr[mid] < target) {
            right = mid - 1;
        }
    }
    return -1;
}

/**
 *  二分查找法, 在有序数组arr中, 查找target
 *          如果找到 target , 返回第一个 target 相应的索引 index
 *          如果没有找到 target, 返回比 target 小的最大值相应的索引, 如果这个最大值有多个, 返回最大索引
 *          如果这个 target 比整个数组的最小元素值还要小, 则不存在这个target 的 floor 值, 返回 -1
 */
template<typename T>
int floor(T arr[], int size, T target) {

    assert(size >= 0);

    int left = -1;
    int right = size - 1;
    int mid{};
    //解题思路：先让 right 无限趋近于比 target 小的那个最大值，让 left 无限趋近于 right，结束条件为：l == r
    while (left < right) {
        //因为上面条件是 left < right，而如果按照 left + (right - left) / 2 算，得到的 mid 可能永远小于 right
        //对于 [4, 5]， left + (right - left) / 2 = 4
        //对于 [4, 5]， left + (right - left + 1) / 2 =5
        mid = left + (right - left + 1) / 2;
        if (arr[mid] >= target) {//>= target 的情况都要减少右边界，而且必须是 right 先进行判断
            right = mid - 1;//减少的位置是 1
        } else {
            left = mid;//否则让 left 无限趋紧 right
        }
    }
    //到这里，肯定是 left == right
    assert(left == right);

    //有元素等于 target，就返回该索引
    if (left + 1 < size && arr[left + 1] == target) {
        return left + 1;
    }

    return left;/*默认返回 -1*/
}

/**
 * 二分查找法, 在有序数组 arr 中, 查找 target
 *      如果找到 target, 返回最后一个 target 相应的索引 index
 *      如果没有找到 target, 返回比 target 大的最小值相应的索引, 如果这个最小值有多个, 返回最小的索引
 *      如果这个 target 比整个数组的最大元素值还要大, 则不存在这个 target 的 ceil 值 , 返回整个数组元素个数 n
 */
template<typename T>
int ceil(T arr[], int size, T target) {

    assert(size >= 0);

    int left = 0;
    int right = size;
    int mid{};
    //解题思路：先让 left 无限趋近于比 target 大的那个最小值，让 right 无限趋近于 left，结束条件为：l == r
    while (left < right) {
        mid = left + (right - left) / 2;
        if (arr[mid] <= target) {
            left = mid + 1;
        } else {//arr[mid] > target
            right = mid;
        }
    }
    assert(left == right);

    if (right < size && arr[right] == target) {
        return right;
    }

    if (right - 1 >= 0 && right - 1 < size && arr[right - 1] == target) {
        return right - 1;
    }

    return right;
}

#endif //PLAY_WITH_ALGORITHMS_BINARYSEARCH_H
