/*
 ============================================================================
 
 Description : C++显式类型转换

 ============================================================================
 */

#include <iostream>

using namespace std;

class Building {
};

class Animal {
};

class Cat : public Animal {
};

//static_cast
void test01() {

    int a = 97;
    char c = static_cast<char>(a);
    cout << c << endl;

    //基础数据类型指针
    //int* p = NULL;
    //char* sp = static_cast<char*>(p);

    //对象指针
    //Building* building = NULL;
    //Animal* ani = static_cast<Animal*>(building);

    //转换具有继承关系的对象指针
    //父类指针转成子类指针
    Animal *ani = NULL;
    Cat *cat = static_cast<Cat *>(ani);
    //子类指针转成父类指针
    Cat *soncat = NULL;
    Animal *anifather = static_cast<Animal *>(soncat);

    //Animal aniobj;
    //Animal& aniref = aniobj;
    //Cat& cat = static_cast<Cat&>(aniref);

    Cat catobj;
    Cat &catref = catobj;
    Animal &anifather2 = static_cast<Animal &>(catref);

    //static_cast 用于内置的数据类型，还有具有继承关系的指针或者引用
}

//dynamic_cast 转换具有继承关系的指针或者引用，在转换前会进行对象类型检查
void test02() {

    //基础数据类型
    //int a = 10;
    //char c = dynamic_cast<char>(a);

    //非继承关系的指针
    //Animal* ani = NULL;
    //Building* building = dynamic_cast<Building*>(ani);


    //具有继承关系指针
    //Animal* ani = NULL;
    //Cat* cat = dynamic_cast<Cat*>(ani);
    //原因在于 dynamic做类型安全检查

    Cat *cat = NULL;
    Animal *ani = dynamic_cast<Animal *>(cat);

    //结论:dynamic只能转换具有继承关系的指针或者引用，并且只能由子类型转成基类型
}

//const_cast 指针 引用或者对象指针
void test03() {

    //1 基础数据类型
#if 0
    int a = 10;
    const int& b = a;
    //b = 10;
    int& c = const_cast<int&>(b);
    c = 20;
#endif
    const int a = 10;
    const int &pp = a;
    int &cc = const_cast<int &>(pp);
    cc = 100;

    cout << "a:" << a << endl;

    //看指针
    const int *p = NULL;
    int *p2 = const_cast<int *>(p);

    int *p3 = NULL;
    const int *p4 = const_cast<const int *>(p3);

    //增加或者去除变量的const性
}

//reinterpret_cast 强制类型转换 无关的指针类型，包括函数指针都可以进行转换
typedef void(*FUNC1)(int, int);

typedef int(*FUNC2)(int, char *);

void test04() {

    //1. 无关的指针类型都可以进行转换
    Building *building = NULL;
    Animal *ani = reinterpret_cast<Animal *>(building);

    //2. 函数指针转换
    FUNC1 func1;
    FUNC2 func2 = reinterpret_cast<FUNC2>(func1);
}


int main(void) {
    test01();
    test03();
    return 0;
}

