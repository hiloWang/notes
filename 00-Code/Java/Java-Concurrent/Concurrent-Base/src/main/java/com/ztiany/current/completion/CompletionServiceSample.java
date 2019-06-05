package com.ztiany.current.completion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 如果向Executor提交了一组计算任务，并且希望在计算完成后获得结果，那么可以保留与每个任务关联的Future，
 * 然后反复使用get方法，同时将参数timeout指定为0，从而通过轮询来判断任务是否完成。这种方法虽然可行，
 * 但却有些繁琐。幸运的是，还有一种更好的方法：完成服务CompletionService。
 *
 * @author Ztiany
 *         Email ztiany3@gmail.com
 *         Date 17.8.27 16:15
 */
public class CompletionServiceSample {

    public static void main(String... args) {
//        traditionalWay();
        completionWay();
    }

    /**
     * CompletionService实现了生产者提交任务和消费者获取结果的解耦，生产者和消费者都不用关心任务的完成顺序，
     * 由CompletionService来保证，消费者一定是按照任务完成的先后顺序来获取执行结果。
     * ExecutorCompletionService是CompletionService的实现，融合了线程池Executor和阻塞队列BlockingQueue的功能。
     */
    private static void completionWay() {
        long start = System.currentTimeMillis();
        int taskSize = 6;
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 构建完成服务
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor);
        //提交任务
        for (int i = 1; i <= taskSize; i++) {
            int sleep = taskSize - i; // 睡眠时间
            // 向线程池提交任务
            completionService.submit(new ReturnAfterSleepCallable(sleep, i));
        }
        // 按照完成顺序,打印结果
        for (int i = 0; i < taskSize; i++) {
            try {
                //take 获取并移除表示下一个已完成任务的 Future，如果目前不存在这样的任务，则等待。
                System.out.println(completionService.take().get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        // 所有任务已经完成，关闭线程池
        System.out.println("all over.");
        System.out.println("time：" + (System.currentTimeMillis() - start));
        executor.shutdown();
    }


    /**
     * 繁琐的做法
     */
    private static void traditionalWay() {
        long start = System.currentTimeMillis();
        int taskSize = 6;
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Future<Integer>> futureList = new ArrayList<>();
        for (int i = 1; i <= taskSize; i++) {
            int sleep = taskSize - i; // 睡眠时间
            // 向线程池提交任务
            Future<Integer> future = executor.submit(new ReturnAfterSleepCallable(sleep, i));
            // 保留每个任务的Future
            futureList.add(future);
        }
        // 轮询,获取完成任务的返回结果
        while (taskSize > 0) {
            for (Future<Integer> future : futureList) {
                Integer result = null;
                try {
                    result = future.get(0, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    // 超时异常需要忽略,因为我们设置了等待时间为0,只要任务没有完成,就会报该异常
                }
                // 任务已经完成
                if (result != null) {
                    System.out.println("result=" + result);
                    // 从future列表中删除已经完成的任务
                    futureList.remove(future);
                    taskSize--;
                    //此处必须break，否则会抛出并发修改异常。（也可以通过将futureList声明为CopyOnWriteArrayList类型解决）
                    break; // 进行下一次while循环
                }
            }
        }
        // 所有任务已经完成,关闭线程池
        System.out.println("all over.");
        System.out.println("time：" + (System.currentTimeMillis() - start));
        executor.shutdown();
    }


    public static class ReturnAfterSleepCallable implements Callable<Integer> {

        private int sleepSeconds;
        private int returnValue;

        ReturnAfterSleepCallable(int sleepSeconds, int returnValue) {
            this.sleepSeconds = sleepSeconds;
            this.returnValue = returnValue;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println("begin to execute.");
            TimeUnit.SECONDS.sleep(sleepSeconds);
            System.out.println("end to execute.");
            return returnValue;
        }
    }

}
