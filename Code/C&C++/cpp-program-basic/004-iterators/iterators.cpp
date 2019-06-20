/*
 ============================================================================

 Author      : Ztiany
 Description : 迭代器

 ============================================================================
 */


#include <cstdlib>
#include <string>
#include <iostream>
#include <vector>

using namespace std;

//1：使用迭代器
static void useIterator() {

    string s1("abcdefg hello!!");

    //1：第一个字符变大写
    if (s1.begin() != s1.end()) {
        auto it = s1.begin();
        *it = toupper(*it);
    }
    cout << "s1 = " << s1 << endl;

    //2：所有的字符变大写
    for (auto it = s1.begin(); it != s1.end(); it++) {
        if (!isspace(*it)) {
            *it = toupper(*it);
        }
    }
    cout << "s1 = " << s1 << endl;
}


//2：迭代器类型
static void iteratorType() {
    //常量迭代器
    const string string1 = "abc";
    string::const_iterator iterator1 = string1.begin();
    //普通迭代器
    string string2 = "abc";
    string::iterator iterator2 = string2.begin();
    //auto
    auto it3 = string1.begin();
}


//3：解引用操作，箭头操作符
static void dereference() {
    vector<string> str_vector;
    for (char i = 'a'; i < 'z'; ++i) {
        string str = to_string(i);
        str_vector.push_back(str);
    }
    //->操作符把解引用和成员操作符结合在一起
    auto it = str_vector.begin();
    for (; it != str_vector.end(); it++) {
        cout << *it << "-" << (it->size()) << endl;
    }

    //注意：但凡使用了迭代器循环体，就不要向迭代器所属容器添加元素了。
}


int main() {
    //useIterator();
    //iteratorType();
    dereference();
    return EXIT_SUCCESS;
}