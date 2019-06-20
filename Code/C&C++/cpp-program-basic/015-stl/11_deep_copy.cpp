/*
 ============================================================================

 Description : 深拷贝与浅拷贝

 ============================================================================
 */

#include <iostream>
#include <vector>

using namespace std;

class Person {
public:
    Person(char *name, int age) {
        this->pName = new char[strlen(name) + 1];
        strcpy(this->pName, name);
        this->mAge = age;
    }

    Person(const Person &p) {
        this->pName = new char[strlen(p.pName) + 1];
        strcpy(this->pName, p.pName);
        this->mAge = p.mAge;
    }

    Person &operator=(const Person &p) {

        if (this->pName != NULL) {
            delete[] this->pName;
        }

        this->pName = new char[strlen(p.pName) + 1];
        strcpy(this->pName, p.pName);
        this->mAge = p.mAge;

        return *this;
    }

    ~Person() {
        if (this->pName != NULL) {
            delete[] this->pName;
        }
    }

public:
    char *pName; //指针与浅拷贝的问题
    int mAge;
};

void test01() {
    Person p("aaa", 20);
    vector<Person> vPerson;
    vPerson.push_back(p);
}

int main(void) {
    test01();
    return 0;
}