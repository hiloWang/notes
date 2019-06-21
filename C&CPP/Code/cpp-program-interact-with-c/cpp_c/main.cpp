/*
 ============================================================================

 Author      : Ztiany
 Description : CPP 调用 C

 ============================================================================
 */

/*
参考：https://github.com/hokein/Wiki/wiki/extern-%22C%22、http://www.cppblog.com/macaulish/archive/2008/06/17/53689.html

在C++中常在头文件见到extern "C"修饰函数，那有什么作用呢？ 是用于C++链接在C语言模块中定义的函数。

    C++虽然兼容C，但C++文件中函数编译后生成的符号与C语言生成的不同。因为C++支持函数重载，C++函数编译后生成的符时带有函数参数类型的信息，而C则没有。
    例如int add(int a, int b)函数经过C++编译器生成.o文件后，add会变成形如add_int_int之类的, 而C的话则会是形如_add, 就是说：相同的函数，在C和C++中，编译后生成的符号不同。
    这就导致一个问题：如果C++中使用C语言实现的函数，在编译链接的时候，会出错，提示找不到对应的符号。此时extern "C"就起作用了：告诉链接器去寻找_add这类的C语言符号，而不是经过C++修饰的符号。

 */

#include <iostream>

//C++调用C函数的例子: 引用C的头文件时，需要加extern "C"
extern "C" {
#include "CLibrary.h"
}

int main() {
    int a = 100;
    int b = 300;
    int ret = add(a, b);
    std::cout << "cpp call c add result = " << ret << std::endl;
    return 0;
}