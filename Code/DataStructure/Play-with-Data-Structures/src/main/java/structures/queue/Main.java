package structures.queue;

import java.util.Random;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.22 23:11
 */
public class Main {

    public static void main(String... args) {
        //testQueue(new ArrayQueue<>());
        //testQueue(new LoopQueue<>());
        testPerformance();
    }

    /*
    ArrayQueue, time: 5.845564673 s
    LoopQueue, time: 0.022820496 s
    */
    private static void testPerformance() {
        int opCount = 100000;

        ArrayQueue<Integer> arrayQueue = new ArrayQueue<>();
        double time1 = testQueue(arrayQueue, opCount);
        System.out.println("ArrayQueue, time: " + time1 + " s");

        LoopQueue<Integer> loopQueue = new LoopQueue<>();
        double time2 = testQueue(loopQueue, opCount);
        System.out.println("LoopQueue, time: " + time2 + " s");
    }

    private static void testQueue(Queue<Integer> queue) {
        System.out.println("---------------------------------" + queue.getClass().getSimpleName());
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
            System.out.println(queue);
            if (i % 3 == 2) {
                queue.dequeue();
                System.out.println(queue);
            }
        }
    }

    // 测试使用q运行opCount个enqueueu和dequeue操作所需要的时间，单位：秒
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
