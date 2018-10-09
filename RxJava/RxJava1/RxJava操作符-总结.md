# 创建型

|  名称 | 作用  | 线程  |
| ------------ | ------------ | ------------ |
| create  | 创建Observable的根操作符  | *  |
|  just |  把其他类型的对象和数据类型转化成Observable  | *  |
| from  |  把其他类型的对象和数据类型转化成Observable |  * |
|  defer |  直到有订阅者订阅时才创建Observable | *  |
| timer  | 一种是隔一段时间产生一个数字，然后就结束，可以理解为延迟产生数字，一种是每隔一段时间就产生一个数字，没有结束符  | `computation`  |
|  interval |  每隔一段时间就产生一个数字，没有结束符 |  `computation` |
|  repat  | 重复订阅  | `trampoline` |
|  repatWhen | 有条件的重复订阅  | `trampoline `|
|range|创建一组在从n开始，个数为m的连续数字|*|
|empty|创建一个不发射任何数据但是正常终止的Observable|*|
|error|创建一个不发射数据以一个错误终止的Observable|*|
|never|创建一个不发射数据也不终止的Observable|*|


# 过滤型

|  名称 | 作用  | 线程  |
| ------------ | ------------ | ------------ |
|filter|过滤数据|*|
|take/takeLast|过滤数据|*|
|distinct|过滤重复数据|*|
|distinctUntilsChanged|过滤连续重复数据|*|
|first/last/firstOrDefault/lastOrDefault|过滤数据，first对于没有数据发射会抛出异常|
| skip/skipLast|过滤数据|定时的skip在`computation`|
|elementAt/elementAtOrEefault|获取指定数据|*|
|sample(throttleLast)|定期采样，取最后个|`computation`|
|timeOut|定义时间段被不发射数据则抛出异常|`computation`|
|ignoreElements|过滤所有数据|*|
|debounce和throttleWithTimeout|过滤发射频率过快的数据，取最后一个|默认`computation`|
|throttleFirst|定期采样，取第一个|`computation`|
|single|用于提取单个数据，超过单个或没有则抛出异常|*|
|OfType|用于根据类型提取数据，比如String.class|*|

# 转换型
|  名称 | 作用  | 线程  |
| ------------ | ------------ | ------------ |
|map|数据转换||
|flatMap|无序一对多变换||
|flatMapIterable|||
|concatMap|有序一对多变换||
|switchMap|一对多变换，会丢弃旧数据||
|buffer|定期收集后，发送List|`computation `|
|scan|累加变换||
|window|定期收集后，发送Observable|`computation`|
|cast|类型转换|||


# 组合
|  名称 | 作用  | 线程  |
| ------------ | ------------ | ------------ |
|merge/mergeWith|合并多个Observable，合并将他们发射的数据然后逐个发射，不保证顺序||
|mergeDelayError|基于merge，增加了对error的处理||
|zip/zipWith|合并多个Observable发射的数据，应用组合函数后再发送||
| combineLatest|略||
|join|略||
|and/them/when|略||
|swtch|对于订阅的多个Observable，当第二个Observable发射数据，则立即取消第一个||
|startWith|在Observable发射的数据序列前添加定义的数据||

# 错误处理
|  名称 | 作用  | 线程  |
| ------------ | ------------ | ------------ |
|onErrorReturn|响应错误，返回默认值|*|
|onErrorResumeNext|响应错误，开始一个新的Observable|*|
|onExceptionResumeNext|响应Exception，开始一个新的Observable|*|
|retry|响应onError，重试|`trampoline`|
|retryWhen|响应onError，有条件的重试|`trampoline`|



# 辅助与变换操作
|  名称 | 作用  | 线程  |
| ------------ | ------------ | ------------ |
| delay  | 延迟发生数据  | `computation`  |
| delaySubscription | 延迟订阅   |  `computation` |
| doOnComolete/doOnError/doOnUnsubscribe  | 略  |  * |
| doOnEach/doOnNext  |  当onNext是被调用 |  * |
| doOnTerminate/doAfterTerminate  |  不管是完成还是错误都被调用 | *  |
| doOnSubscribe  | 被订阅时调用  | *  |
| Materialize/Dematerialize  | 把所有事件都转换成integerNotification/反转换 | * |
| Serialize  |  强制一个Observable连续调用并保证行为正确  | *  |
| TimeInterval  | 记录连续发射的数据的间隔 | *  |
| timeTamp  | 记录发射数据的时间戳  | *  |
| using | 创建Observable订阅周期内使用的数据  | *  |
| getIterator/toFuture  | BlockingObservable才有  | *  |
|toBolcking| 转换成阻塞的Observable，只用于测试或演示|*|
| toIterable  |  BlockingObservable才有 | *  |
| toList  | 多个数据转换成列表  |  * |
| toMap  |  多个数据转换成map | *   |
| toMultiMap  | 多个数据转换成map   |  *  |
| toSortedList  |  多个数据转换成有序列表   |  *  |
| nest  | 多个数据转换成发射此数据的Observable  |  *  |


# 条件操作符
|  名称 | 作用  | 线程  |
| ------------ | ------------ | ------------ |
| All   |   判定是否Observable发射的所有数据都满足某个条件   |   *  |     
| Amb   |   给定两个或多个Observables，它只发射首先发射数据或通知的那个Observable的所有数据   |   *  |     
| Contains   |  判定一个Observable是否发射一个特定的值    |  *   |     
| InEmpty   |   用于判定原始Observable是否没有发射任何数据。   |     |     
| Exists   |   只要任何一项满足条件就返回一个发射true的Observable，否则返回一个发射false的Observable   |  *   |     
| DefaultIfEmpty   |  发射来自原始Observable的值，如果原始Observable没有发射任何值，就发射一个默认值    |   *  |     
| SequenceEqual   |  判定两个Observables是否发射相同的数据序列    |     |     
|  SkipUntil  |  丢弃原始Observable发射的数据，直到第二个Observable发射了一项数据    |   *  |
|  SkipWhile  |  丢弃Observable发射的数据，直到一个指定的条件不成立    | *    |     
|  TakeUntil  |   当第二个Observable发射了一项数据或者终止时，丢弃原始Observable发射的任何数据   |  *   |     
|  TakeWhile  |   发射Observable发射的数据，直到一个指定的条件不成立   |  *   |     


# 聚合操作符

|  名称 | 作用  | 线程  |
| ------------ | ------------ | ------------ |
|count/countLong |  用于计算数据项的个数并发射结果| *|
|concat/concatWith |顺序连接多个Observables |* |
|reduce |对序列使用reduce()函数并发射对应的结果 | *|
|collect | 将原始Observable发射的数据放到一个单一的可变的数据结构中，然后返回一个发射这个数据结构的Observable |* |

# 可连接操作符

|  名称 | 作用  | 线程  |
| ------------ | ------------ | ------------ |
| publish  | publish可把一个普通的操作符变为一个`ConnectableObservable`  |  * |
| replay  | repaly也是把一个普通的操作符变为一个`ConnectableObservable`，并且保证所有的观察者收到相同的数据序列  | *  |
| connect  | 调用`ConnectableObservable`的`connect`方法会让它后面的Observable开始给发射数据给订阅者  |  * |
| refCount | refCount让一个可连接的Observable行为像普通的Observable  | *  |
| share  | share == publish()+refCount()  | *  |






















