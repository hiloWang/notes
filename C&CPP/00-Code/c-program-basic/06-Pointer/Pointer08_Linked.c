/*
 ============================================================================
 
 Author      : Ztiany
 Description : 指针处理链表

 ============================================================================
 */
#include <malloc.h>
#include <stdio.h>
#include <stdlib.h>


struct Student {
    long num;
    float score;
    struct Student *next;
};

#define SIZE sizeof(struct Student)

static int n;

struct Student *create(void) {
    struct Student *head;
    struct Student *p1, *p2;
    n = 0;
    p1 = p2 = (struct Student *) malloc(SIZE);//申请一个结构体的空间，指向同一个空间
    scanf("%ld,%f", &p1->num, &p1->score);//用户输入两个数字，初始化字段

    head = NULL;//链表头
    //把p1当作始终是最新的那个元素、
    //p2始终是一个零时元素
    while (p1->num != 0) {
        n = n + 1;
        if (n == 1) {//第一次
            head = p1;
        } else {
            p2->next = p1;//连上尾部
        }
        p2 = p1;
        p1 = (struct Student *) malloc(SIZE);//又申请了一个结构的空间
        scanf("%ld,%f", &p1->num, &p1->score);
    }

    p2->next = NULL;
    return head;
}

int main() {
    struct Student *pt;
    pt = create();
    printf("%ld , %f", pt->num, pt->score);
    return EXIT_SUCCESS;
}
