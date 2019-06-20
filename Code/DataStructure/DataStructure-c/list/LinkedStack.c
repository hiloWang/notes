/*
 ============================================================================
 
 Author      : Ztiany
 Description : 链式栈

 ============================================================================
 */

#include "LinkedStack.h"


//初始化函数
LinkStack *Init_LinkStack() {
    LinkStack *linkStack = malloc(sizeof(LinkStack));
    linkStack->size = 0;
    return linkStack;
}

//入栈
void Push_LinkStack(LinkStack *stack, StackLinkNode *data) {
    if (stack == NULL || data == NULL) {
        return;
    }
    data->next = stack->head.next;
    stack->head.next = data;
    stack->size++;
}

//出栈
void Pop_LinkStack(LinkStack *stack) {
    if (stack == NULL || stack->size == 0) {
        return;
    }
    StackLinkNode *stackLinkNode = stack->head.next;
    stack->head.next = stackLinkNode->next;
    stack->size--;
}

//返回栈顶元素
StackLinkNode *Top_LinkStack(LinkStack *stack) {
    if (stack == NULL) {
        return NULL;
    }
    return stack->head.next;
}

//返回栈元素的个数
int Size_LinkStack(LinkStack *stack) {
    if (stack == NULL) {
        return -1;
    }
    return stack->size;
}

//清空栈
void Clear_LinkStack(LinkStack *stack) {
    if (stack == NULL) {
        return;
    }
    stack->size = 0;
    stack->head.next = NULL;
}

//销毁栈
void FreeSpace_LinkStack(LinkStack *stack) {
    if (stack == NULL) {
        return;
    }
    free(stack);
}
