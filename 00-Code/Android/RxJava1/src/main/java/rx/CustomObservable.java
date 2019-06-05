package rx;


public class CustomObservable<T> extends Observable<T> {

    CustomObservable(OnSubscribe<T> f) {
        super(f);
    }

    public interface ThreadFactory {
        Thread createThread(Runnable runnable);
    }

    Observable<T> customSubscribeOn(ThreadFactory threadFactory) {
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
                        CustomObservable.this.subscribe(s);
                    }
                }).start();

            }
        });
    }

    public <R> Observable<T> customLift(final Operator<? extends R, ? super T> operator) {
        return new CustomObservable<T>(new OnSubscribe<T>() {
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
