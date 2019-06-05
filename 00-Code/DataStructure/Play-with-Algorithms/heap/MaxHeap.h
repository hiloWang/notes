/*
 ============================================================================
 
 Author      : Ztiany
 Description : 最大堆

 ============================================================================
 */

#ifndef PLAY_WITH_ALGORITHMS_MAXHEAP_H
#define PLAY_WITH_ALGORITHMS_MAXHEAP_H

#include <algorithm>
#include <stdexcept>
#include <iostream>
#include <cstdlib>

/**
 * 最大堆实现，根节点索引从 1 开始：
 * 从父节点索引求子节点索引，left = (parent*2)；right = (parent*2)+1
 * 从子节点索引求父节点索引，parent = child/2
 */
template<typename Item>
class MaxHeap {

private:
    Item *data;
    int capacity{};
    int count{};

    void shiftUP(int index) {
        while (index > 1) {
            int p = parent(index);
            if (data[p] > data[index]) {
                break;
            }
            std::swap(data[index], data[p]);
            index = p;
        }
    }

    void shiftDown(int index) {
        while (leftChild(index) <= count) {
            int target = leftChild(index);
            if (target + 1 <= count && data[target + 1] > data[target]) {
                target++;
            }
            //target 是左右两个子节点中大的那个。
            if (data[index] < data[target]) {
                std::swap(data[index], data[target]);
                index = target;
            } else {
                break;
            }
        }
    }

    int parent(int index) {
        return index / 2;
    }

    int leftChild(int index) {
        return index * 2;
    }

    int rightChild(int index) {
        return index * 2 + 2;
    }

    void resize(int newCapacity) {
        Item *temp = new Item[newCapacity + 1];
        for (int i = 1; i <= count; ++i) {
            temp[i] = data[i];
        }
        delete[] data;
        this->data = temp;
        this->capacity = newCapacity;
    }

public:
    /*explicit 表示我不是拷贝构造函数*/
    explicit MaxHeap(int capacity) {
        data = new Item[capacity + 1];
        this->capacity = capacity;
        count = 0;
    }

    MaxHeap() : MaxHeap{15} {
    }

    /*直接将数组转化为最大堆*/
    explicit MaxHeap(Item *arr, int size) {
        data = new Item[size + 1];
        for (int j = 0; j < size; ++j) {
            data[j + 1] = arr[j];
        }
        this->capacity = size;
        count = size;

        /*heapify 从最后一个非叶子节点开始*/
        for (int i = parent(count); i >= 1; --i) {
            shiftDown(i);
        }
    }

    ~MaxHeap() {
        delete[] data;
    }

    void insert(Item item) {

        if (count >= capacity) {
            resize(capacity * 2);
        }
        data[count + 1] = item;
        count++;
        shiftUP(count);
    }

    bool isEmpty() {
        return count <= 0;
    }

    int getSize() {
        return count;
    }

    /*如果返回引用，那么返回的将会是局部变量的引用，索引还是返回拷贝吧*/
    Item extractMax() {
        if (isEmpty()) {
            throw std::out_of_range("heap is empty");
        }
        Item item = data[1];
        std::swap(data[1], data[count]);
        count--;
        shiftDown(1);
        if (count <= capacity / 4 && capacity / 2 > 0) {
            resize(capacity / 2);
        }
        return item;
    }

    Item &getMax() {
        if (isEmpty()) {
            throw std::out_of_range("heap is empty");
        }
        return data[1];
    }

};

#endif //PLAY_WITH_ALGORITHMS_MAXHEAP_H
