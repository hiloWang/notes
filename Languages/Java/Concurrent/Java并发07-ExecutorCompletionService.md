# CompletionService

---
## 1 CompletionService 介绍

如果向Executor提交了一组计算任务，并且希望在计算完成后获得结果，那么可以保留与每个任务关联的Future，然后反复使用get方法，同时将参数timeout指定为0，从而通过轮询来判断任务是否完成。这种方法虽然可行，但却有些繁琐。幸运的是，还有一种更好的方法：完成服务 `CompletionService`。

`CompletionService` 实现了生产者提交任务和消费者获取结果的解耦，生产者和消费者都不用关心任务的完成顺序，由 `CompletionService` 来保证，消费者一定是按照任务完成的先后顺序来获取执行结果。`ExecutorCompletionService` 是CompletionService的实现，融合了线程池Executor和阻塞队列BlockingQueue的功能。

### 官方文档

CompletionService 将生产新的异步任务与使用已完成任务的结果分离开来的服务。生产者 submit 执行的任务。使用者 take 已完成的任务，**并按照完成这些任务的顺序处理它们的结果**。例如，CompletionService 可以用来管理异步 IO ，执行读操作的任务作为程序或系统的一部分提交，然后，当完成读操作时，会在程序的不同部分执行其他操作，执行操作的顺序可能与所请求的顺序不同。通常，CompletionService 依赖于一个单独的 Executor 来实际执行任务，在这种情况下，CompletionService 只管理一个内部完成队列。ExecutorCompletionService 类提供了此方法的一个实现。

**内存一致性效果**：线程中向 CompletionService 提交任务之前的操作 `happen-before` 该任务执行的操作，后者依次 happen-before 紧跟在从对应 `take()` 成功返回的操作。



---
## 2 使用示例

```java
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
```

---
## 引用

- 《Java核心技术》