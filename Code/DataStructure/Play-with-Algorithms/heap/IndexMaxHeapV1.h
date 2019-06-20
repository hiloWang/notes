/*
 ============================================================================
 
 Author      : Ztiany
 Description : 最大索引堆

 ============================================================================
 */


#ifndef PLAY_WITH_ALGORITHMS_INDEXMAXHEAPV1_H
#define PLAY_WITH_ALGORITHMS_INDEXMAXHEAPV1_H


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
class IndexMaxHeapV1 {

private:
    Item *data;
    int capacity{};
    int count{};
    //indexes 用来维持 data 中元素在堆中的索引
    int *indexes{};

    void shiftUP(int index) {
        while (index > 1) {
            int p = parent(index);
            //indexes 中维护的才是堆，需要从 indexes 取元素的索引
            if (data[indexes[p]] > data[indexes[index]]) {
                break;
            }
            std::swap(indexes[index], indexes[p]);
            index = p;
        }
    }

    void shiftDown(int index) {
        while (leftChild(index) <= count) {
            int target = leftChild(index);
            if (target + 1 <= count && data[indexes[target + 1]] > data[indexes[target]]) {
                target++;
            }
            //target 是左右两个子节点中大的那个。
            if (data[indexes[index]] < data[indexes[target]]) {
                std::swap(indexes[index], indexes[target]);
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
        int *tempIndexes = new int[newCapacity + 1];
        for (int i = 1; i <= count; ++i) {
            temp[i] = data[i];
            tempIndexes[i] = indexes[i];
        }
        delete[] data;
        delete[] indexes;
        this->data = temp;
        this->indexes = tempIndexes;
        this->capacity = newCapacity;
    }

public:
    /*explicit 表示我不是拷贝构造函数*/
    explicit IndexMaxHeapV1(int capacity) {
        data = new Item[capacity + 1];
        indexes = new int[capacity + 1];
        this->capacity = capacity;
        count = 0;
    }

    IndexMaxHeapV1() : IndexMaxHeapV1{15} {
    }

    ~IndexMaxHeapV1() {
        delete[] data;
        delete[] indexes;
    }

    void insert(Item item) {
        /*index+=1 的原因是，对于用户而言，索引从 0 开始，而内部实现是从 1 开始*/
        if (count >= capacity) {
            resize(capacity * 2);
        }
        count++;
        data[count] = item;
        indexes[count] = count;
        shiftUP(count);
    }

    bool isEmpty() {
        return count <= 0;
    }

    int getSize() {
        return count;
    }

    /*如果返回引用，那么返回的将会是局部变量的引用，所以还是返回拷贝吧*/
    Item extractMax() {
        if (isEmpty()) {
            throw std::out_of_range("heap is empty");
        }
        Item item = data[indexes[1]];
        std::swap(indexes[1], indexes[count]);
        count--;
        shiftDown(1);
        /*索引堆是不能缩容的，因为 extractMax 的不是数组中最后的元素*/
        return item;
    }

    Item getMax() {
        if (isEmpty()) {
            throw std::out_of_range("heap is empty");
        }
        return data[indexes[1]];
    }

    // 获取最大索引堆中索引为 index 的元素
    Item getItem(int index) {
        /*index+1 的原因是，对于用户而言，索引从 0 开始，而内部实现是从 1 开始*/
        assert(index + 1 >= 1 && index + 1 <= count);
        return data[index + 1];
    }

    // 将最大索引堆中索引为i的元素修改为newItem
    void change(int index, Item newItem) {
        assert(index + 1 >= 1 && index + 1 <= count);
        /*index+=1 的原因是，对于用户而言，索引从 0 开始，而内部实现是从 1 开始*/
        index += 1;
        data[index] = newItem;
        // 找到indexes[j] = i, j表示data[i]在堆中的位置
        // 之后shiftUp(j), 再shiftDown(j)，这是为了维护堆的性质，因为change的这个数是无法确定的，所以两个操作都要试一下。
        for (int j = 1; j <= count; j++) {
            if (indexes[j] == index) {
                shiftUP(j);
                shiftDown(j);
                return;
            }
        }
    }

};

#endif
