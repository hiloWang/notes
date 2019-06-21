/*
 ============================================================================
 
 Author      : Ztiany
 Description : 多态

 ============================================================================
 */


#include "polymorphic.h"

static void testShape() {
    Shape *shape;
    Rectangle rec(10, 7);
    Triangle tri(10, 5);

    // 存储矩形的地址
    shape = &rec;
    // 调用矩形的求面积函数 area
    shape->area();

    // 存储三角形的地址
    shape = &tri;
    // 调用三角形的求面积函数 area
    shape->area();

    Shape emptyShape;
    emptyShape.area();
}

static void testObject() {
    Person person("Ztiany");
    std::cout << person.toString() << std::endl;
}

// 程序的主函数
int main() {
    testShape();
    testObject();
    return EXIT_SUCCESS;
}
