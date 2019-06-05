#include <stdio.h>

/*
 ============================================================================
 
 Author      : Ztiany
 Description : 指向函数的指针，函数指针作为函数参数，返回指针的函数

 ============================================================================
 */

void functionPointer();
void functionPointerParameter();
void returnPointerFunction();

int main() {
    //示例：指针函数
    functionPointer();
    //示例：函数指针参数
    functionPointerParameter();
    //示例：返回指针的函数
    returnPointerFunction();
}

/**
 * @param pointer 指向float[4]的指针
 * @param i 查询的位置
 * @return 该函数返回一个float类型的指针
 */
float *search(float(*pointer)[4], int i) {
    float *pt;
    //pointer + i移动的是 i*sizeof(float[4])个字节
    pt = *(pointer + i);//取出第i个float[4]的首地址返回
    return pt;//返回指针的函数
}

void returnPointerFunction() {
    float score[][4] = {{66,  77,  88,  99},
                        {666, 727, 868, 994},
                        {666, 757, 848, 939},
                        {166, 277, 188, 199}};
    float (*ps)[4] = score;//有一个指针Ps，指向一个一维数组
    float *p;
    int i, k;
    scanf("%d %d", &i, &k);
    p = search(score, k);
    printf("%f", *(p + i));
}


/**
 *
 * @param function 一个int(int, int)的函数
 * @param a 函数参数a
 * @param b 函数参数b
 * @return 返回function的结果
 */
int runFunction(int(*function)(int, int), int a, int b) {
    return (*function)(a, b);
}

void functionPointerParameter() {
    int maxValue(int, int);
    int (*p)(int, int) = maxValue;
    int a, b;
    scanf("%d %d", &a, &b);
    int result = runFunction(p, a, b);
    printf("%d ", result);
}


void functionPointer() {
    //函数指针，把函数理解为数据类型，所以变量名为maxValue，类型为int
    int maxValue(int, int);
    int a, b;
    scanf("%d %d", &a, &b);

    //首先是*p,它是一个指针，然后是int (int,int),这是一个函数，所以类型是一个指向函数的指针
    int (*p)(int, int);
    //把maxValue赋值给p
    p = maxValue;
    //调用指针函数
    int c = (*p)(a, b);
    printf("maxValue = %d", c);
}

int maxValue(int a, int b) {
    return a > b ? a : b;
}

