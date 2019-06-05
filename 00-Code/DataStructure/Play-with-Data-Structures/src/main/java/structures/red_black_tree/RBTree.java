package structures.red_black_tree;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/19 15:50
 */
public class RBTree<Key extends Comparable<Key>, Value> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {

        Key key;
        Value value;
        Node left;
        Node right;
        boolean color;

        public Node(Key key, Value value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            color = RED;
        }

    }

    private Node root;
    private int size;

    private boolean isRed(Node node) {
        if (node == null) {
            return BLACK;
        }
        return node.color;
    }

    public void add(Key key, Value value) {
        root = addImproved(root, key, value);
        root.color = BLACK;//保持根节点为黑色
    }

    /**
     * 向以node为根的二分搜索树中插入元素e，递归算法，返回插入新节点后二分搜索树的根。
     * 对于二分搜索树，递归的每一次深度都把源树的层级减一，直到最后的null位就是需要添加新节点的位置。这个递归算法的精髓在于定义了返回值。
     */
    private Node addImproved(Node node, Key key, Value value) {
        if (node == null) {
            size++;
            return new Node(key, value);//默认插入红色节点
        }
        if (key.compareTo(node.key) < 0) {
            node.left = addImproved(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = addImproved(node.right, key, value);
        } else {// e.compareTo(node.e) == 0
            node.value = value;
        }

        /*是否需要左旋转，右边的孩子是红色，左边的孩子不是红色*/
        if (isRed(node.right) && !isRed(node.left)) {
            node = leftRotate(node);
        }

        /*是否需要右旋转，右边的孩子是红色，左边孩子的左孩子也是红色*/
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rightRotate(node);
        }

        //是否需要进行颜色翻转
        if(isRed(node.right) && isRed(node.left)){
            flipColor(node);
        }

        return node;
    }

    //        node                            x
    //       /   \     左旋转             /  \
    //      T1   x   --------->      node   T3
    //            / \                    /   \
    //         T2  T3               T1   T2
    private Node leftRotate(Node node) {
        Node x = node.right;
        //左旋转
        node.right = x.left;
        x.left = node;
        //维护颜色
        x.color = node.color;
        node.color = RED;
        return x;
    }

    //          node                          x
    //         /   \       右旋转         /  \
    //        x    T2   ------->      y   node
    //       / \                        /  \
    //     y   T1                    T1   T2
    private Node rightRotate(Node node) {
        Node x = node.left;
        //右旋转
        node.left = x.right;
        x.right = node;
        //颜色维护
        x.color = node.color;
        node.color = RED;
        return x;
    }

    /*颜色翻转*/
    private void flipColor(Node node) {
        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;
    }

    public Value remove(Key key) {
        Node node = getNode(root, key);
        if (node != null) {
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

        if (compare < 0) {
            node.left = remove(node.left, key);
            return node;
        } else if (compare > 0) {
            node.right = remove(node.right, key);
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

    /* 递归算法，一直往左找，直到左子节点为null，就是该树的最小值*/
    private Node minimum(Node node) {
        if (node.left == null) {
            return node;
        }
        return minimum(node.left);
    }

    @SuppressWarnings("all")
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
