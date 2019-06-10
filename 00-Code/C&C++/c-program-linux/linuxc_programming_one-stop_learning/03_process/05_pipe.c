/*
 ============================================================================

 Author      : Ztiany
 Description : Linux 进程：管道

 ============================================================================
 */

/*
wait 和 waitpid函数  函数

具体参考：https://akaedu.github.io/book/ch30s04.html#id2867812
*/

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <error.h>
#include <sys/wait.h>

#define MAXLINE 80

int main(void)
{
    int n;
    int fd[2];
    pid_t pid;
    char line[MAXLINE];

    if (pipe(fd) < 0)
    {
        perror("pipe");
        exit(1);
    }
    if ((pid = fork()) < 0)
    {
        perror("fork");
        exit(1);
    }
    if (pid > 0)
    { /* parent */
        close(fd[0]);
        write(fd[1], "hello world\n", 12);
        wait(NULL);
    }
    else
    { /* child */
        close(fd[1]);
        n = read(fd[0], line, MAXLINE);
        sleep(1);
        write(STDOUT_FILENO, line, n);
    }
    return 0;
}