package structures.segment_tree;

import java.util.Arrays;
import java.util.Random;

import structures.segment_tree.SegmentTree.Merger;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/16 22:43
 */
public class Main {

    private static Merger<Integer> sumMerger = (left, right) -> left + right;

    public static void main(String... args) {
        testSegmentTree();
    }

    private static void testSegmentTree() {
        final int length = 10;
        Random random = new Random();
        Integer[] array = new Integer[length];
        for (int i = 0; i < length; i++) {
            array[i] = random.nextInt(10);
        }

        System.out.println("array = " + Arrays.toString(array));
        System.out.println("sum of array = " + Arrays.asList(array).stream().reduce((integer, integer2) -> integer + integer2).get());

        SegmentTree<Integer> segmentTree = new SegmentTree<>(array, sumMerger);

        System.out.println("[0,1] sum of array =" + segmentTree.query(0, 1));
        System.out.println("[0,2] sum of array =" + segmentTree.query(0, 2));
        System.out.println("[1,2] sum of array =" + segmentTree.query(1, 2));
        System.out.println("[1,3] sum of array =" + segmentTree.query(1, 3));
        System.out.println("[0,9] sum of array =" + segmentTree.query(0, 9));
    }
}
