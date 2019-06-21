#ifndef DATASTRUCTURE_C_LINKEDSTACK_H
#define DATASTRUCTURE_C_LINKEDSTACK_H

#include <stdlib.h>
#include <stdio.h>

//链式栈的结点
typedef struct StackLinkNodeTag {
    struct StackLinkNodeTag *next;
} StackLinkNode;

//链式栈
typedef struct {
    StackLinkNode head;
    int size;
} LinkStack;

//初始化函数
LinkStack *Init_LinkStack();

//入栈
void Push_LinkStack(LinkStack *stack, StackLinkNode *data);

//出栈
void Pop_LinkStack(LinkStack *stack);

//返回栈顶元素
StackLinkNode *Top_LinkStack(LinkStack *stack);

//返回栈元素的个数
int Size_LinkStack(LinkStack *stack);

//清空栈
void Clear_LinkStack(LinkStack *stack);

//销毁栈
void FreeSpace_LinkStack(LinkStack *stack);


#endif
