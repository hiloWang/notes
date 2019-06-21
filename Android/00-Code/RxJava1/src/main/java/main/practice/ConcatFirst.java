package main.practice;

import rx.Observable;

/**
 * Concat+First从不同的数据源获取数据，只获取最先到达的数据。
 * 具体参考：http://blog.danlew.net/2015/06/22/loading-data-from-multiple-sources-with-rxjava/
 *
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class ConcatFirst {

    /*
    先取 memory 中的数据，如果有就取出，然后停止检索队列；没有就取 disk 的数据，有就取出，然后停止检索队列；最后才是网络请求,
    first() 和 takeFirst() 区别在于，如果没有符合的数据源，first() 会抛 NoSuchElementException 异常
    */
    public static void main(String... args) {

        Observable<Data> memory = Observable.just(new Data());
        Observable<Data> disk = Observable.just(new Data());
        Observable<Data> network = Observable.just(new Data());

        Observable<Data> source = Observable
                .concat(memory, disk, network)
                .first();
    }

    private static class Data {

    }
}
