# Why Not RxLifecycle

>origin reference [Why Not RxLifecycle?](https://blog.danlew.net/2017/08/02/why-not-rxlifecycle/)

在作者开始使用 RxJava 后，发现需要手动地去管理订阅和取消订阅，否则很容易导致内存泄漏问题，手动地去管理定语是非常乏味的，作者希望订阅关系在 Activity 或 Fragment 销毁时自动解除，于是就诞生了 RxLifecycle 这个库。

随着时间的推移，作者发现了 RxLifecycle 存在许多的难缠的问题，按照重要关系排列如下：

1. 自动生命周期检测会导致一些扑朔迷离的不确定的代码。难以确定订阅会在什么时候被取消，特别是在非 Activity/Fragment 组件中。不过使用 `bindUntilEvent()` 会好过  `bindLifecycle()` 。
2. 很多时候，你还是需要去手动管理订阅关系，有些情况 RxLifecycle 无法处理，比如取消一个旧的订阅。转而去订阅新的 Observable。
3. RxLifecycle 只能模拟 `Subscription.unsubscribe()`，`onComplete()` 与 `Subscription.unsubscribe()` 是有细微区别的，如果不注意，这会导致问题。
4. 在使用 Single / Completable 时，使用 RxLifecycle 管理生命周期可能会导致异常，还是因为我们只能模拟流的结束，Single/Completable 要么是发射数据，要么是产生错误，这没得选择。具体参考 [issues/187](https://github.com/trello/RxLifecycle/issues/187)。
5. RxLint 不能很好地检测 RxLifecycle。

Uber 开源的  AutoDispose 解决了以上部分问题，但作者还是选择了手动的去管理订阅关系。

- 使用 CompositeSubscription 管理多个订阅关系。
- 使用 kotlin 可以方面地处理非空判断，`mySubscription?.unsubscribe()`。
- 使用 kotlin 的操作符重载： `compositeSubscription += Observable.just().etc().subscribe()`。

关于上面第四点，模拟如下：

```java
public class RxLifecycleMain {

    public static void main(String... args) {

        PublishSubject<String> subject = PublishSubject.create();
        Observable<?> observable = subject;

        Completable.fromAction(() -> Utils.sleep(2000))
                .subscribeOn(Schedulers.computation())
                .compose(upstream -> Completable.ambArray(upstream, observable.flatMapCompletable(CANCEL_COMPLETABLE)))
                .observeOn(Schedulers.newThread())
                .doOnComplete(() -> System.out.println("doOnComplete"))
                .doOnDispose(() -> System.out.println("doOnDispose"))
                .subscribe(
                        () -> {
                            Utils.sleep(1000);
                            System.out.println("-----------------------completed");
                        }, throwable -> {
                            System.out.println("-----------------------throwable: " + throwable);
                        });

        Utils.sleep(1000);

        //一个生命周期事件触发，而此时 Completable 还没有完成，则会导致异常。
        subject.onNext("");

        Utils.sleep(10000);
    }

    //https://github.com/trello/RxLifecycle/blob/master/rxlifecycle/src/main/java/com/trello/rxlifecycle3/Functions.java
    //抛出异常的原因就是这里，生命周期事件触发之后，将导致一个 CancellationException 异常。
    private static final Function<Object, Completable> CANCEL_COMPLETABLE = new Function<Object, Completable>() {
        @Override
        public Completable apply(Object ignore) throws Exception {
            System.out.println("CANCEL_COMPLETABLE");
            return Completable.error(new CancellationException());
        }
    };

}
```