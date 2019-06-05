package main.practice;

import main.utils.RxLock;
import rx.*;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class BehaviorSubjectCache {

    public static void main(String... args) {
        testCache();
        RxLock.lock(2000);

        testCache();
        RxLock.lock(2000);

        Data.getInstance().clearMemoryAndDiskCache();
        testCache();
        RxLock.lock(2000);
    }

    private static void testCache() {
        Data.getInstance().subscribeData(new Observer<List<String>>() {
            @Override
            public void onCompleted() {
                System.out.println("BehaviorSubjectCache.onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e);
            }

            @Override
            public void onNext(List<String> strings) {
                System.out.println(strings);
            }
        });
    }
}


class Data {

    private static Data instance;

    private BehaviorSubject<List<String>> cache;

    private Data() {
    }

    static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    Subscription subscribeData(Observer<List<String>> observer) {
        if (cache == null) { //没有缓存
            cache = BehaviorSubject.create();
            Observable.create(
                    new Observable.OnSubscribe<List<String>>() {
                        @Override
                        public void call(Subscriber<? super List<String>> subscriber) {
                            //读取本地缓存
                            List<String> items = Database.getInstance().readItems();
                            if (items == null) {
                                System.out.println("从网络获取的数据");
                                loadFromNetwork();
                            } else {
                                System.out.println("从本地获取的数据");
                                subscriber.onNext(items);
                            }
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe(cache);
        } else {
            System.out.println("从内存获取的数据");
        }
        return cache
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        cache = null;
                    }
                })
                .subscribe(observer);
    }


    public void loadFromNetwork() {
        Network.getData()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> items) {
                        Database.getInstance().writeItems(items);
                    }
                })
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> items) {
                        cache.onNext(items);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        cache.onError(throwable);
                    }
                });
    }

    private void clearMemoryCache() {
        cache = null;
    }

    public void clearMemoryAndDiskCache() {
        clearMemoryCache();
        Database.getInstance().delete();
    }
}

class Network {
    static Observable<List<String>> getData() {
        return Observable.create(
                listEmitter -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    List<String> dataList = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        dataList.add("net data " + i);
                    }
                    listEmitter.onNext(dataList);
                }, Emitter.BackpressureMode.BUFFER);
    }
}

class Database {

    private static Database INSTANCE;
    private List<String> cache;

    private Database() {
    }

    static Database getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }
        return INSTANCE;
    }

    List<String> readItems() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return cache;
    }

    void writeItems(List<String> items) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cache = items;
    }

    void delete() {
        cache = null;
    }
}