/*
 ============================================================================
 
 Author      : Ztiany
 Description : 类的继承

 ============================================================================
 */

#include "inheritance.h"
#include <iostream>

using namespace std;

int main() {

    Rectangle rectangle;

    rectangle.setWidth(5);
    rectangle.setHeight(7);

    // 输出对象的面积
    cout << "Total area: " << rectangle.getArea() << endl;
}
