# RxJava 的 observeOn 分析

使用observeOn可以很轻易的切换观察者的执行线程，但是这个是如何实现的呢？下面通过跟随RxJava的源码来分析。

## 1 线程切换

```java
      Observable.just(1, 2, 3)
                 .observeOn(Schedulers.io())
                 .subscribe(integer -> {
                     System.out.println(Thread.currentThread().getName());
                 });
```

 打印结果为：

     RxCachedThreadScheduler-1
     RxCachedThreadScheduler-1
     RxCachedThreadScheduler-1

通过这段代码，就改变了订阅者的执行线程，下面来分析一下observeOn的实现原理

## 2 observeOn的实现

observeOn 的源码实现如下：

```java
        public final Observable<T> observeOn(Scheduler scheduler) {
         if (this instanceof ScalarSynchronousObservable) {
             return ((ScalarSynchronousObservable<T>)this).scalarScheduleOn(scheduler);
         }
         //一般会走这里
         return lift(new OperatorObserveOn<T>(scheduler, false));
     }
```

可以看到 observeOn 内部使用了 lift 变换，此处的操作符实现是 OperatorObserveOn 是，lift 的原理已经学习过，其影响的阶段是在数据发射阶段，下面来看一下 OperatorObserveOn 的实现：

代码不是很长，所以直接贴出全部代码分析；

```java
    public final class OperatorObserveOn<T> implements Operator<T, T> {

     private final Scheduler scheduler;//调度器，用于创建Workder
     private final boolean delayError;//是否延迟错误

     public OperatorObserveOn(Scheduler scheduler, boolean delayError) {
         this.scheduler = scheduler;
         this.delayError = delayError;
     }

    @Override
     public Subscriber<? super T> call(Subscriber<? super T> child) {
         //这里做了判断，如果调度线程是ImmediateScheduler或者TrampolineScheduler，那么直接返回原始的Subscriber
         if (scheduler instanceof ImmediateScheduler) {
             // avoid overhead, execute directly
             return child;
         } else if (scheduler instanceof TrampolineScheduler) {
             // avoid overhead, execute directly
             return child;
         } else {
             //否则就会使用ObserveOnSubscriber来包装原来的Subscriber
             ObserveOnSubscriber<T> parent = new ObserveOnSubscriber<T>(scheduler, child, delayError);
             parent.init();
             return parent;
         }
     }

     /** 这里是ObserveOnSubscriber的实现，ObserveOnSubscribe继承了ObserveOnSubscribe，并且是新了Action0 */
     private static final class ObserveOnSubscriber<T> extends ObserveOnSubscribe<T> implements Action0 {

        final Subscriber<? super T> child;//原来的Subscriber
         final Scheduler.Worker recursiveScheduler;//指定的调度器工作者
         final NotificationLite<T> on;
         final boolean delayError;//是否延迟通知错误，如果上游在通知了错误，是否等待下游处理完ObserveOn暂存的所有数据项后再发射错误。
         final Queue<Object> queue;//暂存数据的队列

         volatile boolean finished;//当前序列的状态

        final AtomicLong requested = new AtomicLong();//请求的数量
         final AtomicLong counter = new AtomicLong();//请求的计数器

         /** 
          * The single exception if not null, should be written before setting finished (release) and read after
          * reading finished (acquire).
          */
         Throwable error;//接受上游通知的错误

        // do NOT pass the Subscriber through to couple the subscription chain ... unsubscribing on the parent should
         // not prevent anything downstream from consuming, which will happen if the Subscription is chained
         public ObserveOnSubscriber(Scheduler scheduler, Subscriber<? super T> child, boolean delayError) {
             this.child = child;
             this.recursiveScheduler = scheduler.createWorker();
             this.delayError = delayError;
             this.on = NotificationLite.instance();
             if (UnsafeAccess.isUnsafeAvailable()) {
                 queue = new SpscArrayQueue<Object>(RxRingBuffer.SIZE);
             } else {
                 queue = new SpscAtomicArrayQueue<Object>(RxRingBuffer.SIZE);
             }
         }
         //被OperatorObserveOn创建后，就会立即被调用
         void init() {
             // don't want this code in the constructor because `this` can escape through the 
             // setProducer call
             Subscriber<? super T> localChild = child;

             localChild.setProducer(new Producer() {

                @Override
                 public void request(long n) {
                     if (n > 0L) {
                         BackpressureUtils.getAndAddRequest(requested, n);
                         schedule();//在request中调用schedule方法
                     }
                 }

            });
             //管理所有的可以被反订阅的对象。
             localChild.add(recursiveScheduler);
             localChild.add(this);
         }

        @Override
         public void onStart() {
             // signal that this is an async operator capable of receiving this many
             request(RxRingBuffer.SIZE);
         }

        @Override
         public void onNext(final T t) {
             if (isUnsubscribed() || finished) {
                 return;
             }
             if (!queue.offer(on.next(t))) {//队列满了还要放入数据就会抛出MissingBackpressureException
                 onError(new MissingBackpressureException());
                 return;
             }
             schedule();
         }

        @Override
         public void onCompleted() {
             if (isUnsubscribed() || finished) {
                 return;
             }
             finished = true;
             schedule();
         }

        //记住错误，并更新序列的状态
         @Override
         public void onError(final Throwable e) {
             if (isUnsubscribed() || finished) {
                 RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                 return;
             }
             error = e;
             finished = true;
             schedule();
         }

        //在schedule方法中，调用recursiveScheduler的schedule方法，开启子线程，这里的schedule调度就会调用当前ObserveOnSubscriber的call方法
         protected void schedule() {
             //这里的逻辑是为了保证：下面的call在同一个时刻只被一个线程调度
             if (counter.getAndIncrement() == 0) {//这里加1，在call方法中的每次循环都会减1
                 recursiveScheduler.schedule(this);
             }
         }

        // only execute this from schedule()
         //call方法只被Workder的schedule方法调用，即call运行在异步线程
         @Override
         public void call() {
             long emitted = 0L;

            long missed = 1L;
             final Queue<Object> q = this.queue;
             final Subscriber<? super T> localChild = this.child;
             final NotificationLite<T> localOn = this.on;

             for (;;) {
                 //坚持是否终止
                 if (checkTerminated(finished, q.isEmpty(), localChild, q)) {
                     return;
                 }

                long requestAmount = requested.get();
                 boolean unbounded = requestAmount == Long.MAX_VALUE;
                 long currentEmission = 0L;

                 while (requestAmount != 0L) {
                     boolean done = finished;
                     Object v = q.poll();//去除数据
                     boolean empty = v == null;

                     if (checkTerminated(done, empty, localChild, q)) {//坚持是否终止
                         return;
                     }

                     if (empty) {//没有了数据，但是没有终止则跳槽while循环
                         break;
                     }

                     localChild.onNext(localOn.getValue(v));//否则把数据传递给包装的Subscriber
                     //数量统计
                     requestAmount--;
                     currentEmission--;
                     emitted++;
                 }

                 if (currentEmission != 0L && !unbounded) {
                     requested.addAndGet(currentEmission);
                 }

                 missed = counter.addAndGet(-missed);//循环完一次就减去1
                 if (missed == 0L) {
                     break;
                 }
             }

             if (emitted != 0L) {
                 request(emitted);
             }
         }

         //判断是否终止序列
         boolean checkTerminated(boolean done, boolean isEmpty, Subscriber<? super T> a, Queue<Object> q) {
             if (a.isUnsubscribed()) {//已经终止就情况暂存的数据，返回
                 q.clear();
                 return true;
             }

             if (done) {//如果已经完成了，就通知包装的Subscriber
                 if (delayError) {//如果延迟发射错误，先判断queue中是否已经为空
                     if (isEmpty) {
                         Throwable e = error;
                         try {
                             if (e != null) {
                                 a.onError(e);
                             } else {
                                 a.onCompleted();
                             }
                         } finally {
                             recursiveScheduler.unsubscribe();
                         }
                     }
                 } else {//否则判断只有上游通知了错误就理解通知错误

                     Throwable e = error;
                     if (e != null) {
                         q.clear();
                         try {
                             a.onError(e);
                         } finally {
                             recursiveScheduler.unsubscribe();
                         }
                         return true;
                     } else
                     if (isEmpty) {
                         try {
                             a.onCompleted();
                         } finally {
                             recursiveScheduler.unsubscribe();
                         }
                         return true;
                     }
                 }

             }

             return false;
         }
      }
    }
```

ObserveOn的逻辑梳理如下：

- 使用 lift+ OperatorObserveOn 操作符实现
- 由OperatorObserveOn操作符创建的Subscriber包装原来的传入的Subscriber，然后在转发数据的时候使用了异步转发，从而实现了发射数据的异步切换。

### 具体实现的细节

ObserveOnSubscriber：由 OperatorObserveOn 操作符实现创建并返回的 Subscriber，由于是异步转发，不可能实现无阻塞的同步发射，所以需要用队列Queue来暂存数据

```java
    final Queue<Object> queue
```

ObserveOn首先会请求128条数据，多余的数据项由上游保存，这里的128是告诉上游你先给我128条数据，然后在你能力范围内保存数据，最好如果上游也无法保存了，就只能继续把数据发射给ObserverOn创建的Subscribe，如果此时队列已满，就会抛出 MissingBackpressureException 异常，前面学习的背压的就是这个意思了。

```java
            public void onStart() {
            //RxRingBuffer.SIZE = 128
                request(RxRingBuffer.SIZE);
            }

            public void onNext(final T t) {
                if (isUnsubscribed() || finished) {
                    return;
                }
                //如果放入了就抛出异常
                if (!queue.offer(on.next(t))) {
                    onError(new MissingBackpressureException());
                    return;
                }
                schedule();
            }
```

delayError可以延迟发射错误，一般情况下，如果上游通知了序列发生了错误，应该立即把错误转发个下游，但是设置delayError为true的话，ObserverOn实现的Subscriber会先把暂存的所有数据转发给下游之后，在发射错误：

```java
    public void onError(final Throwable e) {
                if (isUnsubscribed() || finished) {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                    return;
                }
                error = e;
                finished = true;
                schedule();
            }

                      if (delayError) {//如果delayError
                        if (isEmpty) {//当队列为没有元素之后才会通知
                            Throwable e = error;
                            try {
                                if (e != null) {
                                    a.onError(e);
                                } else {
                                    a.onCompleted();
                                }
                            } finally {
                                recursiveScheduler.unsubscribe();
                            }
                        }
                    } else {//否则只有发生了错误，就理解发射给下游
                        Throwable e = error;
                        if (e != null) {
                            q.clear();
                            try {
                                a.onError(e);
                            } finally {
                                recursiveScheduler.unsubscribe();
                            }
                            return true;
                        }

包装call方法在同一个时刻只被一个线程调度的技巧：

               //这里加1，在call方法中的每次循环都会减1
                if (counter.getAndIncrement() == 0) {
                    recursiveScheduler.schedule(this);
                }
                //call方法中
                 missed = counter.addAndGet(-missed);//循环完一次就减去1
```

ObserveOn的实现分析完毕。