# RxJava实现变换的方式

在RxJava的一个链式调用中可以做如下操作

- 线程切换
- 数据变换

而实现变换的方式无外乎两种：

- 使用lift方法，通过Operator返回的Subscriber包装原来的Subscriber来实现变换。
- 创建新的OnSunscribe，控制上游的OnSunscribe的订阅方式和向下发射数据的方式。


 lift也是基于OnSunscribe的实现的，只是lift方法中的OnSunscribe是固定不变的。所以OnSunscribe的实现才是变换的关键。

 subscribeOn的线程切换是在OnSunscribe中完成的，在OnSunscribe改变了继续往上游通知的订阅的线程

下面使用自定义的Observable来说明变换的原理：

```java
    public class CustomerObservable<T> extends Observable<T> {
    
        public CustomerObservable(OnSubscribe<T> f) {
            super(f);
        }
    
    
        public interface ThreadFactory {
            Thread createThread(Runnable runnable);
        }
    
    
        public Observable<T> customSubscribeOn(ThreadFactory threadFactory) {
    
            return create(new OnSubscribe<T>() {
                @Override
                public void call(Subscriber<? super T> subscriber) {
    
                    threadFactory.createThread(new Runnable() {
                        @Override
                        public void run() {
    
                            //创建一个Subscriber来保证原来的subscriber
                            Subscriber<T> s = new Subscriber<T>() {
                                @Override
                                public void onCompleted() {
                                    subscriber.onCompleted();
                                }
    
                                @Override
                                public void onError(Throwable e) {
                                    subscriber.onError(e);
                                }
    
                                @Override
                                public void onNext(T t) {
                                    subscriber.onNext(t);
                                }
                            };
    
                            //往上通知订阅的时候，改变了线程，所以可以指定Observable运行的线程
                            CustomerObservable.this.subscribe(s);
                        }
                    }).start();
    
                }
            });
        }
    
        public Observable<T> customLift(final Operator<? extends R, ? super T> operator) {
    
            return new CustomerObservable<T>(new OnSubscribe<T>() {
    
                @Override
                public void call(Subscriber<? super T> subscriber) {
                    //--->实际上是使用operator来创建新的Subscriber,这里投个懒，直接写死
    
                    //我们创建的Subscriber,用于向下转发数据，
                    Subscriber<T> chainSubscriber = new Subscriber<T>() {
                        //而在具体的实现中，我们就可以对数据进行变换，也可以对
                        //转发的线程做切换。
                        
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }
    
                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                        }
    
                        @Override
                        public void onNext(T t) {
                            subscriber.onNext(t);
                        }
                    };
    
                    chainSubscriber.onStart();
                    //使用上游的onSubscribe直接call(这里就是向上传递订阅)创建的chainSubscriber
                    onSubscribe.call(chainSubscriber);
    
                }
            });
        }
    }
```

上面customSubscribeOn和customLift方法是对SubscribeOn和customLift的简化版，主要用于说明变换的真实原理，相信搞懂了这些会对理解RxJava会更有帮助。








