#include <malloc.h>
#include <stdio.h>

/*
 ============================================================================
 
 Author      : Ztiany
 Description : 动态内存管理与指向它的指针变量

 ============================================================================
 */

void mallocSample();
void callocSample();
void voidTypePointerSample1();
void voidTypePointerSample2();

int main() {
    mallocSample();
    callocSample();
    voidTypePointerSample1();
    voidTypePointerSample2();
}

void voidTypePointerSample1() {
    int *p1 = NULL;
    //申请内测后，强转为int类型的指针
    p1 = (int *) malloc(5 * sizeof(int));
    for (int j = 0; j < 5; ++j) {
        scanf("%d", p1 + j);
    }
    printf("不及格的：");
    for (int i = 0; i < 5; ++i) {
        if (*(p1 + i) < 60) {
            printf("%d ", *(p1 + i));
        }
    }
    printf("\n");
}

void voidTypePointerSample2() {
    //void指针类型 c99允许使用void指针类型，void不指向任何类型的数据，应该把其理解为执行向空类型或者不指向确定的类型的数据
    int a = 3;
    int *p1 = &a;
    char *p2 = NULL;
    void *p3 = NULL;
    p3 = (void *) p1;//p1转换为void指针类型
    p2 = (char *) p3;//void转换为char*类型
    printf("%d", *p1);

    p3 = &a;//赋值后得到的是纯地址，没有类型信息，不能通过*p3获取a的值。
    //printf("%d", *p3);//不合法，编译错误
}

void mallocSample() {
    //内存的动态分配，分配在堆中
    unsigned int size = 100;
    void *memory = malloc(size);//开启100个字节的临时分配空间，函数的返回值为第一个字节的地址，如果内存不足，返回NULL空指针
    printf("size = %d \n", sizeof(memory));// 64位系统 = 8
    free(memory);//使用完后要释放，memory应该是最近一次调用calloc或malloc函数的返回值
    memory = NULL;//置空是个好习惯
}

void callocSample() {
    //calloc(n, size)  n为元素个数，size为元素的长度，使用于为数组动态发分配空间
    void *memory = calloc(100, 4);//开启100*4的字节临时分配区域，返回起始地址，
    //已经分配的空间可以改变其大小，使用realloc重新分配，可以缩小也可以增大
    realloc(memory, 800);
    printf("size = %d \n", sizeof(memory));// 64位系统 = 8
    free(memory);
    memory = NULL;//置空是个好习惯
}