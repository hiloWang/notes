package main.study;


import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import main.utils.RxLock;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.internal.operators.BackpressureUtils;
import rx.schedulers.Schedulers;

/**
 * @author Ztiany
 */
public class RxJavaSource {

    public static void main(String... args) {
        //testRequest();
        //testBackPressure();
        testZip();
    }

    private static void testZip() {
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

        RxLock.lock();
    }

    private static void testBackPressure() {
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

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }
                });


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
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        System.out.println("along:" + aLong);
                    }
                })
                .subscribe(customerSubscriber);

        customerSubscriber.requestMore(3);
        customerSubscriber.requestMore(3);
        customerSubscriber.requestMore(3);
        customerSubscriber.requestMore(3);

        RxLock.lock();
    }

    private static void testRequest() {

        long andAddRequest = BackpressureUtils.getAndAddRequest(new AtomicLong(), Long.MAX_VALUE);
        System.out.println("andAddRequest " + andAddRequest);

        Observable.range(1, 100)
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        System.out.println(aLong);

                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void setProducer(Producer p) {
                        System.out.println(p);
                        super.setProducer(p);
                    }
                });
    }


}
