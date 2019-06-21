/*
 ============================================================================
 
 Author      : Ztiany
 Description : 继承

 ============================================================================
 */


#ifndef C_BASIC_INHERITANCE_H
#define C_BASIC_INHERITANCE_H

// 基类 Shape
class Shape {
public:
    void setWidth(int w) {
        width = w;
    }

    void setHeight(int h) {
        height = h;
    }

protected:
    int width;
    int height;
};


// 基类 PaintCost(油漆成本)
class PaintCost {
public:
    int getCost(int area) {
        return area * 70;
    }
};

// 派生类
class Rectangle : public Shape, public PaintCost {
public:
    int getArea() {
        return (width * height);
    }
};

#endif //C_BASIC_INHERITANCE_H
