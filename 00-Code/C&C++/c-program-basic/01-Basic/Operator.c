/*
 ============================================================================
 Author      : Ztiany
 Description : C语言的运算符
 ============================================================================
 */

#include <stdio.h>

void bitOperation(); //位运算
void printInt(char name, int x);

int main(void)
{
    //位运算
    bitOperation();
    return 1;
}

void bitOperation()
{

    int x = 10;
    int y = 12;
    int z = x & y;
    int v = ~x;
    int b = x ^ y;
    int n = x | y;
    int m = x << 3;
    int l = x >> 3;

    printInt('x', x);
    printInt('y', y);
    printInt('z', z);
    printInt('v', v);
    printInt('b', b);
    printInt('n', n);
    printInt('m', m);
    printInt('l', l);
    printInt('l', l);
}

void printInt(char name, int x)
{
    printf("数据%c的值为%d \n", name, x);
}