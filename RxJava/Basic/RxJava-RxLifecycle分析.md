# RxLifecycle使用和分析

---
## 1 RxLifecycle简介

[Rxlifecycle](https://github.com/trello/RxLifecycle "RxLife地址")是RxAndroid的一个扩展，
使用RxLifecycle可以严格控制由于发布了一个订阅后，由于没有及时取消，导致Activity/Fragment无法销毁导致的内存泄露。

在gradle集成`RxLifecycle`：

```
    //RxLifecycle核心
    compile 'com.trello:rxlifecycle:0.8.0'
    //提供了Android中Activiy/Fragment的生命周期事件
    compile 'com.trello:rxlifecycle-android:0.8.0' 
    //提供了一个Activity和Fragment的基类，用于方便的使用RxLifecycler
    compile 'com.trello:rxlifecycle-components:0.8.0'
```
另外还有两个可选模块
```
    // If you want to use Navi for providers
    compile 'com.trello:rxlifecycle-navi:0.8.0'
    // If you want to use Kotlin syntax
    compile 'com.trello:rxlifecycle-kotlin:0.8.0'
```

- navi部分用于给[navi](https://github.com/trello/navi)库提供RxLife的支持
- kotlin用于对kotlin语言的支持

---
## 2 RxLifecycle的使用

使用RxLifecycle需要继承`RxCompatActivity`或者`RxFragment`，`RxDialogFragment`、`RxFragmentActivity`。

```
    public class RxLifecycleTestActivity extends RxAppCompatActivity {
       ......
    }
```
### 2.1 bindToLifecycle()方法

在子类使用Observable中的compose操作符，调用，完成Observable发布的事件和当前的组件绑定，实现生命周期同步。从而实现当前组件生命周期结束时，自动取消对Observable订阅。
```java
    Observable.range(1, 40).delay(4, TimeUnit.SECONDS)
                    .compose(this.<Integer>bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Integer>() {
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "onCompleted() called with: " + "");
                        }
    
                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                        }
    
                        @Override
                        public void onNext(Integer integer) {
                            Log.d(TAG, "onNext() called with: " + "integer = [" + integer + "]");
                        }
                    });
```
关键的一句在于`compose(this.<Integer>bindToLifecycle())`，这是`RxCompatActivity`提供的方法。使用这个`compose`就可以绑定Activity的生命周期，**如果在`onCreate`后订阅Observale默认是在`onDestroy`时取消订阅，如果在`onResume`后订阅Observable默认是在`onPause`时取消订阅**，在`onStart`后订阅的默认实在`onStop`时取消订阅。


### 2.2 bindUntilEvent() 方法

使用ActivityEvent类，其中的CREATE、START、 RESUME、PAUSE、STOP、 DESTROY分别对应生命周期内的方法。使用bindUntilEvent指定在哪个生命周期方法调用时取消订阅。

```java
    Observable.range(1, 40)
                    .delay(4, TimeUnit.SECONDS)
                    .compose(this.bindUntilEvent(ActivityEvent.PAUSE))
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            Log.d(TAG, "doOnSubscribe----=" + Thread.currentThread().getName());
    
                        }
                    })
                    .subscribe(new Subscriber<Object>() {
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "onCompleted() called with: " + "");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                        }

                        @Override
                        public void onNext(Object integer) {
                            Log.d(TAG, "onNext() called with: " + "integer = [" + integer + "]");
                        }
                    });
```
使用`.compose(this.bindUntilEvent(ActivityEvent.PAUSE))`就可以指定在哪个声明周期回调中取消订阅。

---
## 3 RxLifecycle的源码浅析

使用RxLifecycle可以方便解决**控制由于发布了一个订阅后，由于没有及时取消，导致Activity/Fragment无法销毁导致的内存泄露**。

那么到底是如何做到的呢？需要从源码中找答案。


### 3.1 RxCompatActivity

RxCompatActivity的全部源码如下：

```java
    public class RxAppCompatActivity extends AppCompatActivity implements ActivityLifecycleProvider {
        //BehaviorSubject会把最近发射过的数据发射给他之后的订阅者
        private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    
        @Override
        @NonNull
        @CheckResult
        public final Observable<ActivityEvent> lifecycle() {
            return lifecycleSubject.asObservable();
        }
    
        @Override
        @NonNull
        @CheckResult
        public final <T> Observable.Transformer<T, T> bindUntilEvent(@NonNull ActivityEvent event) {
            return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
        }
    
        @Override
        @NonNull
        @CheckResult
        public final <T> Observable.Transformer<T, T> bindToLifecycle() {
            return RxLifecycle.bindActivity(lifecycleSubject);
        }
    
        @Override
        @CallSuper
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            lifecycleSubject.onNext(ActivityEvent.CREATE);
        }
    
        @Override
        @CallSuper
        protected void onStart() {
            super.onStart();
            lifecycleSubject.onNext(ActivityEvent.START);
        }
    
        @Override
        @CallSuper
        protected void onResume() {
            super.onResume();
            lifecycleSubject.onNext(ActivityEvent.RESUME);
        }
    
        @Override
        @CallSuper
        protected void onPause() {
            lifecycleSubject.onNext(ActivityEvent.PAUSE);
            super.onPause();
        }
    
        @Override
        @CallSuper
        protected void onStop() {
            lifecycleSubject.onNext(ActivityEvent.STOP);
            super.onStop();
        }
    
        @Override
        @CallSuper
        protected void onDestroy() {
            lifecycleSubject.onNext(ActivityEvent.DESTROY);
            super.onDestroy();
        }
    }
```
RxAppCompatActivity创建了一个BehaviorSubjec，而当订阅BehaviorSubject时，它会把最近发射过的数据发射给他之后的订阅者，然后再RxAppCompatActivity重写了Activity声明周期的几个方法，并转发给了BehaviorSubject，所以重点在于：

- BehaviorSubjec的特性
- bindToLifecycle和bindUntilEvent方法的实现


###  3.2 bindToLifecycle

bindToLifecycle的使用方式是：
```java
    ...
    .compose(this.<Long>bindToLifecycle())
    ...
```
使用的是compose变换，所以bindToLifecycle方法返回的是一个Transformer的实现：


`bindToLifecycle`调用的是`RxLifelcycle.bindActivity`:
```java
    public static <T> Observable.Transformer<T, T> bindActivity(@NonNull final Observable<ActivityEvent> lifecycle) {
        return bind(lifecycle, ACTIVITY_LIFECYCLE);
    }
```

bindActivity中调用了bind方法，参数：

- lifecycle是RxAppCompatActivity中的lifecycleSubject
- ACTIVITY_LIFECYCLE是一个Func1



`ACTIVITY_LIFECYCLE` 其实是一个 Func1，其行为是对 `ActivityEvent` 的各个生命周期事件进行映射，返回在该事件订阅时，触发解除这些订阅的响应  Event：

```java
    private static final Func1<ActivityEvent, ActivityEvent> ACTIVITY_LIFECYCLE =
            new Func1<ActivityEvent, ActivityEvent>() {
                @Override
                public ActivityEvent call(ActivityEvent lastEvent) {
                    switch (lastEvent) {
                        case CREATE:
                            return ActivityEvent.DESTROY;
                        case START:
                            return ActivityEvent.STOP;
                        case RESUME:
                            return ActivityEvent.PAUSE;
                        case PAUSE:
                            return ActivityEvent.STOP;
                        case STOP:
                            return ActivityEvent.DESTROY;
                        case DESTROY:
                            throw new OutsideLifecycleException("Cannot bind to Activity lifecycle when outside of it.");
                        default:
                            throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
                    }
                }
            };
```

ACTIVITY_LIFECYCLE中对Activity事件处理列表如下：

接收事件|返回事件
---|---
CREATE | DESTROY
START | STOP
RESUME | PAUSE
PAUSE | STOP
STOP | DESTROY
DESTROY | 抛出异常


然后看一下`bind`方法的实现。

```java
     public static <T, R> Observable.Transformer<T, T> bind(@NonNull Observable<R> lifecycle,
                                                               @NonNull final Func1<R, R> correspondingEvents) {
            checkNotNull(lifecycle, "lifecycle == null");
            checkNotNull(correspondingEvents, "correspondingEvents == null");
            // Make sure we're truly comparing a single stream to itself
            final Observable<R> sharedLifecycle = lifecycle.share();
    
            // Keep emitting from source until the corresponding event occurs in the lifecycle
            return new Observable.Transformer<T, T>() {
                @Override
                public Observable<T> call(Observable<T> source) {
                    return source.takeUntil(
                        Observable.combineLatest(
                            sharedLifecycle.take(1).map(correspondingEvents),
                            sharedLifecycle.skip(1),
                            new Func2<R, R, Boolean>() {
                                @Override
                                public Boolean call(R bindUntilEvent, R lifecycleEvent) {
                                    return lifecycleEvent.equals(bindUntilEvent);
                                }
                            })
                            .onErrorReturn(RESUME_FUNCTION)
                            .takeFirst(SHOULD_COMPLETE)
                    );
                }
            };
        }
```

bind方法参数：

- lifecycle 是转发生命周期的Behavior
- correspondingEvents 是对生命周期事件swich的Fuck1

1. 调用了`sharedLifecycle.share()`，让其变成一个可以被多订阅(多播)的`Observable`
2. `Transformer`是对原有的`Observable`做转换
3. 理解`takeUntil`，只要第二个Observable发射了一项数据或者终止时，丢弃原始Observable发射的任何数据
4. 理解`combineLatest`，它用于结合两个`Observable`最近发射的数据，生成一个新的数据类型
5. 然后加上在`RxCompatActivity`里面的`BehaviorSubject`

假设我们在`onResume`后创建并订阅了请求网络数据的`Observable`，并在这个`Observable`中使用了`bindToLifecycle`，，由于`BehaviorSubject`的特性，它会先发射它最近发射过的数据给订阅者，于是`onResume`被发射。

这时`sharedLifecycle.take(1).map(correspondingEvents)`得到的事件是`ActivityEvent.PAUSE`，并且他只有这一个事件。然后由于是`combineLatest`，它还在等待第二个Observable发射数据。


然后这时退出当前Activity，于是`onPause`被执行，于是`sharedLifecycle.skip(1)`得到数据是`ActivityEvent.PAUSE`，然后在`combineLatest`的`Func2`会返回true。

接下来是`takeFirst(SHOULD_COMPLETE)`。
```java
    private static final Func1<Boolean, Boolean> SHOULD_COMPLETE = new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean shouldComplete) {
                return shouldComplete;
            }
        };
```
很简单，就是返回参数本身。

所以当`Activity`执行`onPause`时，`combineLatest`会发射一个数据，导致原始的`Observable`被取消订阅。
