/*
 ============================================================================
 
 Author      : Ztiany
 Description : 复杂类型

 ============================================================================
 */

// fp 是一个指针，指向一个函数，该函数的参数为  void*，返回为一个数组的指针，该指针指向的数组类型为 `int [10]`。
int (*(*fp)(void *))[10];


//signal 是一个函数，该函数的参数为一个 int 变量和一个 函数指针 handler
//phandler 接受一个 int 变量，没有返回值。
//signal 返回一个 函数指针，该指针指向一个函数，假设为 p2
//p2 接受一个 int 变量，没有返回值。
void (*signal(int signum, void (*handler)(int)))(int);

int main(int argc, char const *argv[])
{
    int a = 3;
    int b = (fp(&a)[0]);
    return 0;
}
