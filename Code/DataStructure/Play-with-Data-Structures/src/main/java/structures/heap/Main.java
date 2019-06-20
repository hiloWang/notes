package structures.heap;

import java.util.Random;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/14 12:37
 */
public class Main {

    public static void main(String[] args) {
        //testHeap();
        testHeapify();
    }

    private static void testHeapify() {
        int n = 5000000;
        Random random = new Random();
        Integer[] arr = new Integer[n];

        for (int i = 0; i < n; i++) {
            arr[i] = random.nextInt(Integer.MAX_VALUE);
        }

        testHeapify(arr, false);
        testHeapify(arr, true);
    }

    private static void testHeapify(Integer[] arr, boolean useHeapify) {
        long startTime = System.nanoTime();
        ArrayMaxHeap<Integer> heap;

        if (useHeapify) {
            heap = new ArrayMaxHeap<>(arr);
        } else {
            heap = new ArrayMaxHeap<>(arr.length);
            for (Integer integer : arr) {
                heap.add(integer);
            }
        }

        int size = heap.size();
        int[] testArr = new int[size];
        for (int i = 0; i < size; i++) {
            testArr[i] = heap.extractMax();
        }
        for (int i = 1; i < testArr.length; i++) {
            if (testArr[i - 1] < testArr[i]) {
                throw new IllegalArgumentException("Error");
            }
        }

        long endTime = System.nanoTime();

        System.out.println("Test MaxHeap completed.");

        System.out.println("useHeapify = " + useHeapify + " use time =" + ((endTime - startTime) / 1000000000.0) +"s");
    }

    private static void testHeap() {
        int n = 1000000;

        ArrayMaxHeap<Integer> maxHeap = new ArrayMaxHeap<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            maxHeap.add(random.nextInt(Integer.MAX_VALUE));
        }

        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = maxHeap.extractMax();
        }

        for (int i = 1; i < n; i++) {
            if (arr[i - 1] < arr[i]) {
                throw new IllegalArgumentException("Error");
            }
        }

        System.out.println("Test MaxHeap completed.");
    }


}
