/*
 ============================================================================
 
 Author      : Ztiany
 Description : 二分搜索

 ============================================================================
 */
#include "bst/BinarySearch.h"
#include "bst/BST.h"
#include "bst/SequenceST.h"
#include "tools/FileOps.h"
#include "sort-basic/SortTestHelper.h"
#include "sort-basic/InsertSort.h"
#include <cstdlib>
#include <iostream>
#include <vector>
#include <string>

using namespace std;

static void testBinarySearch() {
    int n = 10;
    int *arr = SortTestHelper::generateRandomArray(n, 0, n);
    insertionSortV2(arr, n);
    SortTestHelper::printArray(arr, n);
    cout << "binarySearch(arr, 3, n); = " << (binarySearch(arr, n, 3)) << endl;
    cout << "floor(arr, n, 3); = " << (floor(arr, n, 3)) << endl;
    cout << "ceil(arr, n, 3); = " << (ceil(arr, n, 3)) << endl;
    delete[]arr;
}

static void testBST1() {
    int n = 100;
    BST<int, int> bst;
    int *arr = SortTestHelper::generateRandomArray(n, 0, n);
    for (int i = 0; i < n; ++i) {
        bst.insert(arr[i], i);
    }
    bst.preOrder();
    bst.inOrder();
    bst.postOrder();
    bst.levelOrder();
    for (int i = 0; i < n; ++i) {
        bst.remove(arr[i]);
    }
    cout << "bst is empty = " << bst.isEmpty() << endl;
}

static void testBST2() {
    // 我们使用文本量更小的共产主义宣言进行试验
    string filename = "../tools/communist.txt"
    vector<string> words;
    if (FileOps::readFile(filename, words)) {
        cout << "There are totally " << words.size() << " words in " << filename << endl;
        cout << endl;

        // 测试1, 我们按照文本原有顺序插入进二分搜索树
        time_t startTime = clock();
        BST<string, int> *bst = new BST<string, int>();
        for (vector<string>::iterator iter = words.begin(); iter != words.end(); iter++) {
            int *res = (*bst).search(*iter);
            if (res == NULL)
                (*bst).insert(*iter, 1);
            else
                (*res)++;
        }

        // 我们查看unite一词的词频
        if (bst->contain("unite"))
            cout << "'unite' : " << *(*bst).search("unite") << endl;
        else
            cout << "No word 'unite' in " + filename << endl;
        time_t endTime = clock();

        cout << "BST , time: " << double(endTime - startTime) / CLOCKS_PER_SEC << " s." << endl;
        cout << endl;

        delete bst;


        // 测试2, 我们按照文本原有顺序插入顺序查找表
        startTime = clock();
        SequenceST<string, int> *sst = new SequenceST<string, int>();
        for (vector<string>::iterator iter = words.begin(); iter != words.end(); iter++) {
            int *res = (*sst).search(*iter);
            if (res == NULL)
                (*sst).insert(*iter, 1);
            else
                (*res)++;
        }

        // 我们查看unite一词的词频
        if (sst->contain("unite"))
            cout << "'unite' : " << *(*sst).search("unite") << endl;
        else
            cout << "No word 'unite' in " + filename << endl;
        endTime = clock();

        cout << "SST , time: " << double(endTime - startTime) / CLOCKS_PER_SEC << " s." << endl;
        cout << endl;

        delete sst;


        // 测试3, 我们将原文本排序后插入二分搜索树, 查看其效率
        startTime = clock();
        BST<string, int> *bst2 = new BST<string, int>();

        sort(words.begin(), words.end());
        for (vector<string>::iterator iter = words.begin(); iter != words.end(); iter++) {
            int *res = (*bst2).search(*iter);
            if (res == NULL)
                (*bst2).insert(*iter, 1);
            else
                (*res)++;
        }

        // 我们查看unite一词的词频
        cout << "'unite' : " << *(*bst2).search("unite") << endl;
        endTime = clock();

        cout << "BST2 , time: " << double(endTime - startTime) / CLOCKS_PER_SEC << " s." << endl;
        cout << endl;

        delete bst2;
    }
}

int main() {
    cout << "--------------------------------------------------" << endl;
    testBinarySearch();
    cout << "--------------------------------------------------" << endl;
    testBST1();
    cout << "--------------------------------------------------" << endl;
    testBST2();
    return EXIT_SUCCESS;
}