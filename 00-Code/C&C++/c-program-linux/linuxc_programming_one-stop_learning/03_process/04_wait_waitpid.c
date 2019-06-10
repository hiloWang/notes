/*
 ============================================================================

 Author      : Ztiany
 Description : Linux 进程：wait和waitpid函数 函数

 ============================================================================
 */

/*
wait 和 waitpid函数  函数

具体参考：https://akaedu.github.io/book/ch30s03.html#id2867242
*/

#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <error.h>
#include <sys/types.h>
#include <sys/wait.h>

/*
在后台运行这个程序，然后用ps命令查看：
*/
int main(void)
{
    pid_t pid;
    pid = fork();
    if (pid < 0)
    {
        perror("fork failed");
        exit(1);
    }
    if (pid == 0)
    {
        int i;
        for (i = 3; i > 0; i--)
        {
            printf("This is the child\n");
            sleep(1);
        }
        exit(3);
    }
    else
    {
        int stat_val;
        waitpid(pid, &stat_val, 0);
        if (WIFEXITED(stat_val))
            printf("Child exited with code %d\n", WEXITSTATUS(stat_val));
        else if (WIFSIGNALED(stat_val))
            printf("Child terminated abnormally, signal %d\n", WTERMSIG(stat_val));
    }
    return 0;
}