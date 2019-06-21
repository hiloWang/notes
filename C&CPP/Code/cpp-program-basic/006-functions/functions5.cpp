/*
 ============================================================================
 
 Author      : Ztiany
 Description : 默认参数

 ============================================================================
 */

#include <cstdlib>
#include <string>

using namespace std;

typedef string::size_type sz;

/*参数中可以定义默认参数*/
static string screen(sz ht = 24, sz wid = 80, char back = ' ');

/*声明内联函数*/
inline int add(int a, int b) {
    return a + b;
}

/*声明constexpr函数 */
constexpr int length() {
    return 3;
}

int main() {
    string window;

    window = screen();
    window = screen(33);
    window = screen(33, 44);
    window = screen(33, 44, '#');

    return EXIT_SUCCESS;
}

static string screen(sz ht = 24, sz wid = 80, char back = ' ') {
    return "Fake Screen";
}