/*
 ============================================================================
 
 Description : 常用遍历算法

 ============================================================================
 */
#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

//transform 将一个容器的元素 搬运 到另一个容器中
struct MyPlus {
    int operator()(int val) {
        return val + 100;
    }
};

void MyPrint(int val) {
    cout << val << " ";
}

void test01() {

    vector<int> v1;
    vector<int> v2;

    for (int i = 0; i < 10; i++) {
        v1.push_back(i);
    }

    v2.resize(v1.size()); //开辟空间

    transform(v1.begin(), v1.end(), v2.begin(), MyPlus());
    for_each(v2.begin(), v2.end(), MyPrint);
    cout << endl;
}

//常用的查找算法
void test02() {

    vector<int> v1;
    for (int i = 0; i < 10; i++) {
        v1.push_back(i);
    }

    vector<int>::iterator ret = find(v1.begin(), v1.end(), 5);
    if (ret == v1.end()) {
        cout << "么有找到!" << endl;
    } else {
        cout << "找到了:" << *ret << endl;
    }
}

class Person {
public:
    Person(int age, int id) : age(age), id(id) {}

    bool operator==(const Person &p) const {
        return p.id == this->id && p.age == this->age;
    }

public:
    int id;
    int age;
};

void test03() {

    vector<Person> v1;

    Person p1(10, 20), p2(20, 30);

    v1.push_back(p1);
    v1.push_back(p2);

    vector<Person>::iterator ret = find(v1.begin(), v1.end(), p1);
    if (ret == v1.end()) {
        cout << "么有找到!" << endl;
    } else {
        cout << "找到了:" << endl;
    }

}

//binary_search 二分查找法
bool MySearch(int val) {
    return val > 5;
}

bool MySearch2(int val) {
    return val > 5;
}

void test04() {

    vector<int> v1;
    for (int i = 0; i < 10; i++) {
        v1.push_back(i);
    }
    v1.push_back(9);

    bool ret = binary_search(v1.begin(), v1.end(), 5);
    if (ret) {
        cout << "找到！" << endl;
    } else {
        cout << "没有找到!" << endl;
    }

    vector<int>::iterator it = adjacent_find(v1.begin(), v1.end());
    if (it != v1.end()) {
        cout << "找到相邻重复元素:" << *it << endl;
    } else {
        cout << "没有找打相邻重复元素" << endl;
    }

    //find_f 会根据我们的条件(函数) ，返回第一个满足条件的元素的迭代器
    it = find_if(v1.begin(), v1.end(), MySearch);
    if (it != v1.end()) {
        cout << "找到:" << *it << endl;
    } else {
        cout << "没有找到" << endl;
    }

    //count count_if
    int num = count(v1.begin(), v1.end(), 9);
    cout << "9出现的次数:" << num << endl;
    num = count_if(v1.begin(), v1.end(), MySearch2);
    cout << "大于5的个数：" << num << endl;

}


int main(void) {
    //test01();
    //test02();
    //test03();
    test04();
    return 0;
}

