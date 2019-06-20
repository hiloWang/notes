package main.practice;

import rx.Observable;

/**
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class AutoConnect {

    public static void main(String... args) {
        //返回一个Observable，当有规定数量的Subscribers订阅它时自动连接这个ConnectableObservable
        Observable<Integer> integerObservable = Observable.range(1, 10)
                .replay()
                .autoConnect(2);
    }
}
