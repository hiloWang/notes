/*
 ============================================================================

 Description : 字符串反转

 ============================================================================
 */

char *inverse(char *str) {
    int length = strlen(str);
    char *p1 = NULL;
    char *p2 = NULL;
    char tmp_ch;

    if (str == NULL) {
        return NULL;
    }

    p1 = str;
    p2 = str + length - 1;

    while (p1 < p2) {
        tmp_ch = *p1;
        *p1 = *p2;
        *p2 = tmp_ch;
        ++p1;
        --p2;
    }

    return str;
}
