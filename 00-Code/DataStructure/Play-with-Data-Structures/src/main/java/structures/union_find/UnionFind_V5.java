package structures.union_find;

/**
 * QuickUnion 版，基于 rank 的优化 + 路劲压缩。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/17 17:24
 */
public class UnionFind_V5 implements UnionFind {

    private int[] parent;//记录所有元素所属组
    private int[] rank;//rank[i] 表示以i为根节点的集合所表示树的层数。

    /**
     * @param size 该并查集需要处理多少个元素
     */
    public UnionFind_V5(int size) {
        parent = new int[size];
        rank = new int[size];
        //默认所有元素都不是连接的
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
            rank[i] = 1;//默认每棵树的数量都是1
        }
    }

    /**
     * p 和 q 是否属于同一个集合。
     */
    @Override
    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    //查找元素 p 所对应的集合编号
    private int find(int p) {
        if (p < 0 || p >= parent.length) {
            throw new IndexOutOfBoundsException();
        }
        /*p的身份是值也是索引，可以认为索引值就是元素值*/
        while (p != parent[p]/*获取数据p的父节点索引，如果节点没有指向它自身，就不是根节点*/) {
            parent[p] = parent[parent[p]];//路径压缩
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
        // 根据两个元素所在树的层级数不同判断合并方向
        // 将层级数少的集合合并到层级数多的集合上
        if (rank[pRoot] < rank[qRoot]) {
            //让 p 的根节点的编号指向 q 的根节点
            parent[pRoot] = qRoot;
            //rank[qRoot]不需要增加，因为深度少的树合并到它上面，它自身的深度时不会郑家的
        } else if (rank[qRoot] < rank[pRoot]) {
            //让 p 的根节点的编号指向 q 的根节点
            parent[qRoot] = pRoot;
            //同理，不需要改变树的高度
        } else {//rank[qRoot] == rank[pRoot]
            parent[pRoot] = qRoot;
            rank[qRoot] += 1;
        }
    }

    @Override
    public int getSize() {
        return parent.length;
    }

}
