/*
 ============================================================================
 
 Author      : Ztiany
 Description : 堆排序测试

 ============================================================================
 */
#include <cstdlib>
#include "heap/HeapSort.h"
#include "sort-basic/SortTestHelper.h"
#include "sort-basic/Student.h"
#include "sort-basic/InsertSort.h"
#include "heap/MaxHeap.h"
#include "heap/IndexMaxHeapV1.h"
#include "heap/IndexMaxHeapV2.h"

using namespace std;

int main() {
    int n = 100000;
    int *randomArr = SortTextHelper::generateRandomArray(n, 0, n);
    SortTextHelper::testSort("heap    SortV1", heapSortV3, randomArr, n);
    delete[]randomArr;

    //学生排序
    std::cout << "testSort Students-----------------------------------------" << std::endl;
    Student students[] = {Student("A", 96), Student("B", 96), Student("C", 99), Student("D", 98)};
    insertionSortV2(students, 4);
    SortTextHelper::printArray(students, 4);

    return EXIT_SUCCESS;
}