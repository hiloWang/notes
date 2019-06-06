/*
 ============================================================================

 Author      : Ztiany
 Description : Linux 进程：环境变量

 ============================================================================
 */

/*
exec系统调用执行新程序时会把命令行参数和环境变量表传递给main函数。

具体参考：https://akaedu.github.io/book/ch30s02.html
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
下面函数用于打印环境遍历
*/
void printEnv()
{
    //libc中定义的全局变量environ指向环境变量表，environ没有包含在任何头文件中，所以在使用时要用extern声明。
    extern char **environ;
    int i;
    for (i = 0; environ[i] != NULL; i++)
    {
        printf("%s\n", environ[i]);
    }
}

/*
用environ指针可以查看所有环境变量字符串，但是不够方便，如果给出name要在环境变量表中查找它对应的value，可以用getenv函数。

    #include <stdlib.h>
    char *getenv(const char *name);

修改环境变量可以用以下函数

    #include <stdlib.h>

    int setenv(const char *name, const char *value, int rewrite);
    void unsetenv(const char *name);
*/
void modifyEnv()
{
    printf("PATH=%s\n", getenv("PATH"));
    setenv("PATH", "hello", 1);
    printf("PATH=%s\n", getenv("PATH"));
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
        printEnv();
    }
    else if (strcmp(target, "2") == 0)
    {
        modifyEnv();
    }
    else if (strcmp(target, "3") == 0)
    {
    }
    else
    {
        printf("specfiy which function that you want to call\n");
    }

    return 0;
}
