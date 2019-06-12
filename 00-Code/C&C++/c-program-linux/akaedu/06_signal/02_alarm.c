/*
 ============================================================================

 Author      : Ztiany
 Description : Linux 进程：由软件条件产生信号

 ============================================================================
 */

/*
具体参考：https://akaedu.github.io/book/ch33s02.html
*/

#include <unistd.h>
#include <stdio.h>

int main(void)
{
    int counter;
    alarm(1);
    for (counter = 0; 1; counter++)
    {
        printf("counter=%d ", counter);
    }
    return 0;
}