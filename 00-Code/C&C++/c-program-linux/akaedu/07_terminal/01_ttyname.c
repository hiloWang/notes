/*
 ============================================================================

 Author      : Ztiany
 Description : Linux 进程：终端

 ============================================================================
 */

/*
具体参考：https://akaedu.github.io/book/ch34s01.html
*/

#include <unistd.h>
#include <stdio.h>

int main()
{
    printf("fd 0: %s\n", ttyname(0));
    printf("fd 1: %s\n", ttyname(1));
    printf("fd 2: %s\n", ttyname(2));
    return 0;
}