#ifndef DATASTRUCTURE_COMPANIESLINKEDLIST_H
#define DATASTRUCTURE_COMPANIESLINKEDLIST_H


#include<stdlib.h>
#include<stdio.h>

//链表结点
typedef struct CLinkNodeTag {
    struct CLinkNodeTag *next;
} CLinkNode;

//链表结构
typedef struct CLinkListTag {
    CLinkNode head;
    int size;
} CLinkList;

//遍历函数指针
typedef void(*PRINT_CNODE)(CLinkNode *);

//比较函数指针
typedef int(*COMPARE_CNODE)(CLinkNode *, CLinkNode *);

//初始化链表
CLinkList *Init_CLinkList();

//插入
void Insert_CLinkList(CLinkList *list, int pos, CLinkNode *data);

//删除
void Remove_CLinkList(CLinkList *list, int pos);

//查找
int Find_CLinkList(CLinkList *list, CLinkNode *data, COMPARE_CNODE compare);

//返回链表大小
int Size_CLinkList(CLinkList *list);

//打印
void Print_CLinkList(CLinkList *list, PRINT_CNODE print);

//释放链表内存
void FreeSpace_CLinkList(CLinkList *list);


#endif
