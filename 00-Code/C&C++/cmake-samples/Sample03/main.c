#include <stdio.h>
#include <stdlib.h>
#include "Lib/Lib.h"

int main() {
    int a;
    int b;
    printf("enter two numbers, like 22,32\n");
    scanf("%d,%d", &a, &b);
    int result = add(a, b);
    printf("sum of number = %d", result);
    return EXIT_SUCCESS;
}