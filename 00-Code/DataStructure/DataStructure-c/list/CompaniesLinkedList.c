/*
 ============================================================================
 
 Author      : Ztiany
 Description : 企业链表

 ============================================================================
 */


#include "CompaniesLinkedList.h"

//初始化链表
CLinkList *Init_CLinkList() {
    CLinkList *cLinkList = malloc(sizeof(cLinkList));
    cLinkList->size = 0;
    cLinkList->head.next = NULL;
    return cLinkList;
}

//插入
void Insert_CLinkList(CLinkList *list, int pos, CLinkNode *data) {
    if (list == NULL) {
        return;
    }

    if (data == NULL) {
        return;
    }

    if (pos < 0 || pos > list->size) {
        pos = list->size;
    }
    CLinkNode *current = &(list->head);
    //找到插入位置之前的那个位置
    for (int i = 0; i < pos; ++i) {
        current = current->next;
    }
    //连接
    data->next = current->next;
    current->next = data;
    list->size++;
}

//删除
void Remove_CLinkList(CLinkList *list, int pos) {
    if (list == NULL) {
        return;
    }
    if (pos >= list->size) {
        return;
    }
    CLinkNode *current = &list->head;
    //找到删除位置之前的那个结点
    for (int i = 0; i < pos; ++i) {
        current = current->next;
    }
    //删除结点
    current->next = current->next->next;
    list->size--;
}

//查找
int Find_CLinkList(CLinkList *list, CLinkNode *data, COMPARE_CNODE compare) {
    if (list == NULL) {
        return -1;
    }

    if (data == NULL) {
        return -1;
    }

    //赋值指针变量
    CLinkNode *pCurrent = list->head.next;
    int index = 0;
    int flag = -1;
    while (pCurrent != NULL) {
        if (compare(pCurrent, data) == 0) {
            flag = index;
            break;
        }
        pCurrent = pCurrent->next;
        index++;
    }

    return flag;
}

//返回链表大小
int Size_CLinkList(CLinkList *list) {
    return list->size;
}

//打印
void Print_CLinkList(CLinkList *list, PRINT_CNODE print) {
    if (list == NULL) {
        return;
    }

    //辅助指针
    CLinkNode *pCurrent = list->head.next;
    while (pCurrent != NULL) {
        print(pCurrent);
        pCurrent = pCurrent->next;
    }
}

//释放链表内存
void FreeSpace_CLinkList(CLinkList *list) {
    if (list == NULL) {
        return;
    }
    //此处只需要释放list即可
    free(list);
}
