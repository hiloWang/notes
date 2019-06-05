/*
 ============================================================================
 
 Author      : Ztiany
 Description : 友元

 ============================================================================
 */

#ifndef C_BASIC_FRIEND_CLASS_H
#define C_BASIC_FRIEND_CLASS_H


#include <iostream>
#include <vector>


class Box {
private:
    double width;
public:
    void setWidth(double wid);
public://友元放在一起
    friend void printWidth(Box box);
    friend class Friend;
};

// 成员函数定义
void Box::setWidth(double wid) {
    width = wid;
}

// 请注意：printWidth() 不是任何类的成员函数
void printWidth(Box box) {
    /* 因为 printWidth() 是 Box 的友元，它可以直接访问该类的任何成员 */
    std::cout << "Width of box : " << box.width << std::endl;
}

//这是一个友元类
class Friend {
public:
    double getBoxWidth(Box box) {
        return box.width;
    }
};

#endif //C_BASIC_FRIEND_CLASS_H
