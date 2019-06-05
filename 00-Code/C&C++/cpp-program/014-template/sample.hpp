#pragma once

#include<iostream>


template<class T>
class Person {
public:
    Person(T age);
    void Show();
public:
    T age;
};

template<class T>
Person<T>::Person(T age) {
    this->age = age;
}

template<class T>
void Person<T>::Show() {
    std::cout << "Age: " << age << std::endl;
}
