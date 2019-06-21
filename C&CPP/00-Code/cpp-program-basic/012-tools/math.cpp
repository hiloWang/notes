/*
 ============================================================================
 
 Author      : Ztiany
 Description : 数字运算

 ============================================================================
 */
#include <iostream>
#include <cmath>
#include <ctime>

using namespace std;

void digit() {
    // 数字定义
    short s;
    int i;
    long l;
    float f;
    double d;

    // 数字赋值
    s = 10;
    i = 1000;
    l = 1000000;
    f = 230.47;
    d = 30949.374;

    // 数字输出
    cout << "short  s :" << s << endl;
    cout << "int    i :" << i << endl;
    cout << "long   l :" << l << endl;
    cout << "float  f :" << f << endl;
    cout << "double d :" << d << endl;
}

//C++ 内置了丰富的数学函数，可对各种数字进行运算。
void math() {
    // 数字定义
    short s = 10;
    int i = -1000;
    long l = 100000;
    float f = 230.47;
    double d = 200.374;

    // 数学运算
    cout << "sin(d) :" << sin(d) << endl;
    cout << "abs(i)  :" << abs(i) << endl;
    cout << "floor(d) :" << floor(d) << endl;
    cout << "sqrt(f) :" << sqrt(f) << endl;
    cout << "pow( d, 2) :" << pow(d, 2) << endl;
}

void randomNumber() {
    int i, j;
    // 设置种子
    srand((unsigned) time(NULL));

    /* 生成 10 个随机数 */
    for (i = 0; i < 10; i++) {
        // 生成实际的随机数
        j = rand();
        cout << "randomNumber:  " << j << endl;
    }
}


int main() {
    digit();
    math();
    randomNumber();
    return 0;
}