#include <stdio.h>

/*
 ============================================================================
 
 Author      : Ztiany
 Description : 指针与字符串

 ============================================================================
 */


void stringPointer();


int main(){
    //通过指针引用字符串
    stringPointer();
}

void stringPointer() {
    char *string;
    string = "I am arr - 我是字符数组，我指向首元素的地址";
    printf("%s\n", string);
    printf("%c\n", *string);//从首地址取值
    char string1[] = "I am String";
    char *string2 = string1;
    printf("%s\n", string2);
    printf("%c\n", string1[2]);
    printf("%c\n", *(string2 + 5));
}


