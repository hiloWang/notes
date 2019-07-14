#include <stdio.h>
#include "get_num.h"

int main() {
    printf("Hello, World!\n");

    char *str = "123";

    int result = getInt(str, 0, "");

    printf("%d", result);

    return 0;
}