/*
 ============================================================================

 Author      : Ztiany
 Description : Windows获取路径

 ============================================================================
 */

//direct.h is a C/C++ header file provided by Microsoft Windows, which contains functions for manipulating file system directories. Some POSIX functions that do similar things are in unistd.h.
//https://en.wikipedia.org/wiki/Direct.h
#include <direct.h>
#include <stdio.h>

int main() {
    char path[100];
    char* result = _getcwd(path, sizeof(path));
    printf("当前路径为：%s\n", result);
    printf("当前路径为：%s\n", path);
}