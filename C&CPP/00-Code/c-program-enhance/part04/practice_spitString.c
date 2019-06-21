#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int spitString(const char *str, char c, char buf[10][30], int *count) {

    if (str == NULL || count == NULL) {
        return -1;
    }

    char *start = str;
    char *p = NULL;
    int i = 0; //第几行

    do {
        //"abcdef,acccd,eeee,aaaa,e3eeee,ssss,"
        p = strchr(start, c);
        if (p != NULL) {
            int len = p - start;
            if (len > 0) {
                strncpy(buf[i], start, len);
                //添加结束符
                buf[i][len] = '\0';
                i++;
            }
            start = p + 1;
        } else {
            break;
        }


    } while (*start != 0);

    *count = i; //有多少行字符串

    return 0;
}

int main01() {
    char *p = "abcdef,acccd,eeee,aaaa,e3eeee,ssss,";
    char buf[10][30] = {0};
    int n = 0;
    int ret = 0;

    ret = spitString(p, ',', buf, &n);
    if (ret != 0) {
        return ret;
    }
    return EXIT_SUCCESS;
}


char **getMem(int n) {
    char **p = malloc(n * sizeof(char *)); //char *p[]
    if (p == NULL) {
        return NULL;
    }

    //每个元素的空间
    int i = 0;
    for (i = 0; i < n; i++) {
        p[i] = malloc(30 * sizeof(char)); //char a[30]
        if (p[i] == NULL) {
            return NULL;
        }
    }

    return p;

}

void getMemFree(char ***buf, int n) {
    if (buf == NULL) {
        return;
    }

    char **tmp = *buf;
    int i = 0;

    for (i = 0; i < n; i++) {
        if (tmp[i] != NULL) {
            free(tmp[i]);
            tmp[i] = NULL;
        }
    }

    free(tmp);
    *buf = NULL;

}

int spitString2(const char *str, char c, char **buf, int *count) {
    if (str == NULL || count == NULL) {
        return -1;
    }

    char *start = str;
    char *p = NULL;
    int i = 0; //第几行

    do {
        //"abcdef,acccd,eeee,aaaa,e3eeee,ssss,"
        p = strchr(start, c);
        if (p != NULL) {
            int len = p - start;
            if (len > 0) {
                strncpy(buf[i], start, len);
                //添加结束符
                buf[i][len] = '\0';

                i++;
            }
            start = p + 1;
        } else {
            break;
        }


    } while (*start != 0);

    *count = i; //有多少行字符串

    return 0;
}

int main() {
    char *p = "abcdef,acccd,eeee,aaaa,e3eeee,ssss,";
    char **buf = getMem(10); //动态打造二维数组
    int n = 0;
    int ret = 0;

    ret = spitString2(p, ',', buf, &n);
    if (ret != 0) {
        return ret;
    }

    int i = 0;
    for (i = 0; i < n; i++) {
        printf("%s\n", buf[i]);
    }

    getMemFree(&buf, n);


    printf("\n");
    system("pause");
    return 0;
}