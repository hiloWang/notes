package structures.avl;

import java.util.ArrayList;
import java.util.Collections;

import structures.map.BSTMap;
import structures.set.FileOperation;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/13 23:13
 */
public class Main {

    public static void main(String... args) {
        System.out.println("AVLTree---------------------------------------------------------------");
        printWords();
        System.out.println("testAVL_BST---------------------------------------------------------------");
        testAVL_BST();
    }

    private static void printWords() {
        ArrayList<String> words = new ArrayList<>();
        FileOperation.readFile("files/pride-and-prejudice.txt", words);
        AVLTree<String, Integer> avlTree = new AVLTree<>();

        long start = System.nanoTime();

        for (String word : words) {
            if (avlTree.contains(word)) {
                avlTree.set(word, avlTree.get(word) + 1);
            } else {
                avlTree.add(word, 1);
            }
        }

        System.out.println("傲慢与偏见" + " 单词量：" + avlTree.size());
        System.out.println("pride 出现 " + avlTree.get("pride") + "次");
        System.out.println("prejudice 出现 " + avlTree.get("prejudice") + "次");
        System.out.println("isBTS  " + avlTree.isBTS());
        System.out.println("isBalanced " + avlTree.isBalanced());

        for (String word : words) {
            avlTree.remove(word);
            if (!avlTree.isBTS() || !avlTree.isBalanced()) {
                throw new IllegalStateException("AVL树实现错误");
            }
        }

        long used = System.nanoTime() - start;
        System.out.println("AVL 测试完成，用时 " + (used / 1000000000.0) + "s");
    }


    private static void testAVL_BST() {
        ArrayList<String> words = new ArrayList<>();
        FileOperation.readFile("files/pride-and-prejudice.txt", words);
        Collections.sort(words);

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

}