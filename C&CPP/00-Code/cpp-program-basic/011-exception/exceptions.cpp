/*
 ============================================================================
 
 Author      : Ztiany
 Description : 异常处理

 ============================================================================
 */
#include <iostream>
#include <vector>

using namespace std;

static void tryCatch() {

    try {
        //throw用于抛出异常
        throw exception();
    } catch (exception &e) {//catch用于抓住异常
        cout << e.what() << endl;
    }

    try {
        throw logic_error("throw error");
    } catch (logic_error &logicError) {
        cout << logicError.what() << endl;
    }
}


struct MyException : public exception {
    const char *what() const throw() {
        return "C++ Exception";
    }
};

void customException() {
    try {
        throw MyException();
    }
    catch (MyException &e) {
        std::cout << "MyException caught" << std::endl;
        std::cout << e.what() << std::endl;
    }
    catch (std::exception &e) {
        //其他的错误
    }
}


//异常接口声明
//这个函数只能抛出int float char三种类型异常，抛出其他的程序就报错
void func() throw(int, float, char) {
    throw "abc";
}

//不能抛出任何异常
void func02() throw() {
    throw -1;
}

//可以抛出任何类型异常
void func03() {

}

void exceptionInterface() {
    try {
        func();
    }
    catch (char *str) {
        cout << str << endl;
    }
    catch (int e) {
        cout << "异常!" << endl;
    }
    catch (...) { //捕获所有异常
        cout << "未知类型异常！" << endl;
    }
}

int main() {
    tryCatch();
    customException();
    exceptionInterface();
    return EXIT_SUCCESS;
}