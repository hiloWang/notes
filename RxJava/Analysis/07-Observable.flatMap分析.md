# flatMap 分析

flatMap 源码：

```java
    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> func) {
        if (getClass() == ScalarSynchronousObservable.class) {
            return ((ScalarSynchronousObservable<T>)this).scalarFlatMap(func);
        }
        return merge(map(func));
    }
```

可以见，flatMap 使用的是 merge 和 map 实现的。

- map 用于变换将一个数据变换为一个 Observable，这里会创建全新的 Observable
- merge 用于将 map 产生的 Observable 添加到原始的事件序列中，在 merge 中，会把 创建全新的 Observable 发射的数据发射到下游


