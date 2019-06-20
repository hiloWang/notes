package structures.hash;

import java.util.ArrayList;

import structures.avl.AVLTree;
import structures.map.BSTMap;
import structures.red_black_tree.RBTree;
import structures.set.FileOperation;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/20 18:10
 */
public class Main {

    public static void main(String... args) {
        testHash_BST_AVL_RBT();
    }

    private static void testHash_BST_AVL_RBT() {

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
        HashTable<String, Integer> hashTable = new HashTable<>();

        start = System.nanoTime();

        for (String word : words) {
            if (hashTable.contains(word)) {
                hashTable.set(word, hashTable.get(word) + 1);
            } else {
                hashTable.add(word, 1);
            }
        }
        for (String word : words) {
            hashTable.contains(word);
        }

        used = System.nanoTime() - start;
        System.out.println("hashTable 用时 " + (used / 1000000000.0) + "s");

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
