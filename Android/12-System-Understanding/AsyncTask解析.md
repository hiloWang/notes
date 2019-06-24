# AsyncTask

AsyncTask是安卓为简化子线程访问ui操作提供的一个线程类工具类，但是AsncTask在Android系统的不断升级中，经过了多次修改，导致不同的API版本上使用AsyncTask具有不同的表现，接下来主要分析AsyncTask的使用与注意事项。

AsyncTask是一个轻量级的一部任务类，它可以在子线程执行任务，然后把执行结果分发到主线程进行处理，原理上来讲，**AsyncTask是对线程池和Handler的封装**，而且AsyncTask **并不适合执行特别耗时的后台任务**，否则很有可能造成内存泄漏。

---
## AsyncTask使用

AsyncTask的定义如下：

```java
    public abstract class AsyncTask<Params, Progress, Result>{.....}
```

AsyncTask定义了三个泛型参数，如果不需要传递具体的参数可以使用Void代替，这三个泛型参数与AsyncTask定义的几个核心方法有关：

```java
    //主线程执行，异步任务执行前被调用   
    protected void onPreExecute() {
    }
    
    //工作线程执行，用于执行任务，方法参数由泛型参考Params确定，返回参数有Result参数规定
    protected abstract Result doInBackground(Params... params);
    //主线程执行，用于处理doInBackground的返回值
    oprotected void onPostExecute(Result result) {
    }
    
    //需要在doInBackground方法中调用，用于更新任务的执行进度，方法参数有Progress泛型参数规定
    protected final void publishProgress(Progress... values) {
    //在主线程被调用，当在doInBackground调用publishProgress分发任务进度是，此方法就会被调用
    protected void onProgressUpdate(Progress... values) {
    }
```

其次，AsyncTask还提供了onCancel方法，它同样在主线程执行，当调用AsyncTask的cancel方法时，任务被取消，onCancel被调用，而onPostExecute不会被调用

## AsyncTask的使用注意事项

1. AsyncTask的类必须在主线程被加载，这意味着第一次访问AsycnTask必须在主线程，当然这个过程在Android4.1以上版本系统已经自动完成，在android5.0的源码中ActivityThread的main方法中就掉用了AsyncTask.init()方法（具体为什么必须在主线程被加载，稍后分析）
2. AsyncTask对象必须在住下次被创建
3. execute方法必须在主线程被调用
4. 不要主动调用onPreExecute等方法
5. 一个AsyncTask只能被执行一次，否则会有异常
6. AsyncTask在不同系统版本的表现
    - **Android1.6之前**：AsyncTask是串行执行的
    - **Android1.6开始**：AsyncTask开始使用线程池处理并行任务，他是并行的
    - **Android3.0开始**：为了避免AsyncTask带来的并发错误，AsyncTask又开始使用单个线程来执行任务，但那时在3.0之后，可以使用AsyncTask的executeOnExecutor方法来并行的执行任务。
 
---
## AsyncTask的工作原理

了解AsyncTask的工作原理，先从它的execute方法开始分析：

```java
    public final AsyncTask<Params, Progress, Result> execute(Params... params) {
            return executeOnExecutor(sDefaultExecutor, params);
    }
    
     public final AsyncTask<Params, Progress, Result> executeOnExecutor(Executor exec,
                Params... params) {
            if (mStatus != Status.PENDING) {
                switch (mStatus) {
                    case RUNNING:
                        throw new IllegalStateException("Cannot execute task:"
                                + " the task is already running.");
                    case FINISHED:
                        throw new IllegalStateException("Cannot execute task:"
                                + " the task has already been executed "
                                + "(a task can be executed only once)");
                }
            }
            mStatus = Status.RUNNING;
            onPreExecute();
            mWorker.mParams = params;
            exec.execute(mFuture);
            return this;
        }
```

execute调用了executeOnExecutor方法，executeOnExecutor方法会把mFuture交给sDefaultExecutor去执行，看到这里，我们需要先了解一下executeOnExecutor中的几个变量：

- mStatus： 用于记录一个AsyncTask的运行状态
- mWorker：它的类型是WorkRunnbale在AsyncTask构造方法被创建，WorkRunnbale实现了Callable接口，mWorker作为WorkRunnbale的子类，实现了来自Callable的call方法，并在call方法中调用了doInBackground方法。
- mFuture：它的实际类型是FutureTask，是一个并发相关类，它的父类实现了Runnable和Future接口，它本身接收一个Callable类型对象，当它被提交给一个任务执行器是，它的run(来自Runnable)方法被调用，而在run中会调用Callable的call方法。


具体我们可以看AsyncTask的构造方法：

```java
    public AsyncTask() {
            mWorker = new WorkerRunnable<Params, Result>() {
                public Result call() throws Exception {
                    mTaskInvoked.set(true);
    
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    //noinspection unchecked
                    Result result = doInBackground(mParams);
                    Binder.flushPendingCommands();
                    return postResult(result);
                }
            };
    
            mFuture = new FutureTask<Result>(mWorker) {
                @Override
                protected void done() {
                    try {
                        postResultIfNotInvoked(get());
                    } catch (InterruptedException e) {
                        android.util.Log.w(LOG_TAG, e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException("An error occurred while executing doInBackground()",
                                e.getCause());
                    } catch (CancellationException e) {
                        postResultIfNotInvoked(null);
                    }
                }
            };
        }
```
    
可以看到mFuture接收的Callable对象正是mWorker。
    

接着分析executeOnExecutor方法，任务执行的参数params赋值给了mWorker.mParams，然后把mFuture叫给了sDefaultExecutor执行，下面来分析一些这个sDefaultExecutor：

```
    private static class SerialExecutor implements Executor {
            final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
            Runnable mActive;
    
            public synchronized void execute(final Runnable r) {
                mTasks.offer(new Runnable() {
                    public void run() {
                        try {
                            r.run();
                        } finally {
                            scheduleNext();
                        }
                    }
                });
                if (mActive == null) {
                    scheduleNext();
                }
            }
    
            protected synchronized void scheduleNext() {
                if ((mActive = mTasks.poll()) != null) {
                    THREAD_POOL_EXECUTOR.execute(mActive);
                }
            }
        }
```

sDefaultExecutor就是一个SerialExecutor，它内部维护着一个mTasks队列，用于存储进程内AsyncTask提交的任务，看SerialExecutor的execute方法，接收的mFuture并不是直接运行，而是封装成为一个Runnable插入到队列中去，然后判断mActive是否为null，如果mActive=null，则会调用scheduleNext取出一个任务交给THREAD_POOL_EXECUTOR去真的执行任务，只有当一个任务执行完毕，才会再一次调用scheduleNext方法，由此分析AsyncTask确实是串行执行任务的。

可见AsyncTask有两个线程池：

- SerialExecutor 用于对任务进行串行调度，而不是真的执行任务
- THREAD_POOL_EXECUTOR真正的任务执行器，

上面分析到当mFutrue被提交到线程池中，他的run方法被调用，run方法调用Callable发call，这里对应的就是mWorker的call方法：

```java
     mWorker = new WorkerRunnable<Params, Result>() {
                    public Result call() throws Exception {
                        mTaskInvoked.set(true);
        
                        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                        //noinspection unchecked
                        Result result = doInBackground(mParams);
                        Binder.flushPendingCommands();
                        return postResult(result);
                    }
                };
```

在call方法中，调用了AsyncTask的doInBackground方法，然后调用postResult(result)方法分发执行结果：

```java
        private Result postResult(Result result) {
            @SuppressWarnings("unchecked")
            Message message = getHandler().obtainMessage(MESSAGE_POST_RESULT,
                    new AsyncTaskResult<Result>(this, result));
            message.sendToTarget();
            return result;
        }
```

这里调用getHandler()获取的Handler发送了消息，消息中封装了任务的执行结果。先来看一下 `getHandler()`：

```java
        private static Handler getHandler() {
            synchronized (AsyncTask.class) {
                if (sHandler == null) {
                    sHandler = new InternalHandler();
                }
                return sHandler;
            }
        }
        
     private static class InternalHandler extends Handler {
            public InternalHandler() {
                super(Looper.getMainLooper());
            }
    
            @SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
            @Override
            public void handleMessage(Message msg) {
                AsyncTaskResult<?> result = (AsyncTaskResult<?>) msg.obj;
                switch (msg.what) {
                    case MESSAGE_POST_RESULT:
                        // There is only one result
                        result.mTask.finish(result.mData[0]);
                        break;
                    case MESSAGE_POST_PROGRESS:
                        result.mTask.onProgressUpdate(result.mData);
                        break;
                }
            }
        }
```

它是 Handler 的子类，在调用 `getHandler()` 方法如果没有初始化，则它会被初始化，**这里也说明了为什么AsyncTask必须在主线程调用的原因**。

postResult方法把执行结果封装成AsyncTaskResult对象，然后发送了一个 MESSAGE_POST_RESULT 类型的消息，很显然这个消息会在 InternalHandler 中被处理，处理代码如下：

```java
    result.mTask.finish(result.mData[0]);
```

AsyncTask的finish被调用：

```java
        private void finish(Result result) {
            if (isCancelled()) {
                onCancelled(result);
            } else {
                onPostExecute(result);
            }
            mStatus = Status.FINISHED;
        }
```

方法很简单，如果任务被取消，则调用onCancelled方法，否则调用onPostExecute方法，最后把任务的状态置为FINISHED。


## AsyncTask的取消

```java
        public final boolean cancel(boolean mayInterruptIfRunning) {
            mCancelled.set(true);
            return mFuture.cancel(mayInterruptIfRunning);
        }
```
    
mCancelled一个保证原子操作的并发类，mCancelled被设置为true，而AsyncTask中使用到它的就是isCancelled方法，而isCancelled方法被调用的地方有

```
    finish方法
    publishProgress方法
```

可见mCancelled的作用只是任务取消的时候onPostExecute和onProgressUpdate被调用，所以关在在于mFuture.cancel(mayInterruptIfRunning)方法，其实它的内部也只是调用了interrupt方法，而我们知道这个interrupt用来停止线程并不可靠，也就是说AsyncTask的cancel方法内部并不能真正的让任务停止执行。


## 关于AsyncTask带来的内存泄漏

一般AsyncTask作用一个内部类被创建并调用，而内部类持有外部类的引用，让AsyncTask去执行一个长时间任务时，这是即使退出当前界面，调用他的cancel方法，并不见得就可以立即停止
AsyncTask所执行的任务，只要任务在执行AsyncTask对象就不会被回收，而AsyncTask所持有的外部类应用也肯定不会被回收，所以很有可能就造成了内存泄漏，在Android中，AsyncTask也只被推荐用于短耗时的异步任务。

## AsyncTask的任务承载量与兼容

为了解决系统版本的差异性，在Support V4包中提供了AsyncTaskCompat类，实现如下：

```java
    public class AsyncTaskCompat {
    
        public static <Params, Progress, Result> AsyncTask<Params, Progress, Result> executeParallel(
                AsyncTask<Params, Progress, Result> task,
                Params... params) {
            if (task == null) {
                throw new IllegalArgumentException("task can not be null");
            }
    
            if (Build.VERSION.SDK_INT >= 11) {
                // From API 11 onwards, we need to manually select the THREAD_POOL_EXECUTOR
                AsyncTaskCompatHoneycomb.executeParallel(task, params);
            } else {
                // Before API 11, all tasks were run in parallel
                task.execute(params);
            }
    
            return task;
        }
    }
    
        class AsyncTaskCompatHoneycomb {
    
        static <Params, Progress, Result> void executeParallel(
                AsyncTask<Params, Progress, Result> task,
                Params... params) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        }
    
    }
```

主要为了实现AsyncTask的在个版本中的并行执行，所以直接把AsyncTask的任务交给THREAD_POOL_EXECUTOR执行。

而THREAD_POOL_EXECUTOR的实现如下，它就是一个ThreadPoolExecutor。

```java
        public static final Executor THREAD_POOL_EXECUTOR
                = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                        TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
    
        private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
        private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
        private static final int KEEP_ALIVE = 1;
        private static final BlockingQueue<Runnable> sPoolWorkQueue =
                new LinkedBlockingQueue<Runnable>(128);
```

ThreadPoolExecutor所能承载任务数是有限的，它能承载的任务数为sPoolWorkQueue的size+MAXIMUM_POOL_SIZE,如果踢提交的任务数超过了这个数，默认ThreadPoolExecutor会抛出异常，所以在使用的时候一定要注意。

对于任务超载，ThreadPoolExecutor提供了一个方法用时实现超载时候的任务处理策略

```java
      executor.setRejectedExecutionHandler();
```








