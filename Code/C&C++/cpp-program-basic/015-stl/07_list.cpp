/*
 ============================================================================
 
 Description : list

 ============================================================================
 */
#include <iostream>
#include <list>

using namespace std;

//初始化
void test01() {
    list<int> mlist1;
    list<int> mlist2(10, 10); //有参构造
    list<int> mlist3(mlist2);//拷贝构造
    list<int> mlist4(mlist2.begin(), mlist2.end());

    for (list<int>::iterator it = mlist4.begin(); it != mlist4.end(); it++) {
        cout << *it << " ";
    }
    cout << endl;
}

//list容器插入删除
void test02() {

    list<int> mlist;

    //插入操作
    mlist.push_back(100);
    mlist.push_front(200);

    mlist.insert(mlist.begin(), 300);
    mlist.insert(mlist.end(), 400);
    mlist.insert(mlist.end(), 200);

    list<int>::iterator it = mlist.begin();
    it++;
    it++;
    mlist.insert(it, 500);
    mlist.insert(mlist.end(), 200);
    //删除
    //mlist.pop_back();
    //mlist.pop_front();
    //mlist.erase(mlist.begin(), mlist.end()); //mlist.clear();
    mlist.remove(200); //删除匹配所有值

    list<int>::iterator testit = mlist.begin();
    for (int i = 0; i < mlist.size() - 1; i++) {
        testit++;
    }

    (*(mlist.end()));

    cout << "------------" << endl;
    cout << (*testit) << endl;
    cout << mlist.back() << endl;
    cout << "------------" << endl;

    //删除所有200 还是删除第一次出现的200
    /*
        for (list<int>::iterator lit = mlist.begin(); lit != mlist.end(); lit++){
        cout << *lit << " ";
        }
        cout << endl;
    */
}

//赋值操作
void test03() {
    list<int> mlist;
    mlist.assign(10, 10);

    list<int> mlist2;
    mlist2 = mlist;

    mlist2.swap(mlist);
}

//排序 翻转
void test04() {

    list<int> mlist;
    for (int i = 0; i < 10; i++) {
        mlist.push_back(i);
    }

    for (list<int>::iterator it = mlist.begin(); it != mlist.end(); it++) {
        cout << *it << " ";
    }
    cout << endl;

    //容器元素反转
    mlist.reverse();

    for (list<int>::iterator it = mlist.begin(); it != mlist.end(); it++) {
        cout << *it << " ";
    }
    cout << endl;

}

bool mycompare05(int v1, int v2) {
    return v1 > v2;
}

//排序
void test05() {

    list<int> mlist;
    mlist.push_back(2);
    mlist.push_back(1);
    mlist.push_back(7);
    mlist.push_back(5);

    for (list<int>::iterator it = mlist.begin(); it != mlist.end(); it++) {
        cout << *it << " ";
    }
    cout << endl;

    //排序  对象怎么排序? 默认从小到大
    mlist.sort();


    for (list<int>::iterator it = mlist.begin(); it != mlist.end(); it++) {
        cout << *it << " ";
    }
    cout << endl;

    //从大到小
    mlist.sort(mycompare05);

    for (list<int>::iterator it = mlist.begin(); it != mlist.end(); it++) {
        cout << *it << " ";
    }
    cout << endl;

    //算法sort 支持可随机访问的
}

int main(void) {
    //test01();
    //test02();
    //test04();
    test05();
    return 0;
}