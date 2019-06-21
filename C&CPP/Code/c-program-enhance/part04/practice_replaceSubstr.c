#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
src:    原字符串
dst:    生成的或需要填充的字符串
sub:    需要查找的子字符串
new_sub:提换的新子字符串

return : 0 成功
-1 失败
*/
int replaceSubstr(/* in */char *src, /* out */char **dst, /* in */char *sub,  /* in */char *new_sub) {

    // src = "22abcd11111abcd2222abcdqqqqq"
    // dst = "22dcba11111dcba2222dcbaqqqqq"

    if (src == NULL || dst == NULL || sub == NULL || new_sub == NULL) {
        return -1;
    }

    char tmp[512] = {0}; //临时变量, 字符数组

    char *start = src; //记录查找的起点
    char *p = NULL;    //匹配字符串的首地址

    do {
        // src = "22abcd11111abcd2222abcdqqqqq"
        p = strstr(start, sub);
        if (p != NULL) {
            int len = p - start;
            if (len > 0) {
                //把匹配字符串前面的内容连接过去
                strncat(tmp, start, len);
            }

            strncat(tmp, new_sub, strlen(new_sub)); //追加替换的新串
            //更改查找的起点位置
            start = p + strlen(sub);

        } else {
            strcat(tmp, start);
            break;
        }

    } while (*start != 0);

    char *buf = malloc(strlen(tmp) + 1);
    strcpy(buf, tmp);
    *dst = buf;

    return 0;
}



int main() {
    char *p = "abcd11111abcd2222abcdqqqqq";
    int ret = 0;
    char *buf = NULL;

    ret = replaceSubstr(p, &buf, "abcd", "12345");
    if (ret != 0) {
        printf("replaceSubstr err:　%d\n", ret);
        return ret;
    }
    printf("buf = %s\n", buf);

    if (buf != NULL) {
        free(buf);
        buf = NULL;
    }

    printf("\n");
    system("pause");
    return 0;
}