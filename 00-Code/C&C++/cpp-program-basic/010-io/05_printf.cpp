/*
 ============================================================================
 
 Author      : Ztiany
 Description : 格式化输出

 ============================================================================
 */

#include <iostream>
#include <cstdlib>
#include <iomanip>

using namespace std;

/*
 在输出数据时，为简便起见，往往不指定输出的格式，由系统根据数据的类型采取默认的格式，但有时希望数据按指定的格式输出，
 如要求以十六进制或八进制形式输出一个 整数，对输出的小数只保留两位小数等。有两种方法可以达到此目的。

1. 使用控制符的方法
2. 使用流对象的有关成员函数。
 */

//用控制符控制输出格式。
void printf1() {

    int a;
    cout << "input a:";
    cin >> a;
    cout << "dec:" << dec << a << endl;  //以十进制形式输出整数
    cout << "hex:" << hex << a << endl;  //以十六进制形式输出整数a
    cout << "oct:" << setbase(8) << a << endl;  //以八进制形式输出整数a

    const char *pt = "China";  //pt指向字符串"China"
    cout << setw(10) << pt << endl;  //指定域宽为,输出字符串
    cout << setfill('*') << setw(10) << pt << endl;  //指定域宽,输出字符串,空白处以'*'填充

    double pi = 22.0 / 7.0;  //计算pi值
    //按指数形式输出,8位小数
    cout << setiosflags(ios::scientific) << setprecision(8);
    cout << "pi=" << pi << endl;  //输出pi值
    cout << "pi=" << setprecision(4) << pi << endl;  //改为位小数
    cout << "pi=" << setiosflags(ios::fixed) << pi << endl;  //改为小数形式输出
}

//用流控制成员函数输出数据。
void printf2() {
    int a = 21;
    cout.setf(ios::showbase);//显示基数符号(0x或)
    cout << "dec:" << a << endl;         //默认以十进制形式输出a
    cout.unsetf(ios::dec);         //终止十进制的格式设置
    cout.setf(ios::hex);           //设置以十六进制输出的状态
    cout << "hex:" << a << endl;         //以十六进制形式输出a
    cout.unsetf(ios::hex);         //终止十六进制的格式设置
    cout.setf(ios::oct);           //设置以八进制输出的状态
    cout << "oct:" << a << endl;         //以八进制形式输出a
    cout.unsetf(ios::oct);
    const char *pt = "China";              //pt指向字符串"China"
    cout.width(10);                //指定域宽为
    cout << pt << endl;                //输出字符串
    cout.width(10);                //指定域宽为
    cout.fill('*');                //指定空白处以'*'填充
    cout << pt << endl;                //输出字符串
    double pi = 22.0 / 7.0;            //输出pi值
    cout.setf(ios::scientific);    //指定用科学记数法输出
    cout << "pi=";                   //输出"pi="
    cout.width(14);                //指定域宽为
    cout << pi << endl;                //输出pi值
    cout.unsetf(ios::scientific); //终止科学记数法状态
    cout.setf(ios::fixed);        //指定用定点形式输出
    cout.width(12);               //指定域宽为
    cout.setf(ios::showpos);      //正数输出“+”号
    cout.setf(ios::internal);     //数符出现在左侧
    cout.precision(6);            //保留位小数
    cout << pi << endl;               //输出pi,注意数符“+”的位置
}

int main() {
    printf1();
    printf2();
    return 0;
}