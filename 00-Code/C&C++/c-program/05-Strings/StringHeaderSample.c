/*
 ============================================================================
 
 Author      : Ztiany
 Description : string.h 相关示例

 ============================================================================
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void memsetSample();
void getStringLength();

int main(int argc, char const *argv[])
{
    // memsetSample();
    getStringLength();
    return EXIT_SUCCESS;
}

/*
1.2. 取字符串的长度：
strlen函数返回s所指的字符串的长度。该函数从s所指的第一个字符开始找'\0'字符，一旦找到就返回，返回的长度不包括'\0'字符在内。例如定义char buf[] = "hello";，则strlen(buf)的值是5，但要注意，如果定义char buf[5] = "hello";，则调用strlen(buf)是危险的，会造成数组访问越界。
*/
void getStringLength()
{
    char hello[] = "Hello";
    printf("hello length = %d \n", strlen(hello));
}

/*
1.1. 初始化字符串：
memset函数把s所指的内存地址开始的n个字节都填充为c的值。通常c的值为0，把一块内存区清零。例如定义char buf[10];，如果它是全局变量或静态变量，则自动初始化为0（位于.bss段），如果它是函数的局部变量，则初值不确定，可以用memset(buf, 0, 10)清零，由malloc分配的内存初值也是不确定的，也可以用memset清零。
*/
void memsetSample()
{
    char buf[10];

    int length = sizeof(buf);
    printf("length = %d \n", length);

    memset(buf, 'd', sizeof(buf));

    for (int i = 0; i < length; i++)
    {
        printf("index %d is %c \n", i, buf[i]);
    }
}