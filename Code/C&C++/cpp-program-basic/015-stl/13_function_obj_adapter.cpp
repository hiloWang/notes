/*
 ============================================================================
 
 Description : 函数对象适配器

 ============================================================================
 */


#include <iostream>
#include<vector>
#include<functional>
#include<algorithm>

using namespace std;

struct MyPrint : public binary_function<int, int, void> {
    void operator()(int v, int val) const {
        cout << "v:" << v << " val:" << val << endl;
        cout << v + val << " ";
    }
};

//仿函数适配器 bind1st bind2nd 绑定适配器
void test01() {

    vector<int> v;
    for (int i = 0; i < 10; i++) {
        v.push_back(i);
    }

    int addNum = 200;
    for_each(v.begin(), v.end(), bind1st(MyPrint(), addNum));
    //绑定适配器  将一个二元函数对象转变成一元函数对象
    //bind1st bind2nd区别？
    //bind1st，将addNum绑定为函数对象的第一个参数
    //bind2nd，将addNum绑定为函数对象的第二个参数
}

struct MyPrint02 {
    void operator()(int v) {
        cout << v << " ";
    }
};

struct MyCompare : public binary_function<int, int, bool> {
    bool operator()(int v1, int v2) const {
        return v1 > v2;
    }
};

struct MyGreater5 : public binary_function<int, int, bool> {
    bool operator()(int v, int val) const {
        cout << "v:" << v << " val:" << val << endl;
        return v > val;
    }
};

//仿函数适配器 not1 not2 取反适配器
void test02() {

    vector<int> v;
    for (int i = 0; i < 10; i++) {
        v.push_back(i);
    }

    for_each(v.begin(), v.end(), MyPrint02());
    cout << endl;
    sort(v.begin(), v.end(), not2(MyCompare()));
    for_each(v.begin(), v.end(), MyPrint02());
    cout << endl;

    //not1 not2
    //如果对二元谓词取反，用not2
    //如果对一元谓词取反，用not1
    vector<int>::iterator it = find_if(v.begin(), v.end(), not1(bind2nd(MyGreater5(), 10)));
    if (it == v.end()) {
        cout << "没有找到" << endl;
    } else {
        cout << *it << endl;
    }
}

//仿函数适配器 ptr_fun
void MyPrint03(int val, int val2) {
    cout << "val1:" << val << " val2:" << val2 << endl;
    cout << val + val2 << endl;
}

void test03() {

    vector<int> v;
    for (int i = 0; i < 10; i++) {
        v.push_back(i);
    }

    //ptr_func把普通函数 转成  函数对象
    for_each(v.begin(), v.end(), bind2nd(ptr_fun(MyPrint03), 10));
}

//成员函数适配器 mem_fun mem_fun_ref
class Person {
public:
    Person(int age, int id) : age(age), id(id) {}

    void show() {
        cout << "age:" << age << " id:" << id << " aaa" << endl;
    }

public:
    int age;
    int id;
};

void test04() {

    //如果容器中存放的对象或者对象指针，我们for_each算法打印的时候，调用类
    //自己提供的打印函数

    vector<Person> v;
    Person p1(10, 20), p2(30, 40), p3(50, 60);
    v.push_back(p1);
    v.push_back(p2);
    v.push_back(p3);

    //格式: &类名::函数名
    for_each(v.begin(), v.end(), mem_fun_ref(&Person::show));

    vector<Person *> v1;
    v1.push_back(&p1);
    v1.push_back(&p2);
    v1.push_back(&p3);

    for_each(v1.begin(), v1.end(), mem_fun(&Person::show));

    //mem_fun_ref mem_fun区别?
    //如果存放的是对象指针 使用mem_fun
    //如果使用的是对象 使用mem_fun_ref
}

int main(void) {
    //test01();
    //test02();
    //test03();
    test04();
    return 0;
}