/*
 ============================================================================

 Author      : Ztiany
 Description : Linux 进程：通过终端按键产生信号

 ============================================================================
 */

/*
具体参考：https://akaedu.github.io/book/ch33s02.html
*/

#include <unistd.h>


/*
1：首先用 ulimit 命令改变Shell进程的 Resource Limit，允许 core 文件最大为 1024K:  ulimit -c 1024
2：执行程序
3：然后在终端键入 Ctrl-C或 Ctrl-\ 产生信号中断程序
4：使用 ls 可以查看 dump 出来的文件。
*/
int main(void)
{
	while(1);
	return 0;
}