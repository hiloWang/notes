package structures.union_find;

/**
 * 并查集
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/17 17:24
 */
public interface UnionFind {

    boolean isConnected(int p, int q);

    void unionElements(int p, int q);

    int getSize();

}
