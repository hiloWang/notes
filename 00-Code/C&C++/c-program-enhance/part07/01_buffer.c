/*
 ============================================================================

 Description : 缓冲区验证

 ============================================================================
 */
#include <stdio.h>
#include <stdlib.h>

int main(void) {
    char buf[] = "this is a test\n";

    FILE *fp = fopen("./test.txt", "w+");
    fputs(buf, fp);
    system("pause");

    fflush(fp);
    fclose(fp);
    system("pause");
    return 0;
}