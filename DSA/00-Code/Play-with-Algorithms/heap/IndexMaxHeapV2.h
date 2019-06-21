/*
 ============================================================================
 
 Author      : Ztiany
 Description : 最大索引堆

 ============================================================================
 */


#ifndef PLAY_WITH_ALGORITHMS_INDEXMAXHEAPV2_H
#define PLAY_WITH_ALGORITHMS_INDEXMAXHEAPV2_H


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
class IndexMaxHeapV2 {

private:
    Item *data;
    int capacity{};
    int count{};
    //indexes 用来维持 data 中元素在堆中的索引
    int *indexes;//  最大索引堆中的索引, indexes[x] = i 表示索引 i 指向的数组中的值在堆中的位置是 x
    int *reverse; // 最大索引堆中的反向索引, reverse[i] = x 表示数组中索引 i 指向的值，在 indexes 中的索引是 x

    void shiftUP(int index) {
        while (index > 1) {
            int p = parent(index);
            //indexes 中维护的才是堆，需要从 indexes 取元素的索引
            if (data[indexes[p]] > data[indexes[index]]) {
                break;
            }
            std::swap(indexes[index], indexes[p]);
            //维护 reverse
            //假设现在 indexes[index] = 3，那么 data[3] 的值在 indexes 中的索引就是 3，那么 reverse[3] = index。
            reverse[indexes[index]] = index;
            reverse[indexes[p]] = p;
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
                //假设现在 indexes[index] = 3，那么 data[3] 的值在 indexes 中的索引就是 3，那么 reverse[3] = index。
                reverse[indexes[index]] = index;
                reverse[indexes[target]] = target;
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
        int *tempReverse = new int[newCapacity + 1];

        for (int i = 1; i <= count; ++i) {
            temp[i] = data[i];
            tempIndexes[i] = indexes[i];
            tempReverse[i] = reverse[i];
        }

        delete[] data;
        this->data = temp;

        delete[] indexes;
        this->indexes = tempIndexes;

        delete[] reverse;
        this->reverse = tempReverse;

        this->capacity = newCapacity;
    }

public:
    /*explicit 表示我不是拷贝构造函数*/
    explicit IndexMaxHeapV2(int capacity) {
        data = new Item[capacity + 1];
        indexes = new int[capacity + 1];
        reverse = new int[capacity + 1];
        this->capacity = capacity;
        count = 0;
    }

    IndexMaxHeapV2() : IndexMaxHeapV2{15} {
    }

    ~IndexMaxHeapV2() {
        delete[] data;
        delete[] indexes;
        delete[] reverse;
    }

    void insert(Item item) {
        /*index+=1 的原因是，对于用户而言，索引从 0 开始，而内部实现是从 1 开始*/
        if (count >= capacity) {
            resize(capacity * 2);
        }
        count++;
        data[count] = item;//加入到最后的位置
        // 维护 reverse
        indexes[count] = count;//先放入count位置，稍后会进行shiftUP操作
        reverse[count] = count;//data[indexes[reverse[count]]] == data[count]
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

        reverse[indexes[count]] = 0;//count被移除了
        count--;

        if (count) {//count != 0
            reverse[indexes[1]] = 1;
            shiftDown(1);
        }
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
        assert(contain(index));
        return data[index + 1];
    }

    // 将最大索引堆中索引为i的元素修改为newItem
    void change(int index, Item newItem) {
        assert(contain(index));
        /*index+=1 的原因是，对于用户而言，索引从 0 开始，而内部实现是从 1 开始*/
        index += 1;
        data[index] = newItem;
        // 因为 data[index] = newItem = data[indexes[reverse[index]]]，所以 index = indexes[reverse[index]]，则 index 在 indexes 中的位置就是 reverse[index]
        // 之后shiftUp(j), 再shiftDown(j)，这是为了维护堆的性质，因为change的这个数是无法确定的，所以两个操作都要试一下。
        shiftUP(reverse[index]);
        shiftDown(reverse[index]);
    }

    // 看索引i所在的位置是否存在元素
    bool contain(int index) {
        /*index+1 的原因是，对于用户而言，索引从 0 开始，而内部实现是从 1 开始*/
        assert(index + 1 >= 1 && index + 1 <= count);
        return reverse[index + 1] != 0;
    }

};

#endif
