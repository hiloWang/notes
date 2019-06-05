/*
 ============================================================================
 
 Description : 单向链表

 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Node {
    int id;
    struct Node *next; //指针域
} Node;

//创建头结点
//链表的头结点地址由函数值返回。
Node *SListCreat() {
    Node *head = NULL;

    //头结点作为标志，不存储有效数据
    head = (Node *) malloc(sizeof(Node));
    if (head == NULL) {
        return NULL;
    }

    //给head的成员变量赋值
    head->id = -1;
    head->next = NULL;

    Node *pCur = head;
    Node *pNew = NULL;

    int data;

    while (1) {
        printf("Please enter the number: ");
        int result = scanf("%d", &data);
        printf("result = %d\n", result);

        if (data == -1 || result == 0 || result == -1) { //输入-1，退出
            break;
        }

        //新结点动态分配空间
        pNew = (Node *) malloc(sizeof(Node));
        if (pNew == NULL) {
            //continue;
            break;
        }

        //给pNew成员变量赋值
        pNew->id = data;
        pNew->next = NULL;

        //链表建立关系

        //当前结点的next指向pNew
        pCur->next = pNew;

        //pNew下一个结点指向NULL
        pNew->next = NULL;

        //把pCur移动到pNew,pCur指向pNew
        pCur = pNew;
    }

    return head;
}

//链表的遍历
int SListPrint(Node *head) {
    if (head == NULL) {
        return -1;
    }

    //取出第一个有效结点（头结点的next）
    Node *pCur = head->next;

    printf("head -> ");
    while (pCur != NULL) {
        printf("%d -> ", pCur->id);

        //当前结点往下移动一位，pCur指向下一个
        pCur = pCur->next;
    }
    printf("NULL\n");
    return 0;
}

//在值为x的结点前，插入值为y的结点；若值为x的结点不存在，则插在表尾。
int SListNodeInsert(Node *head, int x, int y) {
    if (head == NULL) {
        return -1;
    }
    Node *pPre = head;
    Node *pCur = head->next;

    while (pCur != NULL) {
        if (pCur->id == x) //找到了匹配结点
        {
            break;
        }

        //pPre指向pCur位置
        pPre = pCur;
        pCur = pCur->next; //pCur指向下一个结点
    }

    //2种情况
    //1. 找匹配的结点，pCur为匹配结点，pPre为pCur上一个结点
    //2. 没有找到匹配结点，pCur为空结点，pPre为最后一个结点

    //给新结点动态分配空间
    Node *pNew = (Node *) malloc(sizeof(Node));
    if (pNew == NULL) {
        return -2;
    }
    //给pNew的成员变量赋值
    pNew->id = y;
    pNew->next = NULL;

    //插入指定位置
    pPre->next = pNew; //pPre下一个指向pNew
    pNew->next = pCur; //pNew下一个指向pCur

    return 0;
}

//删除第一个值为x的结点
int SListNodeDel(Node *head, int x) {

    if (head == NULL) {
        return -1;
    }
    Node *pPre = head;
    Node *pCur = head->next;
    int flag = 0; //0没有找，1找到

    while (pCur != NULL) {
        if (pCur->id == x) {//找到了匹配结点
            //pPre的下一个指向pCur的下一个
            pPre->next = pCur->next;
            free(pCur);
            pCur = NULL;
            flag = 1;
            break;
        }

        //pPre指向pCur位置
        pPre = pCur;
        pCur = pCur->next; //pCur指向下一个结点

    }

    if (0 == flag) {
        printf("No value is %d node\n", x);
        return -2;
    }

    return 0;
}

//清空链表，释放所有结点
int SListNodeDestroy(Node *head) {
    if (head == NULL) {
        return -1;
    }

    Node *tmp = NULL;
    int i = 0;

    while (head != NULL) {
        //保存head的下一个结点
        tmp = head->next;
        free(head);
        head = NULL;

        //head指向tmp
        head = tmp;
        i++;
    }

    printf("i = %d \n", i);

    return 0;
}

int main(void) {
    Node *head = NULL;

    head = SListCreat();//创建头结点
    SListPrint(head);

    SListNodeInsert(head, 5, 4);
    printf("insert 4 before 5\n");
    SListPrint(head);

    SListNodeDel(head, 5);
    printf("delete 5\n");
    SListPrint(head);

    SListNodeDestroy(head);
    head = NULL;

    printf("\n");
    system("pause");
    return 0;
}