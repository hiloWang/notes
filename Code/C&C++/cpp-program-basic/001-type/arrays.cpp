/*
 ============================================================================

 Author      : Ztiany
 Description : C++数组

 ============================================================================
 */

#include <iostream>
#include <vector>

using namespace std;

//1：初始化数据
static void initializeArray() {
    int size = 3;
    int const cSize = 10;
    int arr1[size];
    int arr2[] = {1, 2, 3};
    int arr3[5] = {1, 2, 3};
    string arr4[7] = {"a", "bc", "dd"};
    int arr5[cSize];

    const char *arr6 = "abc";
    char arr7[10] = "C++";
    char arr8[] = {'c', '+', '+', '\0'};

    int *p1[10];//pInt是指向含有是个整型元素的数组的指针
    int arr9[10][10];
    int (*arr10)[10] = arr9;//arr10是一个指针数组
    int (&arrRef)[5] = arr3;//arrRef引用一个含有5个元素的int数组
}

//2：访问数组
static void accessArray() {
    //通过下标访问数组时，通常将其定义为size_t
    int arr[10];
    for (int i = 0; i < 10; ++i) {
        arr[i] = i;
    }
    size_t index = 4;
    int i4 = arr[index];
}

//3：数组与指针、迭代器
static void arrayPointer() {
    int arr[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    int *pInt1 = arr;
    int *pInt2 = begin(arr);//等同于pInt1
    int *end_1 = &arr[10];//指向尾元素
    int *end_2 = end(arr);//等同于end_1

    //遍历arr
    for (int *p = pInt1; p != end_1; ++p) {
        cout << (*p);
    }
    cout << endl;

    //指针相减的结果是ptrdiff_t类型，它是一个带符号的整型
    ptrdiff_t pt = end_1 - pInt1;
    cout << pt << endl;//10

    //下标和指针
    int arr2[] = {1, 2, 3, 4, 5};
    int *pInt3 = arr2;
    int int_1 = *(++pInt3);
    int int_2 = pInt3[-2];
}

//4：使用数组初始化vector
static void arrayInitializeVector() {
    //允许使用数组来初始化vector对象
    int arr[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    //用于创建vec的两个指针指定了用来初始化的值在数组中的位置
    //第一个指针，起始拷贝地址
    //第二个指针，待拷贝区域尾元素的下一个位置
    vector<int> vec1(begin(arr), end(arr));
    vector<int> vec2(arr + 1, arr + 4);//拷贝三个元素
    cout << "vec1 size = " << vec1.size() << endl;//10
    cout << "vec2 size = " << vec2.size() << endl;//3
}

int main() {
    //initializeArray();
    //accessArray();
    //arrayPointer();
    arrayInitializeVector();
    return EXIT_SUCCESS;
}