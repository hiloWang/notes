/*
 ============================================================================
 
 Author      : Ztiany
 Description : 并查集测试类

 ============================================================================
 */


#ifndef PLAY_WITH_ALGORITHMS_UNIONFINDTESTHELPER_H
#define PLAY_WITH_ALGORITHMS_UNIONFINDTESTHELPER_H

#include <cstdlib>
#include <ctime>
#include <iostream>
#include "UnionFindBase.h"
#include "UnionFind1.h"
#include "UnionFind2.h"
#include "UnionFind3.h"
#include "UnionFind4.h"
#include "UnionFind5.h"

using namespace std;

namespace UnionFIndTestHelper {

    // 测试第二版本的并查集, 测试元素个数为n
    static void testUF(UnionFindBase &uf, int n, const string &name) {

        srand(time(NULL));

        time_t startTime = clock();

        // 进行n次操作, 每次随机选择两个元素进行合并操作
        for (int i = 0; i < n; i++) {
            int a = rand() % n;
            int b = rand() % n;
            uf.unionElements(a, b);
        }
        // 再进行n次操作, 每次随机选择两个元素, 查询他们是否同属一个集合
        for (int i = 0; i < n; i++) {
            int a = rand() % n;
            int b = rand() % n;
            uf.isConnected(a, b);
        }
        time_t endTime = clock();

        // 打印输出对这2n个操作的耗时
        cout << name << ", " << 2 * n << " ops, " << double(endTime - startTime) / CLOCKS_PER_SEC << " s" << endl;
    }


    // 测试第一版本的并查集, 测试元素个数为n
    void testUF1(int n) {
        UF1::UnionFind1 uf = UF1::UnionFind1(n);
        UnionFIndTestHelper::testUF(uf, n, "UF2");
    }

    // 测试第二版本的并查集, 测试元素个数为n
    void testUF2(int n) {
        UF2::UnionFind uf = UF2::UnionFind(n);
        UnionFIndTestHelper::testUF(uf, n, "UF1");
    }

    // 测试第三版本的并查集, 测试元素个数为n
    void testUF3(int n) {
        UF3::UnionFind uf = UF3::UnionFind(n);
        UnionFIndTestHelper::testUF(uf, n, "UF3");
    }

    // 测试第四版本的并查集, 测试元素个数为n
    void testUF4(int n) {
        UF4::UnionFind uf = UF4::UnionFind(n);
        UnionFIndTestHelper::testUF(uf, n, "UF4");
    }

    // 测试第四版本的并查集, 测试元素个数为n
    void testUF5(int n) {
        UF5::UnionFind uf = UF5::UnionFind(n);
        UnionFIndTestHelper::testUF(uf, n, "UF5");
    }

}


#endif //PLAY_WITH_ALGORITHMS_UNIONFINDTESTHELPER_H
