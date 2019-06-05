package main.practice;

import main.utils.RxLock;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 使用RepeatWhen实现轮询
 *
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class RepeatWhen {

    public static void main(String[] args) {
        testRepeatWhen();
    }

    private static final Random RANDOM = new Random();

    private static void testRepeatWhen() {

        Observable.just(null)
                .flatMap(new Func1<Object, Observable<String>>() {
                    @Override
                    public Observable<String> call(Object o) {
                        //模拟获取数据
                        return Observable.just(getNumber());
                    }
                })
                //notificationHandler的作用，每次发射一个数据，表示重复执行一次
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        //每两秒订阅
                        return observable.delay(2, TimeUnit.SECONDS);
                    }

                })
                .subscribe(new Subscriber<String>() {
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

        RxLock.lock();
    }



    private static String getNumber() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return String.valueOf(RANDOM.nextInt());
    }

}
