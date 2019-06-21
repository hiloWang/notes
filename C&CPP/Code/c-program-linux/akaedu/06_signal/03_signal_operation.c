/*
 ============================================================================

 Author      : Ztiany
 Description : Linux 进程：阻塞信号

 ============================================================================
 */

/*
具体参考：https://akaedu.github.io/book/ch33s03.html
*/

#include <stdio.h>
#include <error.h>
#include <signal.h>
#include <unistd.h>

void printsigset(const sigset_t *set)
{
	int i;
	for (i = 1; i < 32; i++)
		if (sigismember(set, i) == 1)
			putchar('1');
		else
			putchar('0');
	puts("");
}

int main(void)
{
	sigset_t s, p;
	sigemptyset(&s);
	sigaddset(&s, SIGINT);
	sigprocmask(SIG_BLOCK, &s, NULL);
	while (1) {
		sigpending(&p);
		printsigset(&p);
		sleep(1);
	}
	return 0;
}