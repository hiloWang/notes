/*
 ============================================================================
 
 Author      : Ztiany
 Description : 堆排序

 ============================================================================
 */

#ifndef PLAY_WITH_ALGORITHMS_HEAPSORT_H
#define PLAY_WITH_ALGORITHMS_HEAPSORT_H

#include "MaxHeap.h"

template<typename Item>
void heapSortV1(Item *item, int size) {
    MaxHeap<Item> maxHeap(size);
    for (int i = 0; i < size; ++i) {
        maxHeap.insert(item[i]);
    }
    for (int j = size - 1; j >= 0; --j) {
        /*把值赋值给指针，不会存在拷贝*/
        item[j] = maxHeap.extractMax();
    }
}

/**优化：拷贝 + heapify*/
template<typename Item>
void heapSortV2(Item *item, int size) {
    MaxHeap<Item> maxHeap(item, size);
    for (int j = size - 1; j >= 0; --j) {
        /*把值赋值给指针，不会存在拷贝*/
        item[j] = maxHeap.extractMax();
    }
}

template<typename Item>
static void shiftDown(Item *item, int size, int index) {
    while ((index * 2 + 1) < size) {
        /*找到最大的那个自己点比较*/
        int target = (index * 2 + 1);//默认找左边子节点
        if (target + 1 < size && item[target + 1] > item[target]) {
            target++;
        }
        /*下沉*/
        if (item[target] > item[index]) {
            std::swap(item[index], item[target]);
            index = target;
        } else {
            break;
        }
    }
}

/**优化：原地  heapify，不需要额外的空间*/
template<typename Item>
void heapSortV3(Item *item, int size) {
    //第一步 heapify 操作，堆根节点索引从 0 开始。
    for (int i = (size - 1) / 2; i >= 0; --i) {
        shiftDown(item, size, i);
    }
    //第二步，不断从堆中获取最大元素放在末尾，再使用 shiftDown 维护堆的特性
    for (int j = size - 1; j > 0; --j) {
        std::swap(item[j], item[0]);//每次都把最大值放到之后，把最后那个元素放到最前面
        //除掉之后部分的以排序的最大值，把前面未排序的部分看做一个堆，堆根节点做 heapify 操作。
        shiftDown(item, j, 0);
    }
}

#endif
