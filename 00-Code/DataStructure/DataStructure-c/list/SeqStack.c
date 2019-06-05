/*
 ============================================================================
 
 Author      : Ztiany
 Description : 顺序存储栈

 ============================================================================
 */

#include "SeqStack.h"


//初始化栈
SeqStack *Init_SeqStack() {
    SeqStack *seqStack = malloc(sizeof(SeqStack));
    for (int i = 0; i < MAX_SEQ_SIZE; ++i) {
        seqStack->data[i] = NULL;
    }
    seqStack->size = 0;
    return seqStack;
}

//入栈
void Push_SeqStack(SeqStack *stack, void *data) {
    if (stack == NULL) {
        return;
    }
    if (stack->size > MAX_SEQ_SIZE) {
        return;
    }
    stack->data[stack->size] = data;
    stack->size++;
}

//返回栈顶元素
void *Top_SeqStack(SeqStack *stack) {
    if (stack == NULL) {
        return NULL;
    }
    return stack->data[stack->size - 1];
}

//出栈
void Pop_SeqStack(SeqStack *stack) {
    if (stack == NULL) {
        return;
    }
    stack->data[stack->size - 1] = NULL;
    stack->size--;
}

//判断是否为空
int IsEmpty(SeqStack *stack) {
    if (stack == NULL) {
        return -1;
    }
    if (stack->size == 0) {
        return SEQSTACK_TRUE;
    }
    return SEQSTACK_FALSE;
}

//返回栈中元素的个数
int Size_SeqStack(SeqStack *stack) {
    if (stack == NULL) {
        return -1;
    }
    return stack->size;
}

//清空栈
void Clear_SeqStack(SeqStack *stack) {
    if (stack == NULL) {
        return;
    }
    for (int i = 0; i < stack->size; ++i) {
        stack->data[i] = NULL;
    }
    stack->size = 0;
}

//销毁
void FreeSpace_SeqStack(SeqStack *stack) {
    if (stack == NULL) {
        return;
    }
    free(stack);
}

