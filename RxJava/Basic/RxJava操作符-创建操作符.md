#  RxJava的创建操作符

## create

create操作符是所有创建型操作符的“根”，也就是说其他创建型操作符最后都是通过create操作符来创建。

## from

from操作符是把其他类型的对象和数据类型转化成Observable，from可以从列表，迭代器，数组等数据源转换成Observable，from内部会逐个发送这些数据。from还可以从Future创建Observable。

## just

just操作符也是把其他类型的对象和数据类型转化成Observable，它和from操作符很像，只是方法的参数有所差别，just会按数据的原始状态发送数据，可以把任何数据转化成Observable。

##  defer

defer操作符是直到有订阅者订阅时，才通过Observable的工厂方法创建Observable并执行，defer操作符能够保证Observable的状态是最新的。

## time

timer操作符是创建一串连续的数字，产生这些数字的时间间隔是一定的，这里有两种情况：

-  一种是隔一段时间产生一个数字，然后就结束，可以理解为延迟产生数字
- 一种是每隔一段时间就产生一个数字，没有结束符，也就是是可以产生无限个连续的数字(已过时，使用interval代替)

**timer操作符默认情况下是运行在一个`Computation`上的**

## interval

interval操作符是每隔一段时间就产生一个数字，这些数字从0开始，一次递增1直至无穷大，操作符默认情况下是运行在一个`Computation`线程上的，可用于实现计时器。

## range

range操作符是创建一组在从n开始，个数为m的连续数字，比如range(3,10)，就是创建3、4、5…12的一组数字

##  repeat

repeat操作符是对某一个Observable，重复产生多次结果，repeat响应的是onCompleted，当onCompleted执行，便会重新订阅之前的Observable。

## repeatWhen & retryWhen

repeatWhen操作符是对某一个Observable，有条件地重新订阅从而产生多次结果。

具体参考：[RxJava中repeatWhen 和 retryWhen 操作符的解释](https://github.com/bboyfeiyu/android-tech-frontier/blob/master/issue-39/RxJava%E4%B8%ADrepeatWhen%20%E5%92%8C%20retryWhen%20%E6%93%8D%E4%BD%9C%E7%AC%A6%E7%9A%84%E8%A7%A3%E9%87%8A.md)

利用repeatWhen是可以实现轮询的。利用retryWhen可以实现自动重试，**但是不要把重复订阅的逻辑放在初始的Obervable上，而是应该放在调用链上**。


## Empty/Never/Throw

Empty/Never/Throw — 创建行为受限的特殊Observable

  - Empty创建一个不发射任何数据但是正常终止的Observable
  - Never创建一个不发射数据也不终止的Observable
  - Throw创建一个不发射数据以一个错误终止的Observable

这三个操作符生成的Observable行为非常特殊和受限。测试的时候很有用，有时候也用于结合其它的Observables，或者作为其它需要Observable的操作符的参数。

RxJava将这些操作符实现为 empty，never和error。error操作符需要一个Throwable参数，你的Observable会以此终止。这些操作符默认不在任何特定的调度器上执行,如下面代码：


## 注意

- 在订阅Observable时，最好是使用Subscribe，而不是单个的Action，如果使用单个的Action表示只需要结果，如果Observable发送错误，就会抛出。
- 注意主线程退出的问题：当主线程退出后，RxJava的线程池都会关闭。
- 当一个数据序列执行完毕，也表示`subscribe`取消了订阅















