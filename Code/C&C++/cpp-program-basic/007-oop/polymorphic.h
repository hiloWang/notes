/*
 ============================================================================
 
 Author      : Ztiany
 Description : 多态

 ============================================================================
 */


#ifndef C_BASIC_POLYMORPHIC_H
#define C_BASIC_POLYMORPHIC_H

#include <iostream>

//Shape基类
class Shape {
protected:
    int width, height;
public:
    Shape(int a = 0, int b = 0) {
        width = a;
        height = b;
    }

    //定义虚函数
    virtual int area() {
        int area = 0;
        std::cout << "Shape class area :" << area << std::endl;
        return area;
    }
};

//派生类：Rectangle
class Rectangle : public Shape {
public:
    Rectangle(int a = 0, int b = 0) : Shape(a, b) {
    }

    //重写了area函数
    int area() {
        //可以调用基类的函数
        //int superArea = Shape::area();

        int area = (width * height);
        std::cout << "Rectangle class area :" << area << std::endl;
        return area;
    }
};


//派生类：Triangle
class Triangle : public Shape {
public:
    Triangle(int a = 0, int b = 0) : Shape(a, b) {}

    int area() {
        int area = (width * height / 2);
        std::cout << "Triangle class area :" << area << std::endl;
        return area;
    }
};


//定义基类
class Object {
public:
    //定义了纯虚函数则此类为抽象类，不能被实例化
    //纯虚函数，不能被派生类调用
    virtual std::string toString()=0;
};

class Person : public Object {
public:
    Person(std::string name) {
        this->name = name;
    }

private:
    std::string name;
public:
    std::string toString() {
        return "the person name is " + name;
    }
};


#endif //C_BASIC_POLYMORPHIC_H
