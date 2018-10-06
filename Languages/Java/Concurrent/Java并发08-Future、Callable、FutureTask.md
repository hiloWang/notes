# Future、Callable、FutureTask学习

**注意：** Callable、Future和FutureTask在Android中的实现和Java中的实现比较不一样，但是功能是一样的，这里的源码部分是列举的Android中的源码

---
## 1 Callable、Future和FutureTask介绍

在多线程中，Runnable我们都比熟悉，但是Java处理Runnable还有Callable、Future和FutureTask这几个与线程相关的概念，这个类与Runnable不同的是他们只能应用于线程池中，而Runnable既能运行在单线程又能运行在线程池中。

Callable与Runnable类型，但是Callable是一个泛型接口，它有一个泛型参数V，该接口中有一个返回类型为V的call函数，而Runnable的run方法没有返回值，也就是说不能将运行结果返回给客户端程序，Callable的声明如下：

```java
    public interface Callable<V> {
        V call() throws Exception;
    }
```

Runnable和Callable代表一个线程执行的任务，任务一旦发起就行一匹脱缰的野马，很难控制，为了便于管理线程执行的任务，Java提供了Future，Future为线程池定制了一个可管理的任务标准，它提供了对Runnale、Callable任务执行结果进行取消、查询是否完成、获取结果、设置结果的操作，Future的声明如下：

```java
    public interface Future<V> {

        boolean cancel(boolean mayInterruptIfRunning);
        boolean isCancelled();
        boolean isDone();

        //get是一个阻塞方法，直到任务返回结果。
        V get() throws InterruptedException, ExecutionException;
        V get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException;
     }
```

Future只是定义了管理规范的接口，而FutureTask则是它的实现类，FutureTask实现了`RunnableFuture<V>`，而RunnableFuture实现了Runnable又实现了`Future<V>`两个接口，因此FutureTask具备管理他们的能力。

RunnableFuture的声明：

```java
    public interface RunnableFuture<V> extends Runnable, Future<V> {
        void run();
    }
```

FutureTask的构造函数

```java
        public FutureTask(Callable<V> callable) {
            if (callable == null)
                throw new NullPointerException();
            sync = new Sync(callable);
        }

        public FutureTask(Runnable runnable, V result) {
            sync = new Sync(Executors.callable(runnable, result));
        }
```

适配器模式包装Callable

```java
    public static <T> Callable<T> callable(Runnable task, T result) {
            if (task == null)
                throw new NullPointerException();
            return new RunnableAdapter<T>(task, result);
    }
    
    static final class RunnableAdapter<T> implements Callable<T> {
            final Runnable task;
            final T result;
            RunnableAdapter(Runnable task, T result) {
                this.task = task;
                this.result = result;
            }
            public T call() {
                task.run();
                return result;
            }
        }
```

从构造方法可以看出，无论是传入的Callable还有Runnable，最终都会被转换成Callable，然后构建同步复杂对象Sync。其实FutureTask只是一个空壳，真正的实现是其内部继承了同步基础类AbstractQueuedSynchronizer的Sync类。

由于FutureTask实现了Runnable，因此，它既可以通过Thread包装来直接执行，也可以提交给ExecuteService来执行，并且还可以通过get函数获取执行结果，该函数会阻塞，知道有结果返回，因此FutureTask即使Future又是Runnable，而且还包装了Callable，他是这两者的结合体。


### FutureTask的done方法

```
        protected void done() { }
```

FutureTask的done方法是一个受保护的空方法，子类可以对其进行覆写，done方法表示任务执行完毕的回调。

---
## 2 Android中实现细节--设计方法学习

从Android中源码可以看到FutureTask虽然实现了Future，但是其内部功能确实有其内部类Sync实现的:

```
     private final class Sync extends AbstractQueuedSynchronizer{
        ......
     }
```

Sync继承了同步基础类AbstractQueuedSynchronizer，同时也是FutureTask功能真正的实现者，这样做的好处是隐藏了实现细节，如果直接由FutureTask继承AbstractQueuedSynchronizer，那么对外就暴露了实现同步的细节--AbstractQueuedSynchronizer，以后想要通过修改实现同步的方式就变得不可能了，而通过私有内部类来时实现就完全隐藏了所有功能的实现细节，这种方式值得借鉴与学习。

---
## 3 使用示例

Callable、Future和FutureTask在Android中的实现和Java中的实习比较不一样，这里分析的是在Android中的实现，但是功能是一样的，所以在JVM上进行测试也是可以的。

```java
    public class Main {
    
        private static ExecutorService executorService = Executors.newFixedThreadPool(3);
        
        public static void main(String[] args) {
            
                testRunnable();
                testCallable();
                testFutureTask();
        }
    
        private static void testFutureTask() {
            FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable<Integer>() {
                public Integer call() throws Exception {
                    return fibc(20);
                }
            }){
                @Override
                protected void done() {
                    try {
                        Integer integer = get();
                        System.err.println("done---> futureTask "+integer);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            };
            
            
        executorService.submit(futureTask);
            
        }
    
        private static void testCallable() {
                Future<Integer> submit = executorService.submit(new Callable<Integer>() {
    
                    public Integer call() throws Exception {
                        
                        return fibc(20);
                    }
                });
                
                try {
                    System.err.println("Callable " +submit.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            
        }
    
        private static void testRunnable() {
            //提交任务得到了一个新的Future，用于对任务进行管理
            Future submit = executorService.submit(new Runnable() {
                
                public void run() {
                    fibc(20);
                }
            });
            
            try {
                System.err.println("Runnable "+submit.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    
        
        private static int fibc(int num){
            if(num == 0){
                return 0;
            }
            if(num == 1){
                return 1;
            }
            return fibc(num - 1)+ fibc(num - 2);
        }
        
        
    }
    
    
    最终的结果为：
    
    Runnable null
    Callable 6765
    done---> futureTask 6765
```












