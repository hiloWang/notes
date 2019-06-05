/*
 ============================================================================
 
 Author      : Ztiany
 Description : C++函数返回类型

 ============================================================================
 */
#include <cstdlib>
#include <string>
#include <vector>

using namespace std;

//1：返回值，拷贝值
const static string shortString(const string &s1, const string &s2) {
    return s1.size() > s2.size() ? s2 : s1;
}

//2：返回引用，不拷贝值
const static string &shortString(const string s1, const string s2) {
    return s1.size() > s2.size() ? s2 : s1;
}

//3：返回列表
static vector<string> getMessage() {
    return {"A", "B", "C"};//列表转换为vector
}


//4：严重错误：返回局部变量的引用
static string &getString() {
    string ret;
    if (ret.empty()) {
        ret = "empty";
    }
    return ret;
}

//5：返回数组指针
typedef int arrI1[10];
using arrI2 = int[10];

static arrI1 *getIntegers1();//如果没有用到函数声明，该函数可以没有定义
static arrI2 *getIntegers2();
static int *getIntegers3();


int main() {

    return EXIT_SUCCESS;
}


