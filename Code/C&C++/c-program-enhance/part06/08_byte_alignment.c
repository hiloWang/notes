#include <stdio.h>
#include <stdlib.h>

int main() {

    struct A {
        int a;
        double b;
        float c;
    };

    struct {
        char e[2];
        int f;
        double g;
        short h;
        struct A i;
    } B = {
            {'a', 'b'},
            10,
            11.11,
            32,
            {
                    12,
                    12.12,
                    12.1212
            }
    };

    void *p = &B;

    printf("B.e[0]: %c\n", *((char *) p));
    printf("B.e[1]: %c\n", *((char *) p) + 1);

    p = (char *) p + 4;
    printf("B.f: %d\n", *((int *) p));

    p = (char *) p + 4;
    printf("B.g: %f\n", *((double *) p));

    p = (char *) p + 8;
    printf("B.h: %d\n", *((short *) p));

    p = (char *) p + 8;
    printf("B.i.a: %d\n", *((int *) p));

    p = (char *) p + 8;
    printf("B.i.b: %f\n", *((double *) p));

    p = (char *) p + 8;
    printf("B.i.c: %f\n", *((float *) p));

    return EXIT_SUCCESS;
}