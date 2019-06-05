/*
 ============================================================================
 
 Author      : Ztiany
 Description : 字符串

 ============================================================================
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

//定义字符串
static void defineArray() {
    char c[] = {'a', 'b', '\0'};
    char *str1 = "hello";
    char *str2 = "abc""ddd""zz";
    char *str3 = "abc""ddd"
            "zz";

    printf("c = %o\n", c);//以八进制形式输出数组首地址
    printf("c = %s\n", c);//以字符串的形式输出字符数组
}

//获取字符串长度
static void getStringLength() {
    char charArr[] = "我的天啊";
    size_t arrLength = strlen(charArr);
    printf("长度是：%zd\n", arrLength);
}

//字符串比较
static void compareString() {
    char charArr1[] = "abcd";
    char charArr2[] = "ABCD";
    int result = strcmp(charArr1, charArr2);
    printf("字符串比较结果：%d\n", result);
}

//字符串拷贝
static void stringCopy() {
    char charArr1[] = "abc";
    char charArr2[] = "ABCD";
    strcpy(charArr2, charArr1);
    printf("复制后的字符串：%s\n", charArr2);
}

//转换为数字
static void stringToNumber() {
    char strNumber[] = "100";
    int num = atoi(strNumber);
    printf("转为数字：%d\n", num);
}


//遍历字符串
static void traverseString() {
    char *str = "Hello String";
    //*str与*str!='\0'其实是一样的意思，当*str=='\0'时，其整型值就是0，也就是false。
    while (*str) {//*str != '\0'
        putchar(*str);
        str++;
    }
}

int main() {
    //defineArray();
    //getStringLength();
    //compareString();
    //stringCopy();
    //stringToNumber();
    traverseString();
    return 0;
}


