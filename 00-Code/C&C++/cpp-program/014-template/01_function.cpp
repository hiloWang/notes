/*
 ============================================================================

 Author      : Ztiany
 Description : 模板函数

 ============================================================================
 */


#include <iostream>

using namespace std;

#if 0
//int类型数据交换
void MySwap(int& a, int& b){
    int temp = a;
    a = b;
    b = temp;
}

//double类型
void MySwap(double& a, double& b){
    double temp = a;
    a = b;
    b = temp;
}
#endif

//template<typename T> 用于声明下面这是一个方法模板，<>里面可以带多个类型参数，也可以写成template<class T>
//template只对它下面的第一个函数有效
//普通函数可以进行自动类型转换，函数模板必须严格类型匹配
template<class T>
void mSwap(T &a, T &b) {
    T temp = a;
    a = b;
    b = temp;
}

void test() {

    int a = 10;
    int b = 20;

    //调用方式：
    //1 自动类型推导
    cout << "a:" << a << " b:" << b << endl;
    mSwap(a, b); //编译器根据你传的值 进行类型自动推导
    cout << "a:" << a << " b:" << b << endl;

    double da = 1.13;
    double db = 1.14;
    cout << "da:" << da << " db:" << db << endl;
    mSwap(da, db);
    cout << "da:" << da << " db:" << db << endl;

    //2. 显式的指定类型
    mSwap<int>(a, b);
}

int main() {
    test();
    return EXIT_SUCCESS;
}
