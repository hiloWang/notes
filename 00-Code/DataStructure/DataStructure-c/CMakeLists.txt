cmake_minimum_required(VERSION 3.7)
project(DataStructure-C)

set(CMAKE_C_STANDARD 99)

#线性表
#[[set(SOURCE_FILES
        list/DynamicArray.c
        list/LinkedList.c
        list/CompaniesLinkedList.c
        list/SeqStack.c
        list/LinkedStack.c
        list/SeqQueue.c
        list/CircleLinkedList.c
        list/Main.c
        )]]

#二叉树
#[[set(SOURCE_FILES
        list/LinkedStack.c
        tree/BinaryTree.c
        )]]

#排序
set(SOURCE_FILES sort/Main.c)

add_executable(DataStructure-C ${SOURCE_FILES})