/*
 ============================================================================
 
 Author      : Ztiany
 Description : 

 ============================================================================
 */

#ifndef C_BASIC_STRUCT_TYPE_H
#define C_BASIC_STRUCT_TYPE_H

//定义Sales_data，表示销售数据
struct Sales_data {
    std::string bookNo;
    unsigned units_sold = 0;
    double revenue = 0.0;
};

//可以为创建的类型取一个"别名"
typedef struct {
    char title[50];
    char author[50];
    char subject[100];
    int book_id;
} Books;

#endif //C_BASIC_STRUCT_TYPE_H
