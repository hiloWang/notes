/*
 ============================================================================
 
 Author      : Ztiany
 Description : 二分搜索树

 ============================================================================
 */

#ifndef PLAY_WITH_ALGORITHMS_BST_H
#define PLAY_WITH_ALGORITHMS_BST_H

#include <cstdlib>
#include <iostream>
#include <queue>

template<typename Key, typename Value>
class BST {

private:

    struct Node {
        Key key;
        Value value;
        Node *left;
        Node *right;

        Node(Key key, Value value) {
            this->value = value;
            this->key = key;
            this->left = this->right = NULL;
        }

        explicit Node(Node *old) {
            this->key = old->key;
            this->value = old->value;
            this->left = old->left;
            this->right = old->right;
        }
    };


private:

    Node *root;
    int size;

public:
    BST() {
        root = NULL;
        size = 0;
    }

    ~BST() {
        destroy(root);
    }

    bool isEmpty() {
        return size == 0;
    }

    int getSize() {
        return size;
    }

    void insert(Key key, Value value) {
        root = insert(root, key, value);
    }

    Value *search(Key key) {
        return search(root, key);
    }

    // 查看二分搜索树中是否存在键key
    bool contain(Key key) {
        return contain(root, key);
    }

    void preOrder() {
        preOrder(root);
        std::cout << std::endl;
    }

    void inOrder() {
        inOrder(root);
        std::cout << std::endl;
    }

    void postOrder() {
        postOrder(root);
        std::cout << std::endl;
    }

    void levelOrder() {
        if (root == NULL) {
            return;
        }
        std::queue<Node *> queue;
        queue.push(root);
        while (!queue.empty()) {
            Node *node = queue.front();
            queue.pop();
            std::cout << node->key << " ";
            if (node->left != NULL) {
                queue.push(node->left);
            }
            if (node->right != NULL) {
                queue.push(node->right);
            }
        }

        std::cout << std::endl;
    }

    // 寻找二分搜索树的最小的键值
    Key minimum() {
        assert(size != 0);
        Node *minNode = minimum(root);
        return minNode->key;
    }

    // 寻找二分搜索树的最大的键值
    Key maximum() {
        assert(size != 0);
        Node *maxNode = maximum(root);
        return maxNode->key;
    }

    // 从二分搜索树中删除最小值所在节点
    void removeMin() {
        if (root)
            root = removeMin(root);
    }

    // 从二分搜索树中删除最大值所在节点
    void removeMax() {
        if (root)
            root = removeMax(root);
    }

    void remove(Key key) {
        root = remove(root, key);
    }

private:

    Node *insert(Node *node, Key key, Value value) {
        if (node == NULL) {
            size++;
            return new Node(key, value);
        }
        if (node->key == key) {
            node->value = value;
        } else if (node->key > key) {
            node->left = insert(node->left, key, value);
        } else {//node->key < key
            node->right = insert(node->right, key, value);
        }
        return node;
    }

    Value *search(Node *node, Key key) {
        if (node == NULL) {
            return NULL;
        }
        if (node->key == key) {
            return &(node->value);
        } else if (node->key < key) {
            return search(node->right, key);
        } else {//node->key > key
            return search(node->left, key);
        }
    }

    bool contain(Node *node, Key key) {
        if (node == NULL) {
            return false;
        }
        if (node->key == key) {
            return true;
        } else if (node->key < key) {
            return contain(node->right, key);
        } else {//node->key > key
            return contain(node->left, key);
        }
    }

    void preOrder(Node *node) {
        if (node != NULL) {
            std::cout << node->key << " ";
            preOrder(node->left);
            preOrder(node->right);
        }
    }

    void inOrder(Node *node) {
        if (node != NULL) {
            inOrder(node->left);
            std::cout << node->key << " ";
            inOrder(node->right);
        }
    }

    void postOrder(Node *node) {
        if (node != NULL) {
            postOrder(node->left);
            postOrder(node->right);
            std::cout << node->key << " ";
        }
    }

    Node *minimum(Node *node) {
        if (node->left == NULL) {
            return node;
        }
        return minimum(node->left);
    }

    Node *maximum(Node *node) {
        if (node->right == NULL) {
            return node;
        }
        return maximum(node->right);
    }

    Node *removeMin(Node *node) {
        if (node->left == NULL) {
            Node *ret = node->right;
            delete (node);
            size--;
            return ret;
        }
        node->left = removeMin(node->left);
        return node;
    }

    Node *removeMax(Node *node) {
        if (node->right == NULL) {
            Node *ret = node->left;
            delete (node);
            size--;
            return ret;
        }
        node->right = removeMax(node->right);
        return node;
    }

    Node *remove(Node *node, Key key) {
        if (node == NULL) {
            return NULL;
        }

        if (node->key > key) {
            node->left = remove(node->left, key);
            return node;
        }

        if (node->key < key) {
            node->right = remove(node->right, key);
            return node;
        }

        //node->key == key
        if (node->left == NULL) {
            Node *ret = node->right;
            size--;
            delete (node);
            return ret;
        }

        if (node->right == NULL) {
            Node *ret = node->left;
            size--;
            delete (node);
            return ret;
        }

        //获取以待删除节点为根节点的树的右子树中的最小节点，这里new是因为 removeMin 中要删除最小节点
        Node *successor = new Node(minimum(node->right));
        size++;
        successor->left = node->left;
        successor->right = removeMin(node->right);
        delete node;
        size--;
        return successor;
    }

    // 释放以node为根的二分搜索树的所有节点
    // 采用后续遍历的递归算法
    void destroy(Node *node) {
        if (node != NULL) {
            destroy(node->left);
            destroy(node->right);
            delete node;
            size--;
        }
    }

};

#endif //PLAY_WITH_ALGORITHMS_BST_H
