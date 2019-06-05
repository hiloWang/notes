/*
 ============================================================================
 
 Author      : Ztiany
 Description : Windows编程

 ============================================================================
 */
#include <stdlib.h>
#include <windows.h>
#include <stdio.h>

int main(int argc, char *argv[]) {
    Sleep(1000);
    system("mspaint");//启动绘画板
    int *p;
    int a = 4;
    p = &a;
    printf("a = %d \n", a);
    system("pause");//系统暂停
    return EXIT_SUCCESS;
}