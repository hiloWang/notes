# 实现自定义的操作符

---
## 1 自定义Operator

实现一个仿照map操作符。关键函数在于`lift`变换。

```java
    public static void main(String[] args) {
        Observable.just(32)
                .lift(new MapOperator<>(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return String.valueOf(integer);
                    }
                }))
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });
    }

    public static class MapOperator<T, R> implements Observable.Operator<T, R> {

        private Func1<R, T> mMapTransform;

        public MapOperator(Func1<R, T> mapTransform) {
            mMapTransform = mapTransform;
        }

        @Override
        public Subscriber<? super R> call(Subscriber<? super T> subscriber) {
            return new Subscriber<R>() {
                @Override
                public void onCompleted() {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onCompleted();
                        subscriber.setProducer(new Producer() {
                            @Override
                            public void request(long n) {

                            }
                        });
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                }

                @Override
                public void onNext(R r) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(mMapTransform.call(r));
                    }
                }
            };
        }
    }

```

原理在于life函数，下面是简化后的lift：

```java
    public final <R> Observable<R> lift(final Operator<? extends R, ? super T> operator) {
           //创建一个新的Observbale。
            return new Observable<R>(new OnSubscribe<R>() {
                @Override
                public void call(Subscriber<? super R> o) {
                //使用我们的操作符创建一个新的st，
                            Subscriber<? super T> st = hook.onLift(operator).call(o);
                //调用st.start(),将会调用o的start。
                            st.onStart();
                //用原始的Obsrvable的onSubscribe调用新创建的st，综合以上三步就形成了链式调用。
                            onSubscribe.call(st);
                }
            });
        }
```

---
## 2 自定义Transformer

原理在语音compose操作：

```java
    public <R> Observable<R> compose(Transformer<? super T, ? extends R> transformer) {
            return ((Transformer<T, R>) transformer).call(this);
    }
```

其实就是使用我们自定义的Transformer转换Observable。

`compose`的参数`Transformer`可以实现很强大的功能。`Transformer`实际上就是一个`Func1<Observable<T>, Observable<R>>`，换言之就是：可以通过它将一种类型的`Observable`转换成另一种类型的`Observable`，和调用一系列的内联操作符是一模一样的。


**注意`conpose`与`flatMap`的区别**，参考[避免打断链式结构：使用.compose( )操作符](http://www.jianshu.com/p/e9e03194199e)：

1. `compose()`是唯一一个能够从数据流中得到原始`Observable<T>`的操作符，所以，那些需要对整个数据流产生作用的操作（比如，`subscribeOn()`和`observeOn()`）需要使用`compose()`来实现。相较而言，如果在`flatMap()`中使用`subscribeOn()`或者`observeOn()`，**那么它仅仅对在`flatMap()`中创建的`Observable`起作用，而不会对剩下的流产生影响**
2. 当创建`Observable`流的时候，`compose()`会立即执行，犹如已经提前写好了一个操作符一样，而`flatMap()`则是在`onNext()`被调用后执行，`onNext()`的每一次调用都会触发`flatMap()`，也就是说，`flatMap()`转换每一个事件，而`compose()`转换的是整个数据流。
3. 因为每一次调用`onNext()`后，都不得不新建一个`Observable`，所以`flatMap()`的效率较低。事实上，`compose()`操作符只在主干数据流上执行操作。

如果想重用一些操作符，还是使用`compose()`吧，虽然`flatMap()`的用处很多，但作为重用代码这一点来讲，并不适用。
