/*
 ============================================================================
 
 Author      : Ztiany
 Description : 定义拷贝构造函数

 ============================================================================
 */

#include "copy_constructor.h"

using namespace std;

//如果这里不适用引用，则会造成line再次被拷贝
static void display(Line &obj) {
    cout << "line size: " << obj.getLength() << endl;
}


int main() {

    Line line1(10);

    Line line2 = line1; // 这里也调用了拷贝构造函数

    display(line1);
    display(line2);
}