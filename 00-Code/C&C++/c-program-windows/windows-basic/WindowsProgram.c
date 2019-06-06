/*
 ============================================================================
 
 Author      : Ztiany
 Description : Windows编程

 ============================================================================
 */
#include <stdlib.h>
#include <stdio.h>
//windows.h is a Windows-specific header file for the C and C++ programming languages which contains declarations for all of the functions in the Windows API
//https://en.wikipedia.org/wiki/Windows.h
#include <windows.h>

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