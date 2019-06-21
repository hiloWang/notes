/*
 ============================================================================
 
 Author      : Ztiany
 Description : 字符串流

 ============================================================================
 */
#include <cstdlib>
#include <iostream>
#include <sstream>

using namespace std;

void sample1() {
    string line, word;
    while (getline(cin, line)) {

        if (line.empty()) {
            break;
        }

        istringstream stream(line);
        while (stream >> word) {
            cout << word;
            cout << " ";
        }
        cout << endl;
    }
}

void sample2() {
    //拼接字符串
    int val1 = 512, val2 = 1024;
    ostringstream format_message;
    format_message << "val1: " << val1 << "\n" << "val2: " << val2 << "\n";

    cout << "input is " << format_message.str() << endl;

    //从字符串获取数字
    istringstream input_istring(format_message.str());
    string dump;
    input_istring >> dump >> val1 >> dump >> val2;

    cout << "number is " << val1 << " " << val2 << endl;
}

int main() {
    sample1();
    sample2();
    return EXIT_SUCCESS;
}
