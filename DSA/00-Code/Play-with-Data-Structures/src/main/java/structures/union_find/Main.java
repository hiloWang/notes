package structures.union_find;

import java.util.Random;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/17 18:53
 */
public class Main {

    public static void main(String... args) {
        testUFPerformance();
    }

    private static void testUFPerformance() {
        int size = 1000000;
        int m =     1000000;
        //UnionFind unionFind1 = new UnionFind_V1(size);
        //UnionFind unionFind2 = new UnionFind_V2(size);
        UnionFind unionFind3 = new UnionFind_V3(size);
        UnionFind unionFind4 = new UnionFind_V4(size);
        UnionFind unionFind5 = new UnionFind_V5(size);
        UnionFind unionFind6 = new UnionFind_V6(size);
        //System.out.println("unionFind1 time " + testUF(unionFind1, m));
        //System.out.println("unionFind2 time " + testUF(unionFind2, m));
        System.out.println("unionFind3 time " + testUF(unionFind3, m));
        System.out.println("unionFind4 time " + testUF(unionFind4, m));
        System.out.println("unionFind5 time " + testUF(unionFind5, m));
        System.out.println("unionFind6 time " + testUF(unionFind6, m));
    }

    private static double testUF(UnionFind unionFind, int m) {
        int size = unionFind.getSize();
        Random random = new Random();
        long start = System.nanoTime();

        for (int i = 0; i < m; i++) {
            int a = random.nextInt(size);
            int b = random.nextInt(size);
            unionFind.unionElements(a, b);
        }

        for (int i = 0; i < m; i++) {
            int a = random.nextInt(size);
            int b = random.nextInt(size);
            unionFind.isConnected(a, b);
        }

        long end = System.nanoTime();

        return (end - start) / Math.pow(10, 9);
    }

}
