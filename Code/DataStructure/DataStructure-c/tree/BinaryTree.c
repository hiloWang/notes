/*
 ============================================================================

 Author      : Ztiany
 Description : 二叉树

 ============================================================================
 */
#include <stdlib.h>
#include <stdio.h>
#include "../list/LinkedStack.h"

//二叉树结点
typedef struct BinaryNodeTag {
    char ch;
    struct BinaryNodeTag *lchild;
    struct BinaryNodeTag *rchild;
} BinaryNode;

void Recursion(BinaryNode *root) {
    //递归结束条件
    if (root == NULL) {
        return;
    }
    //先序遍历
    //先根
    printf("%c", root->ch);
    //再左
    Recursion(root->lchild);
    //再右
    Recursion(root->rchild);
}

//求叶子数目
void CalcLeafNum(BinaryNode *root, int *leafNum) {
    if (root == NULL) {
        return;
    }
    if (root->lchild == NULL && root->rchild == NULL) {
        (*leafNum)++;
    }
    //左子树叶子结点的数目
    CalcLeafNum(root->lchild, leafNum);
    //右子树叶子结点的数目
    CalcLeafNum(root->rchild, leafNum);
}

#define D_FALSE 0
#define D_TRUE 1

typedef struct TreeStackNodeTag {
    StackLinkNode stackLinkNode;//栈结点
    BinaryNode *binaryNode;//存储二叉树结点
    int flag;//标志位
} TreeStackNode;

TreeStackNode *CreateTreeStackNode(BinaryNode *binaryNode, int flag) {
    TreeStackNode *treeStackNode = malloc(sizeof(TreeStackNode));
    treeStackNode->binaryNode = binaryNode;
    treeStackNode->flag = flag;
    return treeStackNode;
}

void Traverse(BinaryNode *root) {
    if (root == NULL) {
        return;
    }

    LinkStack *stack = Init_LinkStack();
    TreeStackNode *treeStackNode = CreateTreeStackNode(root, D_FALSE);
    Push_LinkStack(stack, (StackLinkNode *) treeStackNode);

    while (Size_LinkStack(stack) > 0) {
        //先弹出栈顶元素
        TreeStackNode *topNode = (TreeStackNode *) Top_LinkStack(stack);
        Pop_LinkStack(stack);

        if (topNode->binaryNode == NULL) {
            continue;
        }

        if (topNode->flag == D_TRUE) {
            printf("%c", topNode->binaryNode->ch);
            free(topNode);
        } else {
            topNode->flag = D_TRUE;
            //根左右，则入栈顺序为：右左根
            Push_LinkStack(stack, (StackLinkNode *) CreateTreeStackNode(topNode->binaryNode->rchild, D_FALSE));
            Push_LinkStack(stack, (StackLinkNode *) CreateTreeStackNode(topNode->binaryNode->lchild, D_FALSE));
            Push_LinkStack(stack, (StackLinkNode *) topNode);
        }
    }
    FreeSpace_LinkStack(stack);
}

//求二叉树的高度
int CalcTreeDepth(BinaryNode *root) {
    if (root == NULL) {
        return 0;
    }
    int depth = 0;
    //求左子树的高度
    int leftDepth = CalcTreeDepth(root->lchild);
    int rightDepth = CalcTreeDepth(root->rchild);
    depth = leftDepth > rightDepth ? leftDepth + 1 : rightDepth + 1;
    return depth;
}


//拷贝二叉树：递归拷贝
BinaryNode *CopyBinaryTree(BinaryNode *root) {
    if (root == NULL) {
        return NULL;
    }
    //拷贝左子树
    BinaryNode *lchild = CopyBinaryTree(root->lchild);
    //拷贝右子树
    BinaryNode *rchild = CopyBinaryTree(root->rchild);
    //创建结点
    BinaryNode *newnode = (BinaryNode *) malloc(sizeof(BinaryNode));
    newnode->ch = root->ch;
    newnode->lchild = lchild;
    newnode->rchild = rchild;
    return newnode;
}

//释放二叉树内存
void FreeSpaceBinaryTree(BinaryNode *root) {
    if (root == NULL) {
        return;
    }
    //释放左子树
    FreeSpaceBinaryTree(root->lchild);
    //释放右子树
    FreeSpaceBinaryTree(root->rchild);
    //释放当前结点
    free(root);
}

BinaryNode *CreateBinaryTree() {
    fflush(stdin);
    char ch;
    int result = scanf("%c", &ch);
    BinaryNode *node;
    printf("-%c-%s", ch, (result == EOF ? "ok" : "eof"));
    if (ch == '#') {
        node = NULL;
    } else {
        node = (BinaryNode *) malloc(sizeof(BinaryNode));
        node->ch = ch;
        node->lchild = CreateBinaryTree();
        node->rchild = CreateBinaryTree();
    }
    return node;
}

int main() {
    //创建结点
    BinaryNode node1 = {'A', NULL, NULL};
    BinaryNode node2 = {'B', NULL, NULL};
    BinaryNode node3 = {'C', NULL, NULL};
    BinaryNode node4 = {'D', NULL, NULL};
    BinaryNode node5 = {'E', NULL, NULL};
    BinaryNode node6 = {'F', NULL, NULL};
    BinaryNode node7 = {'G', NULL, NULL};
    BinaryNode node8 = {'H', NULL, NULL};

    //建立结点关系
    node1.lchild = &node2;
    node1.rchild = &node6;
    node2.rchild = &node3;
    node3.lchild = &node4;
    node3.rchild = &node5;
    node6.rchild = &node7;
    node7.lchild = &node8;

    //递归遍历
    printf("Recursion \n");
    Recursion(&node1);

    //递归遍历
    // printf("Calc LeafNum \n");
    //int count = 0;
    //CalcLeafNum(&node1, &count);
    //printf("count = %d \n", count);

    //非递归遍历
    //Traverse(&node1);

    //求高度
    //printf("Calc TreeDepth \n");
    //int depth = CalcTreeDepth(&node1);
    //printf("depth = %d \n", depth);

    //#号法创建二叉树
    //BinaryNode *root = CreateBinaryTree();
    //Recursion(root);

    printf("\n");
    system("pause");
    return EXIT_SUCCESS;
}
