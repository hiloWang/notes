# RxJava操作符——聚合操作符

## count/countLong

count/countLong用于计算数据项的个数并发射结果

## concat

concat可以顺序连接多个Observables，如果不要求顺序可以使用`merge`，`concat`的行为是当第一个`Observable`完结时开始订阅第二个。

`concatWith`是对象方法。行为与`concat`一样。

## reduce

reduce对序列使用reduce()函数并发射对应的结果

`reduce`=`sacan().last()`

不建议使用reduce收集发射的数据到一个可变的数据结构，那种场景你应该使用collect。

注意reduce与scan不同的是，scan每次结合都会把结果发射给订阅者，而reduce会把所有的数据应用函数后，再发射给订阅者。

## collect

collect将原始 Observable 发射的数据放到一个单一的可变的数据结构中，然后返回一个发射这个数据结构的 Observable

















