/*
 ============================================================================
 
 Author      : Ztiany
 Description : C++函数重载

 ============================================================================
 */
#include <cstdlib>
#include <string>

using namespace std;

//尽管该函数可以接受非常量参数，但是它的返回值始终是常量类型
const string &shortString(const string &s1, const string &s2) {
    return s1.size() <= s2.size() ? s1 : s2;
}


//如果希望传递的是非const引用，那么返回值也是非const引用？

//利用const_cast将参数转换为const引用，然后调用const版的short_string，返回将返回值再次通过const_cast转换为非常量引用
string &shortString(string &s1, string &s2) {
    auto &r = shortString(const_cast<const string>(s1), const_cast<const string>(s2));
    return const_cast<string &>(r);
}
