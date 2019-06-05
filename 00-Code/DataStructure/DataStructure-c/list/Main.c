/*
 ============================================================================
 
 Author      : Ztiany
 Description : 

 ============================================================================
 */
#include "DynamicArray.h"
#include "LinkedList.h"
#include "CompaniesLinkedList.h"
#include "SeqStack.h"
#include "LinkedStack.h"
#include "SeqQueue.h"
#include "CircleLinkedList.h"

/**
 * 动态数组
 */
void testDynamicArray() {
    //初始化动态数组
    Dynamic_Array *myArray = Init_Array();
    //打印容量
    printf("array capacity: %d\n", Capacity_Array(myArray));
    printf("array size: %d\n", Size_Array(myArray));

    //插入元素
    for (int i = 0; i < 30; i++) {
        PushBack_Array(myArray, i);
    }
    printf("array capacity: %d\n", Capacity_Array(myArray));
    printf("array size: %d\n", Size_Array(myArray));

    //打印
    Print_Array(myArray);

    //删除
    RemoveByPos_Array(myArray, 0);
    RemoveByValue_Array(myArray, 27);

    //打印
    Print_Array(myArray);

    //查找5个位置
    int pos = Find_Array(myArray, 5);
    printf("5 fined :pos:%d %d\n", pos, At_Array(myArray, pos));

    //销毁
    FreeSpace_Array(myArray);
}

//自定义数据类型
typedef struct PERSON {
    char name[64];
    int age;
    int score;
} Person;

//打印函数
void PrintPerson(void *data) {
    Person *p = (Person *) data;
    printf("Name:%s Age:%d Score:%d\n", p->name, p->age, p->score);
}

/**
 * 链表
 */
void testLinked() {

    //创建链表
    LinkList *list = Init_LinkList();

    //创建数据
    Person p1 = {"aaa", 18, 100};
    Person p2 = {"bbb", 19, 99};
    Person p3 = {"ccc", 20, 101};
    Person p4 = {"ddd", 17, 97};
    Person p5 = {"eee", 16, 59};

    //数据插入链表
    Insert_LinkList(list, 0, &p1);
    Insert_LinkList(list, 0, &p2);
    Insert_LinkList(list, 0, &p3);
    Insert_LinkList(list, 0, &p4);
    Insert_LinkList(list, 0, &p5);

    //打印
    Print_LinkList(list, PrintPerson);

    //删除3
    RemoveByPos_LinkList(list, 3);

    //打印
    printf("---------------\n");
    Print_LinkList(list, PrintPerson);

    //返回第一个结点
    printf("-----find result------------\n");
    Person *ret = (Person *) Front_LinkList(list);
    printf("Name:%s Age:%d Score:%d\n", ret->name, ret->age, ret->score);

    //销毁链表
    FreeSpace_LinkList(list);
}


typedef struct CPERSON {
    CLinkNode node;
    char name[64];
    int age;
} CPerson;

void PrintCPerson(CLinkNode *data) {
    CPerson *p = (CPerson *) data;
    printf("Name:%s Age:%d\n", p->name, p->age);
}

int CompareCPerson(CLinkNode *node1, CLinkNode *node2) {
    CPerson *p1 = (CPerson *) node1;
    CPerson *p2 = (CPerson *) node2;
    if (strcmp(p1->name, p2->name) == 0 && p1->age == p2->age) {
        return 0;
    }
    return -1;
}

/**
 * 企业链表
 */
void testCompaniesLinkedList() {

    //创建链表
    CLinkList *list = Init_CLinkList();

    //创建数据
    CPerson p1, p2, p3, p4, p5;
    strcpy(p1.name, "aaa");
    strcpy(p2.name, "bbb");
    strcpy(p3.name, "ccc");
    strcpy(p4.name, "ddd");
    strcpy(p5.name, "eee");

    p1.age = 10;
    p2.age = 20;
    p3.age = 30;
    p4.age = 40;
    p5.age = 50;

    //将结点插入链表
    Insert_CLinkList(list, 0, (CLinkNode *) &p1);
    Insert_CLinkList(list, 0, (CLinkNode *) &p2);
    Insert_CLinkList(list, 0, (CLinkNode *) &p3);
    Insert_CLinkList(list, 0, (CLinkNode *) &p4);
    Insert_CLinkList(list, 0, (CLinkNode *) &p5);

    //打印
    Print_CLinkList(list, PrintCPerson);

    //删除结点
    Remove_CLinkList(list, 2);

    //打印
    printf("---------------\n");
    Print_CLinkList(list, PrintCPerson);

    //查找
    CPerson findP;
    strcpy(findP.name, "bbb");
    findP.age = 20;
    int pos = Find_CLinkList(list, (CLinkNode *) &findP, CompareCPerson);
    printf("---------------\n");
    printf("position:%d\n", pos);

    //释放链表内存
    FreeSpace_CLinkList(list);
}


void testQeqStack() {
    //创建栈
    SeqStack *stack = Init_SeqStack();

    //创建数据
    Person p1 = {"aaa", 10};
    Person p2 = {"bbb", 20};
    Person p3 = {"ccc", 30};
    Person p4 = {"ddd", 40};
    Person p5 = {"eee", 50};

    //入栈
    Push_SeqStack(stack, &p1);
    Push_SeqStack(stack, &p2);
    Push_SeqStack(stack, &p3);
    Push_SeqStack(stack, &p4);
    Push_SeqStack(stack, &p5);

    //输出
    while (Size_SeqStack(stack) > 0) {
        //访问栈顶元素
        Person *person = (Person *) Top_SeqStack(stack);
        printf("Name:%s Age:%d\n", person->name, person->age);
        //弹出栈顶元素
        Pop_SeqStack(stack);
    }

    //释放内存
    FreeSpace_SeqStack(stack);
}


typedef struct {
    StackLinkNode node;
    char name[64];
    int age;
} LinkedStackPerson;

void testLinkedStack() {

    //创建栈
    LinkStack *stack = Init_LinkStack();

    //创建数据
    LinkedStackPerson p1, p2, p3, p4, p5;
    strcpy(p1.name, "aaa");
    strcpy(p2.name, "bbb");
    strcpy(p3.name, "ccc");
    strcpy(p4.name, "ddd");
    strcpy(p5.name, "eee");

    p1.age = 10;
    p2.age = 20;
    p3.age = 30;
    p4.age = 40;
    p5.age = 50;

    //入栈
    Push_LinkStack(stack, (StackLinkNode *) &p1);
    Push_LinkStack(stack, (StackLinkNode *) &p2);
    Push_LinkStack(stack, (StackLinkNode *) &p3);
    Push_LinkStack(stack, (StackLinkNode *) &p4);
    Push_LinkStack(stack, (StackLinkNode *) &p5);

    //输出
    while (Size_LinkStack(stack) > 0) {

        //取出栈顶元素
        LinkedStackPerson *p = (LinkedStackPerson *) Top_LinkStack(stack);
        printf("Name:%s Age:%d\n", p->name, p->age);
        //弹出栈顶元素
        Pop_LinkStack(stack);
    }

    //销毁栈
    FreeSpace_LinkStack(stack);
}

void testQueue() {

    //创建队列
    SeqQueue *queue = Init_SeqQueue();

    //创建数据
    Person p1 = {"aaa", 10};
    Person p2 = {"bbb", 20};
    Person p3 = {"ccc", 30};
    Person p4 = {"ddd", 40};
    Person p5 = {"eee", 50};

    //数据入队列
    Push_SeqQueue(queue, &p1);
    Push_SeqQueue(queue, &p2);
    Push_SeqQueue(queue, &p3);
    Push_SeqQueue(queue, &p4);
    Push_SeqQueue(queue, &p5);

    //输出队尾元素
    Person *backPerson = (Person *) Back_SeqQueue(queue);
    printf("tail of the queue Name:%s Age:%d\n", backPerson->name, backPerson->age);
    printf("-----------------\n");

    //输出
    while (Size_SeqQueue(queue) > 0) {
        //取出队头元素
        Person *p = (Person *) Front_SeqQueue(queue);
        printf("Name:%s Age:%d\n", p->name, p->age);
        //从队头弹出元素
        Pop_SeqQueue(queue);
    }
    //销毁队列
    FreeSpace_SeqQueue(queue);
}


typedef struct {
    CircleLinkNode node;
    char name[64];
    int age;
    int score;
} CircleLinkPerson;

void PrintCircleLinkNode(CircleLinkNode *data) {
    CircleLinkPerson *p = (CircleLinkPerson *) data;
    printf("Name:%s Age:%d Score:%d\n", p->name, p->age, p->score);
}

int CompareCircleLinkNode(CircleLinkNode *data1, CircleLinkNode *data2) {
    CircleLinkPerson *p1 = (CircleLinkPerson *) data1;
    CircleLinkPerson *p2 = (CircleLinkPerson *) data2;
    if (strcmp(p1->name, p2->name) == 0 && p1->age == p2->age && p1->score == p2->score) {
        return CIRCLELINKLIST_TRUE;
    }
    return CIRCLELINKLIST_FALSE;
}

void testCircleLinked() {

    //创建循环链表
    CircleLinkList *clist = Init_CircleLinkList();

    //创建数据
    CircleLinkPerson p1, p2, p3, p4, p5;
    strcpy(p1.name, "aaa");
    strcpy(p2.name, "bbb");
    strcpy(p3.name, "ccc");
    strcpy(p4.name, "ddd");
    strcpy(p5.name, "eee");

    p1.age = 10;
    p2.age = 20;
    p3.age = 30;
    p4.age = 40;
    p5.age = 50;

    p1.score = 50;
    p2.score = 50;
    p3.score = 60;
    p4.score = 65;
    p5.score = 70;


    //数据入链表
    Insert_CircleLinkList(clist, 100, (CircleLinkNode *) &p1);
    Insert_CircleLinkList(clist, 100, (CircleLinkNode *) &p2);
    Insert_CircleLinkList(clist, 100, (CircleLinkNode *) &p3);
    Insert_CircleLinkList(clist, 100, (CircleLinkNode *) &p4);
    Insert_CircleLinkList(clist, 100, (CircleLinkNode *) &p5);

    //打印
    Print_CircleLinkList(clist, PrintCircleLinkNode);

    CircleLinkPerson pDel;
    strcpy(pDel.name, "ddd");
    pDel.age = 40;
    pDel.score = 65;

    //根据值删除
    RemoveByValue_CircleLinkList(clist, (CircleLinkNode *) &pDel, CompareCircleLinkNode);

    //打印
    printf("--------------\n");
    Print_CircleLinkList(clist, PrintCircleLinkNode);

    //释放内存
    FreeSpace_CircleLinkList(clist);
}

//约瑟夫斯问题（有时也称为约瑟夫斯置换），是一个出现在计算机科学和数学中的问题。在计算机编程的算法中，类似问题又称为约瑟夫环。
#define M 8
#define N 3

typedef struct {
    CircleLinkNode node;
    int val;
} Num;

void PrintNum(CircleLinkNode *data) {
    Num *num = (Num *) data;
    printf("%d ", num->val);
}

int CompareNum(CircleLinkNode *data1, CircleLinkNode *data2) {
    Num *num1 = (Num *) data1;
    Num *num2 = (Num *) data2;
    if (num1->val == num2->val) {
        return CIRCLELINKLIST_TRUE;
    }
    return CIRCLELINKLIST_FALSE;
}

void JosephusProblem() {

    //创建循环链表
    CircleLinkList *clist = Init_CircleLinkList();

    //链表插入数据
    Num num[M];
    for (int i = 0; i < 8; i++) {
        num[i].val = i + 1;
        Insert_CircleLinkList(clist, i, (CircleLinkNode *) &num[i]);
    }

    //打印
    Print_CircleLinkList(clist, PrintNum);
    printf("\n");


    int index = 1;
    //辅助指针
    CircleLinkNode *pCurrent = clist->head.next;
    while (Size_CircleLinkList(clist) > 1) {

        if (index == N) {

            Num *temNum = (Num *) pCurrent;
            printf("%d ", temNum->val);

            //缓存待删除结点的下一个结点
            CircleLinkNode *pNext = pCurrent->next;
            //根据值删除
            RemoveByValue_CircleLinkList(clist, pCurrent, CompareNum);
            pCurrent = pNext;
            if (pCurrent == &(clist->head)) {
                pCurrent = pCurrent->next;
            }
            index = 1;
        }

        pCurrent = pCurrent->next;
        if (pCurrent == &(clist->head)) {
            pCurrent = pCurrent->next;
        }
        index++;
    }

    if (Size_CircleLinkList(clist) == 1) {
        Num *tempNum = (Num *) Front_CircleLinkList(clist);
        printf("\nlast = %d ", tempNum->val);
    } else {
        printf("error!");
    }

    printf("\n");

    //释放链表内存
    FreeSpace_CircleLinkList(clist);
}

int main() {
//    testDynamicArray();//动态数组
//    testLinked();//链表
//    testCompaniesLinkedList();//企业链表
//    testQeqStack();//顺序栈
//    testLinkedStack();//链式栈
//    testQueue();//队列
//    testCircleLinked();//环行链表
    JosephusProblem();//约瑟夫问题
    return EXIT_SUCCESS;

}
