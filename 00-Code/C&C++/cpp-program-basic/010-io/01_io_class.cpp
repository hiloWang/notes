/*
 ============================================================================
 
 Author      : Ztiany
 Description : IO类

 ============================================================================
 */

#include <cstdlib>
#include <iostream>


void ioStatus();

void ioError();

void ioFlush();

using namespace std;

int main() {
    ioStatus();
    ioError();
    ioFlush();
    return EXIT_SUCCESS;
}

void ioFlush() {
    cout << unitbuf;//每一次操作都要刷新一次
    cout << "hello";
    cout << nounitbuf;//复位
}

void ioError() {
    int word;
    while (cin >> word) {//输入不合法则条件为false
        //ok 操作是成功的
        cout << "word = " << word << endl;
    }
}

void ioStatus() {
    cout << "hello" << endl;
    cout << cout.goodbit << endl;
    cout << cout.eofbit << endl;
    cout << cout.badbit << endl;
    cout << cout.failbit << endl;
}

