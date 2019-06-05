/*
 ============================================================================
 
 Author      : Ztiany
 Description : 环行链表

 ============================================================================
 */


#include "CircleLinkedList.h"


//初始化函数
CircleLinkList *Init_CircleLinkList() {
    CircleLinkList *circleLinkList = malloc(sizeof(CircleLinkList));
    circleLinkList->size = 0;
    circleLinkList->head.next = &(circleLinkList->head);
    return circleLinkList;
}

//插入函数
void Insert_CircleLinkList(CircleLinkList *clist, int pos, CircleLinkNode *data) {
    if (clist == NULL) {
        return;
    }

    if (data == NULL) {
        return;
    }

    if (pos < 0 || pos > clist->size) {
        pos = clist->size;
    }
    //插入点之前的那个位置
    CircleLinkNode *pCurrent = &(clist->head);
    for (int i = 0; i < pos; i++) {
        pCurrent = pCurrent->next;
    }
    CircleLinkNode *insert = pCurrent->next;
    pCurrent->next = data;
    data->next = insert;
    clist->size++;
}

//获得第一个元素
CircleLinkNode *Front_CircleLinkList(CircleLinkList *clist) {
    if (clist == NULL || clist->size == 0) {
        return NULL;
    }
    return clist->head.next;
}

//根据位置删除
void RemoveByPos_CircleLinkList(CircleLinkList *clist, int pos) {
    if (clist == NULL) {
        return;
    }
    //删除点之前的那个位置
    CircleLinkNode *pCurrent = &(clist->head);
    for (int i = 0; i < pos; i++) {
        pCurrent = pCurrent->next;
    }
    pCurrent->next = pCurrent->next->next;
    clist->size--;
}

//根据值去删除
void RemoveByValue_CircleLinkList(CircleLinkList *clist, CircleLinkNode *data, COMPARE_CIRCLENODE compare) {
    if (clist == NULL) {
        return;
    }

    if (data == NULL) {
        return;
    }

    //这个是循环链表
    CircleLinkNode *pPrev = &(clist->head);
    CircleLinkNode *pCurrent = pPrev->next;
    int i = 0;
    for (i = 0; i < clist->size; i++) {
        if (compare(pCurrent, data) == CIRCLELINKLIST_TRUE) {
            pPrev->next = pCurrent->next;
            clist->size--;
            break;
        }
        pPrev = pCurrent;
        pCurrent = pPrev->next;
    }
}

//获得链表的长度
int Size_CircleLinkList(CircleLinkList *clist) {
    if (clist == NULL) {
        return -1;
    }
    return clist->size;
}

//判断是否为空
int IsEmpty_CircleLinkList(CircleLinkList *clist) {
    if (clist->size == 0) {
        return CIRCLELINKLIST_TRUE;
    }
    return CIRCLELINKLIST_FALSE;
}

//查找
int Find_CircleLinkList(CircleLinkList *clist, CircleLinkNode *data, COMPARE_CIRCLENODE compare) {
    if (clist == NULL || data == NULL || compare == NULL) {
        return -1;
    }
    CircleLinkNode *pCurrent = &clist->head;
    for (int i = 0; i < clist->size; ++i) {
        pCurrent = pCurrent->next;
        if (compare(pCurrent, data) == CIRCLELINKLIST_TRUE) {
            return i;
        }
    }
    return -1;
}

//打印节点
void Print_CircleLinkList(CircleLinkList *clist, PRINT_CIRCLENODE print) {
    if (clist == NULL) {
        return;
    }
    CircleLinkNode *pCurrent = clist->head.next;
    for (int i = 0; i < clist->size; i++) {
        if (pCurrent == &(clist->head)) {
            pCurrent = pCurrent->next;
            printf("------------------\n");
        }
        print(pCurrent);
        pCurrent = pCurrent->next;
    }

}

//释放内存
void FreeSpace_CircleLinkList(CircleLinkList *clist) {
    if (clist == NULL) {
        return;
    }
    free(clist);
}
