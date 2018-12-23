# Coroutines-Guide

- [Guide to kotlinx.coroutines by example](https://github.com/Kotlin/kotlinx.coroutines/blob/master/docs/coroutines-guide.md)

作为一种语言，它在其标准库中仅提供最小的低级API，以使各种其他库能够利用协同程序。与许多其他具有类似功能的语言不同，async 和 await不是 Kotlin 中的关键字，甚至不是其标准库的一部分。此外，Kotlin的暂停函数概念为异步操作提供了比 futures  和 promises 更安全且更不容易出错的抽象。

kotlinx.coroutines 是由 JetBrains 开发的丰富的协同程序库。它包含本指南涵盖的许多高级协程启用的基元，包括 launch ，async 等。这是关于kotlinx.coroutines 核心功能的指南，其中包含一系列示例，分为不同的主题。为了使用协同程序以及遵循本指南中的示例，您需要添加对kotlinx-coroutines-core 模块的依赖性，如项目 [README](https://github.com/Kotlin/kotlinx.coroutines/blob/master/README.md#using-in-your-projects) 中所述。

## 协程基础

### Your first coroutine

执行下面代码：

```kotlin
import kotlinx.coroutines.*

fun main() {
    GlobalScope.launch { // launch new coroutine in background and continue
        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
        println("World!") // print after delay
    }
    println("Hello,") // main thread continues while coroutine is delayed
    Thread.sleep(2000L) // block main thread for 2 seconds to keep JVM alive
}
```

执行结果：

```
Hello,
World!
```

从本质上讲，协同程序是轻量级的线程，它们被在一些 CoroutineScope 的 context 中 launch 协程构建器启动。在这里，我们在 GlobalScope 中启动一个新的协同程序，这意味着新协程的生命周期仅受整个应用程序的生命周期的限制。

您可以使用 `Thread.sleep（...`）替换 `GlobalScope.launch {...} ` 和`thread{...}`以及`delay(...）`。试试吧。

如果你使用 thread 替换 GlobalScope.launch，编译器将生成以下错误：

```
Error: Kotlin: Suspend functions are only allowed to be called from a coroutine or another suspend function
```

这是因为 delay 是一个特殊的挂起函数，它不会阻塞一个线程，但会挂起协同程序，它只能在协程中使用。

### Bridging blocking and non-blocking worlds

第一个示例在同一代码中混合非阻塞延迟`delay(...)`和阻塞`Thread.sleep(...)`。这很容易让人忘记哪一个阻塞，哪一个非阻塞。我们明确使用runBlocking 协程构建器进行阻塞：

```kotlin
import kotlinx.coroutines.*

fun main() { 
    GlobalScope.launch { // launch new coroutine in background and continue
        delay(1000L)
        println("World!")
    }
    println("Hello,") // main thread continues here immediately
    runBlocking {     // but this expression blocks the main thread
        delay(2000L)  // ... while we delay for 2 seconds to keep JVM alive
    } 
}
```

结果是相同的，但此代码仅使用非阻塞 delay 。调用 runBlocking 的主线程阻塞，直到 runBlocking 内的协程完成。这个例子也可以用更惯用的方式重写，使用runBlocking来包装main函数的执行：

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking<Unit> { // start main coroutine
    GlobalScope.launch { // launch new coroutine in background and continue
        delay(1000L)
        println("World!")
    }
    println("Hello,") // main coroutine continues here immediately
    delay(2000L)      // delaying for 2 seconds to keep JVM alive
}
```

runBlocking 作为适配器，用于启动顶级主协程。我们明确指定其 Unit 返回类型，因为 Kotlin 中一个结构良好的 main 函数必须返回 Unit。

### Waiting for a job

在另一个协程正在工作时，延迟当前协程一段时间以等待另一个协程结束并不是一个好方法，让我们明确等待（以非阻塞的方式）直到我们发布的后台作业完成。

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
//sampleStart
    val job = GlobalScope.launch { // launch new coroutine and keep a reference to its Job
        delay(1000L)
        println("World!")
    }
    println("Hello,")
    job.join() // wait until child coroutine completes
//sampleEnd    
}
```


现在结果仍然相同，但主协程的代码不以任何方式与后台作业的持续时间相关联。好多了


### Structured concurrency