#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
3. 有一个字符串“1a2b3d4z”；
要求写一个函数实现如下功能：
	功能1：把偶数位字符挑选出来，组成一个字符串1。
	功能2：把奇数位字符挑选出来，组成一个字符串2。
	功能3：把字符串1和字符串2，通过函数参数，传送给main，并打印。
	功能4：主函数能测试通过。
int getStr1Str2(char *source, char *buf1, char *buf2);
*/

int getStr1Str2(char *source, char *buf1, char *buf2) {

    if (source == NULL || buf1 == NULL || buf2 == NULL) {
        return -1;
    }

    int i = 0;
    int n = strlen(source); //字符串长度

    //最好不要直接操作形参，借助辅助变量把形参接过来
    char *p1 = buf1;
    char *p2 = buf2;

    for (i = 0; i < n; i++) {
        if ((i + 1) % 2 == 0)//偶数位
        {
            *p1 = *(source + i); //等价于 *p1 = source[i]
            p1++;
        } else //奇数位
        {
            *p2 = *(source + i);//等价于 *p2 = source[i]
            p2++;
        }
    }

    //字符串结束符
    *p1 = 0; //'\0'
    *p2 = 0; //'\0'

    return 0;
}

int main(void) {
    char str[] = "1a2b3d4z";
    char buf1[50] = {0};
    char buf2[50] = {0};

    int ret = getStr1Str2(str, buf1, buf2);
    if (ret != 0) {
        printf("getStr1Str2 error: %d\n", ret);
        return ret;
    }
    printf("buf1 = %s\nbuf2 = %s\n", buf1, buf2);

    printf("\n");
    system("pause");
    return 0;
}