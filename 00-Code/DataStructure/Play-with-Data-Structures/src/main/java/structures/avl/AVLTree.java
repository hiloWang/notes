package structures.avl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/18 10:03
 */
public class AVLTree<Key extends Comparable<Key>, Value> {

    private class Node {

        Key key;
        Value value;
        Node left;
        Node right;
        int height;

        Node(Key key, Value value) {
            this.key = key;
            this.value = value;
            this.height = 1;//默认高度是 1
        }
    }

    private Node root;
    private int size;

    public void add(Key key, Value value) {
        root = addImproved(root, key, value);
    }

    /**
     * 向以node为根的二分搜索树中插入元素e，递归算法，返回插入新节点后二分搜索树的根。
     * 对于二分搜索树，递归的每一次深度都把源树的层级减一，直到最后的null位就是需要添加新节点的位置。这个递归算法的精髓在于定义了返回值。
     */
    private Node addImproved(Node node, Key key, Value value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        if (key.compareTo(node.key) < 0) {
            node.left = addImproved(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = addImproved(node.right, key, value);
        } else {// e.compareTo(node.e) == 0
            node.value = value;
        }

        //维护高度
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        //平衡因子
        int balanceFactor = getBalanceFactor(node);

        //维护平衡

        //LL 向左侧子节点的左侧插入了新节点，破坏了平衡性，进行右旋转
        if (balanceFactor > 1 /*左边高*/ && getBalanceFactor(node.left) >= 0/*左节点的左子树高了*/) {
            return rightRotate(node);
        }

        //RR 向右侧子节点的右侧插入了新节点，破坏了平衡性，进行左旋转
        if (balanceFactor < -1 /*右边高*/ && getBalanceFactor(node.right) <= 0/*右节点的右子树高了*/) {
            return leftRotate(node);
        }

        //LR
        if (balanceFactor > 1 /*左边高*/ && getBalanceFactor(node.left) < 0 /*左子节点的右子树高了*/) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        //RL
        if (balanceFactor < -1 /*右边高*/ && getBalanceFactor(node.right) > 0/*右节点的左子树高了*/) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // 对节点y进行向左旋转操作，返回旋转后新的根节点x
    //         y                                            x
    //       /  \                                        /   \
    //      T1   x          向左旋转 (y)       y        z
    //           / \        - - - - - - - ->     / \        / \
    //        T2    z                           T1  T2   T3  T4
    //        /        \
    //      T3       T4
    private Node leftRotate(Node y) {
        Node x = y.right;
        Node t2 = x.left;

        //向左旋转
        x.left = y;
        y.right = t2;

        //更新高度
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    // 对节点y进行向右旋转操作，返回旋转后新的根节点x
    //          y                                         x
    //         / \                                     /   \
    //        x   T4     向右旋转 (y)        z        y
    //       / \           - - - - - - - ->    / \      / \
    //      z   T3                              T1  T2 T3 T4
    //     / \
    //   T1   T2
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node t3 = x.right;

        //向右旋转
        x.right = y;
        y.left = t3;

        //更新高度
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    private int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    private int getBalanceFactor(Node node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    // 判断该二叉树是否是一棵二分搜索树
    public boolean isBTS() {
        List<Key> list = new ArrayList<>();
        inOrder(root, list);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1).compareTo(list.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    private void inOrder(Node node, List<Key> list) {
        if (node == null) {
            return;
        }
        inOrder(node.left, list);
        list.add(node.key);
        inOrder(node.right, list);
    }

    // 判断该二叉树是否是一棵平衡二叉树
    public boolean isBalanced() {
        return isBalanced(root);
    }

    // 判断该二叉树是否是一棵平衡二叉树
    private boolean isBalanced(Node node) {
        if (node == null) {
            return true;
        }
        if (Math.abs(getBalanceFactor(node)) > 1) {
            return false;
        }
        return isBalanced(node.left) && isBalanced(node.right);
    }

    public Value remove(Key key) {
        Node node = getNode(root, key);
        if(node != null){
            root = remove(root, key);
            return node.value;
        }
        return null;
    }

    /*删除指定元素的节点*/
    @SuppressWarnings("all")
    private Node remove(Node node, Key key) {
        if (node == null) {
            return null;
        }

        int compare = key.compareTo(node.key);

        Node preReturn = null;

        if (compare < 0) {
            node.left = remove(node.left, key);
            preReturn = node;
        } else if (compare > 0) {
            node.right = remove(node.right, key);
            preReturn = node;
        } else {
            //compare == 0
            if (node.left == null) {/*要删除的节点只有右子树*/
                Node rightNode = node.right;
                node.right = null;
                size--;
                preReturn = rightNode;
            } else if (node.right == null) {/*要删除的节点只有左子树*/
                Node leftNode = node.left;
                node.left = null;
                size--;
                preReturn = leftNode;
            } else {/*要删除的节点既有左子树也有右子树*/
                //node.left != null && node.right != null
                //把node右子树中的最小值作为node 的替代
                Node successor = minimum(node.right);
                successor.right = remove(node.right, successor.key);/*这里采用递归方式，是为了在这次删除操作中因为维护好树的平衡性*/
                successor.left = node.left;
                node.left = node.right = null;
                preReturn = successor;
            }
        }

        if (preReturn == null) {
            return null;
        }

        //维护高度
        preReturn.height = 1 + Math.max(getHeight(preReturn.left), getHeight(preReturn.right));
        //平衡因子
        int balanceFactor = getBalanceFactor(preReturn);

        //维护平衡

        //LL 向左侧子节点的左侧插入了新节点，破坏了平衡性，进行右旋转
        if (balanceFactor > 1 /*左边高*/ && getBalanceFactor(preReturn.left) >= 0/*左节点的左子树高了*/) {
            return rightRotate(preReturn);
        }

        //RR 向右侧子节点的右侧插入了新节点，破坏了平衡性，进行左旋转
        if (balanceFactor < -1 /*右边高*/ && getBalanceFactor(preReturn.right) <= 0/*右节点的右子树高了*/) {
            return leftRotate(preReturn);
        }

        //LR
        if (balanceFactor > 1 /*左边高*/ && getBalanceFactor(preReturn.left) < 0 /*左子节点的右子树高了*/) {
            preReturn.left = leftRotate(preReturn.left);
            return rightRotate(preReturn);
        }

        //RL
        if (balanceFactor < -1 /*右边高*/ && getBalanceFactor(preReturn.right) > 0/*右节点的左子树高了*/) {
            preReturn.right = rightRotate(preReturn.right);
            return leftRotate(preReturn);
        }
        return preReturn;
    }

    /* 递归算法，一直往左找，直到左子节点为null，就是该树的最小值*/
    private Node minimum(Node node) {
        if (node.left == null) {
            return node;
        }
        return minimum(node.left);
    }


    public Value get(Key key) {
        Node node = getNode(root, key);
        return node == null ? null : node.value;
    }

    public boolean contains(Key key) {
        return getNode(root, key) != null;
    }

    public int size() {
        return size;
    }

    public void set(Key key, Value value) {
        Node node = getNode(root, key);
        if (node == null) {
            throw new NullPointerException("no this key");
        }
        node.value = value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private Node getNode(Node node, Key key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) < 0) {
            return getNode(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            return getNode(node.right, key);
        } else {// e.compareTo(node.e) == 0
            return node;
        }
    }

}
