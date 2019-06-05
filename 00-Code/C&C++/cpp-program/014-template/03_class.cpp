/*
 ============================================================================

 Author      : Ztiany
 Description : 模板类——派生类

 ============================================================================
 */

#include <iostream>

using namespace std;

//模板类——派生普通类
// 子模板类派生时，需要具体化模板类，c++编译器要知道父类的数据类型具体是什么样的//因为c++编译器要分配内存，必须知道父类所占内存大小
template<class T>
class Person {
public:
    Person() {
        mAge = 0;
    }

public:
    T mAge;
};

class SubPerson : public Person<int> {
};

//模板类——派生模板类
template<class T>
class Animal {
public:
    void Jiao() {
        cout << mAge << "岁动物在叫!" << endl;
    }

public:
    T mAge;
};


template<class T>
class Cat : public Animal<T> {
};

