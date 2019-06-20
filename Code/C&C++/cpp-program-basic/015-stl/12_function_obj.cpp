/*
 ============================================================================
 
 Description : 函数对象

 ============================================================================
 */

#define _CRT_SECURE_NO_WARNINGS

#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;


struct MyPrint {
    MyPrint() {
        mNum = 0;
    }

    void operator()(int val) {
        mNum++;
        cout << val << endl;
    }

public:
    int mNum;
};

int num = 0; //真正开中，尽量避免去使用全局变量
void MyPrint02(int val) {
    num++;
    cout << val << endl;
}

void test01() {

    MyPrint print;
    print(10);

    //函数对象可以像普通函数一样调用
    //函数对象可以像普通函数那样接收参数
    //函数对象超出了函数的概念，函数对象可以保存函数调用的状态
}

void test02() {

    vector<int> v;
    v.push_back(10);
    v.push_back(20);
    v.push_back(30);
    v.push_back(40);

    //计算函数调用次数
#if 0
    MyPrint02(10);
    MyPrint02(20);
    cout << num << endl;

    MyPrint print;
    print(10);
    print(20);
    cout << print.mNum << endl;
#endif

    MyPrint print;
    MyPrint &print02 = for_each(v.begin(), v.end(), print);

    cout << "print调用次数:" << print.mNum << endl;
    cout << "print调用次数:" << print02.mNum << endl;
}

int main(void) {
    test02();
    return 0;
}
