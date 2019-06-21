/*
 ============================================================================

 Author      : Ztiany
 Description : 模板类

 ============================================================================
 */

#include <iostream>

using namespace std;

//对char类型和Int类型数组进行排序

//打印函数
template<class T>
void PrintArray(T *arr, int len) {
    for (int i = 0; i < len; i++) {
        cout << arr[i] << " ";
    }
    cout << endl;
}


//排序
template<class T>
void MySort(T *arr, int len) {
    for (int i = 0; i < len; i++) {
        for (int j = i + 1; j < len; j++) {
            //从大到小排序
            if (arr[i] < arr[j]) {
                T temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
    }
}


int main(void) {

    int arr[] = {2, 6, 1, 8, 9, 2};
    int len = sizeof(arr) / sizeof(int);

    cout << "before sort:" << endl;
    PrintArray(arr, len);
    //排序
    MySort(arr, len);
    //排序之后
    cout << "after sort:" << endl;
    PrintArray(arr, len);

    cout << "--------------------" << endl;
    char chArr[] = {'a', 'c', 'b', 'p', 't'};
    len = sizeof(chArr) / sizeof(char);
    PrintArray(chArr, len);
    MySort(chArr, len);
    PrintArray(chArr, len);

    return 0;
}