/*
 ============================================================================
 
 Author      : Ztiany
 Description : stdint.h和inttypes.h系列函数

 ============================================================================
 */

//stdint.h和inttypes.h中新增了可移植的整数类型

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <inttypes.h>

int main() {
    int32_t me32;//不同的平台下，me32始终是一个32位有符号整型
    return EXIT_SUCCESS;
}