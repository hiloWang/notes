package structures.tree;

import java.util.Random;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.29 17:11
 */
public class Main {

    public static void main(String... args) {
        int[] ints = generateArray(10);
        testBST();
        //testAdd(ints);
        //testAddImproved(ints);
    }

    private static void testBST() {

        BST<Integer> bst = new BST<>();
        int[] nums = {5, 3, 6, 8, 4, 2};
        for (int num : nums)
            bst.add(num);

        /////////////////
        //           5      //
        //         /   \    //
        //        3    6    //
        //       / \    \   //
        //      2  4     8  //
        /////////////////

        System.out.println("size: " + bst.getSize());
        bst.preOrder();
        bst.inOrder();
        bst.postOrder();
        bst.preOrderNR();
        bst.levelOrder();
        //bst.removeMin();
        //bst.inOrder();
        //bst.removeMax();
        //bst.inOrder();
        bst.remove(3);
        bst.inOrder();
        System.out.println(bst);
    }

    private static int[] generateArray(int length) {
        Random random = new Random();
        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(length * 10);
        }
        return arr;
    }

    private static void testAddImproved(int[] arr) {
        BST<Integer> integerBST = new BST<>();
        for (int anArr : arr) {
            integerBST.addImproved(anArr);
        }
        System.out.println("size: " + integerBST.getSize());
        integerBST.preOrder();
        integerBST.inOrder();
        integerBST.postOrder();
        integerBST.preOrderNR();
        integerBST.levelOrder();
        System.out.println(integerBST);
    }

    private static void testAdd(int[] arr) {
        BST<Integer> integerBST = new BST<>();
        for (int anArr : arr) {
            integerBST.add(anArr);
        }
        System.out.println("size: " + integerBST.getSize());
        integerBST.preOrder();
        integerBST.inOrder();
        integerBST.postOrder();
        integerBST.preOrderNR();
        integerBST.levelOrder();
        System.out.println(integerBST);
    }

}
