/*
 ============================================================================

 Author      : Ztiany
 Description : stddef.h系列函数

 ============================================================================
 */

//stddef .h 头文件定义了各种变量类型和宏。这些定义中的大部分也出现在其它头文件中。


#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
    void *a = NULL;
    size_t size = sizeof(a);
    size_t wide_char_size = sizeof(wchar_t);
    printf("NULL size = %d\n", size);
    printf("wide_char_size  = %d\n", wide_char_size);

    struct address {
        char name[50];
        char street[50];
        int phone;
    };

    //offsetof:会生成一个类型为 size_t 的整型常量，它是一个结构成员相对于结构开头的字节偏移量。成员是由 member-designator 给定的，结构的名称是在 type 中给定的。
    printf("address 结构中的 name 偏移 = %d 字节。\n", offsetof(struct address, name));
    printf("address 结构中的 street 偏移 = %d 字节。\n", offsetof(struct address, street));
    printf("address 结构中的 phone 偏移 = %d 字节。\n", offsetof(struct address, phone));

    return EXIT_SUCCESS;
}