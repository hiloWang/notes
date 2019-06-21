/*
 ============================================================================
 
 Description : string

 ============================================================================
 */


#include <iostream>
#include<string>

using namespace std;

//初始化
void test01() {

    string s1; //调用无参构造
    string s2(10, 'a');
    string s3("abcdefg");
    string s4(s3); //拷贝构造

    cout << s1 << endl;
    cout << s2 << endl;
    cout << s3 << endl;
    cout << s4 << endl;
}

//赋值操作
void test02() {

    string s1;
    string s2("appp");
    s1 = "abcdef";
    cout << s1 << endl;
    s1 = s2;
    cout << s1 << endl;
    s1 = 'a';
    cout << s1 << endl;

    //成员方法assign
    s1.assign("jkl");
    cout << s1 << endl;
}

//取值操作
void test03() {
    string s1 = "abcdefg";
    //重载[]操作符
    for (int i = 0; i < s1.size(); i++) {
        cout << s1[i] << " ";
    }
    cout << endl;

    //at成员函数
    for (int i = 0; i < s1.size(); i++) {
        cout << s1.at(i) << " ";
    }
    cout << endl;

    //区别：[]方式 如果访问越界，直接挂了
    //at方式 访问越界 抛异常out_of_range

    try {
        //cout << s1[100] << endl;
        cout << s1.at(100) << endl;
    }
    catch (...) {
        cout << "越界!" << endl;
    }

}

//拼接操作
void test04() {
    string s = "abcd";
    string s2 = "1111";
    s += "abcd";
    s += s2;
    cout << s << endl;

    string s3 = "2222";
    s2.append(s3);
    cout << s2 << endl;

    string s4 = s2 + s3;
    cout << s4 << endl;
}

//查找操作
void test05() {
    string s = "abcdefghjfgkl";
    //查找第一次出现的位置
    int pos = s.find("fg");
    cout << "pos:" << pos << endl;

    //查找最后一次出现的位置
    pos = s.rfind("fg");
    cout << "pos:" << pos << endl;
}


//string替换
void test06() {
    string s = "abcdefg";
    s.replace(0, 2, "111");
    cout << s << endl;
}

//string比较
void test07() {
    string s1 = "abcd";
    string s2 = "abce";

    if (s1.compare(s2) == 0) {
        cout << "字符串相等!" << endl;
    } else {
        cout << "字符串不相等!" << endl;
    }
}

//子串操作
void test08() {
    string s = "abcdefg";
    string mysubstr = s.substr(1, 3);
    cout << mysubstr << endl;
}

//插入和删除
void test09() {
    string s = "abcdefg";
    s.insert(3, "111");
    cout << s << endl;

    s.erase(0, 2);
    cout << s << endl;
}

int main(void) {

    //test01();
    test02();
    //test03();
    //test04();
    //test05();
    //test06();
    //test07();
    //test08();
    //test09();

    return 0;
}