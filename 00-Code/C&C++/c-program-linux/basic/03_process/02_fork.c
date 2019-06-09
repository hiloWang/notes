/*
 ============================================================================

 Author      : Ztiany
 Description : Linux 进程：fork 函数

 ============================================================================
 */

/*
fork 进程

具体参考：https://akaedu.github.io/book/ch30s03.html
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <unistd.h>

/*
执行结果：
# ./a.out
This is the parent
This is the child
This is the parent
This is the child
This is the parent
This is the child
This is the child
# This is the child
This is the child
*/
void forkNewProcess()
{
    pid_t pid;
    char *message;
    int n;
    pid = fork();
    if (pid < 0)
    {
        perror("fork failed");
        exit(1);
    }
    if (pid == 0)
    {
        message = "This is the child\n";
        n = 6;
    }
    else
    {
        message = "This is the parent\n";
        n = 3;
    }
    for (; n > 0; n--)
    {
        printf(message);
        sleep(1);
    }
}

int main(int argc, char const *argv[])
{
    forkNewProcess();
    return 0;
}
