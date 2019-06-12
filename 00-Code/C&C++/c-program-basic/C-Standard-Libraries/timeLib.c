/*
 ============================================================================

 Author      : Ztiany
 Description : time.h系列函数

 ============================================================================
 */


//time.h是C标准函数库中获取时间与日期、对时间与日期数据操作及格式化的头文件。

# include <stdio.h>
# include <time.h>
# include <stdlib.h>


int main(void) {
    time_t timer = time(NULL);
    //打印当前时间到标准输出流
    printf("ctime is %s\n", ctime(&timer));
    return EXIT_SUCCESS;
}