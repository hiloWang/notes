/*
 ============================================================================
 
 Author      : Ztiany
 Description : 演示如何定义一个类

 ============================================================================
 */

#ifndef C_BASIC_CLASS1_H
#define C_BASIC_CLASS1_H

class Box {

private:
    double length;   // 盒子的长度
    double breadth;  // 盒子的宽度
    double height;   // 盒子的高度

public:
    //类的成员函数是指那些把定义和原型写在类定义内部的函数，就像类定义中的其他变量一样。类成员函数是类的一个成员
    //类成员函数可以只在类内声明，然后在类外使用范围解析运算符 :: 来定义，可以直接定义在类中
    //在类定义中定义的成员函数把函数声明为内联的，即便没有使用 inline 标识符
    double getVolume() {
        return length * breadth * height;
    }

    void setLength(double len);

    void setBreadth(double bre);

    void setHeight(double hei);
};

//单独使用范围解析运算符 :: 来定义。
void Box::setLength(double len) {
    length = len;
}

void Box::setBreadth(double bre) {
    breadth = bre;
}

void Box::setHeight(double hei) {
    height = hei;
}


#endif //C_BASIC_CLASS1_H
