#ifndef DATASTRUCTURE_C_SEQQUEUE_H
#define DATASTRUCTURE_C_SEQQUEUE_H


#include<stdlib.h>
#include<stdio.h>

#define MAX_SEQQUEUE_SIZE 1024


//顺序队列结构体
//左边队头，右边队尾
typedef struct {
    void *data[MAX_SEQQUEUE_SIZE];
    int size;
} SeqQueue;

//初始化
SeqQueue *Init_SeqQueue();

//入队
void Push_SeqQueue(SeqQueue *queue, void *data);

//返回队头元素
void *Front_SeqQueue(SeqQueue *queue);

//出队
void Pop_SeqQueue(SeqQueue *queue);

//返回队尾元素
void *Back_SeqQueue(SeqQueue *queue);

//返回大小
int Size_SeqQueue(SeqQueue *queue);

//清空队列
void Clear_SeqQueue(SeqQueue *queue);

//销毁
void FreeSpace_SeqQueue(SeqQueue *queue);


#endif
