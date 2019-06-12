/*
 ============================================================================
 
 Author      : Ztiany
 Description : 泛型算法

 ============================================================================
 */

#include <cstdlib>
#include <iostream>
#include <algorithm>
#include <vector>
#include <ctime>
#include <numeric>

using namespace std;

class SortCompare {
public:
    SortCompare() {
        cout << "create a SortCompare" << endl;
    }

    bool operator()(int val1, int val2) {
        return val1 > val2;
    }
};

class Print {
public:
    void operator()(int val) {
        cout << val << " ";
    }
};

/**
 * merge算法
 * random_shuffle算法
 * reverse算法
 *
 * 上门算法要求支持容器的随机访问
 */
void sample01() {

    SortCompare sortCompare;

    vector<int> vector1;
    vector<int> vector2;

    //随机数种子
    srand((unsigned int) time(NULL));

    for (int i = 0; i < 10; ++i) {
        vector1.push_back(rand() % 10);
        vector2.push_back(rand() % 10);
    }

    sort(vector1.begin(), vector1.end(), sortCompare);
    sort(vector2.begin(), vector2.end(), sortCompare);

    vector<int> vector3;
    vector3.resize(vector1.size() + vector2.size());

    //merge算法要求合并的两个范围是有序集合
    merge(vector1.begin(), vector1.end(), vector2.begin(), vector2.end(), vector3.begin(), sortCompare);

    for_each(vector3.begin(), vector3.end(), Print());
    cout << endl;

    //反转算法
    reverse(vector3.begin(), vector3.end());
    for_each(vector3.begin(), vector3.end(), Print());
    cout << endl;

    //随机打算算法
    random_shuffle(vector3.begin(), vector3.end());
    for_each(vector3.begin(), vector3.end(), Print());
    cout << endl;

}

struct ReplaceIf {
    bool operator()(int val) {
        return val > 5;
    }
};

/**
 *  copy算法
 *  swap算法
 *  replace算法
 *  replace_if算法
 */
void sample02() {
    vector<int> vector1;
    srand((unsigned int) time(NULL));

    for (int i = 0; i < 10; i++) {
        vector1.push_back(rand() % 10);
    }

    vector<int> vector2;
    vector2.resize(vector1.size());
    //copy 复制容器内容
    copy(vector1.begin(), vector1.end(), vector2.begin());

    for_each(vector2.begin(), vector2.end(), Print());

    //swap 交互容器内容
    vector<int> vector3;
    for (int i = 0; i < 10; i++) {
        vector3.push_back(rand() % 10);
    }
    cout << endl;
    cout << "--------------------" << endl;
    cout << "swap before " << endl;
    for_each(vector3.begin(), vector3.end(), Print());
    cout << endl;
    for_each(vector2.begin(), vector2.end(), Print());
    cout << endl;
    swap(vector3, vector2);
    cout << "swap end " << endl;
    for_each(vector3.begin(), vector3.end(), Print());
    cout << endl;
    for_each(vector2.begin(), vector2.end(), Print());
    cout << endl;
    cout << "--------------------" << endl;

    cout << "--------------------" << endl;
    cout << "replace before " << endl;
    //replace：容器中所有的5替换为10000
    //replace(vector3.begin(), vector3.end(), 5, 10000);
    //replace_if：容器中所有的满足Replace的替换为10000
    replace_if(vector3.begin(), vector3.end(), ReplaceIf(), -1);
    for_each(vector3.begin(), vector3.end(), Print());
    cout << endl;
    cout << "replace end " << endl;
    cout << "--------------------" << endl;
}

/**
 * 常用算术算法：
 *  accumulate求和算法
 *  fill填充算法
 */
void sample03() {
    vector<int> vector1;
    vector1.push_back(1);
    vector1.push_back(2);
    vector1.push_back(9);
    vector1.push_back(3);

    //accumulate 求和
    cout << "accumulate " << endl;
    int ret = accumulate(vector1.begin(), vector1.end(), 0);
    cout << "ret:" << ret << endl;

    //fill 填充
    vector<int> vector2;
    vector2.resize(10);
    cout << "fill " << endl;
    fill(vector2.begin(), vector2.end(), 10);
    for_each(vector2.begin(), vector2.end(), Print());
    cout << endl;


    vector<int> vector3;
    vector<int> vector4;
    for (int i = 0; i < 10; i++) {
        vector3.push_back(i);
    }

    for (int i = 5; i < 15; i++) {
        vector4.push_back(i);
    }

    vector<int> vector5;
    //求交集
    cout << "set_intersection " << endl;
    vector5.resize(vector3.size() + vector4.size());
    set_intersection(vector3.begin(), vector3.end(), vector4.begin(), vector4.end(), vector5.begin());
    for_each(vector5.begin(), vector5.end(), Print());
    cout << endl;
    //求并集
    cout << "set_union " << endl;
    set_union(vector3.begin(), vector3.end(), vector4.begin(), vector4.end(), vector5.begin());
    for_each(vector5.begin(), vector5.end(), Print());
    cout << endl;
    //求差集
    vector<int> vector6;
    vector6.resize(5);
    cout << "set_difference " << endl;
    set_difference(vector3.begin(), vector3.end(), vector4.begin(), vector4.end(), vector6.begin());
    for_each(vector6.begin(), vector6.end(), Print());
    cout << endl;
}

//查找算法
void sampleFind() {
    vector<int> vec = {27, 210, 12, 47, 109, 83};
    int val = 83;
    //如果没有找到，则返回vec.cend()
    auto result = find(vec.begin(), vec.end(), val);
    int number = *result;
    cout << "number = " << number << endl;
    cout << "The value " << val << (result == vec.cend() ? " is not present" : " is present") << endl;
}

int main() {
    //sample01();
    //sample02();
    sample03();
    //sampleFind();
    return EXIT_SUCCESS;
}