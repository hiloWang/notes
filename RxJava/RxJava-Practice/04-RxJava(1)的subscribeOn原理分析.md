# subscribeOn 原理分析

通过 subscribeOn 可以切换 Observable 所运行的线程，平时在使用的过程中感到非常的好用，但是这个是怎么实现的呢？现在就来研究一下。

分析这个过程需要涉及的类有：

- Observable 被观察者
- OperatorSubscribeOn 继承自 OnSubscribe
- Worker 抽象工作者
- NewThreadWorker Worker的一个实现者

---
## 1 切换线程

```java
      Observable.just(1,2,3)
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        integer -> {
                            System.out.println(Thread.currentThread());
                        }
                );
```

打印结果为：`Thread[RxNewThreadScheduler-1,5,main]`

我们使用了subscribeOn让运行在了一个新的线程，下面的subscribeOn分析也使用Schedulers.newThread()进行说明，至于其他调度器原理都是一样的。

## 2 subscribeOn分析

```java
     public final Observable<T> subscribeOn(Scheduler scheduler) {
        if (this instanceof ScalarSynchronousObservable) {
            return ((ScalarSynchronousObservable<T>)this).scalarScheduleOn(scheduler);
        }
        //一般情况下都会走这里
        return create(new OperatorSubscribeOn<T>(this, scheduler));
    }
```

可以看到subscribeOn内部使用create有创建了一个新的Observable,而传入的是OperatorSubscribeOn，代码很简单，所以线程切换的操作应该在OperatorSubscribeOn中，继续分析 OperatorSubscribeOn的实现，不过在分析OperatorSubscribeOn之前需要先了解几个相关的类：

### Subscription

Subscription我们很熟悉，时对订阅者的取消订阅与状态判断的抽象

```java
    public interface Subscription {
        void unsubscribe();
        boolean isUnsubscribed();
    }
```

### Scheduler

Scheduler 调度者，一个工作调度单元，其内部只有一个重要的方法，那就是创建一个真正的工作者Worker。

```java
    public abstract class Scheduler {
        //
        public abstract Worker createWorker();
    }
```

### Worker

Worker是Scheduler的一个静态内部类，Worker实现了Subscription，但是并没有实现Subscription的两个方法。它本身也是一个抽象类

```java
     public abstract static class Worker implements Subscription {
        //胜省略其schedule相关方法
          public abstract Subscription schedule(Action0 action);
     }
```

Worker有一个schedule方法，接受一个Action0，进行异步调度，通过Schedulers.newThread()分析，其最终创建的是一个NewThreadWorker。其内部是新稍稍有点长，我们只分析我们关心的schedule方法。

```java
    public class NewThreadWorker extends Scheduler.Worker implements Subscription {
        private final ScheduledExecutorService executor;//一个调度执行服务
        ......省略一些代码
             @Override
            public Subscription schedule(final Action0 action) {
                 return schedule(action, 0, null);
            }

            @Override
            public Subscription schedule(final Action0 action, long delayTime, TimeUnit unit) {
                //判断是否已经取消订阅
                if (isUnsubscribed) {
                    return Subscriptions.unsubscribed();
                }
                return scheduleActual(action, delayTime, unit);
            }

            public ScheduledAction scheduleActual(final Action0 action, long delayTime, TimeUnit unit) {
                //schedulersHook返回的还是原来的action。
                Action0 decoratedAction = schedulersHook.onSchedule(action);
                //创建了ScheduledAction，用于包装decoratedAction。
                ScheduledAction run = new ScheduledAction(decoratedAction);

                Future<?> f;//Java中代表异步结果的Future
                if (delayTime <= 0) {//这里吧保证类提交给执行器
                    f = executor.submit(run);
                } else {
                    f = executor.schedule(run, delayTime, unit);
                }
                //把任务管理设置给ScheduledAction
                run.add(f);
                return run;//同时返回给了ScheduledAction
            }
    }
```

看一下ScheduledAction的实现,我们只关心重点，可以看到ScheduledAction实现了Runnable，在run方法中调用了action.call();

```java
    public final class ScheduledAction extends AtomicReference<Thread> implements Runnable, Subscription {
        public void run() {
        try {
            lazySet(Thread.currentThread());
            action.call();
        } catch (Throwable e) {
            ......
        } finally {
            unsubscribe();
        }
      }
    }
```

好了了解到这里我们就可以开始分析OperatorSubscribeOn的实现了

### OperatorSubscribeOn源码实现

首先OperatorSubscribeOn实现子OnSubscribe，而OnSubscribe中call方法定义的是但它的Observable被订阅时，应该执行的操作。

我们以这段代码来分析：

```java
        //创建一个sourceObservable，这是最上游的sourceObservable
         Observable<Integer> sourceObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                try {
                    subscriber.onNext(1);
                    subscriber.onNext(2);
                    subscriber.onNext(3);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
        //创建一个Subscriber，这是我们传入的Subscriber，有这个Subscriber最下游的Observable。及下面的threadObservable
        Subscriber<Integer> startSubscriber =new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {

            }
        };
        //通过Observable的subscribeOn变换，生成一个新的threadObservable
        Observable<Integer> threadObservable = sourceObservable.subscribeOn(Schedulers.newThread());

        //我们刚刚创建的startSubscriber订阅这个threadObservable
        Subscription startSubscription = threadObservable.subscribe(startSubscriber);
```

OperatorSubscribeOn源码：这里OperatorSubscribeOn的实例对应于threadObservable，即经过subscribeOn变换创建的Observable是与OperatorSubscribeOn对应的。

```java
public final class OperatorSubscribeOn<T> implements OnSubscribe<T> {

    final Scheduler scheduler;//异步调度器
    final Observable<T> source;//上游的Observable。

    public OperatorSubscribeOn(Observable<T> source, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.source = source;
    }

     //当threadObservable被订阅时，此处call方法就会执行
    @Override
    public void call(final Subscriber<? super T> subscriber) {
        //这里的subscriber及上述代码的threadObservable

        //使用scheduler创建一个工作者
        final Worker inner = scheduler.createWorker();

        //由于Worker实现了Subscription，所以他也是可以被反订阅的
        subscriber.add(inner);

        //这就创建了一个Action0，让工作者调用，即这里进行了异步调度。
        inner.schedule(new Action0() {
            @Override
            public void call() {
                //这里已经的是异步执行了。

                //1，记录当前一度调度线程，
                final Thread t = Thread.currentThread();

                //2，这里是重点，这创建了一个Subscriber，用于把数据转发给我们的subscriber

                Subscriber<T> s = new Subscriber<T>(subscriber) {
                    @Override
                    public void onNext(T t) {
                        subscriber.onNext(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            subscriber.onError(e);
                        } finally {
                            inner.unsubscribe();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        try {
                            subscriber.onCompleted();
                        } finally {
                            inner.unsubscribe();
                        }
                    }

                    //这里设置了生产者，也是转发给了我们传入的subscriber
                    @Override
                    public void setProducer(final Producer p) {
                        subscriber.setProducer(new Producer() {
                            @Override
                            public void request(final long n) {
                                if (t == Thread.currentThread()) {
                                    p.request(n);
                                } else {
                                    inner.schedule(new Action0() {
                                        @Override
                                        public void call() {
                                            p.request(n);
                                        }
                                    });
                                }
                            }
                        });
                    }
                };

                //3 使用刚刚创建的Subscriber订阅，最上游的source，这里订阅已经发生在了异步线程。
                source.unsafeSubscribe(s);
            }
        });
      }
}
```

最终 source 在异步线程被订阅，然后数据会流向call方法中创建的 Subscribers，然后s又把数据转发给了内部包装的subscriber，也就是我们传入的subscriber。转了一圈又回到了我们的subscriber，但是线程却切换了。