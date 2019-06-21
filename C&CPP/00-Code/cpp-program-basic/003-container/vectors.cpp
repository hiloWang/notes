/*
 ============================================================================
 
 Author      : Ztiany
 Description : 对象容器vector

 ============================================================================
 */

#include <vector>
#include <iostream>


using namespace std;
int sAge = 0;

class A {
private:
    int age;
public:
    A() {
        age = ++sAge;
        cout << "create time" << age << endl;
    }

    int getAge() {
        return age;
    }
};

//1：创建vector
static void createVector() {
    vector<int> vector1;//创建空的vector，执行默认初始化
    vector<int> vector2(vector1);//v2中包含所有v1中元素的副本
    vector<int> vector3 = vector1;//等价于上面vector2
    vector<int> vector4(4, 1);//包含4个1的vector
    vector<int> vector5(4);//包含4个执行了值初始化的对象(此处是int类型，将初始化为0)

    //列表初始化(c++11)
    vector<int> vector6{1, 2, 3, 4, 5, 6, 7};
    vector<int> vector7 = {1, 2, 3, 4, 5, 6, 7};//等价于vector5
    vector<string> vector8{10};//v8中有十个默认初始化的元素
    vector<string> vector9{10, "hi"};//v9中有十个hi

    vector<A> vector10(10);
    for (A a : vector10) {
        cout << a.getAge() << " ";
    }
    vector<A> vector11 = vector10;
    for (A a : vector11) {
        cout << a.getAge() << " ";
    }

    vector<int> c;

}


//2：vector的操作
void vectorOperation() {

    vector<int> vector1;

    //2.1：向尾部添加元素
    for (int i = 0; i < 100; ++i) {
        vector1.push_back(i);
    }
    cout << "vector1 size = " << vector1.size() << endl;//100

    //2.2：size和empty函数、访问元素
    vector<int>::size_type size = vector1.size();//获取size
    bool is_empty = vector1.empty();//是否为空
    int index_10 = vector1[10];//访问元素

    vector<int> vector2 = vector1;//用v1中的元素拷贝替换v2中的元素
    vector1 = {100, 2, 3, 100};//用列表中的元素，替换v1中的元素，虽然vector1范围外的内存依然保持着原有的值，但是vector的size已经变为4了
    cout << "vector1 size = " << vector1.size() << endl;//4


    //2.3：vector的比较，=、!=、>、<、>=、<=操纵
    bool is_same = vector1 == vector2;//当且仅当两个vector的元素数量和对应位置的元素值相等才为true


    //2.4 防止脚标越界
    cout << "vector1[99] = " << vector1[99];//100
    cout << "vector2[100] = " << vector2[101];//位置值，脚标越界。
}


int main() {
    createVector();
//    vectorOperation();
    return EXIT_SUCCESS;
}
