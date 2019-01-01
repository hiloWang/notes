# Kotlin 概要总结

---
## 1 协程 API 概述

Kotlin 作为一门编程语言，在其标准库中只提供了最低级别的 API，以使各种其他库能够使用协程，与许多其他具有类似功能的语言不同，async 和 await 不是 Kotlin 中的关键字，也不是其标准库的一部分。Kotlin 官方对协程提供的三种级别的能力支持, 分别是:

- 最底层的语言层：susppend 函数、编译器(即编译器对 susppend 的支持)。
- 协程标准库（kotlin.coroutines）：标准库中的底层 API，除了创建更高级的库之外，一般不应该使用它。
- 最上层应用层（kotlinx.coroutines）：大多数基于协程的应用程序级 API 都作为单独的库发布。

### 语言层

语言本身主要提供了对 suspend 关键字的支持, 使用 suspend 修饰的方法称为可以称为挂起函数，Kotlin 编译器会对 suspend 修饰的方法或 lambda 特殊处理，生成一些中间类和逻辑代码。

### 标准库

标准库仅仅提供了少量创建协程的方法，这些底层 API 相对较小，并且除了创建更高级的库之外，不应该使用它们，这些方法定义在`kotlin.coroutines.experimental`包中，API 列表如下 ：

- `createCoroutine()`
- `startCoroutine()`
- `suspendCoroutine()`
- `suspendCoroutineOrReturn()`
- 生成器 API
    - `buildSequence()`
    - `buildIterator()`
    - `yield()`
    - `yieldAll()`

### 应用层

只有与协程相关的核心 API 可以从 Kotlin 标准库获得。这主要包括所有基于协程的库可能使用的核心原语与接口。大多数基于协程的应用程序级 API 都作为单独的库发布，它的实现在 `kotlinx.coroutines` 里面，比如常用的 `launch` 方法, `async` 方法等。

这些库包括：

- kotlinx-coroutines-core：是扩展库核心实现。
- kotlinx-coroutines-jdk8
- kotlinx-coroutines-nio
- kotlinx-coroutines-reactive
- kotlinx-coroutines-reactor
- kotlinx-coroutines-rx2：是 kotlin 协程对 RxJava2 上的扩展，为 RxJava 中的数据源（比如 Observable）提供了一些扩展方法，可以在其上面使用协程 api。
- kotlinx-coroutines-javafx
- kotlinx-coroutines-android：是 kotlin 协程在 android 上的扩展，其提供了一个 UI 线程协程调度器：`Dispatchers.Main`。
- kotlinx-coroutines-swing

---
## 2 核心概念

---
### 2.1 挂起函数

在 Kotlin 中，suspend 函数表示一个可以被挂起的函数，suspend 函数是协程的核心概念之一。使协程可以像写单线程方法一样来编写各种异步操作。

```
suspend fun doSomething(foo: Foo): Bar { …… }
```

这样的函数称为挂起函数，因为调用它们可能挂起协程（如果相关调用的结果已经可用，库可以决定继续进行而不挂起）。挂起函数能够以与普通函数相同的方式获取参数与返回值，**但挂起函数挂起协程时，不会阻塞协程所在的线程**。需要注意的是，挂起函数只能从协程、其他挂起函数以及内联到其中的函数字面值中调用。

#### 传统异步编程

在传统的异步回调的模型中，我们一般需要需要使用回调或阻塞的方式来获取异步结果，比如：

- 使用异步回调(Callback)
- 使用阻塞获取(Future.get())
- RxJava 虽然没有了回调，但还是基于观察者模式在序列流的最末端处理结果
- Java8 的 CompletableFuture，风格和Rx类似

除此之外，还有没有其他更简单的方式呢？有，那就是协程。

#### 同步风格的异步编程

在协程中，可以实现 **异步返回** (在编写程序无需考虑异步等待，却能直接享受到异步结果)，由于 suspend 函数是可以被挂起的。被挂起的函数在恢复执行后，会回到之前的挂起点继续执行。而且，从任何一个挂起点恢复都可以切换调度线程。看下面代码：

```kotlin
private fun asyncReturn(){
        log(-1)
        launch(UI) {
            log(-3)
            val result = async {
                delay(1000L)
                1
            }
            log(-4)
            log("result = ${result.await()}")
            log(-5)
        }
        log(-2)
}
```

打印结果为：

    10:46:46:232 -1
    10:46:46:255 -2
    10:46:46:256 -3
    10:46:46:261 -4
    10:46:47:298 result = 1
    10:46:47:298 -5

其实这个函数将被编译为多个函数，具体可以分为三个部分：

```kotlin
    fun p1() {
        log(-1)
        log(-2)
    }

    fun p2() {
        log(-3)
        doAsync {
            p3(it)
        }
        log(-4)
    }

    fun p3(result: Int) {
        log("result = $result}")
        log(-5)
    }

    fun doAsync(onResult:(Int)->Unit){
        thread {
            Thread.sleep(1000)
            runOnUiThread {
                onResult(1)
            }
        }
    }
```

我们写的单个函数，被编译器处理后根据挂起点被编译成了多个函数，这多个函数由内部的一个状态机进行调用，首先在主线程执行 p1，p1 执行完成转而执行 p2，p2 运行在UI上下文指定的线程中，其实还是主线程，p2 中又启动了一个协程进行异步查询，但是我们直接拿到了这个结果(类似Future)，节奏 p2 执行完成后主线程由于没有任务就挂起了，然后当异步协程返回结果后，主线程又有事情做了，要去处理结果了，于是主线程又去调用 p3 了。

要理解协程我们必须要明白，我们写的代码将被协程分成多个段执行，而这多个段所运行的线程是由协程上下文指定的，我们启动协程的时候可以指定任意的协程上下文，比如上面的UI就是一个协程上下文，由它启动的协程都运行在主线程，而 async 默认的协程上下文是 Kolint 内置的 CommonPool。

---
### 2.2 理解暂停点与 Continuation

一个协程的执行经常是断断续续的: `执行一段, 挂起来, 再执行一段, 再挂起来, ...`，每个挂起的地方是一个 suspension point, 每一小段执行是一个 Continuation，协程的执行流被它的 `"suspension point"` 分割成了很多个 `"Continuation"`，kotlin编译器会把这些 `"suspension point"` 编译成单独的函数，然后根据状态转移去调用不同的挂起点分割开的函数：

```kotlin
public interface Continuation<in T> {
    //调度当前挂起段的协程上下文
    public val context: CoroutineContext

    //挂起段执行完毕，把数据传递到下一段
    public fun resume(value: T)
    //挂起段执行完毕，把异常传递到下一段
    public fun resumeWithException(exception: Throwable)
}
```

协程之间通过 Continuation 协作运行，挂起段由 Continuation 中的 CoroutineContext调度。Continuation 把各个挂起段连接起来。

---
### 2.3 分析协程原理

分析协程套路：

- 分析反编译后的Java代码
- 打断点debug

协程被编译成状态机（单个函数被编译成多个函数）：

![](images/kotlin_coroutine_01_status.jpg)

- 协程有编译器实现，我们编写的代码与实际运行的代码有很多的差别
- suspend函数即状态转移，单个suspend函数被编译成多个函数，即把每一个挂牵点编译成一个单独的函数
- 多个协程 Context 可以组合，类型是 CombinedContext
- suspend 代码块会被编译成继承 CoroutineImpl 类的类型

---
## 3 协程标准库（kotlin.coroutines）API 简介

---
### 1 CoroutineContext

上下文很常见，比如 Android 里的 Context，可以认为上下文是一个编程环境。具体一点就是 “与现在这个工作相关的周围环境”，这个 Context 里有一些和当前编程相关的方法和变量。参考[如何理解上下文](https://www.zhihu.com/question/26387327)

CoroutineContext 即协程的上下文，协程总是运行在一些以 CoroutineContext 类型为代表的上下文中，它们被定义在了 Kotlin 的标准库里。协程上下文是各种不同元素的集合。其中主元素是协程中的 Job。CoroutineContext 的定义如下：

```kotlin
//CoroutineContext是一个接口
public interface CoroutineContext {
    //[]操作符重载，通过key获取对应的 Element，Element 继承自 CoroutineContext，可以理解为通过 key 获取对应的上下文。
    public operator fun <E : Element> get(key: Key<E>): E?

    //累加此上下文的实体，operation 的功能是将 Element  转换为 R。
    public fun <R> fold(initial: R, operation: (R, Element) -> R): R

    //+ 操作符重载，支持上下文之间的组合：协程的上下文可以是多个的组合，组合的上下文可以通过 key 来获取。
    public operator fun plus(context: CoroutineContext): CoroutineContext

    //减去 key，移除一个上下文
    public fun minusKey(key: Key<*>): CoroutineContext
}

//Element 的定义如下
    public interface Element : CoroutineContext {
        //一个用于获取上下文的key
        public val key: Key<*>
    }

// Key的定义如下
public interface Key<E : Element>
```

CoroutineContext 的子类实现

- `AbstractCoroutineContextElement`：CoroutineContext 的基础实现，其构造方法需要一个 Key 参数
- `EmptyCoroutineContext`：EmptyCoroutineContext 是一个空实现，没有任何功能，如果我们在使用协程时不需要上下文，那么我们就用这个对象作为一个占位即可。
- key 用来标识 Context 身份

---
### 2 Continuation

挂起的协程可以作为保持其挂起状态与局部变量的对象来存储和传递。这种对象的类型是 Continuation。Continuation 是一个运行控制类，负责结果和异常的返回，每一个 Continuation 都持有一个协程上下文。Continuation 有继续、持续的意思，Continuation 中定义了继续执行协程的方法：

```kotlin
public interface Continuation<in T> {
    //Continuation持有的上下文
    public val context: CoroutineContext

    //如果我们的程序没有任何异常，那么直接调用这个方法并传入需要返回的值。
    //或者如果我们的程序出了异常，那我们可以通过调用这个方法把异常传递出去。
    public fun resumeWith(result: Result<T>)
}
```

协程的基本操作包括`创建、启动、暂停和继续`，`继续`的操作在 Continuation 当中，而其他的方法都在包级函数当中

---
### 3 ContinuationInterceptor 拦截器

ContinuationInterceptor 继承了 Element，所以它本身是一个协程上下文，然后其添加了拦截功能，可以拦截协程的调度，ContinuationInterceptor 通过拦截修改 Continuation，可用来处理协程调度。

```kotlin
public interface ContinuationInterceptor : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<ContinuationInterceptor>
    //拦截调度，返回新的Continuation
    public fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T>

    public fun releaseInterceptedContinuation(continuation: Continuation<*>) {
    }
}
```

---
### 4 CoroutinesLibrary

`kotlin.coroutines.experimental.CoroutinesLibrary` 是 Kotlin 协程的底层 API，它提供了为数不多的协程底层 API，一般用于构建应用级协程库。

启动一个协程：

```kotlin
public fun <R, T> (suspend R.() -> T).startCoroutine(receiver: R, completion: Continuation<T>)
public fun <T> (suspend  () -> T).startCoroutine(completion: Continuation<T>) {}
```

创建一个协程：

```kotlin
public fun <R, T> (suspend R.() -> T).createCoroutine(receiver: R,completion: Continuation<T>): Continuation<Unit> = SafeContinuation(createCoroutineUnchecked(receiver, completion), COROUTINE_SUSPENDED)
public fun <T> (suspend () -> T).createCoroutine( completion: Continuation<T>): Continuation<Unit> 
```

suspendCoroutine 是一个 suspending 函数，所以它只能在协程中调用，suspendCoroutine 的作用是将当前执行流挂起, 在适合的时机再将协程恢复执行。suspendCoroutine 方法接受一个 lambda，这个 lambda 的返回值就是该函数的返回值，block 有一个参数 continuation，当 block 执行完毕后，调用 continuation 的 resumeWith 方法则返回结果，这个结果就是 suspendCoroutine 的返回值，然后 Kotlin 会切换回之前的挂起点继续调度。

```kotlin
public inline suspend fun <T> suspendCoroutine(crossinline block: (Continuation<T>) -> Unit): T =
        suspendCoroutineOrReturn { c: Continuation<T> ->
            val safe = SafeContinuation(c)
            block(safe)
            safe.getResult()
        }
```

### 5 SequenceBuilder

`kotlin.coroutines.experimental.SequenceBuilder` 是 Kotlin 协程的生成器 API，这是 `kotlin.coroutines` 中仅有的应用级函数，它包括：

- buildSequence()：构建一个惰性求值序列
- buildIterator()：构建一个惰性迭代器
- yield()：转让当前协程转调器的线程(或线程池)的执行权到其他协程
- yieldAll()

---
## 4 应用层（kotlinx.coroutines）API 简介

### 1 CoroutineDispatcher

CoroutineDispatcher 是协程的调度器，由它来调度和处理任务。

```kotlin
public abstract class CoroutineDispatcher :
        AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
            
            public open fun isDispatchNeeded(context: CoroutineContext): Boolean = true

            //分派runnable(块)到给定的上下文的另一个线程的执行。
            public abstract fun dispatch(context: CoroutineContext, block: Runnable)

            public override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> = DispatchedContinuation(this, continuation)

        }

        public operator fun plus(other: CoroutineDispatcher) = other
```

CoroutineDispatcher 继承了 ContinuationInterceptor，显然需要处理协程的调度，所以需要拦截的能力——拦截一个协程在原来线程上的运行，然后调度到另一个线程上执行。`kotlinx.coroutines` 提供了一些 CoroutineDispatcher 的标准实现，每种实现都有其各自的特性，它们被定义在 Dispatchers 对象上：

- `Dispatchers.Unconfined`: 协程运行在调用者的线程，但是当在 coroutine 中挂起之后，后面所在的运行线程将完全取决于调用挂起方法的线程。
- `Dispatchers.Default`：如果在其上下文中未指定调度程序或任何其他连续拦截器, 则所有标准生成器都使用该生成器。它使用共享后台线程的公共池。 对于占用 cpu 资源的计算密集型资源, 这是一个合适的选择。
- `Dispatchers.IO`：适用于执行 IO 任务的调度器。

除此之外，Kotlin 也提供了让开发中自己定义 CoroutineDispatcher 的 API，比如:

- `kotlinx.coroutines.ThreadPoolDispatcher` 中定义的方法：
    - newSingleThreadContext
    - newFixedThreadPoolContext。
- `kotlinx.coroutines.Executors` 中定义的 asCoroutineDispatcher 方法：可以使用 `asCoroutineDispatcher` 扩展函数将任意 `java.util.concurrent.Executor/ExecutorService` 转换为协程调度器，其返回值为 ExecutorCoroutineDispatcher，可以说ExecutorCoroutineDispatcher 是 java Executor 与协程调度器之间的桥梁。

---
### 2 CoroutineScope

CoroutineScope 定义创建协程的范围。每个协程构建器（比如 launch、async）都是 CoroutineScope 的扩展，并继承其 coroutineContext 以自动传播上下文元素和取消。

CoroutineScope 封装了协程的内部状态：`isActive 和 coroutineContext` 属性，isActive 表示协程是否是活跃的(没有完成也没有被取消)，coroutineContext 表示当前这个协程的上下文。所谓通用的协程构建器 receiver，即协程运行 block 都以 CoroutineScope 为运行环境。

每个 coroutine 生成器 (如launch、async 等) 和每个作用域函数 (如 coroutinscope、 withContext 等) 都将自己的作用域与自己的 Job 实例一起提供到它运行的内部代码块中。按照惯例, 它们全都会等待块内的所有协程完成后再完成自己，从而强制执行结构化并发的规则。

CoroutineScope 应该在具有明确定义的生命周期的实体上实现，这些实体负责启动子协同程序。 Android 上的此类实体的示例是 Activity。

CoroutineScope 上的扩展方法：

- actor
- async
- launch
- broadcast
- cancel
- newCoroutineContext
- plus
- produce
- promise（for js）

CoroutineScope 的子类：

- GlobalScope：全局的 CoroutineScope，不受任何工作约束。
- ActorScope：actor 协程构建器的 CoroutineScope。
- ProducerScope： produce 协程构建器的 CoroutineScope。

具体可以参考：[CoroutineScope API 文档](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/)

---
### 3 kotlinx.coroutines 中的协程构建器

`kotlinx.coroutines` 是 Kotlin 官方的协程高级 API，它被作为多个单独的组件发布，其中 `kotlinx-coroutines-core` 是平台无关异步编程，它提供了众多启动协程的 API：

```
    - kotlinx.coroutines.experimental.Builder：协程构造器
    - kotlinx.coroutines.experimental.Delay：协程的延时实现
    - kotlinx.coroutines.experimental.Yield：协程的转让执行权实现
    - kotlinx.coroutines.experimental.Job：表示一个协程调度
    - kotlinx.coroutines.experimental.Deferred：表示一个有返回结果的协程调度
```

下面是我们在协程编程中经常会用到的方法：

#### runBlocking

```kotlin
public fun <T> runBlocking(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> T): T {
    //......
    return coroutine.joinBlocking()
}
```

作为一个适配器，用于启动顶级主协程，运行一个新的协程并且阻塞当前线程直到协程执行完毕，当前线程可中断。该 runBlocking 函数不是用来当作普通协程函数使用的，它的设计主要是用来桥接普通阻塞代码和挂起风格的（suspending style）的非阻塞代码的, 例如用在 main 函数中，或者用于测试用例代码中。

#### delay

在给定的时间内延迟协程，仅用于协程

```kotlin
suspend fun delay(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS){}
```

#### launch

启动一个新的协程并返回一个代表协程引用的Job对象，当前线程不会被阻塞，这是最常用的 Coroutine builders，在 Android 中我们可以调用 `launch(Dispatchers.Main) {}` 来启动一个协程。

```kotlin
public fun launch( context: CoroutineContext,start: CoroutineStart = CoroutineStart.DEFAULT,block: suspend CoroutineScope.() -> Unit): Job {
     //......
    return coroutine
}
```

#### async

启动一个带有返回值的协程Deferred，当前线程不会被阻塞。

```kotlin
public fun <T> async(context: CoroutineContext,start: CoroutineStart = CoroutineStart.DEFAULT,block: suspend CoroutineScope.() -> T): Deferred<T> {
    //......
    return coroutine
}
```

#### withContext

withContext 不会创建新的协程，在指定协程上运行挂起代码块，并挂起该协程直至代码块运行完成。

```
public expect suspend fun <T> withContext(
    context: CoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend () -> T
): T
```

#### run

在协程中，根据给定的 CoroutineContext 来调用一个 suspending block，并等待这个 suspending block 执行完成并返回结果。

```kotlin
public suspend fun <T> run(context: CoroutineContext,  start: CoroutineStart = CoroutineStart.DEFAULT, block: suspend () -> T): T
```

#### timeout

`kotlinx.coroutines.Timeout` 中定义了一些协程调度超时机制的相关方法：

```kotlin
public suspend fun <T> withTimeout(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS, block: suspend () -> T): T {}
public suspend fun <T> withTimeoutOrNull(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS, block: suspend () -> T): T? {}
```

#### Job

```
public interface Job : CoroutineContext.Element {}
```

Job 通过协程启动函数launch创建并返回，Job 可以被取消，Job 的 join 函数会让当前协程会等到代表 Job 的协程运行完毕后再执行。Job 是协程创建的后台任务的概念，它持有该协程的引用。Job 接口实际上继承自 CoroutineContext类型。一个 Job 有三种状态：`isActive、isCompleted、isCancelled`。

| 状态 | isActive | isCompleted | isCancelled |
| --- | --- | --- | --- |
| New(可选的初始状态) | `false` | `false` | `false` |
| Active(默认的初始状态) | `true` | `false` | `false` |
| Completing (可选的瞬时状态) | `true` | `false` | `false` |
| Cancelling (可选的瞬时状态) | `false` | `false` | `true` |
| Cancelled (最终状态) | `false` | `true` | `true` |
| Completed (最终状态) | `false` | `true` | `false` |

#### Deferred

```kotlin
public interface Deferred<out T> : Job {
   public suspend fun await(): T
}
```

由协程启动函数 async 创建并返回，其多一个 await函数，当协程执行完毕，await方法会被调用并返回值(但并不是回调方式)。Deferred 继承自 Job，比 Job 多了一种状态：isCompletedExceptionally：

| 状态 | isActive | isCompleted |isCompletedExceptionally| isCancelled |
| --- | --- | --- | --- |
| New (可选的初始状态)|`false`|`false`|`false`|`false`|
| Active (默认的初始状态)|`true`|`false`|`false`|`false`|
| Completing(可选的瞬时状态)|`true`|`false`|`false`|`false`|
| Cancelling (可选的瞬时状态)| `false` |`false`| `false`|`true`|
| Cancelled (最终状态)|`false`|`true`|`true`|`true`|
| Resolved (最终状态)|`false`|`true`|`false`|`false`|
| Failed (最终状态)|`false`|`true`|`true`|`false`|

---
### 4 CoroutineStart

CoroutineStart 是一个枚举类型，它是一个选项值，表示如何启动协程，比如 launch 和 async 协程构建器都需要传递 CoroutineStart 类型参数，但这个参数默认值 CoroutineStart.DEFAULT，CoroutineStart 的值和说明如下：

- `DEFAULT`：表示根据其上下文立即安排协程执行。
- `LAZY`：表示延迟启动协程。
- `ATOMIC`：原子 (以不可取消的方式) 根据其上下文安排要执行的 coroutine。
- `UNDISPATCHED`：立即执行 coroutine, 直到其在当前线程中的第一个挂点。

---
## 5 总结

虽然有这么多概念，但是简单的使用协程只需要掌握以下函数：

```
 fun launch(): Job //启动一个新的协程并返回一个代表协程引用的Job对象
 fun async(): Deferred//用于启动一个洗的协程，返回的Deferred对象表示带有返回值的协程
```