# ForkJon框架

## 1 什么是ForkJon框架

ForkJon框架是`JDK 1.7`提供的一个用于执行并行任务的框架，他可以把一个大任务分解成若干个小人物，最终汇总各个小任务的结果从而得到大人物的结果。

**Fork**：就是把一个大人物分解成若干个子任务，并行执行。
**Join**：就是合并这些子任务的执行结果。最后得到这个大任务的结果。

## 2 工作窃取算法

工作窃取算法是指一个线程从其他队列中窃取任务来执行。为了执行一个大任务，把这些大任务分割成若干互不依赖的子任务，为了减少线程之间的竞争，所以把把这些线程分别放到不同的队列中，并为每个队列创建一个单独的线程来执行任务，线程和队列一一对应，但是有的线程把自己的任务完成后，而其他线程还有未处理的任务，干完活的线程预期等着不如去帮其他线程干活，与他就去其他线程的队列里窃取任务来执行，这时就会有两个线程同时访问一个队列，为了减少窃取任务的线程和被窃取任务的线程之间的竞争，通常将队列设计为双端队列，两个线程分别重不同的头获取任务。

**优点：**充分利用线程并行计算减少了线程了线程间的竞争，**缺点：**在某些情况下还是存在竞争，比如双端队列里只有一个任务时。并且该算法还会消耗更多的系统资源。

## 3 Fork/Join框架设计

1. 分割任务
2. 执行任务并返回结果。


ForkJon框架使用两个类来完成上面步骤。
- ForkJoinTask：我们要使用ForkJon框架，必须先创建一个ForkJon任务，它提供在任务中执行fork()和join()等操作机制，通常情况下，我们不需要直接继承ForkJonTask，而只需要继承它的子类，
 - RecursiveAction：用于没有返回结果的任务
 - RecursiveTask：用于有返回结果的任务
- ForkJoinPool：ForkJoinTask需要通过ForkJoinPool来执行。

示例：根据计算的数量大小，把大与阈值的计算分割成小任务。

```java
    public class ForkJoinTest {

        public static void main(String... args) {

            long start = System.currentTimeMillis();
            testForkJoin();
            //testNormal();
            System.out.println("time = " + (System.currentTimeMillis() - start));
    
        }
    
        private static void testNormal() {
            long sum = 0;
            for (int i = 0; i <= 1000000; i++) {
                sum += i;
            }
            System.out.println(sum);
    
        }
    
        private static void testForkJoin() {
            CountTask countTask = new CountTask(1, 1000000);
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
```


## 4 ForkJoin异常处理

ForkJoinTask执行的时候可能会产生异常，但我们没有办法在主线程里直接捕获异常，ForkJoinTask提供了isCompletedAbnormally()方法来检查任务是否已经抛出异常或者已经被取消了，并且可以通过ForkJoinTask的getException方法来获取异常。
```
      if (countTask.isCompletedAbnormally()) {
                System.out.println(countTask.getException());
       }
```


