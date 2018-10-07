# RxJava操作符——条件操作符

- `all`  判定是否Observable发射的所有数据都满足某个条件
- `amb`  给定两个或多个Observables，它只发射首先发射数据或通知的那个Observable的所有数据，不管发射的是一项数据还是一个`onError`或`onCompleted`通知。`Amb`将忽略和丢弃其它所有Observables的发射物。
-  `inEmpty`用于判定原始Observable是否没有发射任何数据。  
-  `contains`  判定一个Observable是否发射一个特定的值 ，使用的是equals
-  `exists` 只要任何一项满足条件就返回一个发射true的Observable，否则返回一个发射false的Observable
-  `defaultIfEmpty` 发射来自原始Observable的值，如果原始Observable没有发射任何值，就发射一个默认值
-  `sequenceEqual` 判定两个Observables是否发射相同的数据序列,（相同的数据，相同的顺序，相同的终止状态），它就发射true，否则发射false。
-  `skipUntil`丢弃原始Observable发射的数据，直到第二个Observable发射了一项数据
-  `spWhile` 丢弃Observable发射的数据，直到一个指定的条件不成立
-  `takeUntil` 当第二个Observable发射了一项数据或者终止时，丢弃原始Observable发射的任何数据
-  `takeWhile` 发射Observable发射的数据，直到一个指定的条件不成立

