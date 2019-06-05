package chapter6.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2016-12-24 22:52
 *         Email: ztiany3@gmail.com
 */
public class ForkJoinTest {

    public static void main(String... args) {

        long start = System.currentTimeMillis();
        testForkJoin();
//        testNormal();
        System.out.println("time = " + (System.currentTimeMillis() - start));

        handleException();
    }

    private static void handleException() {
        CountTask countTask = new CountTask(1, 1000000);
        if (countTask.isCompletedAbnormally()) {
            System.out.println(countTask.getException());
        }

    }

    private static void testNormal() {
        long sum = 0;
        for (long i = 0; i < 100; i++) {
            sleep();
            sum += i;
        }
        System.out.println(sum);
    }

    private static void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testForkJoin() {
        CountTask countTask = new CountTask(1, 100);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> submit = forkJoinPool.submit(countTask);
        try {
            System.out.println(submit.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    public static class CountTask extends RecursiveTask<Long> {

        private static final int THRESHOLD = 30;//阈值

        private final int start;
        private final int end;

        public CountTask(int start, int end) {
            this.start = start;
            this.end = end;
            if (start > end) {
                throw new IllegalArgumentException();
            }
        }


        @Override
        protected Long compute() {
            long sum = 0;
            boolean canCompute = (end - start) <= THRESHOLD;
            if (canCompute) {
                for (int i = start; i <= end; i++) {
                    sum += i;
                    sleep();
                }
            } else {//如果任务大于阈值，就分裂成两个任务
                /*
                5   9 --->  5-7，8-9
                2   10 --->  2-6，7-10
                 */
                int middle = (start + end) / 2;
                CountTask left = new CountTask(start, middle);
                CountTask right = new CountTask(middle + 1, end);
                //执行子任务
                left.fork();
                right.fork();
                //等待子任务执行完毕
                long leftResult = left.join();
                long rightResult = right.join();
                sum = leftResult + rightResult;
            }
            return sum;
        }
    }
}
