/*
 ============================================================================

 Author      : Ztiany
 Description : 链表

 ============================================================================
 */

#include "LinkedList.h"

//初始化链表
LinkList *Init_LinkList() {
    LinkList *linkList = malloc(sizeof(LinkList));
    linkList->size = 0;
    //头节点不保存数据
    linkList->head = malloc(sizeof(LinkNode));
    linkList->head->data = NULL;
    linkList->head->next = NULL;
    return linkList;
}

//指定位置插入
void Insert_LinkList(LinkList *list, int pos, void *data) {
    if (list == NULL) {
        return;
    }
    if (data == NULL) {
        return;
    }
    if (pos < 0 || pos > list->size) {
        pos = list->size;
    }
    LinkNode *current = list->head;
    //找到指定位置前的那个结点
    for (int i = 0; i < pos; ++i) {
        current = current->next;
    }
    //创建新的结点
    LinkNode *newNode = malloc(sizeof(LinkNode));
    newNode->data = data;
    //新节点指向下一个结点
    newNode->next = current->next;
    //之前的结点指向新的结点
    current->next = newNode;
    //size++
    list->size++;
}

//删除指定位置的值
void RemoveByPos_LinkList(LinkList *list, int pos) {
    if (list == NULL) {
        return;
    }
    if (pos < 0 || pos >= list->size) {
        return;
    }
    //找到需要删除位置之前的那个结点
    LinkNode *current = list->head;
    for (int i = 0; i < pos; ++i) {
        current = current->next;
    }
    LinkNode *target = current->next;
    current->next = target->next;
    free(target);
    list->size--;
}

//获得链表的长度
int Size_LinkList(LinkList *list) {
    return list->size;
}

//查找
int Find_LinkList(LinkList *list, void *data) {
    if (list == NULL) {
        return -1;
    }

    if (data == NULL) {
        return -1;
    }
    int pos = 0;
    //直接定位到第一个元素结点
    LinkNode *current = list->head->next;
    //开始遍历
    while (current != NULL) {
        if (current->data == data) {
            break;
        }
        pos++;
        current = current->next;
    }
    return pos;
}

//返回第一个结点
void *Front_LinkList(LinkList *list) {
    if (list == NULL) {
        return NULL;
    }
    return list->head->next->data;
}

//打印链表结点
void Print_LinkList(LinkList *list, PRINTLINKNODE print) {
    if (list == NULL) {
        return;
    }
    //辅助指针变量
    LinkNode *pCurrent = list->head->next;
    while (pCurrent != NULL) {
        print(pCurrent->data);
        pCurrent = pCurrent->next;
    }
}

//释放链表内存
void FreeSpace_LinkList(LinkList *list) {
    if (list == NULL) {
        return;
    }
    LinkNode *current = list->head;
    LinkNode *freeNode = NULL;
    while (current != NULL) {
        freeNode = current;
        current = current->next;
        free(freeNode);
    }
    list->size = 0;
    free(list);
}
