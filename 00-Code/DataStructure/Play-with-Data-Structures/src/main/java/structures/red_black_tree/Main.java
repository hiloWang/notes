package structures.red_black_tree;

import java.util.ArrayList;
import java.util.Random;

import structures.avl.AVLTree;
import structures.map.BSTMap;
import structures.set.FileOperation;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/20 12:24
 */
public class Main {

    public static void main(String... args) {
        //System.out.println("testRBT_BST---------------------------------------------------------------");
        //testRBT_BST();
        //System.out.println("testAddOperation---------------------------------------------------------------");
        //testAddOperation();
        System.out.println("testOrderAddOperation---------------------------------------------------------------");
        testOrderAddOperation();
    }

    private static void testRBT_BST() {

        ArrayList<String> words = new ArrayList<>();
        FileOperation.readFile("files/pride-and-prejudice.txt", words);
        //Collections.sort(words);

        //------------------------------------------------------------------------avl
        long start = System.nanoTime();
        AVLTree<String, Integer> avlTree = new AVLTree<>();

        for (String word : words) {
            if (avlTree.contains(word)) {
                avlTree.set(word, avlTree.get(word) + 1);
            } else {
                avlTree.add(word, 1);
            }
        }

        for (String word : words) {
            avlTree.contains(word);
        }

        long used = System.nanoTime() - start;
        System.out.println("avl 用时 " + (used / 1000000000.0) + "s");

        //------------------------------------------------------------------------rbt
        RBTree<String, Integer> rbTree = new RBTree<>();

        start = System.nanoTime();

        for (String word : words) {
            if (rbTree.contains(word)) {
                rbTree.set(word, rbTree.get(word) + 1);
            } else {
                rbTree.add(word, 1);
            }
        }

        for (String word : words) {
            rbTree.contains(word);
        }

        used = System.nanoTime() - start;
        System.out.println("rbt 用时 " + (used / 1000000000.0) + "s");

        //------------------------------------------------------------------------bst
        BSTMap<String, Integer> bstMap = new BSTMap<>();

        start = System.nanoTime();

        for (String word : words) {
            if (bstMap.contains(word)) {
                bstMap.set(word, bstMap.get(word) + 1);
            } else {
                bstMap.add(word, 1);
            }
        }

        for (String word : words) {
            bstMap.contains(word);
        }

        used = System.nanoTime() - start;
        System.out.println("bst 用时 " + (used / 1000000000.0) + "s");
    }


    private static void testAddOperation() {
        int n = 20000000;//千万

        Random random = new Random(n);
        ArrayList<Integer> testData = new ArrayList<>(n);
        for(int i = 0 ; i < n ; i ++){
            testData.add(random.nextInt(Integer.MAX_VALUE));
        }


        // Test BST
        long startTime = System.nanoTime();

        BSTMap<Integer, Integer> bst = new BSTMap<>();
        for (Integer x: testData){
            bst.add(x, null);
        }

        long endTime = System.nanoTime();

        double time = (endTime - startTime) / 1000000000.0;
        System.out.println("BST: " + time + " s");


        // Test AVL
        startTime = System.nanoTime();

        AVLTree<Integer, Integer> avl = new AVLTree<>();
        for (Integer x: testData){
            avl.add(x, null);
        }

        endTime = System.nanoTime();

        time = (endTime - startTime) / 1000000000.0;
        System.out.println("AVL: " + time + " s");


        // Test RBTree
        startTime = System.nanoTime();

        RBTree<Integer, Integer> rbt = new RBTree<>();
        for (Integer x: testData){
            rbt.add(x, null);
        }

        endTime = System.nanoTime();

        time = (endTime - startTime) / 1000000000.0;
        System.out.println("RBTree: " + time + " s");
    }

    private static void testOrderAddOperation() {

        int n = 20000000;//千万

        ArrayList<Integer> testData = new ArrayList<>(n);
        for(int i = 0 ; i < n ; i ++)
            testData.add(i);

        // Test AVL
        long startTime = System.nanoTime();

        AVLTree<Integer, Integer> avl = new AVLTree<>();
        for (Integer x: testData)
            avl.add(x, null);

        long endTime = System.nanoTime();

        double time = (endTime - startTime) / 1000000000.0;
        System.out.println("AVL: " + time + " s");


        // Test RBTree
        startTime = System.nanoTime();

        RBTree<Integer, Integer> rbt = new RBTree<>();
        for (Integer x: testData)
            rbt.add(x, null);

        endTime = System.nanoTime();

        time = (endTime - startTime) / 1000000000.0;
        System.out.println("RBTree: " + time + " s");
    }

}
