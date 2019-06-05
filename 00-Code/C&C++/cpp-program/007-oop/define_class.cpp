/*
 ============================================================================
 
 Author      : Ztiany
 Description : 定义类

 ============================================================================
 */


#include "define_class.h"
#include <iostream>

using namespace std;

static void test_box() {

    Box box1;        // 声明 box1，类型为 Box
    Box box2;        // 声明 box2，类型为 Box

    double volume;     // 用于存储体积

    // box 1 详述
    box1.setLength(6.0);
    box1.setBreadth(7.0);
    box1.setHeight(5.0);

    // box 2 详述
    box2.setLength(12.0);
    box2.setBreadth(13.0);
    box2.setHeight(10.0);


    // box 1 的体积
    volume = box1.getVolume();
    cout << "The volume of the box1: " << volume << endl;

    // box 2 的体积
    volume = box2.getVolume();
    cout << "The volume of the box2: " << volume << endl;
}

int main() {
    test_box();
    return EXIT_SUCCESS;
}