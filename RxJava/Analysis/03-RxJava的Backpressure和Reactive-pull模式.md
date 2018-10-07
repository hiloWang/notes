# RxJava的Backpressure和Reactive pull模式

---
##  1 Backpressure

RxJava基于观察者模式，Observable是数据源，也就是被观察者，而Subscriber是观察者，默认的Observable只有Subscriber对其进行订阅时它才会开始发射数据，数据从Observable流向Subscriber，也可以说Observable是生产者，负责产生事件，而Subscriber是消费者，负责处理事件。

但是在Rx中，只要生产者数据好了就发射出去了。如果生产者比较慢，则消费者就会等待新的数据到来。如果生产者快，则就会有很多数据发射给消费者，而不管消费者当前有没有能力处理数据。这样会出现什么样的情况呢？

### 同步的情况

当Observable与Subscriber处于同一线程，由于是同步调用，所以Observable会阻塞Sunscriber处理完一个事件后再发射下一个事件，并不会有什么问题。

### 异步的情况

当Observable与Subscriber处于不同线程会导致一个异常产生，例如：

```java
    Observable.interval(1, TimeUnit.MILLISECONDS)
                    .observeOn(Schedulers.newThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
```

结果：程序运行抛出 MissingBackpressureException，这就是**背压（Backpressure）**，即事件产生的速度比消费快（在 producer-consumer(生产者-消费者) 模式中）。发生 overproucing 后，当链式结构不能承受数据压力时，就会抛出 `MissingBackpressureException` 异常。试想一下，线程A不可能无阻塞的把多个数据同步传递给另一个线程B，所以这种情况只有在线程A中使用容器存储数据，而后调用线程B一个一个的去处理数据，当线程B处理数据太慢，而线程A中容器已满，就会出现了背压。

---
## 2 Reactive pull

RxJava 实现了一种通过 Subscriber 来通知 Observable 发射数据的方式。

在creat方法中我们手动实现了OnSubscribe，Subscriber的onNext，onError，onCompleted方法都是由我们自己调用实现的，这很清晰，但是其他创建型操作符是如何实现的呢？，比如 from：

### from

来分析一下from的实现：

```java
    public static <T> Observable<T> from(T[] array) {
             .......
            //假设array的leng超过1个
            return create(new OnSubscribeFromArray<T>(array));
    }
```

这里创建了一个OnSubscribeFromArray，OnSubscribeFromArray的如下，代码很简单。

```java
    public final class OnSubscribeFromArray<T> implements OnSubscribe<T> {
        final T[] array;
        public OnSubscribeFromArray(T[] array) {
            this.array = array;
        }
        ......
    }
```

当订阅发生是，肯定是调用OnSubscribeFromArray的call方法：

```java
      @Override
        public void call(Subscriber<? super T> child) {
            child.setProducer(new FromArrayProducer<T>(child, array));
        }
```

call方法中调用了订阅者的setProducer方法，Subscribe的setProducer接受一个Producer类型的参数

```java
    public interface Producer {
        void request(long n);
    }
```

Subscriber的setProducer方法实现如下：

```java
     public void setProducer(Producer p) {
            long toRequest;
            boolean passToSubscriber = false;
            synchronized (this) {
                toRequest = requested;//默认的requested为NOT_SET
                producer = p;
                if (subscriber != null) {//内部的subscriber默认为null
                    if (toRequest == NOT_SET) {
                        passToSubscriber = true;
                    }
                }
            }
            if (passToSubscriber) {
                subscriber.setProducer(producer);
            } else {
                if (toRequest == NOT_SET) {//所以一般函数会走这里
                    producer.request(Long.MAX_VALUE);
                } else {
                    producer.request(toRequest);
                }
            }
        }
```

实现了Producer的ProducerFromArray

```
      static final class FromArrayProducer<T> extends AtomicLong implements Producer {......}
```

其request方法实现如下：

```java
       public void request(long n) {
                //从刚刚分析可以n为Long.MAX_VALUE
                if (n < 0) {
                    throw new IllegalArgumentException("n >= 0 required but it was " + n);
                }

                if (n == Long.MAX_VALUE) {//所以走这里
                    if (BackpressureUtils.getAndAddRequest(this, n) == 0) {
                        fastPath();
                    }
                } else if (n != 0) {
                    if (BackpressureUtils.getAndAddRequest(this, n) == 0) {
                        slowPath(n);
                    }
                }
            }
```

由于默认request方法的参数n为Long.MAX_VALUE，所以调用的是 `fastPath()` 方法。

```java
     void fastPath() {
                final Subscriber<? super T> child = this.child;
                for (T t : array) {
                    if (child.isUnsubscribed()) {
                        return;
                    }
                    child.onNext(t);
                }
                if (child.isUnsubscribed()) {
                    return;
                }
                child.onCompleted();
            }
```

在fastPath方法把数据发射给Subscribe，逻辑很简单。至此，from方法的实现就分析完了。

### Producer

上面分析过程中提到了Producer，其实Producer作用是在Observable和Subscriber之间建立一个请求信道的接口，并允许Subscriber调用 `request(n)` 方法来向Observable请求n个数据。

刚刚分析from中可以看出 `request(n)` 的 n 默认是Long.MAX_VALUE，也就是说，默认Subscriberu请求Observable的所有数据，但是我们可以改变这种行为。

Subscriber有个函数 `request(n)` 调用该函数用来通知Observable现在Subscriber准备接受下面n个数据：

```java
    public abstract class Subscriber<T> implements Observer<T>, Subscription {
    
        private Producer producer;
        private long requested = NOT_SET; // default to not set
    
        ......
    
           protected final void request(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("number requested cannot be negative: " + n);
            }
            
            // if producer is set then we will request from it
            // otherwise we increase the requested count by n
            Producer producerToRequestFrom = null;
            synchronized (this) {
                if (producer != null) {
                    producerToRequestFrom = producer;
                } else {
                    addToRequested(n);
                    return;
                }
            }
            // after releasing lock (we should not make requests holding a lock)
            producerToRequestFrom.request(n);
        }
    ......
                      }
```

在onStart方法中调用request方法(这时还没有设置producer)，requested中的值就会被改变，requested则表示当前 Subscriber 的处理能力。


它会影响刚刚我们分析的setProducer方法的逻辑

```java
               if (toRequest == NOT_SET) {
                    producer.request(Long.MAX_VALUE);
                } else {
                  //如果设置了requested，则走这里
                    producer.request(toRequest);
                }
```

再回到刚刚我们分析的FromArrayProducer会执行slowPath方法：

```java
    void slowPath(long r) {
                final Subscriber<? super T> child = this.child;
                final T[] array = this.array;
                final int n = array.length;
                
                long e = 0L;
                int i = index;
    
                for (;;) {
                    
                    while (r != 0L && i != n) {
                        if (child.isUnsubscribed()) {
                            return;
                        }
                        
                        child.onNext(array[i]);
                        
                        i++;
                        
                        if (i == n) {
                            if (!child.isUnsubscribed()) {
                                child.onCompleted();
                            }
                            return;
                        }
                        
                        r--;
                        e--;
                    }
                    
                    r = get() + e;
                    
                    if (r == 0L) {
                        index = i;
                        r = addAndGet(e);
                        if (r == 0L) {
                            return;
                        }
                        e = 0L;
                    }
                }
            }
        }
```

从方法逻辑可看出，它会根据Subscriber的请求数量来发射指定数量的数据。所以我们可以改变from的默认行为：

```java
      class CustomerSubscriber extends Subscriber<Integer> {
    
                @Override
                public void onStart() {
                    super.onStart();
                    request(0);
                }
    
                @Override
                public void onCompleted() {
                    System.out.println("onCompleted");
                }
    
                @Override
                public void onError(Throwable e) {
                    System.out.println(e);
                }
    
                @Override
                public void onNext(Integer integer) {
                    System.out.println(integer);
                }
    
                public void requestMore(int n) {
                    request(n);
                }
            }
    
            CustomerSubscriber customerSubscriber = new CustomerSubscriber();
            Observable.from(Arrays.asList(
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10
            ))
            .subscribe(customerSubscriber);
    
            customerSubscriber.requestMore(3);
            customerSubscriber.requestMore(3);
            customerSubscriber.requestMore(3);
```

>request的数量是累加的，既三次requestMore(3)=requestMore(9).

###  doOnRequested

当Subscriber请求更多的时候的时候，doOnRequest 就会被调用。参数中的值为请求的数量。

```java
    Observable.range(1, 30)
                    .doOnRequest(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            System.out.println("along:" + aLong);
                        }
                    })
                    .subscribe();
    
    执行结果：along:9223372036854775807
    
    Observable.range(1, 30)
                    .doOnRequest(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            System.out.println("along:" + aLong);
                        }
                    })
                    .subscribe(new Subscriber<Integer>() {
    
                        @Override
                        public void onStart() {
                            super.onStart();
                            request(2);
                        }
                          .......
                    );
                    
    执行结果：along:2
```

其内部实现比较简单。

```java
    public class OperatorDoOnRequest<T> implements Operator<T, T> {
        final Action1<Long> request;
        public OperatorDoOnRequest(Action1<Long> request) {
            this.request = request;
        }

        @Override
        public Subscriber<? super T> call(final Subscriber<? super T> child) {
            final ParentSubscriber<T> parent = new ParentSubscriber<T>(child);
            child.setProducer(new Producer() {
    
                @Override
                public void request(long n) {
                    request.call(n);//这里通知request数量
                    parent.requestMore(n);//通知
                }
            });
            child.add(parent);
            return parent;
        }
    
        private static final class ParentSubscriber<T> extends Subscriber<T> {
            private final Subscriber<? super T> child;
            ParentSubscriber(Subscriber<? super T> child) {
                this.child = child;
                this.request(0);
            }
            private void requestMore(long n) {
                request(n);
            }
            @Override
            public void onCompleted() {
                child.onCompleted();
            }
            @Override
            public void onError(Throwable e) {
                child.onError(e);
            }
            @Override
            public void onNext(T t) {
                child.onNext(t);
            }
        }
    }
```

### create方法

单纯的在create中使用request并没有作用，因为我们自己写的OnSubscribe没有对request做任何处理。

```java
    Observable.create(
                    new Observable.OnSubscribe<Integer>() {
                        @Override
                        public void call(Subscriber<? super Integer> subscriber) {
                            if (subscriber.isUnsubscribed()) {
                                return;
                            }
                            try {
                                subscriber.onNext(1);
                                subscriber.onNext(2);
                                subscriber.onNext(3);
                                subscriber.onNext(4);
                                subscriber.onNext(5);
                                subscriber.onNext(6);
                                subscriber.onCompleted();
                            } catch (Exception e) {
                                subscriber.onError(e);
                            }
                        }
                    })
                    .map(String::valueOf)
                    .doOnRequest(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            System.out.println("doOnRequest "+aLong);
                        }
                    })
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            request(2);//并没有实现request
                        }
    
                        @Override
                        public void onCompleted() {
    
                        }
    
                        @Override
                        public void onError(Throwable e) {
    
                        }
    
                        @Override
                        public void onNext(String s) {
                            System.out.println(s);
                        }
                    });
```

结果是：

```
    //打印
    doOnRequest 2
    1
    2
    3
    4
    5
    6
```

如果加上observeOn就不一样了：

```java
     Observable.create(
                    new Observable.OnSubscribe<Integer>() {
                        @Override
                        public void call(Subscriber<? super Integer> subscriber) {
                            if (subscriber.isUnsubscribed()) {
                                return;
                            }
                            try {
                                subscriber.onNext(1);
                                subscriber.onNext(2);
                                subscriber.onNext(3);
                                subscriber.onNext(4);
                                subscriber.onNext(5);
                                subscriber.onNext(6);
                                subscriber.onCompleted();
                            } catch (Exception e) {
                                subscriber.onError(e);
                            }
                        }
                    })
                    .map(String::valueOf)
                    .doOnRequest(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            System.out.println("doOnRequest "+aLong);
                        }
                    })
                    .observeOn(Schedulers.newThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            request(2);//并没有实现request
                        }
    
                        @Override
                        public void onCompleted() {
    
                        }
    
                        @Override
                        public void onError(Throwable e) {
    
                        }
    
                        @Override
                        public void onNext(String s) {
                            System.out.println(s);
                        }
                    });
```

结果：

    //doOnRequest 128 是observeOn请求的，
    1
    2

    doOnRequest 2 是我们告诉observeOn返回的Observable我们先要两个数据，此时程序还没有退出，还在等待request。


可以看出observeOn默认使用了128大小的容器缓冲数据，由于我们调用了request(2)，改变了请求的数据量，所以这里只会发射两个数据。

下面代码必然会造成MissingBackpressureException，因为observeOn只能暂存128个元素，而我们发射了129个。

```java
     Observable.create(
                    new Observable.OnSubscribe<Integer>() {
                        @Override
                        public void call(Subscriber<? super Integer> subscriber) {
                            if (subscriber.isUnsubscribed()) {
                                return;
                            }
                            try {
                                for (int i = 0; i < 129; i++) {
                                    subscriber.onNext(i);
                                }
                                subscriber.onCompleted();
                            } catch (Exception e) {
                                subscriber.onError(e);
                            }
                        }
                    })
                    .observeOn(Schedulers.newThread())
```

---
## 3  Backpressure 策略

Rx 操作函数内部使用队列和缓冲来实现 backpressure ，从而避免保存无限量的数据。大量数据的缓冲应该使用专门的操作函数来处理，例如：cache、buffer 等。 zip 函数就是一个示例，第一个 Observable 可能在第二个 Observable 发射数据之前就发射了一个或者多个数据。所以 zip 需要一个较小的缓冲来匹配两个 Observable，从而避免操作失败。因此， zip 内部使用了一个 128 个数据的小缓冲。

```java
      Observable.interval(1, TimeUnit.SECONDS).take(30)
                    .doOnRequest(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            System.out.println(aLong);
                        }
                    })
                    .zipWith(Observable.interval(300, TimeUnit.MILLISECONDS).take(20), new Func2<Long, Long, String>() {
                        @Override
                        public String call(Long aLong, Long aLong2) {
    
                            return String.valueOf(aLong).concat(String.valueOf(aLong2));
                        }
    
                    })
                    .subscribe();
```

结果：128


很多 Rx 操作函数内部都使用了 backpressure 从而避免过多的数据填满内部的队列。这样处理慢的消费者就会把这种情况传递给前面的消费者，前面的消费者开始缓冲数据直到他也缓存满为止再告诉他前面的消费者。Backpressure 并没有消除这种情况。只是让错误延迟发生，我们还是需要处理这种情况。

Rx 中有操作函数可以用来处理这种消费者处理不过来的情况。

### onBackpressureBuffer

onBackpressureBuffer 会缓存所有当前无法消费的数据，直到 Observer 可以处理为止。

![](index_files/onBackpressureBuffer.png)


可以指定缓冲的数量，如果缓冲满了则会导致数据流失败。

```java
       Observable.interval(1, TimeUnit.MILLISECONDS)
                    .onBackpressureBuffer(100)
                    .observeOn(Schedulers.newThread())
                    .subscribe(new Subscriber<Long>() {
                        @Override
                        public void onCompleted() {
    
                        }
    
                        @Override
                        public void onError(Throwable e) {
                            System.out.println(e);
                        }
    
                        @Override
                        public void onNext(Long aLong) {
                            System.out.println(aLong);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
```

执行结果：

    0
    1
    2
    rx.exceptions.MissingBackpressureException: Overflowed buffer of 100

### onBackpressureDrop

如果消费者无法处理数据，则 onBackpressureDrop 就把该数据丢弃了。

![](index_files/onBackpressureDrop.png)

```java
          Observable.interval(1, TimeUnit.MILLISECONDS)
                    .onBackpressureDrop()
                    .observeOn(Schedulers.newThread())
                    .subscribe(new Subscriber<Long>() {
                        @Override
                        public void onCompleted() {
    
                        }
    
                        @Override
                        public void onError(Throwable e) {
                            System.out.println(e);
                        }
    
                        @Override
                        public void onNext(Long aLong) {
                            System.out.println(aLong);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
```

执行结果

    1
    ....
    127
    12843

前面 128 个数据正常的被处理的，这是应为 observeOn 在切换线程的时候， 使用了一个 128 个数据的小缓冲。







