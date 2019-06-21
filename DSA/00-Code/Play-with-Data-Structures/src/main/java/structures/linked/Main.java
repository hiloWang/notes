package structures.linked;

import java.util.Random;

import structures.queue.ArrayQueue;
import structures.queue.Queue;
import structures.queue.LoopQueue;


/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.24 22:54
 */
public class Main {

    public static void main(String... args) {
        //testLinkedList_Origin();
        //testLinkedList();
        testLinkedListQueue();
        //testAllQueue();
    }

    private static void testLinkedListQueue() {
        LinkedListQueue<Integer> linkedListQueue = new LinkedListQueue<>();
        for (int i = 0; i < 20; i++) {
            linkedListQueue.enqueue(i);
        }
        System.out.println(linkedListQueue);
        for (int i = 0; i < 5; i++) {
            linkedListQueue.dequeue();
        }
        System.out.println(linkedListQueue);
    }

    private static void testLinkedList() {
        LinkedList<Integer> integerLinkedList_Origin = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            integerLinkedList_Origin.addFirst(i);
        }
        System.out.println(integerLinkedList_Origin);
        integerLinkedList_Origin.addLast(100);
        integerLinkedList_Origin.add(1, 101);
        System.out.println(integerLinkedList_Origin);
    }

    private static void testLinkedList_Origin() {
        OriginLinkedList<Integer> integer_OriginLinkedList = new OriginLinkedList<>();
        for (int i = 0; i < 10; i++) {
            integer_OriginLinkedList.addFirst(i);
        }
        System.out.println(integer_OriginLinkedList);
        integer_OriginLinkedList.addLast(100);
        System.out.println(integer_OriginLinkedList);
    }

    private static void testAllQueue() {
        int opCount = 100000;

        ArrayQueue<Integer> arrayQueue = new ArrayQueue<>();
        double time1 = testQueue(arrayQueue, opCount);
        System.out.println("ArrayQueue, time: " + time1 + " s");

        LoopQueue<Integer> loopQueue = new LoopQueue<>();
        double time2 = testQueue(loopQueue, opCount);
        System.out.println("LoopQueue, time: " + time2 + " s");

        LinkedListQueue<Integer> linkedListQueue = new LinkedListQueue<>();
        double time3 = testQueue(linkedListQueue, opCount);
        System.out.println("LinkedListQueue, time: " + time3 + " s");
    }

    // 测试使用Queue运行opCount个enqueueu和dequeue操作所需要的时间，单位：秒
    @SuppressWarnings("all")
    private static double testQueue(Queue<Integer> q, int opCount) {
        long startTime = System.nanoTime();

        Random random = new Random();
        for (int i = 0; i < opCount; i++) {
            q.enqueue(random.nextInt(Integer.MAX_VALUE));
        }

        for (int i = 0; i < opCount; i++) {
            q.dequeue();
        }

        long endTime = System.nanoTime();

        return (endTime - startTime) / 1000000000.0;
    }

}
