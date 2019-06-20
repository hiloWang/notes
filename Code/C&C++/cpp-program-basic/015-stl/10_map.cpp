/*
 ============================================================================
 
 Description : map

 ============================================================================
 */


#include <iostream>
#include <map>

using namespace std;

//map容器初始化
void test01() {

    //map容器模板参数，第一个参数key的类型，第二参数value类型
    map<int, int> mymap;

    //插入数据  pair.first key值 piar.second value值
    //第一种
    pair<map<int, int>::iterator, bool> ret = mymap.insert(pair<int, int>(10, 10));
    if (ret.second) {
        cout << "第一次插入成功!" << endl;
    } else {
        cout << "插入失败!" << endl;
    }
    ret = mymap.insert(pair<int, int>(10, 20));
    if (ret.second) {
        cout << "第二次插入成功!" << endl;
    } else {
        cout << "插入失败!" << endl;
    }
    //第二种
    mymap.insert(make_pair(20, 20));
    //第三种
    mymap.insert(map<int, int>::value_type(30, 30));
    //第四种
    mymap[40] = 40;
    mymap[10] = 20;
    mymap[50] = 50;
    //发现如果key不存在，创建pair插入到map容器中
    //如果发现key存在，那么会修改key对应的value

    //打印
    for (map<int, int>::iterator it = mymap.begin(); it != mymap.end(); it++) {
        // *it 取出来的是一个pair
        cout << "key:" << (*it).first << " value:" << it->second << endl;
    }

    //如果通过[]方式去访问map中一个不存在key，
    //那么map会将这个访问的key插入到map中，并且给value一个默认值
    cout << " mymap[60]: " << mymap[60] << endl;

    //打印
    for (map<int, int>::iterator it = mymap.begin(); it != mymap.end(); it++) {
        // *it 取出来的是一个pair
        cout << "key:" << (*it).first << " value:" << it->second << endl;
    }

}

class MyKey {
public:
    MyKey(int index, int id) {
        this->mIndex = index;
        this->mID = id;
    }

public:
    int mIndex;
    int mID;
};

struct mycompare {
    bool operator()(MyKey key1, MyKey key2) {
        return key1.mIndex > key2.mIndex;
    }
};

void test02() {

    map<MyKey, int, mycompare> mymap; //自动排序，自定数据类型，咋排?

    mymap.insert(make_pair(MyKey(1, 2), 10));
    mymap.insert(make_pair(MyKey(4, 5), 20));

    for (map<MyKey, int, mycompare>::iterator it = mymap.begin(); it != mymap.end(); it++) {
        cout << it->first.mIndex << ":" << it->first.mID << " = " << it->second << endl;
    }
}

//equal_range
void test03() {

    map<int, int> mymap;
    mymap.insert(make_pair(1, 4));
    mymap.insert(make_pair(2, 5));
    mymap.insert(make_pair(3, 6));

    pair<map<int, int>::iterator, map<int, int>::iterator> ret = mymap.equal_range(2);
    if (ret.first != mymap.end()) {
        cout << "找到lower_bound！" << endl;
    } else {
        cout << "没有找到";
    }

    if (ret.second != mymap.end()) {
        cout << "找到upper_bound！" << endl;
    } else {
        cout << "没有找到";
    }
}

int main(void) {
    //test01();
    //test02();
    test03();
    return 0;
}