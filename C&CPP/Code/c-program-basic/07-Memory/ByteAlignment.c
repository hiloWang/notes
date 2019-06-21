/*
 ============================================================================

 Author      : Ztiany
 Description : 字节对齐

 ============================================================================
 */

#include <stdlib.h>
#include <stdio.h>

/* OFFSET宏定义可取得指定结构体某成员在结构体内部的偏移 */
#define OFFSET(st, field)     (size_t)&(((st*)0)->field)

static void structSample1() {

    struct T_FOO {
        char c1;//0
        short s;//2
        char c2;//4
        int i;//8
    } a;

    printf("c1 -> %d, s -> %d, c2 -> %d, i -> %d\n",
           (unsigned int) (void *) &a.c1 - (unsigned int) (void *) &a,
           (unsigned int) (void *) &a.s - (unsigned int) (void *) &a,
           (unsigned int) (void *) &a.c2 - (unsigned int) (void *) &a,
           (unsigned int) (void *) &a.i - (unsigned int) (void *) &a);
    //64位系统：c1 -> 0, s -> 2, c2 -> 4, i -> 8
}


static void structSample2() {
/*
结构体字节对齐的细节和具体编译器实现相关，但一般而言满足三个准则：

    1 结构体变量的首地址能够被其最宽基本类型成员的大小所整除；
    2 结构体每个成员相对结构体首地址的偏移量(offset)都是成员大小的整数倍，如有需要编译器会在成员之间加上填充字节(internal adding)；
    3 结构体的总大小为结构体最宽基本类型成员大小的整数倍，如有需要编译器会在最末一个成员之后加上填充字节{trailing padding}。
*/
    struct A {
        int a;//0-3
        char b;//4,空出5
        short c;//6-7
    };
    struct B {
        char b;//0，空出1-3
        int a;//4-7
        short c;//8-9,空出10-11
    };
    //8：首先分配4，然后分配1，由于需要对齐空出一个字节，然后分配2
    printf("sizeof(A)=%d\n", sizeof(struct A));
    //12：首先分配1，由于需要对齐，空出3个字节，然后分配4个字节，最后分配两个字节，然后需要满足结构体的大小是最大成员的整数倍，再分配2个字节
    printf("sizeof(B)=%d\n", sizeof(struct B));
}


static int structSample3() {

    typedef struct {
        char a;//0，空出1
        short b;//2-3
        char c;//4，空出5-7
        int d;//8-11
        char e[3];//12-14，空出15
    } T_Test;

    //把0转为T_Test的指针类型，然后去拿成员a，然后取其地址，然后转为size_t类型
    (size_t) &(((T_Test *) 0)->a);

    printf("Size = %d\n  a-%d, b-%d, c-%d, d-%d\n  e[0]-%d, e[1]-%d, e[2]-%d\n", sizeof(T_Test),
           OFFSET(T_Test, a),
           OFFSET(T_Test, b),
           OFFSET(T_Test, c),
           OFFSET(T_Test, d),
           OFFSET(T_Test, e[0]),
           OFFSET(T_Test, e[1]),
           OFFSET(T_Test, e[2]));
    /*
     Size = 16
      a-0, b-2, c-4, d-8
      e[0]-12, e[1]-13, e[2]-14
     */
    return 0;
}

int main() {
    structSample1();
    structSample2();
    structSample3();
    return EXIT_SUCCESS;
}