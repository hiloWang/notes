#pragma once

#include <cstdlib>
#include <cstdio>
#include <ctime>
#include <iostream>
#include <algorithm>
#include <cassert>

using namespace std;

namespace SortTestHelper {

    /**
     * 在堆中创建一个整型数组
     *
     * @param n  n为长度
     * @param range_l  数组中允许的最小值
     * @param range_r 数组中允许的最大值
     * @return  返回整型指针
     */
    int *generateRandomArray(int n, int range_l, int range_r) {

        //参数检测
        assert(range_l <= range_r);
        //申请数组
        int *arr = new int[n];
        /*随机数种子*/
        srand(time(NULL));
        //随机赋值
        for (int i = 0; i < n; i++) {
            arr[i] = rand() % (range_r - range_l + 1) + range_l;
        }

        return arr;
    }

    /**
     * 在堆中创建一个整型数组，数组元素之间是近乎有序的
     */
    int *generateNearlyOrderedArray(int n, int swapTimes) {

        int *arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }

        srand(time(NULL));

        for (int i = 0; i < swapTimes; i++) {
            int posx = rand() % n;
            int posy = rand() % n;
            swap(arr[posx], arr[posy]);
        }

        return arr;
    }

    int *copyIntArray(int a[], int n) {
        int *arr = new int[n];
        copy(a, a + n, arr);
        return arr;
    }

    template<typename T>
    void printArray(T arr[], int n) {
        for (int i = 0; i < n; i++) {
            cout << arr[i] << " ";
        }
        cout << endl;
        return;
    }

    /**
     * 函数是否是有序的，并且顺序位从小到大。
     *
     * @return true 表示有序
     */
    template<typename T>
    bool isSorted(T arr[], int n) {
        for (int i = 0; i < n - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * @tparam T  类型
     * @param sortName 函数名
     * @param sort  排序函数
     * @param arr sort的第一个参数，数组
     * @param n  sort的第二个参数，数组长度
     */
    template<typename T>
    void testSort(const string &sortName, void (*sort)(T[], int), T arr[], int n) {
        //clock()是C/C++中的计时函数，而与其相关的数据类型是clock_t。在MSDN中，查得对clock函数定义如下：
        //clock_t clock(void) ;简单而言，就是该程序从启动到函数调用占用CPU的时间。
        clock_t startTime = clock();
        sort(arr, n);
        clock_t endTime = clock();
        cout << sortName << " : " << double(endTime - startTime) / CLOCKS_PER_SEC << " s" << endl;
        assert(isSorted(arr, n));
        return;
    }

};
