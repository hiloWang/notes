/*
 ============================================================================

 Author      : Ztiany
 Description : 内存四区

 ============================================================================
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

//第一种表达式，指针s是局部变量，他的作用域是函数toStr内。它将其指向的地址返回，返回之后s即被销毁，庆幸s指向的地址被返回了回来。最终打印正确。
//s虽然是局部变量，被分配在栈空间，作用域是函数内部，但其指向的内容"abcdefghijkl"是常量，被分配在程序的常量区。直到整个程序结束才被销毁。
char *toStr1() {
    //这里的"abcdefghijkl"在常量区
    char *s = "abcdefghijkl";
    return s;
}

//第二种表达式：s是一数组，虽然在字符常量区存在"abcdefghijkl"，但是s是一个数组，栈种声明的数组是分配到栈空间，
// 所以c会把常量区的"abcdefghijkl"复制一份到toStr2的栈中，一旦函数退出，栈中这块内存就被释放。虽然返回一个地址，
// 但是已经失去它的意义了。所以打印函数2返回的结果是(null)
char *toStr2() {
    //这里的"abcdefghijkl"在栈区
    char s[] = "abcdefghijkl";
    return s;
}

void sample1() {
    char *s1 = toStr1();
    char *s2 = toStr2();
    //s1 =abcdefghijkl
    //s2 =(null)
    printf("s1 =%s\n", s1);
    printf("s2 =%s\n", s2);
}

int integer = 0; //全局初始化区
char *pc; //全局未初始化区

void sample2() {
    int b; //栈
    char s[] = "abc"; //栈
    char *p1; //栈
    char *p2; //栈
    char *p3 = "123456"; //123456\\0在常量区，p3在栈上。
    static int c = 0;//全局（静态）初始化区
    p1 = (char *) malloc(10);
    p2 = (char *) malloc(20);//分配得来的10和20字节的区域就在堆区。
    strcpy(p1, "123456"); //123456\\0放在常量区，编译器可能会将它与p3所指向的"123456"优化成一个地方。
}

//内存的生长方向
void sample3() {
    int a = 1;
    int b = 2;
    int c = 3;
    //&a=6422036,&b=6422032,&c=6422028
    printf("The growth direction of the stack: &a=%d,&b=%d,&c=%d\n", &a, &b, &c);
    int *pInt = malloc(3 * sizeof(int));
    //p1=1447040,p2=1447044,p3=1447048
    printf("The growth direction of the heap: p1=%d,p2=%d,p3=%d\n", pInt, pInt + 1, pInt + 2);
    free(pInt);
}

int main() {
    sample1();
    sample2();
    sample3();
    return EXIT_SUCCESS;
}