#include <stdio.h>

/*
过滤百慕大的数据
*/
int main()
{
    float latitude;
    float longitude;
    char info[80];
    int started = 0;

    while (scanf("%f,%f,%79[^\n]", &latitude, &longitude, info) == 3)
    {
        if ((latitude > 26) && (latitude < 34))
        {
            if ((longitude > -76) && (longitude < -64))
            {
                printf("%f,%f,%s\n", latitude, longitude, info);
            }
        }
    }

    return 0;
}