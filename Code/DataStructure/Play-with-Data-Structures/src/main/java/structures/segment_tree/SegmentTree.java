package structures.segment_tree;

import java.util.Objects;

/**
 * 线段树
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/16 20:34
 */
public class SegmentTree<E> {

    private E[] data;//用于存储数组中的数据
    private E[] tree;//用于存储线段树的各个节点
    private Merger<E> merger;

    @SuppressWarnings("unchecked")
    public SegmentTree(E[] data, Merger<E> merger) {
        this.merger = Objects.requireNonNull(merger);
        this.data = (E[]) new Object[data.length];
        //需要原数组长度的4倍才能满足构建所有情况下的线段树的空间
        this.tree = (E[]) new Object[data.length * 4];
        System.arraycopy(data, 0, this.data, 0, data.length);
        //l 和 r 总是在 0-data.length-1 的范围内，开始构建线段树
        buildSegment(0, 0, this.data.length - 1);
    }

    /**
     * 线段树中，组合两个子节点的操作。
     *
     * @author Ztiany
     * Email ztiany3@gmail.com
     * Date 2018/10/16 20:34
     */
    public interface Merger<E> {
        E merge(E left, E right);
    }

    public int getSize() {
        return data.length;
    }

    public E get(int index) {
        if (index < 0 || index >= data.length) {
            throw new IllegalArgumentException("Index is illegal.");
        }
        return data[index];
    }

    /*在treeIndex的位置创建表示区间 [l...r] 的线段树*/
    @SuppressWarnings("all")
    private void buildSegment(int treeIndex, int l, int r) {
        if (l == r) {
            //如果区间左边界等于右边界了，说明到了线段树的尾节点了（该线段只有一个元素了，无法再分了）
            //该线段树节点的值等于数组中对应为以自己边界索引对应的值
            tree[treeIndex] = data[l];
            return;
        }

        //现在l 和 r 肯定不相等，它们还可以继续拆分
        int leftIndex = leftChild(treeIndex);//左孩子索引
        int rightIndex = rightChild(treeIndex);//右孩子索引

        /*把父节点表示的空间拆分成两部分*/
        int middle = l + (r - l) / 2;//求 r 和 l 的中间值
        buildSegment(leftIndex, l, middle);
        buildSegment(rightIndex, middle + 1, r);

        //递归回来，子节点已经都被赋值了。
        //父节点是左右孩子的综合
        tree[treeIndex] = merger.merge(tree[leftIndex], tree[rightIndex]);
    }

    // 返回完全二叉树的数组表示中，一个索引所表示的元素的左孩子节点的索引
    private int leftChild(int index) {
        return 2 * index + 1;
    }

    // 返回完全二叉树的数组表示中，一个索引所表示的元素的右孩子节点的索引
    private int rightChild(int index) {
        return 2 * index + 2;
    }

    public E query(int queryL, int queryR) {
        if (queryL < 0 || queryL >= data.length || queryR < 0 || queryR >= data.length) {
            throw new IndexOutOfBoundsException("range ouf of bound");
        }
        return query(0, 0, data.length - 1, queryL, queryR);
    }

    /*在以treeIndex为根的线段树中[l...r]的范围里，搜索区间[queryL...queryR]的值*/
    private E query(int treeIndex, int treeL, int treeR, int queryL, int queryR) {
        //递归计算条条件
        if (treeL == queryL && treeR == queryR) {
            return tree[treeIndex];
        }

        int leftIndex = leftChild(treeIndex);//左孩子索引
        int rightIndex = rightChild(treeIndex);//右孩子索引
        int middle = treeL + (treeR - treeL) / 2;//求 r 和 l 的中间值

        // 1 如果查询区间的右边界小于或等于当前根节点的右孩子的左边界，则缩小范围，从左孩子的区间中去查询。
        if (queryR <= middle) {
            return query(leftIndex, treeL, middle, queryL, queryR);
        }
        //2 如果查询区间的左边界大于或等于根节点的左孩子的右边界，则缩小范围，从根节点 的右孩子的区间中去查询。
        else if (queryL >= middle + 1) {
            return query(rightIndex, middle + 1, treeR, queryL, queryR);
        }
        //3 如果查询区间的范围既在的左孩子的区间中，也在的右孩子的区间中。则把查询区间拆分成两部分，左边部分的右边界是的左孩子的右边界，
        //右边部分的左边界是右孩子的左边界，然后将查询得到的两部分综合就是查询的结果了。
        else {
            E leftPart = query(leftIndex, treeL, middle, queryL, middle);
            E rightPart = query(rightIndex, middle + 1, treeR, middle + 1, queryR);
            return merger.merge(leftPart, rightPart);
        }
    }

    /*更新线段树中的某索引对于的值*/
    public void set(int index, E newValue) {
        if (index < 0 || index >= getSize()) {
            throw new IndexOutOfBoundsException();
        }
        data[index] = newValue;
        set(0, 0, data.length - 1, index, newValue);
    }

    /*在以 treeIndex 为根节点的线段树中[l...r]的范围里，更新索引为 index 的值为 newValue*/
    @SuppressWarnings("all")
    private void set(int treeIndex, int l, int r, int index, E newValue) {
        //找到了对应的节点，就是递归结束条件
        if (l == r) {
            tree[treeIndex] = newValue;
            return;
        }

        int leftIndex = leftChild(treeIndex);//左孩子索引
        int rightIndex = rightChild(treeIndex);//右孩子索引
        int middle = l + (r - l) / 2;//求 r 和 l 的中间值

        if (index <= middle) {
            //如果需要更新的值在左孩子区间，就去左边区间去找
            set(leftIndex, l, middle, index, newValue);
        } else if (index >= middle + 1) {
            //如果需要更新的值在右孩子区间，就去右边区间去找
            set(rightIndex, middle + 1, r, index, newValue);
        }

        //递归回来，更新父节点
        tree[treeIndex] = merger.merge(tree[leftIndex], tree[rightIndex]);
    }


    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append('[');
        for (int i = 0; i < tree.length; i++) {
            if (tree[i] != null) {
                res.append(tree[i]);
            } else {
                res.append("null");
            }
            if (i != tree.length - 1) {
                res.append(", ");
            }
        }
        res.append(']');
        return res.toString();
    }

}
