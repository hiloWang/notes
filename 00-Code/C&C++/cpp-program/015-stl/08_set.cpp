/*
 ============================================================================
 
 Description : set

 ============================================================================
 */


#include <iostream>
#include <set>
#include <list>

using namespace std;

//仿函数
class mycompare {
public:
    bool operator()(int v1, int v2) {
        return v1 > v2;
    }
};

//set容器初始化
void test01() {
    set<int, mycompare> s1;  //自动进行排序 默认从小到大
    s1.insert(7);
    s1.insert(2);
    s1.insert(4);
    s1.insert(5);
    s1.insert(1);

    for (set<int>::iterator it = s1.begin(); it != s1.end(); it++) {
        cout << *it << " ";
    }
    cout << endl;

#if 0
    //赋值操作
    set<int> s2;
    s2 = s1;

    //删除操作
    s1.erase(s1.begin());
    s1.erase(7);

    for (set<int>::iterator it = s1.begin(); it != s1.end(); it++){
        cout << *it << " ";
    }
    cout << endl;

    //先序遍历 中序遍历 后序遍历
    //如何改变默认排序？
#endif
}


//set查找
void test02() {

    set<int> s1;
    s1.insert(7);
    s1.insert(2);
    s1.insert(4);
    s1.insert(5);
    s1.insert(1);

    set<int>::iterator ret = s1.find(14);
    if (ret == s1.end()) {
        cout << "没有找到!" << endl;
    } else {
        cout << "ret:" << *ret << endl;
    }

    //找第一个大于等于key的元素
    ret = s1.lower_bound(2);
    if (ret == s1.end()) {
        cout << "没有找到!" << endl;
    } else {
        cout << "ret:" << *ret << endl;
    }

    //找第一个大于key的值
    ret = s1.upper_bound(2);
    if (ret == s1.end()) {
        cout << "没有找到!" << endl;
    } else {
        cout << "ret:" << *ret << endl;
    }

    //equal_range 返回Lower_bound 和 upper_bound值
    pair<set<int>::iterator, set<int>::iterator> myret = s1.equal_range(2);
    if (myret.first == s1.end()) {
        cout << "没有找到！" << endl;
    } else {
        cout << "myret:" << *(myret.first) << endl;
    }

    if (myret.second == s1.end()) {
        cout << "没有找到！" << endl;
    } else {
        cout << "myret:" << *(myret.second) << endl;
    }

}

class Person {
public:
    Person(int age, int id) : id(id), age(age) {}

public:
    int id;
    int age;
};

class mycompare2 {
public:
    bool operator()(Person p1, Person p2) {
        if (p1.id == p2.id) {
            return p1.age > p2.age;
        } else {
            return p1.id > p2.id;
        }
    }
};

void test03() {

    set<Person, mycompare2> sp; //set需要排序，当你放对象，set知道怎么排吗？

    Person p1(10, 20), p2(20, 20), p3(50, 60);
    sp.insert(p1);
    sp.insert(p2);
    sp.insert(p3);

    Person p4(10, 30);

    for (set<Person, mycompare2>::iterator it = sp.begin(); it != sp.end(); it++) {
        cout << (*it).age << " " << (*it).id << endl;
    }

    //查找
    set<Person, mycompare2>::iterator ret = sp.find(p4);
    if (ret == sp.end()) {
        cout << "没有找到!" << endl;
    } else {
        cout << "找到：" << (*ret).id << " " << (*ret).age << endl;
    }
}

int main(void) {
    //test01();
    //test02();
    test03();
    return 0;
}

