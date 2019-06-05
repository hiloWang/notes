/*
 ============================================================================
 
 Author      : Ztiany
 Description : 动态对象

 ============================================================================
 */

#include "DynamicObject.h"
#include <cstdlib>
#include <iostream>
#include <vector>
#include <cstring>

using namespace std;

int main() {

    //基本类型
    int *pi1 = new int;  // pi指向一个未知的int值
    int *pi2 = new int(1024);  // pi指向一个int，值为1024
    int *pi3 = new int();  // pi指向初始化值为0的int
    cout << "pi1 = " << *pi1 << "   pi2 = " << *pi2 << "   pi3 = " << *pi3 << endl;
    delete (pi1);
    delete (pi2);
    delete (pi3);


    //内置类类型
    string *ps1 = new string(10, '9');    // *ps 为 "9999999999"
    string *ps2 = new string(); //空字符串
    cout << "ps1 = " << *ps1 << endl;
    cout << "ps2 = " << *ps2 << endl;
    delete (ps1);
    delete (ps2);

    //类类型
    DynamicObject2 *dynamicObject = new DynamicObject2(12, 24);
    cout << "dynamicObject.total = " << dynamicObject->getTotal() << endl;
    delete (dynamicObject);

    //动态数组

    //1个参数
    DynamicObject1 *dynamicObject2Arr = new DynamicObject1[5]{1, 2, 3, 4, 5};
    cout << "dynamicObject1Arr[1].total = " << (dynamicObject2Arr + 1)->getTotal() << endl;
    delete[] dynamicObject2Arr;

    //2个参数
    DynamicObject2 *dynamicObjectArr = new DynamicObject2[2]{DynamicObject2(3, 3), DynamicObject2(4, 4)};
    cout << "dynamicObjectArr2[1].total = " << (dynamicObjectArr + 1)->getTotal() << endl;
    delete[] dynamicObjectArr;

    //其实使用vector更加合适
    vector<DynamicObject2> doVector(100, DynamicObject2(2, 3));

    //基本类型

    //动态数组的应用
    const char *noerr = "success";
    const char *err189 = "Error: a function declaration must specify a function return type!";
    const char *errorTxt;

    if (false)
        errorTxt = err189;
    else
        errorTxt = noerr;

    // +1是为了存放'\0'
    size_t dimension = strlen(errorTxt) + 1;
    char *errMsg = new char[dimension];
    strncpy(errMsg, errorTxt, dimension);
    cout << "errMsg= " << errMsg << endl;
    delete[] errMsg;

    return EXIT_SUCCESS;
}
