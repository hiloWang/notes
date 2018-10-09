# 使用RxJava插件

插件让你可以用多种方式修改RxJava的默认行为：

*   修改默认的计算、IO和新线程调度器集合
*   为RxJava可能遇到的特殊错误注册一个错误处理器
*   注册一个函数记录一些常规RxJava活动的发生

---
## 1 线程调度

```java
    RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
                @Override
                public Action0 onSchedule(Action0 action) {
                    System.out.println("onSchedule " + action);
                    return super.onSchedule(action);
                }
    
                @Override
                public Scheduler getComputationScheduler() {
                    return super.getComputationScheduler();
                }
    
                @Override
                public Scheduler getIOScheduler() {
                    return super.getIOScheduler();
                }
    
                @Override
                public Scheduler getNewThreadScheduler() {
                    return super.getNewThreadScheduler();
                }
            });
```

---
## 2 全局错误处理

RxJava会开始使用通过插件注册的错误处理器处理传递给 `Subscriber.onError(Throwable)` 的错误。注意，设置了全局异常处理**不代表**就可以在订阅时不处理异常了。

```java
     RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
                @Override
                public void handleError(Throwable e) {
                    System.out.println("RxJavaErrorHandler" + e);
                }
            });
```

---
## 3 全局函数调用记录

可以注册一个函数用于记录日志或者性能数据收集，RxJava在某些常规活动时会调用它。

```java
     RxJavaPlugins.getInstance()
                    .registerObservableExecutionHook(new RxJavaObservableExecutionHook() {
                        @Override
                        public <T> Observable.OnSubscribe<T> onCreate(Observable.OnSubscribe<T> f) {
                            System.out.println("RxJavaObservableExecutionHook onCreate-->" + f);
                            return super.onCreate(f);
                        }

                        @Override
                        public <T> Observable.OnSubscribe<T> onSubscribeStart(Observable<? extends T> observableInstance, Observable.OnSubscribe<T> onSubscribe) {
                            System.out.println("RxJavaObservableExecutionHook onSubscribeStart-->" + observableInstance + "   " + onSubscribe);
    
                            return super.onSubscribeStart(observableInstance, onSubscribe);
                        }
    
                        @Override
                        public <T> Subscription onSubscribeReturn(Subscription subscription) {
                            System.out.println("RxJavaObservableExecutionHook onSubscribeReturn-->" + subscription);
    
                            return super.onSubscribeReturn(subscription);
                        }
    
                        @Override
                        public <T> Throwable onSubscribeError(Throwable e) {
                            System.out.println("RxJavaObservableExecutionHook onSubscribeError-->" + e);
    
                            return super.onSubscribeError(e);
                        }
    
                        @Override
                        public <T, R> Observable.Operator<? extends R, ? super T> onLift(Observable.Operator<? extends R, ? super T> lift) {
                            System.out.println("RxJavaObservableExecutionHook onLift-->" + lift);
    
                            return super.onLift(lift);
                        }
                    });
```