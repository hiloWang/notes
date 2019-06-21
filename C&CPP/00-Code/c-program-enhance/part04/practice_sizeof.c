/*
 ============================================================================
 
 Author      : Ztiany
 Description : sizeof

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>

int main(void) {

    char *p1[] = {"1111", "2222", "3333"};
    printf("%d  %d %d \n", sizeof(p1), sizeof(p1[0]), sizeof(p1) / sizeof(p1[0]));//3，长度为3

    char *p2[10] = {"1111", "2222", "3333"};
    //10，sizeof(p2) = 80; 10个指针为80，sizeof(p2[0]) = 8;一个指针为8
    printf("%d  %d %d \n", sizeof(p2), sizeof(p2[0]), sizeof(p2) / sizeof(p2[0]));

    char p3[][30] = {"1111", "2222", "3333"};
    printf("%d  %d %d \n", sizeof(p3), sizeof(p3[0]), sizeof(p3) / sizeof(p3[0]));//3

    char p4[10][30] = {"1111", "2222", "3333"};
    printf("%d  %d %d \n", sizeof(p4), sizeof(p4[0]), sizeof(p4) / sizeof(p4[0]));//10

    return EXIT_SUCCESS;
}


