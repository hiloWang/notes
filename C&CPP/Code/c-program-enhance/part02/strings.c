/*
 ============================================================================

 Description : 字符串初始化

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*c语言没有字符串类型，通过字符数据模拟
  C语言字符串，以字符‘\0’, 数字0
*/
int main01(void) {
    //不指定长度, 没有0结束符，有多少个元素就有多长
    char buf[] = {'a', 'b', 'c'};
    printf("buf = %s\n", buf);//可能会乱码，没有0结束符

    //指定长度，后面没有赋值的元素，自动补0
    char buf2[100] = {'a', 'b', 'c'};
    printf("buf2 = %s\n", buf2);

    //所有元素赋值为0
    char buf3[100] = {0};

    //char buf4[2] = { '1', '2', '3' };//数组越界

    char buf5[50] = {'1', 'a', 'b', '0', '7'};
    printf("buf5 = %s\n", buf5);

    char buf6[50] = {'1', 'a', 'b', 0, '7'};
    printf("buf6 = %s\n", buf6);

    char buf7[50] = {'1', 'a', 'b', '\0', '7'};
    printf("buf7 = %s\n", buf7);


    //使用字符串初始化，常用
    char buf8[] = "agjdslgjlsdjg";
    //strlen: 测字符串长度，不包含数字0，字符'\0'
    //sizeof：测数组长度，包含数字0，字符'\0'
    printf("strlen = %d, sizeof = %d\n", strlen(buf8), sizeof(buf8));

    char buf9[100] = "agjdslgjlsdjg";
    printf("strlen = %d, sizeof = %d\n", strlen(buf9), sizeof(buf9));

    printf("test");
    //\012相当于\n
    char str[] = "\0129";
    printf("%s\n", str);


    printf("\n");
    system("pause");
    return 0;
}

int main(void) {

    char buf[] = "algjdlksajgldksjg";
    int i = 0;
    int n = strlen(buf);
    char *p = NULL;

    //[]方式
    for (i = 0; i < n; i++) {
        printf("%c", buf[i]);
    }
    printf("\n");

    //指针方法
    //数组名字，数组首元素地址
    p = buf;
    for (i = 0; i < n; i++) {
        printf("%c", p[i]);
    }
    printf("\n");

    for (i = 0; i < n; i++) {
        printf("%c", *(p + i));
    }
    printf("\n");

    for (i = 0; i < n; i++) {
        printf("%c", *(buf + i));
    }
    printf("\n");

    //buf和p完全等价吗？
    //p++;
    //buf++;
    //buf只是一个常量，不能修改

    printf("\n");
    system("pause");

    return 0;
}