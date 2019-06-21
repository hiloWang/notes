/*
 ============================================================================
 
 Author      : Ztiany
 Description : 表达式与语句

 ============================================================================
 */
#include <iostream>
#include <vector>

using namespace std;

static void sizeofExpression() {
    cout << sizeof(char) << endl;//char或char类型为1
    cout << sizeof(vector<int>) << endl;//对于vector，sizeof只返回该类型的固定大小，32
    cout << sizeof(string) << endl;//对于string，sizeof只返回该类型的固定大小，32
    //对引用类型执行sizeof运算得到的是引用类型所占内存字节数
    //对指针类型执行sizeof运算得到的是指针所占内存字节数，在固定的系统中，这是固定的
    //对数组类型执行sizeof运算得到的是数组所占总内存的字节数
}

int main() {
    sizeofExpression();
    return EXIT_SUCCESS;
}

