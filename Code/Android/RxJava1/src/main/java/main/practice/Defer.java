package main.practice;

import main.utils.RxLock;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class Defer {


    public static void main(String... args) {
        //direct
        Observable.just(getData())
                .subscribeOn(Schedulers.io())
                .subscribe(System.out::println);

        //defer
        Observable.defer(() -> Observable.just(getData()))
                .subscribeOn(Schedulers.io())
                .subscribe(System.out::println);

        RxLock.lock(3000);
    }


    static String getData() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
        return "Data";
    }

}
