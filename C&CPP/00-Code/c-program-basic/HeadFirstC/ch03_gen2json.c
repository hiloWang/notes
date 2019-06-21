#include <stdio.h>

/*
将 CVS 格式的经纬度转化为 json 格式。

使用示例：
    ./gen2json.out < ch03_location.cvs > output.json
 
 配合 bermuda 过滤数据
    (./bermuda.out | ./gen2json.out) < ch03_location.cvs > output.josn
     ./bermuda.out < ch03_location.cvs | gen2json.out  > output.josn
 */
int main()
{
    float latitude;
    float longitude;
    char info[80];
    int started = 0;
    puts("data=[");

    while (scanf("%f,%f,%79[^\n]", &latitude, &longitude, info) == 3)
    {
        if (started)
            printf(",\n");
        else
            started = 1;

        if ((latitude < -90.0) || (latitude > 90.0))
        {
            fprintf(stderr, "Invalid latitude: %f\n", latitude);
            return 2;
        }
        if ((longitude < -180.0) || (longitude > 180.0))
        {
            fprintf(stderr, "Invalid longitude: %f\n", longitude);
            return 2;
        }

        printf("{latitude: %f, longitude: %f, info: '%s'}", latitude, longitude, info);
    }

    puts("\n]");
    return 0;
}