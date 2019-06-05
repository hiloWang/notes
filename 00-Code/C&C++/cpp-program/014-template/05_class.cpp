/*
 ============================================================================

 Author      : Ztiany
 Description : 模板类——类外实现

 ============================================================================
 */

#include <cstdlib>
#include <iostream>

using namespace std;

//声明Person
template<class T>
class Person;

//声明PrintPerson
template<class T>
void PrintPerson(Person<T> &p);


template<class T>
class Person {
public:
    //普通友元函数，需要上面的声明
    friend void PrintPerson<T>(Person<T> &p);

    //重载左移操作符
    //friend ostream &operator<< <T>(ostream &os, Person<T> &p);
    template<class T>
    friend ostream &operator<<(ostream &os, Person<T> &p);

    Person(T age, T id);

    void Show();

private:
    T mAge;
    T mID;
};

template<class T>
Person<T>::Person(T age, T id) {
    this->mID = id;
    this->mAge = age;
}

template<class T>
void Person<T>::Show() {
    cout << "Age:" << mAge << " ID:" << mID << endl;
}


//重载左移运算操作符
template<class T>
ostream &operator<<(ostream &os, Person<T> &p) {
    os << "Age:" << p.mAge << " ID:" << p.mID << endl;
    return os;
}

template<class T>
void PrintPerson(Person<T> &p) {
    cout << "Age:" << p.mAge << " ID:" << p.mID << endl;
}


//不要滥用友元
void test01() {

    Person<int> p(10, 20);
    p.Show();
    cout << p;
    PrintPerson(p);
    system("pause");
}


int main() {
    test01();
    return EXIT_SUCCESS;
}