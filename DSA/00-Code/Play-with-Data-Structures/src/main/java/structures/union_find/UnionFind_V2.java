package structures.union_find;

/**
 * QuickUnion 版
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/17 17:24
 */
public class UnionFind_V2 implements UnionFind {

    //记录所有元素所属组
    private int[] parent;

    /**
     * @param size 该并查集需要处理多少个元素
     */
    public UnionFind_V2(int size) {
        parent = new int[size];
        //默认所有元素都不是连接的
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }
    }

    /**
     * p 和 q 是否属于同一个集合。
     */
    @Override
    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    //查找元素p所对应的集合编号
    private int find(int p) {
        if (p < 0 || p >= parent.length) {
            throw new IndexOutOfBoundsException();
        }
        while (p != parent[p]) {
            p = parent[p];//往上移动
        }
        return p;
    }

    // 合并元素p和元素q所属的集合，O(h) 的复杂度, h为树的高度
    @Override
    public void unionElements(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);
        if (qRoot == pRoot) {
            return;
        }
        //让 p 的根节点的编号指向 q 的根节点
        parent[pRoot] = qRoot;
    }

    @Override
    public int getSize() {
        return parent.length;
    }

}
