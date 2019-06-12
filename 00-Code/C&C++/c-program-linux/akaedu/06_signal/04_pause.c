/*
 ============================================================================

 Author      : Ztiany
 Description : Linux 进程：捕捉信号

 ============================================================================
 */

/*
具体参考：https://akaedu.github.io/book/ch33s04.html
*/

#include <unistd.h>
#include <signal.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>

void sig_alrm(int signo)
{
    printf("sig_alrm %d\n",signo);
}

unsigned int mysleep1(unsigned int nsecs)
{
    struct sigaction newact, oldact;
    unsigned int unslept;

    newact.sa_handler = sig_alrm;
    sigemptyset(&newact.sa_mask);
    newact.sa_flags = 0;
    sigaction(SIGALRM, &newact, &oldact);

    alarm(nsecs);

    printf("after alarm ---------\n");

    pause();

    unslept = alarm(0);
    sigaction(SIGALRM, &oldact, NULL);

    return unslept;
}

int runMySleep1(void)
{
    while (1)
    {
        mysleep1(2);
        printf("Two seconds passed\n");
    }
    return 0;
}

unsigned int mysleep2(unsigned int nsecs)
{
    struct sigaction newact, oldact;
    sigset_t newmask, oldmask, suspmask;
    unsigned int unslept;

    /* set our handler, save previous information */
    newact.sa_handler = sig_alrm;
    sigemptyset(&newact.sa_mask);
    newact.sa_flags = 0;
    sigaction(SIGALRM, &newact, &oldact);

    /* block SIGALRM and save current signal mask */
    sigemptyset(&newmask);
    sigaddset(&newmask, SIGALRM);
    sigprocmask(SIG_BLOCK, &newmask, &oldmask);

    alarm(nsecs);

    printf("after alarm ---------\n");

    suspmask = oldmask;
    sigdelset(&suspmask, SIGALRM); /* make sure SIGALRM isn't blocked */
    sigsuspend(&suspmask);         /* wait for any signal to be caught */

    /* some signal has been caught,   SIGALRM is now blocked */

    unslept = alarm(0);
    sigaction(SIGALRM, &oldact, NULL); /* reset previous action */

    /* reset signal mask, which unblocks SIGALRM */
    sigprocmask(SIG_SETMASK, &oldmask, NULL);
    return (unslept);
}

int runMySleep2(void)
{
    while (1)
    {
        mysleep2(2);
        printf("Two seconds passed\n");
    }
    return 0;
}

int main(int argc, char const *argv[])
{

    if (argc < 2)
    {
        printf("specfiy which function that you want to call\n");
        return EXIT_FAILURE;
    }

    const char *target = argv[1];

    if (strcmp(target, "1") == 0)
    {
        runMySleep1();
    }
    else if (strcmp(target, "2") == 0)
    {
        runMySleep2();
    }
    else
    {
        printf("specfiy which function that you want to call\n");
    }

    return 0;
}