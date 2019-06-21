/*
 ============================================================================
 
 Author      : Ztiany
 Description : 定义拷贝构造函数

 ============================================================================
 */


#ifndef C_BASIC_COPY_H
#define C_BASIC_COPY_H


#include <iostream>

class Line {
public:
    int getLength(void);

    Line(int len);             // 简单的构造函数
    Line(const Line &obj); // 拷贝构造函数
    ~Line();                     // 析构函数

private:
    int *ptr;
};


// 成员函数定义，包括构造函数
Line::Line(int len) {
    std::cout << "construct" << std::endl;
    // 为指针分配内存
    ptr = new int;
    *ptr = len;
}

Line::Line(const Line &obj) {
    std::cout << "copy constructor" << std::endl;
    ptr = new int;
    *ptr = *obj.ptr; // 拷贝值
}

Line::~Line(void) {
    std::cout << "release dynamic_memory" << std::endl;
    delete ptr;
}

int Line::getLength(void) {
    return *ptr;
}

#endif //C_BASIC_COPY_H
