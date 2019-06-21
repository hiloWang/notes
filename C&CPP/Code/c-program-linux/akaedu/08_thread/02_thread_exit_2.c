/*
 ============================================================================

 Author      : Ztiany
 Description : Linux线程：pthread

 ============================================================================
 */

/*
 Linux查看pthread手册：

man -k pthread 查看 pthread
man pthread-create 查看方法
apt-get install manpages-posix-dev 安装 pthread 手册
 */

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>

static int a = 3;

void *thr_fun(void *arg)
{
    char *no = (char *)arg;
    int i = 0;
    for (; i < 10; i++)
    {
        printf("%s thread, i:%d\n", no, i);
        if (i == 5)
        {
            //线程自己退出
            pthread_exit(&a);
        }
    }
}

void main()
{
    printf("main thread\n");
    //线程id
    pthread_t tid;
    //pthread_attr_t：线程的属性，NULL默认属性
    //thr_fun，线程创建之后执行的函数
    pthread_create(&tid, NULL, thr_fun, "1");

    void *rval;
    //等待tid线程结束
    //thr_fun与退出时传入的参数，都作为第二个参数的内容
    pthread_join(tid, &rval);
    printf("rval:%d\n", *((int *)rval));
}
