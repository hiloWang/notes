/*
 ============================================================================

 Author      : Ztiany
 Description : 定义类的构造函数与析构函数

 ============================================================================
 */


#ifndef C_BASIC_LINE_H
#define C_BASIC_LINE_H


#include <iostream>

class Line {
public:
    //成员函数
    void setLength(double len);
    double getLength();
    void setWidth(double len);
    double getWidth();

    // 构造函数
    Line() {
        std::cout << "use default length = 1, width = 1" << std::endl;
        this->length = 1;
        this->width = 1;
    }
    Line(double len);
    Line(double len, double width);

    //这是析构函数，当对象被释放时调用
    ~Line(void) {
        std::cout << "Object line is being deleted" <<std::endl;
    }

private:
    double length;//线的长度
    double width;//线的宽度
};

// 成员函数定义，包括构造函数
Line::Line(double len) {
    std::cout << "Object is being created, length = " << len << std::endl;
    length = len;
    width = 1;
}

//使用初始化列表来初始化字段
Line::Line(double len, double width) : length(len), width(width) {
    std::cout << "Object is being created, length = " << len << "width = " << width << std::endl;
}

void Line::setLength(double len) {
    length = len;
}

double Line::getLength() {
    return length;
}

void Line::setWidth(double len) {
    length = len;
}

double Line::getWidth() {
    return width;
}




#endif //C_BASIC_LINE_H
