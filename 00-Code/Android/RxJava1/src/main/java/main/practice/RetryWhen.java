package main.practice;

import main.utils.RxLock;
import rx.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * RetryWhen示例，重试3次
 *
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class RetryWhen {

    public static void main(String... args) {
        //testRetryWhen();
        //testFuncRetryWithDelay();
        testDeferFuncRetryWithDelay();
        testCreateFuncRetry();
        RxLock.lock();
    }

    private static volatile boolean sTokenOk = false;
    private static AtomicInteger sRetryCount = new AtomicInteger(0);

    private static void testCreateFuncRetry() {
        Observable.create(
                (Action1<Emitter<String>>) emitter -> {
                    for (int i = 0; i < 10; i++) {
                        emitter.onNext(String.valueOf(i));
                        RxLock.sleep(100);
                    }
                    emitter.onError(new RuntimeException("ABC"));
                }, Emitter.BackpressureMode.BUFFER)
                .retryWhen(observable ->
                        observable.flatMap(new Func1<Throwable, Observable<?>>() {

                            int time = 0;

                            @Override
                            public Observable<?> call(Throwable throwable) {
                                time++;
                                if (time < 4) {
                                    return Observable.just(1);
                                } else {
                                    return Observable.error(throwable);
                                }
                            }
                        }))
                .observeOn(Schedulers.io())
                .subscribe(System.out::println, Throwable::printStackTrace);
    }


    private static void testRetryWhen() {
        Observable.just(null)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<Object, Observable<String>>() {
                    @Override
                    public Observable<String> call(Object o) {
                        System.out.println("sTokenOk = " + sTokenOk);
                        if (sTokenOk) {
                            return Observable.just("获取数据--->你好");
                        } else {
                            System.out.println("token实现，抛出异常");
                            throw new TokenInvalidateException();
                        }
                    }
                })
                //retryWhen接收一个Fun1作为notificationHandler，每次notificationHandler发射一个数据，上游就重试一次。
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                //如果是Token实现，则重试
                                if (throwable instanceof TokenInvalidateException) {
                                    System.out.println("重试" + (sRetryCount.get() + 1) + "次，在" + Thread.currentThread().getName() + "线程");
                                    sRetryCount.addAndGet(1);
                                    return Observable.just(getToken())
                                            .doOnNext(new Action1<String>() {
                                                @Override
                                                public void call(String s) {
                                                    if (sRetryCount.get() >= 3) {
                                                        sTokenOk = true;
                                                    }
                                                }
                                            });
                                } else {
                                    //否则直接结束，不需要重试
                                    return Observable.error(throwable);
                                }
                            }
                        });
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e);
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println(s);
                    }
                });
    }

    private static String getToken() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "get Token";
    }


    private static class TokenInvalidateException extends RuntimeException {
    }


    private static void testFuncRetryWithDelay() {
        Observable.just(null)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<Object, Observable<String>>() {
                    @Override
                    public Observable<String> call(Object o) {
                        System.out.println("sTokenOk = " + sTokenOk);
                        if (sTokenOk) {
                            return Observable.just("获取数据--->你好");
                        } else {
                            System.out.println("token实现，抛出异常");
                            throw new TokenInvalidateException();
                        }
                    }
                })
                .retryWhen(new FuncRetryWithDelay(3, 3000, new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) {
                        return throwable instanceof TokenInvalidateException;
                    }
                }))
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e);
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println(s);
                    }
                });
    }


    private static void testDeferFuncRetryWithDelay() {
        Observable.defer(() -> {
            System.out.println("sTokenOk = " + sTokenOk);
            if (sTokenOk) {
                return Observable.just("获取数据--->你好");
            } else {
                System.out.println("token实现，抛出异常");
                throw new TokenInvalidateException();
            }
        })
                .subscribeOn(Schedulers.io())
                .retryWhen(new FuncRetryWithDelay(3, 3000, new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) {
                        return throwable instanceof TokenInvalidateException;
                    }
                }))
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e);
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println(s);
                    }
                });
    }


    //封装工具类
    public static class FuncRetryWithDelay implements Func1<Observable<? extends Throwable>, Observable<?>> {

        private final int _maxRetries;
        private final int _retryDelayMillis;
        private AtomicInteger _retryCount;
        private Predicate<Throwable> _retryChecker;

        public FuncRetryWithDelay(final int maxRetries, final int retryDelayMillis) {
            this(maxRetries, retryDelayMillis, null);
        }

        public FuncRetryWithDelay(final int maxRetries, final int retryDelayMillis, Predicate<Throwable> retryChecker) {
            _maxRetries = maxRetries;
            _retryDelayMillis = retryDelayMillis;
            _retryCount = new AtomicInteger(0);
            _retryChecker = retryChecker != null ? retryChecker : new Predicate<Throwable>() {
                @Override
                public boolean test(Throwable throwable) {
                    return true;
                }
            };
        }

        @Override
        public Observable<?> call(Observable<? extends Throwable> attempts) {
            return attempts.flatMap(new Func1<Throwable, Observable<?>>() {
                @Override
                public Observable<?> call(Throwable throwable) {
                    if (!_retryChecker.test(throwable)) {
                        return Observable.error(throwable);
                    }
                    System.out.println("重试" + (_retryCount.get() + 1) + "次，在" + Thread.currentThread().getName() + "线程");
                    if (_retryCount.addAndGet(1) < _maxRetries) {
                        return Observable.timer(_retryDelayMillis, TimeUnit.MILLISECONDS);
                    }
                    return Observable.error(throwable);
                }
            });
        }
    }
}
