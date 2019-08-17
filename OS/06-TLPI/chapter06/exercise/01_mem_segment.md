# 问题

假设下面程序文件名为 `mem_segment.c`：

```c
#include <stdio.h>
#include <stdlib.h>

char globBuf[65536];            /* 未初始化数据段 */
int primes[] = { 2, 3, 5, 7 };  /* 初始化数据段 */

static int
square(int x)                   /* 在 square() 函数的栈帧中分配  */
{
    int result;                 /* 在 square() 函数的栈帧中分配 */

    result = x * x;
    return result;              /* 返回值通过寄存器传递 */
}

static void
doCalc(int val)                 /* 在 doCalc() 函数的栈帧中分配 */
{
    printf("The square of %d is %d\n", val, square(val));

    if (val < 1000) {
        int t;                  /* 在 doCalc() 函数的栈帧中分配 */

        t = val * val * val;
        printf("The cube of %d is %d\n", val, t);
    }
}

int
main(int argc, char *argv[])    /* 在 main() 函数的栈帧中分配 */
{
    static int key = 9973;      /* 初始化数据段 */
    static char mbuf[10240000]; /* 未初始化数据段 */
    char *p;                    /* 在 main() 函数的栈帧中分配 */

    p = malloc(1024);           /* 在堆中分配 */

    doCalc(key);

    exit(EXIT_SUCCESS);
}
```

使用 `ls -l` 命令显示该可执行文件的大小。虽然该程序包含一个大约 `10MB` 的数组，但可执行文件大小要远小于此，为什么？

答：这个数据只需要在运行时进行分配，可执行文件只需记录未初始化数据段的位置及所需大小，直到运行时再由程序加载器来分配这一空间。
