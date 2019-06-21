/*
 ============================================================================
 
 Author      : Ztiany
 Description : 使用struct自定义数据结构

 ============================================================================
 */

#include <iostream>
#include "struct_type.h"
#include <cstring>

using namespace std;


static void test_sales_data() {

    Sales_data data1;//声明一个Sales_data

    double price;//书的单价

    cin >> data1.bookNo >> data1.units_sold >> price;//输入单价

    data1.revenue = data1.units_sold * price;

    cout << "bookNo = " << data1.bookNo << ", units_sold = " << data1.units_sold << ", revenue=" << data1.revenue;
}

static void test_Books() {
    Books Book1, Book2;

    // Book1 详述
    strcpy(Book1.title, "C++ 教程");//数据无法直接赋值，只能拷贝
    strcpy(Book1.author, "Runoob");
    strcpy(Book1.subject, "编程语言");
    Book1.book_id = 12345;

    // Book2 详述
    strcpy(Book2.title, "CSS 教程");
    strcpy(Book2.author, "Runoob");
    strcpy(Book2.subject, "前端技术");
    Book2.book_id = 12346;

    // 输出 Book1 信息
    cout << "第一本书标题 : " << Book1.title << endl;
    cout << "第一本书作者 : " << Book1.author << endl;
    cout << "第一本书类目 : " << Book1.subject << endl;
    cout << "第一本书 ID : " << Book1.book_id << endl;

    // 输出 Book2 信息
    cout << "第二本书标题 : " << Book2.title << endl;
    cout << "第二本书作者 : " << Book2.author << endl;
    cout << "第二本书类目 : " << Book2.subject << endl;
    cout << "第二本书 ID : " << Book2.book_id << endl;

}


int main() {
    test_sales_data();
    test_Books();
    return EXIT_SUCCESS;
}