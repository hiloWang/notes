/*
 ============================================================================
 
 Author      : Ztiany
 Description : 复合类型：引用和指针

 ============================================================================
 */

#include "cstdlib"
#include "iostream"

using namespace std;

//1：引用类型
static void reference() {
    int value_a = 323;
    int &ref_a = value_a;//ref_a指向value_a
    //int &ref_b; //报错，引用类型必需被初始化
    ref_a = 3;
    cout << "value_a = " << value_a << endl;
}

//2：指针类型
static void pointer() {
    int value_a = 32;
    int *pA = &value_a;
    void *pVoid = &value_a;
    int *&refPa = pA;//refPa是对指针pA的引用
    cout << "value_a address = " << pA << endl;

    const int *pB = &value_a;//pB是一个指向整型常量的指针，pB不能修改其地址所指向的值
    int *const pC = &value_a;//pC是一个指针常量，pC不能再指向其他int变量
}

//3：const与引用
static void cons_reference() {
    int value_a = 3;
    const int &ref_a1 = value_a;
    const int &ref_a2 = 3;
    const int &ref_a3 = ref_a1 * 3;

    const int value_b = 3;
    //int &ref_b = value_b; //编译错误，因为value_b是一个常量，所以必需使用引用也必须是一个常量
}


//4：constexpr常量表达式
static constexpr int size_a() {
    return 333;
}

static void constexpr_type() {
    constexpr int value_b = 3243;
    constexpr int value_c = value_b + 3;
    constexpr int sz = size_a();//只有size_a是一个constexpr函数时才是一条正确的声明语句
}


//5：使用typedef定义类型别名
static void typeDef() {
    typedef int Integer;
    typedef const char *String;
}

//6：使用auto定义变量
static void autoValue() {
    int value_a = 3, value_b = 4;
    auto value_c = value_a + value_b;//编译器推断出value_c的类型是int类型
    auto pA = &value_a;//编译器推断出value_c的类型是int类型指针
    int *pB = pA;
    int value_d = value_c;
}

//7：使用delctype定义变量
static void decltype_type() {
    const int ci = 0, &cj = ci;
    decltype(ci) x = 0;//x的类型是const int
    decltype(cj) y = x;//y的类型是const int&，y绑定到了x
    //decltype(cj) z; //decltype(cj)是引用类型，必需初始化

    int i = 4, h = 44, *p = &i, &r = i;
    decltype(r + 0) b;//加法的结果是一个int，所以b的类型是一个int类型
    //(*p)是引用类型int&，必需被初始化。如果表达式的内容是解引用操作，则decltype将得到引用类型，
    // 正如我们熟悉的那样，解引用指针可以得到指针所指的对象
    //而且还能给这个对象赋值，因此decltype(*p)的结果就是int&，而非int
    decltype(*p) c = i;
    decltype(i) j;
    // decltype((i))形式的结果永远是引用，而 decltype(i)只有当i本身是一个引用的时候才是引用。
    decltype((i)) rB = h;
}

int main() {
    reference();
    pointer();
    constexpr_type();
    typeDef();
    autoValue();
    decltype_type();
    return EXIT_SUCCESS;
}
