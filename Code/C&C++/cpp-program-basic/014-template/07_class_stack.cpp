#include <iostream>
#include <vector>
#include <cstdlib>
#include <string>
#include <stdexcept>

using namespace std;

template<class T>
class Stack {
private:
    vector<T> elems;     // 元素

public:
    void push(T const &);  // 入栈
    void pop();               // 出栈
    T top() const;            // 返回栈顶元素
    bool empty() const {       // 如果为空则返回真。
        return elems.empty();
    }
};

template<class T>
void Stack<T>::push(T const &elem) {
    elems.push_back(elem);
}

template<class T>
void Stack<T>::pop() {
    if (elems.empty()) {
        throw out_of_range("Stack<>::pop(): empty stack");
    }
    // 删除最后一个元素
    elems.pop_back();
}

template<class T>
T Stack<T>::top() const {
    if (elems.empty()) {
        throw out_of_range("Stack<>::top(): empty stack");
    }
    // 返回最后一个元素的副本
    return elems.back();
}

class A {
public:
    static int sA;
    int a;

    A() {
        a = sA++;
        cout << "init A: " << a << endl;
    }

};

int A::sA = 0;

int main() {
    try {
        // 操作 int 类型的栈
        Stack<int> intStack;  // int 类型的栈
        intStack.push(7);
        cout << intStack.top() << endl;

        // 操作 string 类型的栈
        Stack<string> stringStack;    // string 类型的栈
        stringStack.push("hello");
        cout << stringStack.top() << std::endl;
        stringStack.pop();

        // 操作 A 类型的栈
        Stack<A> aStack;  // a 类型的栈
        aStack.push(A());
        aStack.push(A());
        aStack.push(A());
        cout << "aStack.top() " << aStack.top().a << endl;
        A top = aStack.top();
        cout << "aStack.top() " << aStack.top().a << endl;
        cout << "top.a " << top.a << endl;
        top.a = 100;
        cout << "top.a " << top.a << endl;

        A a1;//默认初始化
        A a2 = a1;//拷贝初始化
        cout << "a1.a " << a1.a << endl;
        cout << "a2.a " << a2.a << endl;

        return EXIT_SUCCESS;
    }
    catch (exception const &ex) {
        cerr << "Exception: " << ex.what() << endl;
        return EXIT_FAILURE;
    }
}