package main.practice;

import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Executors;

/**
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class FiledSafe {

    private static int count;
    private static final Scheduler single = Schedulers.from(Executors.newSingleThreadExecutor());

    public static void main(String... args) {
        doTest();
    }


    private static void doTest() {
        List<Integer> names = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            names.add(i);
        }
        rx.Observable.defer(() -> rx.Observable.from(names))
                .subscribeOn(Schedulers.io())
                .flatMap(integer -> {
                    System.out.println(Thread.currentThread());
                    return rx.Observable.just(integer + integer)
                            .subscribeOn(Schedulers.newThread())
                            .doOnNext(integer1 -> {
                                for (int i = 0; i < 10; i++) {
                                    count++;
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }, 2)
                .doOnNext(integer -> {
                    System.out.println("counting= " + count);
                })
                .observeOn(single)
                .subscribe(integers -> {
                    System.out.println("integers " + integers);
                    System.out.println("count= " + count);
                });
    }


}
