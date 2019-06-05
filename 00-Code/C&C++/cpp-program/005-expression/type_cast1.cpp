/*
 ============================================================================
 
 Author      : Ztiany
 Description : C++显式类型转换

 ============================================================================
 */
#include <cstdlib>
#include <iostream>

using namespace std;

int main() {

    //static_cast
    int i1 = 4;
    int i2 = 5;
    double slope = static_cast<double >(i1) / i2;

    void *pSlope = &i1;//任何非常量对象的地址都能存入void*
    double *slope2 = static_cast<double * >(pSlope);


    //const_cast
    int i3 = 333;
    const int *p_ci3 = &i3;
    int *p_i3 = const_cast<int *>(p_ci3);
    *p_i3 = 100;//由于i3本身不是常量，所以这里的写是合法的

    cout << "p_i3 = " << *p_i3 << endl;//始终为：100
    cout << "i3 = " << i3 << endl;//如果i3是const的，那么无法修改i3的值，i3始终未为333

    return EXIT_SUCCESS;
}


