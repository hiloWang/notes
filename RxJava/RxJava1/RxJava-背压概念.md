# BackPressure

---
## 1 BackPressure概念

Rx 中的数据流是从一个地方发射到另外一个地方。每个地方处理数据的速度是不一样的。如果生产者发射数据的速度比消费者处理的快会出现什么情况？

当消费者与生产者处于同一个线程时，生产者会等待消费者处理完一个数据后发射之后的数据，但是在消费者与生产者不在同一个线程时会如何？

```java
    Observable.interval(1, TimeUnit.MILLISECONDS)
                    .observeOn(Schedulers.newThread())
                    .subscribe(new Subscriber<Long>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError() called with: e = [" + e + "]");
                        }

                        @Override
                        public void onNext(Long aLong) {
                                Thread.sleep(100);
                                Log.d(TAG, "onNext() called with: aLong = [" + aLong + "]");
                        }
                    });
```

运行结果为：


    onNext() called with: aLong = [0]
    onError() called with: e = [rx.exceptions.MissingBackpressureException]

可以看到RxJava抛出了MissingBackpressureException异常。

---
## 2 原因

在一个响应式流中，如果存在线程切换操作，就编程了一个典型的生成者消费者模型，线程间通过队列来传递数据。

在RxJava中，只要生产者数据好了就发射出去了。如果生产者比较慢，则消费者就会等待新的数据到来。如果生产者快，则就会有很多数据发射给消费者，而不管消费者当前有没有能力处理数据，当消费者来不及处理生产者发射的数时，就会产生MissingBackpressureException异常。

---
## 3 解决方法

- sample
- throttle/debounce
- onBackpressureBuffer 缓存数据，如果指定了缓存数量，还是有可能抛出异常
- onBackpressureDrop 丢弃来不及处理的数据
- 自定义Subscribe，在request中处理

具体参考[RxJava 教程第四部分：并发之数据流发射太快如何办](http://blog.chengyunfeng.com/?p=981#ixzz4FFYULgFu)








