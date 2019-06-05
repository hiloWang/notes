package structures.tree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 二分搜索树，不支持插入空元素
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.29 16:49
 */
public class BST<E extends Comparable<E>> {

    private Node root;
    private int size;

    public void add(E e) {
        if (root == null) {
            root = new Node(e);
            size++;
        } else {
            add(root, e);
        }
    }

    private void add(Node root, E e) {
        if (e.compareTo(root.e) < 0 && root.left == null) {
            root.left = new Node(e);
            size++;
            return;
        }

        if (e.compareTo(root.e) > 0 && root.right == null) {
            root.right = new Node(e);
            size++;
            return;
        }

        if (e.compareTo(root.e) < 0 && root.left != null) {
            add(root.left, e);
        } else if (e.compareTo(root.e) > 0 && root.right != null) {
            add(root.right, e);
        }

    }

    public void addImproved(E e) {
        root = addImproved(root, e);
    }

    /**
     * 向以node为根的二分搜索树中插入元素e，递归算法，返回插入新节点后二分搜索树的根。
     * 对于二分搜索树，递归的每一次深度都把源树的层级减一，直到最后的null位就是需要添加新节点的位置。这个递归算法的精髓在于定义了返回值。
     */
    private Node addImproved(Node node, E e) {
        if (node == null) {
            size++;
            return new Node(e);
        }
        if (e.compareTo(node.e) < 0) {
            node.left = addImproved(node.left, e);
        } else if (e.compareTo(node.e) > 0) {
            node.right = addImproved(node.right, e);
        }

        // e.compareTo(node.e) == 0
        // no op

        return node;
    }

    public int getSize() {
        return size;
    }

    // 看二分搜索树中是否包含元素e
    public boolean contains(E e) {
        return contains(root, e);
    }

    //看以node为根的二分搜索树中是否包含元素e, 递归算法
    private boolean contains(Node node, E e) {
        if (node == null) {
            return false;
        }

        if (e.compareTo(node.e) == 0) {
            return true;
        } else if (node.e.compareTo(e) > 0) {
            return contains(node.right, e);
        } else {//e.compareTo(node.e) <0
            return contains(node.left, e);
        }
    }

    /**
     * 前序遍历
     */
    public void preOrder() {
        preOrder(root);
        System.out.println();
    }

    private void preOrder(Node node) {
        if (node == null) {
            return;
        }
        System.out.print(node.e + ", ");
        preOrder(node.left);
        preOrder(node.right);
    }

    /**
     * 中序遍历
     */
    public void inOrder() {
        inOrder(root);
        System.out.println();
    }

    private void inOrder(Node node) {
        if (node == null) {
            return;
        }
        inOrder(node.left);
        System.out.print(node.e + ", ");
        inOrder(node.right);
    }

    /**
     * 中序遍历
     */
    public void postOrder() {
        postOrder(root);
        System.out.println();
    }

    private void postOrder(Node node) {
        if (node == null) {
            return;
        }
        postOrder(node.left);
        postOrder(node.right);
        System.out.print(node.e + ", ");
    }

    // 二分搜索树的非递归前序遍历，使用栈实现
    public void preOrderNR() {
        if (root == null) {
            return;
        }
        //栈是先进先出的，这里先进先出符合前序遍历的深度优先场景，栈用于帮助我们记住需要遍历节点的顺序。
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            System.out.print(node.e + ", ");
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
        System.out.println();
    }

    //广度优先遍历，队列的先进先出特性满足广度优先遍历，使用队列记住遍历元素的顺序
    public void levelOrder() {
        if (root == null) {
            return;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node node = queue.remove();
            System.out.print(node.e + ", ");
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
        System.out.println();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        generateBSTString(root, 0, res);
        return res.toString();
    }

    // 生成以node为根节点，深度为depth的描述二叉树的字符串
    private void generateBSTString(Node node, int depth, StringBuilder res) {
        if (node == null) {
            res.append(generateDepthString(depth) + "null\n");
            return;
        }
        res.append(generateDepthString(depth) + node.e + "\n");
        generateBSTString(node.left, depth + 1, res);
        generateBSTString(node.right, depth + 1, res);
    }

    private String generateDepthString(int depth) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < depth; i++)
            res.append("--");
        return res.toString();
    }

    //寻找树中的最小值
    public E minimum() {
        if (root == null) {
            throw new RuntimeException("tree is empty");
        }
        return minimum(root).e;
    }

    /* 递归算法，一直往左找，直到左子节点为null，就是该树的最小值*/
    private Node minimum(Node node) {
        if (node.left == null) {
            return node;
        }
        return minimum(node.left);
    }

    //寻找树中的最大值
    public E maximum() {
        if (root == null) {
            throw new RuntimeException("tree is empty");
        }
        return maximum(root).e;
    }

    /* 递归算法，一直往右找，直到左子节点为null，就是该树的最小值*/
    private Node maximum(Node node) {
        if (node.right == null) {
            return node;
        }
        return maximum(node.right);
    }


    // 从二分搜索树中删除最小值所在节点, 返回最小值
    public E removeMin() {
        E minimum = minimum();
        root = removeMin(root);
        return minimum;
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            Node rightNode = node.right;
            node.right = null;
            size--;
            return rightNode;
        }
        node.left = removeMin(node.left);
        return node;
    }

    // 从二分搜索树中删除最小值所在节点, 返回最大值
    public E removeMax() {
        E minimum = minimum();
        root = removeMax(root);
        return minimum;
    }

    private Node removeMax(Node node) {
        if (node.right == null) {
            Node leftNode = node.left;
            node.left = null;
            size--;
            return leftNode;
        }
        node.right = removeMax(node.right);
        return node;
    }

    /**
     * 删除tree中值为e的结点
     */
    public void remove(E e) {
        if (root == null) {
            throw new RuntimeException("tree is empty");
        }
        root = remove(root, e);
    }

    /*删除指定元素的节点*/
    private Node remove(Node node, E e) {
        if (node == null) {
            return null;
        }

        int compare = e.compareTo(node.e);

        if (compare < 0) {
            node.left = remove(node.left, e);
            return node;
        } else if (compare > 0) {
            node.right = remove(node.right, e);
            return node;
        } else {
            //compare == 0
            if (node.left == null) {/*要删除的节点只有右子树*/
                Node rightNode = node.right;
                node.right = null;
                size--;
                return rightNode;
            } else if (node.right == null) {/*要删除的节点只有左子树*/
                Node leftNode = node.left;
                node.left = null;
                size--;
                return leftNode;
            } else {/*要删除的节点既有左子树也有右子树*/
                //node.left != null && node.right != null
                //把node右子树中的最小值作为node 的替代
                Node successor = minimum(node.right);
                successor.right = removeMin(node.right);
                successor.left = node.left;
                node.left = node.right = null;
                return successor;
            }
        }
    }

    private class Node {

        private final E e;
        private Node left, right;

        private Node(E e) {
            this.e = e;
        }

    }

}
