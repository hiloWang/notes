package structures.segment_tree;

import java.util.Objects;

/**
 * int 类型线段树
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/16 20:34
 */
public class IntSegmentTree {

    private int[] data;
    private int[] tree;
    private Merger merger;

    public IntSegmentTree(int[] data, Merger merger) {
        this.merger = Objects.requireNonNull(merger);
        this.data = new int[data.length];
        this.tree = new int[data.length * 4];
        System.arraycopy(data, 0, this.data, 0, data.length);
        buildSegment(0, 0, this.data.length - 1);
    }

    public interface Merger {
        int merge(int left, int right);
    }

    @SuppressWarnings("all")
    private void buildSegment(int treeIndex, int l, int r) {
        if (l == r) {
            tree[treeIndex] = data[l];
            return;
        }
        int leftIndex = leftChild(treeIndex);
        int rightIndex = rightChild(treeIndex);
        int middle = l + (r - l) / 2;
        buildSegment(leftIndex, l, middle);
        buildSegment(rightIndex, middle + 1, r);
        tree[treeIndex] = merger.merge(tree[leftIndex], tree[rightIndex]);
    }


    public int getSize() {
        return data.length;
    }

    public int get(int index) {
        if (index < 0 || index >= data.length) {
            throw new IllegalArgumentException("Index is illegal.");
        }
        return data[index];
    }

    private int leftChild(int index) {
        return 2 * index + 1;
    }

    private int rightChild(int index) {
        return 2 * index + 2;
    }

    public int query(int queryL, int queryR) {
        if (queryL < 0 || queryL >= data.length || queryR < 0 || queryR >= data.length) {
            throw new IllegalArgumentException("range ouf of bound");
        }
        return query(0, 0, data.length - 1, queryL, queryR);
    }

    private int query(int treeIndex, int treeL, int treeR, int queryL, int queryR) {
        if (treeL == queryL && treeR == queryR) {
            return tree[treeIndex];
        }

        int leftIndex = leftChild(treeIndex);
        int rightIndex = rightChild(treeIndex);
        int middle = treeL + (treeR - treeL) / 2;

        if (queryR <= middle) {
            return query(leftIndex, treeL, middle, queryL, queryR);
        } else if (queryL >= middle + 1) {
            return query(rightIndex, middle + 1, treeR, queryL, queryR);
        } else {
            int leftPart = query(leftIndex, treeL, middle, queryL, middle);
            int rightPart = query(rightIndex, middle + 1, treeR, middle + 1, queryR);
            return merger.merge(leftPart, rightPart);
        }

    }

    public void set(int index, int newValue) {
        if (index < 0 || index >= getSize()) {
            throw new IndexOutOfBoundsException();
        }
        data[index] = newValue;
        set(0, 0, data.length - 1, index, newValue);
    }

    @SuppressWarnings("all")
    private void set(int treeIndex, int l, int r, int index, int newValue) {
        if (l == r) {
            tree[treeIndex] = newValue;
            return;
        }
        int leftIndex = leftChild(treeIndex);
        int rightIndex = rightChild(treeIndex);
        int middle = l + (r - l) / 2;
        if (index <= middle) {
            set(leftIndex, l, middle, index, newValue);
        } else if (index >= middle + 1) {
            set(rightIndex, middle + 1, r, index, newValue);
        }
        tree[treeIndex] = merger.merge(tree[leftIndex], tree[rightIndex]);
    }

}
