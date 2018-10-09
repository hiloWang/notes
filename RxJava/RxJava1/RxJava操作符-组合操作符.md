# RxJava操作符——组合操作符

- **merge** 把两个甚至更多的Observables合并，然后发射他们的数据，merge是静态的方法，接收多个Obserable。而mergeWith属于对象方法，表示一个Observable与另一个Observable合并，需要注意的是merge不保证多个Observable的数据顺序发送。即`merge`可能会让合并的Observables发射的数据交错

- **mergeDelayError** 与merge功能类似，但是当合并的多个Observable中有一个以异常终止，merge会立即停止发射数据，报告错误，而mergeDelayError则会延迟错误，直到所有数据发射完毕。如果只发生一个错误，则直接发送原有的错误。如果发生多个错误，则合并多个错误成`rx.exceptions.CompositeException`。

- **zip** 可以合并多个Observable发射的数据，对每个Observable发射数据应用一个函数，使之结合，然后发射结合后的数据，它只发射与发射数据项最少的那个Observable一样多的数据。zip是一个静态方法，可接收多个Observable，zipWith是对象方法，接收一个Observable

- **combineLatest**：RxJava的`combineLatest()`函数有点像`zip()`函数的特殊形式。正如我们已经学习的，`zip()`作用于最近未打包的两个Observables。相反，`combineLatest()`作用于最近发射的数据项：如果`Observable1`发射了A并且`Observable2`发射了B和C，`combineLatest()`将会分组处理AB和AC

- **join** 操作符把类似于combineLatest操作符，也是两个Observable产生的结果进行合并，合并的结果组成一个新的Observable，但是join操作符可以控制每个Observable产生结果的生命周期，在每个结果的生命周期内，可以与另一个Observable产生的结果按照一定的规则进行合并。

- **and/then/when**：使用Pattern和Plan作为中介，将两个或多个Observable发射的数据集合并到一起，参考[文档](http://rxjava.yuxingxin.com/chapter6/and_then_when.html)，它们属于rxjava-joins模块

- **swtch** 的作用是：给出一个发射多个Observables序列的源Observable，`switch()`订阅到源Observable然后开始发射由第一个发射的Observable发射的一样的数据。当源Observable发射一个新的Observable时，`switch()`立即取消订阅前一个发射数据的Observable（因此打断了从它那里发射的数据流）然后订阅一个新的Observable，并开始发射它的数据。

- **startWith** 很简单，用于向一个Observable发射的数据序列前添加数据






















