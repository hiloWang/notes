#ifndef SALES_DATA_H
#define SALES_DATA_H

#include <string>
#include <iostream>

class Sales_data {

    //友元
    friend Sales_data add(const Sales_data &, const Sales_data &);

    friend std::ostream &print(std::ostream &, const Sales_data &);

    friend std::istream &read(std::istream &, Sales_data &);

public:
    // constructors，表示使用合成的默认构造函数
    Sales_data() = default;

    Sales_data(const std::string &s) : bookNo(s) {}

    //构造函数初始化列表
    Sales_data(const std::string &s, unsigned n, double p) : bookNo(s), units_sold(n), revenue(p * n) {}

    Sales_data(std::istream &);

    std::string isbn() const { return bookNo; }

    Sales_data &combine(const Sales_data &);

    double avg_price() const;

private:
    std::string bookNo;
    unsigned units_sold = 0;
    double revenue = 0.0;
};


//非成员函数接口
Sales_data add(const Sales_data &, const Sales_data &);

//定义成员函数
std::ostream &print(std::ostream &, const Sales_data &);

std::istream &read(std::istream &, Sales_data &);

inline bool compareIsbn(const Sales_data &lhs, const Sales_data &rhs) {
    return lhs.isbn() < rhs.isbn();
}

#endif
