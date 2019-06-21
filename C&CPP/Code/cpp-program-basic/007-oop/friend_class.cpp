/*
 ============================================================================
 
 Author      : Ztiany
 Description : 友元函数和友元类

 ============================================================================
 */

#include "friend_class.h"

using namespace std;

int main() {

    Box box;
    box.setWidth(10.0);

    // 使用友元函数输出宽度
    printWidth(box);

    // 使用友元类获取宽度
    Friend aFriend;
    cout << "box width = " << aFriend.getBoxWidth(box);
};