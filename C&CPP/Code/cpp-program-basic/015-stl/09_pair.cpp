/*
 ============================================================================
 
 Author      : Ztiany
 Description : 对组：pair类型

 ============================================================================
 */

#include <cstdlib>
#include <iostream>
#include <utility>

using namespace std;

int main() {
    //pair 类型是一种简单的模板类型，该类型在 utility 头文件中定义。
    pair<string, int> pair1("name", 21);
    pair<string, int> pair2 = make_pair("name", 28);
    return EXIT_SUCCESS;
}