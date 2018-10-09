# RxJava的过滤类型操作符

RxJava的过滤类型操作符用于对数据进行一些过滤，比如符合某些条件的数据，某些位置上的数据，过滤重复数据或者过滤连续重复的数据等等，操作符如下：

## filter

filter操作符接收一个函数对象，用于对数据进行验证，返回true则发射该数据，返回false不发射。

## take/takeLast

take接收一个整数，用于从原始序列发射前N个数据

takeLast接收一个整数，用于从原生序列发射后N个数据，**值得注意的是因为takeLast()方法只能作用于一组有限的完整的序列（发射元素）**

take还有一重载函数，接收一个时间值，表示只获取在规定时间前发射的数据序列

```java
      //表示值获取开始订阅后10s内发射的数据
      Observable.interval(1,1,TimeUnit.SECONDS)
                    .take(10, TimeUnit.SECONDS)
                    .subscribe(System.out::println);
```

takeLast也有一重载函数，接收一个时间值(或数量+时间值)，表示在源数据序列发射完成后，只获取在完成前规定时间段发射的数据序列

```java
       Observable.create(subscriber -> {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(100);
                        subscriber.onNext(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }
                subscriber.onCompleted();
            //获取源数据序列发射完成前三秒内的最后三个数据
            })
            .takeLast(3,3, TimeUnit.SECONDS).subscribe(
                    System.out::println
            );
            结果是:97 98 99
```

## distinct

distinct用来过滤掉重复的数据,默认的distinct不需要任何参数，还有一个重载的distinct接收一个函数对象用作选择器，判断数据是否为重复，实现选择器的方法需要返回一个新的对象，然后Observable会根据你返回的对象作为判断数据是否重复的依据

## distinctUntilsChanged

distinctUntilsChanged用于过滤掉连续发射数据中重复的数据

## first/last/firstOrDefault/lastOrDefault

用于获取第一个数据或者最后一个数据，使用Default表示没有发生数据时发射默认值。同理last和lastOrDefault只能应用于一个完整的数据序列

如果原始Observable没有发射任何满足条件的数据，`first`会抛出一个`NoSuchElementException`

##  skip/skipLast

skip/skipLast用于让原始的Observable不发射前几个/后几个数据
同理skipLast只能应用于一个完整的数据序列，ip还有一个重载接收一个规定的时间值，用于让原始的Observable不发射从开始订阅到规定时间内发射的数据。**这个操作符在`computation`调度器执行**

## elementAt/elementAtOrEefault

elementAt接收一个整数，表示只需要发射的第N个数据，elementAtOrEefault可以设定一个默认值。

## sample

sample用于对一个Observable发射的数据序列进行周期采样，每次采样发射原Observable周期内最近发射的数据。当原始的Observable发射完成，则采样结束。

`throttleLast`的功能与`sample`一样，默认在`computation`调度器上执行


sample还有一个重载，接收一个Observable，当这个Observable **发射一个数据** 或者当 **终止** 时，它才去对原始的Observable进行一次采样。

## timeOut

timeOut用于给原Observable设定一个超时时间，当原Observable在timeOut规定的时间被没有发射新的数据，timeOut就会以异常终止

## ignoreElements

ignoreElements用于过滤掉所有数据

## debounce

debounce函数过滤掉由Observable发射的速率过快的数据；如果在一个指定的时间间隔过去了仍旧没有发射一个数据，那么它将发射指定时间段内最后发射的那个数据。

**debounce与throttleWithTimeout功能一样。`debounce`默认在computation调度器上执行。**

## throttleFirst

throttleFirst函数过滤掉由Observable发射的速率过快的数据；如果在一个指定的时间间隔过去了仍旧没有发射一个，那么它将发射指定时间段内发射的第一个数据。

**`throttleFirst`操作符默认在`computation`调度器上执行**

## sngle

single的变体接受一个谓词函数，发射满足条件的单个值，如果不是正好只有一个数据项满足条件，**会以错误通知终止**。还有一个singleOrDefault，如果没有数据满足条件，返回默认值；如果有多个数据满足条件，以错误通知终止。

## ofType

ofType用于过滤类型












