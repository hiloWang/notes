# RxJava-Subject

---
## 1 Subject介绍

`subject` **它可以是一个Observable同时也可以是一个Observer：它作为连接这两个世界的一座桥梁** 。一个Subject可以订阅一个Observable，就像一个观察者，并且它可以发射新的数据，或者传递它接受到的数据，就像一个Observable。很明显，作为一个Observable，观察者们或者其它Subject都可以订阅它。

Subject使用静态的`create`方法来创建自身对象。可用用于订阅，也可以被订阅。

### 冷启动与热启动

一旦Subject订阅了Observable，它将会触发Observable开始发射。如果原始的Observable是冷的，被Subject订阅后，它将转换为一个热的。


---
## 2 PublishSubject

PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者

代码示例1：

```java
     Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);
            //创建要给PublishSubject。
            PublishSubject<Long> objectPublishSubject = PublishSubject.create();
            //PublishSubject订阅原始的observable
            observable.subscribe(objectPublishSubject);
    
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            objectPublishSubject.subscribe(new Subscriber<Long>() {
                @Override
                public void onCompleted() {
    
                }
    
                @Override
                public void onError(Throwable e) {
    
                }
    
                @Override
                public void onNext(Long aLong) {
                    System.out.println(aLong);
                }
            });
```

代码示例2：

```java
     PublishSubject<Long> objectPublishSubject = PublishSubject.create();
    
            objectPublishSubject.subscribe(new Subscriber<Long>() {
                @Override
                public void onCompleted() {
                    System.out.println("objectPublishSubject1 onCompleted");
                }
    
                @Override
                public void onError(Throwable e) {
                    System.out.println("objectPublishSubject1 onError" + e);
    
                }
    
                @Override
                public void onNext(Long aLong) {
                    System.out.println("objectPublishSubject1" + aLong);
                }
            });
    
            objectPublishSubject.subscribe(new Subscriber<Long>() {
                @Override
                public void onCompleted() {
                    System.out.println("objectPublishSubject2 onCompleted");
    
                }
    
                @Override
                public void onError(Throwable e) {
                    System.out.println("objectPublishSubject2 onError" + e);
    
                }
    
                @Override
                public void onNext(Long aLong) {
                    System.out.println("objectPublishSubject2" + aLong);
    
                }
            });

            objectPublishSubject.onNext(1L);
            objectPublishSubject.onNext(2L);
            objectPublishSubject.onNext(3L);
            objectPublishSubject.onNext(4L);
            objectPublishSubject.onCompleted();
```

---
## 3 AsyncSubject

一个AsyncSubject只在原始Observable完成后，发射来自原始Observable的最后一个值。（如果原始Observable没有发射任何值，AsyncObject也不发射任何值）它会把原始Observable的最后一个值发射给任何后续的观察者。

```java
            Observable<Integer> range = Observable.range(1, 10);
    
            AsyncSubject<Integer> objectAsyncSubject = AsyncSubject.create();
   
            range.subscribe(objectAsyncSubject);
    
            objectAsyncSubject.subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer integer) {
                    System.out.println("objectAsyncSubject " + integer);
                }
            });

    //打印结果是：
    objectAsyncSubject 10
```

---
## 4 BehaviorSubject

当观察者订阅BehaviorSubject时，它开始发射原始Observable最近发射的数据（如果此时还没有收到任何数据，它会发射一个默认值，前提是你设置一个默认的值），然后继续发射其它任何来自原始Observable的数据。


代码示例1：发射最近发射过的数据。

```java
    BehaviorSubject<Integer> integerObservable = BehaviorSubject.create();
    
            integerObservable.onNext(1);
            integerObservable.onNext(2);
            integerObservable.onNext(3);
    
            integerObservable.subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer integer) {
                    System.out.println(integer);
                }
            });
    
            integerObservable.onNext(4);
```

代码示例2：发射默认值的数据

```java
    Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);
            //创建一个发射默认数据的behaviorSubject
            BehaviorSubject<Long> behaviorSubject = BehaviorSubject.create(110L);
            observable.subscribe(behaviorSubject);
            behaviorSubject.subscribe(new Action1<Long>() {
                @Override
                public void call(Long integer) {
                    System.out.println("subscribe" + integer);
                }
            });

    打印结果：
    subscribe110
    subscribe0
    subscribe1
    subscribe2
    subscribe3
    ......
```

---
## 5 ReplaySubject

ReplaySubject会发射所有来自原始Observable的数据给观察者，无论它们是何时订阅的。也有其它版本的ReplaySubject，在重放缓存增长到一定大小的时候或过了一段时间后会丢弃旧的数据（原始Observable发射的）。

如果你把ReplaySubject当作一个观察者使用，注意不要从多个线程中调用它的onNext方法（包括其它的on系列方法），这可能导致同时（非顺序）调用，这会违反Observable协议，给Subject的结果增加了不确定性。

ReplaySubject有很多的`create`方法重置：

- `create()` 默认的创建方法
- `create(int capacity)`创建ReplaySubject，指定初始容量
- `createWithSize(int size)`创建ReplaySubject，指定缓存是数量
- `createWithTime(long time, TimeUnit unit, final Scheduler scheduler)`创建ReplaySubject，指定缓存的时间
- `createWithTimeAndSize(long time, TimeUnit unit, int size, final Scheduler scheduler)`创建ReplaySubject，指定缓存的时间和数量


代码示例：

```java
            Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);

            ReplaySubject<Long> longReplaySubject = ReplaySubject.createWithSize(4);

            observable.subscribe(longReplaySubject);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            longReplaySubject.subscribe(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    System.out.println("longReplaySubject1 " + aLong);
                }
            });
    
    
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            longReplaySubject.subscribe(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    System.out.println("longReplaySubject2 " + aLong);
                }
            });
    

    打印结果：
    ...等待5秒
    longReplaySubject1 1
    longReplaySubject1 2
    longReplaySubject1 3
    longReplaySubject1 4//s1——>瞬间发生1-4
    longReplaySubject1 5
    longReplaySubject1 6
    longReplaySubject1 7
    longReplaySubject1 8
    longReplaySubject1 9
    longReplaySubject2 6
    longReplaySubject2 7
    longReplaySubject2 8
    longReplaySubject2 9////s2——>瞬间发生6-9
    longReplaySubject1 10
    longReplaySubject2 10
    longReplaySubject1 11
    longReplaySubject2 11
    ......
```

---
## 6 串行化

如果你把 `Subject` 当作一个 `Subscriber` 使用，注意不要从多个线程中调用它的onNext方法（包括其它的on系列方法），这可能导致同时（非顺序）调用，这会违反Observable协议，给Subject的结果增加了不确定性。

要避免此类问题，你可以将 `Subject` 转换为一个 [`SerializedSubject`](http://reactivex.io/RxJava/javadoc/rx/subjects/SerializedSubject.html)。比如在多个线程调用`on`系列方法而没有同步话：

```java
    private static void testSerializedSubject() {
    
            PublishSubject<Object> objectPublishSubject = PublishSubject.create();
    
            objectPublishSubject.subscribe(new Subscriber<Object>() {
                @Override
                public void onCompleted() {
                    System.out.println("onCompleted");
                }
    
                @Override
                public void onError(Throwable e) {
                    System.out.println(e);
                }
    
                @Override
                public void onNext(Object o) {
                    System.out.println(o);
                }
            });
    
            class TempThread extends Thread {
                private int mAction;
                private PublishSubject mPs;
    
                TempThread(int action, PublishSubject<Object> ps) {
                    mAction = action;
                    mPs = ps;
                }
    
                @Override
                public void run() {
                    if (mAction == 3) {
                        mPs.onCompleted();
                    } else {
                        mPs.onNext(mAction);
                    }
                }
            }
    
    
            for (int i = 0; i < 4; i++) {
                new TempThread(i, objectPublishSubject).start();
            }
        }

    打印结果可能是：
    2
    onCompleted
    1
```

而串行化可以保证`onCompleted`在最后调用。

```java
       PublishSubject<Object> objectPublishSubject = PublishSubject.create();
       Subject subject = new SerializedSubject(objectPublishSubject);
```
