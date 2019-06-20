package structures.union_find;

/**
 * QuickFind 版
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/17 17:24
 */
public class UnionFind_V1 implements UnionFind {

    //记录所有元素所属组
    private int[] ids;

    /**
     * @param size 该并查集需要处理多少个元素
     */
    public UnionFind_V1(int size) {
        ids = new int[size];
        //默认所有元素都不是连接的
        for (int i = 0; i < ids.length; i++) {
            ids[i] = i;
        }
    }

    /**
     * p 和 q 是否属于同一个集合。
     */
    @Override
    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    private int find(int index) {
        if (index < 0 || index >= ids.length) {
            throw new IndexOutOfBoundsException();
        }
        return ids[index];
    }

    @Override
    public void unionElements(int p, int q) {
        int qId = find(q);
        int pId = find(p);
        if (pId == qId) {
            return;
        }
        // 合并过程需要遍历一遍所有元素, 将两个元素的所属集合编号合并
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] == qId) {
                ids[i] = pId;
            }
        }
    }

    @Override
    public int getSize() {
        return ids.length;
    }

}
