/*
 ============================================================================

 Author      : Ztiany
 Description : Linux线程：线程控制

 ============================================================================
 */

/*
具体参考：https://akaedu.github.io/book/ch35s02.html
*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

pthread_t ntid;

void printids(const char *s)
{
    pid_t pid;
    pthread_t tid;

    pid = getpid();
    tid = pthread_self();
    printf("%s pid %u tid %u (0x%x)\n", s, (unsigned int)pid, (unsigned int)tid, (unsigned int)tid);
}

void *thr_fn(void *arg)
{
    printids(arg);
    return NULL;
}

int main(void)
{
    int err;

    err = pthread_create(&ntid, NULL, thr_fn, "new thread: ");
    if (err != 0)
    {
        fprintf(stderr, "can't create thread: %s\n", strerror(err));
        exit(1);
    }
    printids("main thread:");
    sleep(1);

    return 0;
}