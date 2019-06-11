/*
 ============================================================================

 Author      : Ztiany
 Description : Linux socket编程：程序判断大小端模式

 ============================================================================
 */

/*
具体参考：https://akaedu.github.io/book/ch37s01.html
*/

#include <stdio.h>

int main()
{
    unsigned int i = 1;

    char *p = (char *)&i;

    if (*p)
        printf("little");
    else
        printf("little");

    return 0;
}