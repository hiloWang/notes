#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
4. 键值对（“key = value”）字符串，在开发中经常使用
	要求1：请自己定义一个接口，实现根据key获取.
	要求2：编写测试用例。
	要求3：键值对中间可能有n多空格，请去除空格
*/

/*
功能：获取非空字符串
参数：
	inbuf： 原始字符串buf首地址
	outbuf：非空字符串buf首地址
返回值：
	成功：0
	失败：非0
*/
int trimSpace(char *inbuf, char *outbuf) {
    if (inbuf == NULL || outbuf == NULL) {
        return -1;
    }

    int begin = 0; //字符串第一个元素位置
    int end = strlen(inbuf) - 1;//字符串最后一个元素位置
    int n = 0;

    if (end < 0) //出错处理
    {
        return -2;
    }

    //从左往右，如果当前字符为空，而且没有结束
    while (inbuf[begin] == ' ' && inbuf[begin] != 0) {
        begin++; //往右移动
    }

    //从右往左，如果当前字符为空
    while (inbuf[end] == ' ') {
        end--; //往左移动
    }

    n = end - begin + 1; //非空元素个数

    strncpy(outbuf, inbuf + begin, n);
    outbuf[n] = 0; //因为strncpy()不会自动添加'\0'，所以需要人为添加

    return 0;
}

/*
功能：
	键值对（“key = value”）字符串更加键key查找对应的值value
参数：
	keyval：键值对（“key = value”）字符串
	pkey：键
	buf：匹配键所对应的内容
	len：匹配键所对应内容的长度
返回值：
	成功：0
	失败：非0
*/
int getKeyByValue(char *keyval, char *pkey, char *buf, int *len) {
    if (keyval == NULL || pkey == NULL || buf == NULL || len == NULL) {
        return -1;
    }

    //辅助变量把形参接过来
    char *p = keyval; //"key1 =    valude1"

    //查看key是否在母串
    //查找 key1 是否在 "key1 =    valude1"
    p = strstr(p, pkey);
    if (NULL == p) //没有匹配键
    {
        return -2;
    }

    //重新设置起点位置，字符串跳过key值
    //"key1 =    valude1" -> " =    valude1"
    p = p + strlen(pkey);

    //查找是否包含=号
    p = strstr(p, "=");
    if (NULL == p)//没有
    {
        return -3;
    }

    //字符串跳过"="
    //" =    valude1" -> "    valude1"
    p = p + strlen("=");

    //获取非空字符串
    int ret = trimSpace(p, buf);
    if (ret != 0) {
        return ret;
    }

    //通过*操作符操作内存
    //间接赋值
    *len = strlen(buf);

    return 0;
}

int main(void) {
    char keyval[] = "key1 =    valude1"; //键值对字符串
    char *pkey = "key1"; //键值
    char buf[128] = {0};
    int len = 0;

    int ret = getKeyByValue(keyval, pkey, buf, &len);
    if (ret != 0) {
        printf("getKeyByValue: %d\n", ret);
        return ret;
    }
    printf("value: %s\nlen = %d\n", buf, len);

    printf("\n");
    system("pause");
    return 0;
}