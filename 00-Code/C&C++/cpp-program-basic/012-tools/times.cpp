/*
 ============================================================================
 
 Author      : Ztiany
 Description : 日期时间

 ============================================================================
 */
/*
有四个与时间相关的类型：clock_t、time_t、size_t 和 tm。类型 clock_t、size_t 和 time_t 能够把系统时间和日期表示为某种整数。

 结构类型 tm 把日期和时间以 C 结构的形式保存：
 struct tm {
      int tm_sec;   // 秒，正常范围从 0 到 59，但允许至 61
      int tm_min;   // 分，范围从 0 到 59
      int tm_hour;  // 小时，范围从 0 到 23
      int tm_mday;  // 一月中的第几天，范围从 1 到 31
      int tm_mon;   // 月，范围从 0 到 11
      int tm_year;  // 自 1900 年起的年数
      int tm_wday;  // 一周中的第几天，范围从 0 到 6，从星期日算起
      int tm_yday;  // 一年中的第几天，范围从 0 到 365，从 1 月 1 日算起
      int tm_isdst; // 夏令时
}

 */
#include <cstdlib>
#include <ctime>
#include <istream>

using namespace std;

int main() {

    time_t rawtime;
    //time函数返回系统的当前日历时间，自 1970 年 1 月 1 日以来经过的秒数。如果系统没有时间，则返回 .1。
    time(&rawtime);
    //localtime函数返回一个指向表示本地时间的 tm 结构的指针。
    struct tm *info = localtime(&rawtime);
    //asctime函数返回一个指向字符串的指针，字符串包含了 time 所指向结构中存储的信息
    printf("time: %s", asctime(info));

    return EXIT_SUCCESS;
}
