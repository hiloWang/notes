/*
 ============================================================================

 Author      : Ztiany
 Description : 字符串与ctype.h

 ============================================================================
 */


#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

static bool isStrDigit(const char *str) {
    bool is_digit;
    while (*str) {
        is_digit = isdigit(*str);
        str++;
        if (!is_digit) {
            return false;
        }
    }
    return true;
}

int main() {
    char *str1 = "123432423";
    char *str2 = "32e2badfsdf";
    printf("str1 isdigit = %d\n", isStrDigit(str1));
    printf("str2 isdigit = %d\n", isStrDigit(str2));
    return EXIT_FAILURE;
}