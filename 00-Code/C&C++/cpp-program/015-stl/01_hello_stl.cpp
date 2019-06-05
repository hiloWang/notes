/*
 ============================================================================
 
 Description : stl介绍

 ============================================================================
 */
#include <iostream>
#include<vector> //动态数组、可变数组
#include<algorithm> //算法

using namespace std;


void PrintVector(int v) {
    cout << v << " ";
}

//STL基本语法
void test01() {
    //定义一个容器，并且制指定这个容器存放的元素类型是Int
    vector<int> v;
    v.push_back(10);
    v.push_back(20);
    v.push_back(30);
    v.push_back(40);

    //通过STL提供的for_each算法
    //容器提供的迭代器
    //vector<int>::iterator 迭代器类型
    vector<int>::iterator pBegin = v.begin();
    vector<int>::iterator pEnd = v.end();

    //容器中可能存放基础的数据类型，也可能存放自定义数据类型
    for_each(pBegin, pEnd, PrintVector);
}

//容器也可以存放自定义数据类型
class Person {
public:
    Person(int age, int id) : age(age), id(id) {}

public:
    int age;
    int id;
};

void test02() {

    //创建容器，并且指定容器的元素类型是Person
    vector<Person> v;
    Person p1(10, 20), p2(30, 40), p3(50, 60);
    v.push_back(p1);
    v.push_back(p2);
    v.push_back(p3);

    //遍历
    for (vector<Person>::iterator it = v.begin(); it != v.end(); it++) {
        cout << (*it).age << " " << (*it).id << endl;
    }
}


int main(void) {
    test01();
    test02();
    return 0;
}
