/*
 ============================================================================

 Author      : Ztiany
 Description : Linux 进程：exec 函数

 ============================================================================
 */

/*
exec 函数

具体参考：https://akaedu.github.io/book/ch30s03.html#id2866732
*/

#include <stdlib.h>
#include <error.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <unistd.h>

int execPS()
{
    execlp("ps", "ps", "-o", "pid,ppid,pgrp,session,tpgid,comm", NULL);
    perror("exec ps");
    exit(1);
}

/*
示例1：
    输入任何字母，都会重新以大写字母打印出来，注意：按Ctrl-D表示EOF

实例2：（调用exec后，原来打开的文件描述符仍然是打开的。利用这一点可以实现I/O重定向。）
    编译 03_exec.c：gcc 03_exec.c -o 3.out
    创建文件：touch file.txt
    输入内容：echo "this is the file, file.txt, it is all lower case." > file.txt
    执行以下命令：./3.out 2 < file.txt
    输出结果：THIS IS THE FILE, FILE.TXT, IT IS ALL LOWER CASE.
*/
void upper(){
    int ch;
	while((ch = getchar()) != EOF) {
		putchar(toupper(ch));
	}
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
       execPS();
    }
    else if (strcmp(target, "2") == 0)
    {
        upper();
    }
    else
    {
        printf("specfiy which function that you want to call\n");
    }

    return 0;
}
