/*
 ============================================================================

 Description : 栈解旋

 ============================================================================
 */

#include <iostream>

using namespace std;


class Person {
public:
    Person() {
        cout << "对象构建!" << endl;
    }

    ~Person() {
        cout << "对象析构!" << endl;
    }
};

int divide(int x, int y) {

    Person p1, p2;

    if (y == 0) {
        throw y;
    }

    return x / y;
}

void test01() {

    try {
        divide(10, 0);
    }
    catch (int e) {
        cout << "异常捕获！" << endl;
    }
}

int main(void) {
    test01();
    return 0;
}