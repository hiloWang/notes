/*
 ============================================================================
 
 Author      : Ztiany
 Description : 标准输入输出

 ============================================================================
 */
#include <iostream>

using namespace std;

int main() {
    string line;
    cin.getline(line); //最后换行丢弃
    cin.get(buf, 256); //不会读最后换行
    return 0;
}


