# Coroutines-Guide

- [Guide to kotlinx.coroutines by example](https://github.com/Kotlin/kotlinx.coroutines/blob/master/docs/coroutines-guide.md)

作为一种语言，它在其标准库中仅提供最小的低级API，以使各种其他库能够利用协程。与许多其他具有类似功能的语言不同，async 和 await不是 Kotlin 中的关键字，甚至不是其标准库的一部分。此外，Kotlin的暂停函数概念为异步操作提供了比 futures  和 promises 更安全且更不容易出错的抽象。

kotlinx.coroutines 是由 JetBrains 开发的丰富的协程库。它包含本指南涵盖的许多高级协程启用的基元，包括 launch ，async 等。这是关于kotlinx.coroutines 核心功能的指南，其中包含一系列示例，分为不同的主题。为了使用协程以及遵循本指南中的示例，你需要添加对kotlinx-coroutines-core 模块的依赖性，如项目 [README](https://github.com/Kotlin/kotlinx.coroutines/blob/master/README.md#using-in-your-projects) 中所述。

## Basic（协程基础）

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

从本质上讲，协程是轻量级的线程，在 CoroutineScope 定义的协程上下文中，它们 launch coroutine builder（launch协程构建器） 启动。在这里，我们在 GlobalScope 中启动一个新的协程，这意味着新协程的生命周期仅受整个应用程序的生命周期的限制。

你可以使用 `Thread.sleep（...`）替换 `GlobalScope.launch {...} ` 和`thread{...}`以及`delay(...）`。试试吧。

如果你使用 thread 替换 GlobalScope.launch，编译器将生成以下错误：

```
Error: Kotlin: Suspend functions are only allowed to be called from a coroutine or another suspend function
```

这是因为 delay 是一个特殊的挂起函数，它不会阻塞一个线程，但会挂起协程，它只能在协程中使用。

### Bridging blocking and non-blocking worlds

第一个示例在同一代码中混合非阻塞延迟`delay(...)`和阻塞`Thread.sleep(...)`。这很容易让人忘记哪一个是阻塞，哪一个是非阻塞。现在我们明确使用runBlocking 协程构建器进行阻塞：

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

结果是相同的，但此代码仅使用非阻塞 delay 。调用 runBlocking 的主线程将被阻塞，直到 runBlocking 内的协程完成。这个例子也可以用更惯用的方式重写，使用 runBlocking 来包装 main 函数的执行：

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

在另一个协程正在工作时，延迟当前协程一段时间以等待另一个协程结束并不是一个好方法（因为不确定具体要多久被等待的协程才会返回），让我们明确地等待（以非阻塞的方式）直到由我们启动的 Job 完成。

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

现在结果仍然相同，但主协程的代码与后台作业的持续时间无关了（不许再指定具体的等待时间）。好多了

### Structured concurrency

对于协程的实际使用仍有一些需求，当我们使用 GlobalScope.launch 时，我们创建了一个顶级协程。尽管它很轻量级，但它在运行时仍会消耗一些内存资源。如果我们忘记保留对新启动的协程的引用，它仍会运行。如果协程中的代码挂起怎么办？（例如，我们错误地延迟了太久），如果我们启动太多的协程并且内存不足会怎么样？必须手动保持对所有已启动的协程的引用并加入它们是容易出错的。

有一个更好的解决方案。我们可以在代码中使用 Structured concurrency。而不是在 GlobalScope 中启动协程，就像我们通常使用线程一样（线程总是全局的），我们可以在我们正在执行的操作的特定范围内启动协程。

在我们的示例中，我们使用 runBlocking coroutine builder 将 main 函数转换为协程。每个协程构建器（包括 runBlocking）都将 CoroutineScope 的实例添加到其代码块的范围内。我们可以在这个范围内启动协程，而无需明确地加入它们，因为在其范围内启动的所有协程完成之前，外部协程（在我们的示例中为 runBlocking）不会完成。因此，我们可以使我们的示例更简单：

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking { // this: CoroutineScope
    launch { // launch new coroutine in the scope of runBlocking
        delay(1000L)
        println("World!")
    }
    println("Hello,")
}
```

### Scope builder

除了由不同的 builders（比如runBlocking）提供的协程范围，可以使用 coroutineScope 构建器声明自己的作用域。它会创建新的协程范围，并且其在所有已启动的子项完成之前不会完成。runBlocking 和 coroutineScope 之间的主要区别在于后者在等待所有子协程完成时不会阻塞当前线程。

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking { // this: CoroutineScope
    launch { 
        delay(200L)
        println("Task from runBlocking")
    }
    
    coroutineScope { // Creates a new coroutine scope
        launch {
            delay(500L) 
            println("Task from nested launch")
        }
    
        delay(100L)
        println("Task from coroutine scope") // This line will be printed before nested launch
    }
    
    println("Coroutine scope is over") // This line is not printed until nested launch completes
}
```

执行结果：

    Task from coroutine scope
    Task from runBlocking
    Task from nested launch
    Coroutine scope is over

### Extract function refactoring

让我们将 `launch {...}` 中的代码块提取到一个单独的函数中，当你对此代码执行“Extract function”重构时，你将获得一个带有 suspend 修饰符的新函数。这是你的第一个 suspending function。可以在协程内部像使用常规函数一样使用 suspending functions，但是 suspending functions 的附加功能是它们可以反过来使用其他的 suspending functions ，例如本例中的 delay ，暂停正在执行的协程。

```kotlin
fun main() = runBlocking {
    launch { doWorld() }
    println("Hello,")
}

// this is your first suspending function
suspend fun doWorld() {
    delay(1000L)
    println("World!")
}
```

但是，如果提取的函数包含了一个在当前作用域上调用的 coroutine builder（协程构建器，比如runBlocking），该怎么办？在这种情况下，提取的函数上的 suspend 修饰符是不够的，在 CoroutineScope 上创建一个 doWorld 扩展方法是其中一个解决方案，但它可能并不总是适用，因为它不会使 API 更清晰。惯用解决方案是有一个明确的 CoroutineScope 作为包含目标函数的类中的字段，或者也可以是隐式的，当外部类实现了 CoroutineScope 时，作为最后的手段，可以使用 `CoroutineScope(coroutineContext)`，但是这种方法在结构上是不安全的，因为你不再能够控制此方法的执行范围，只有私有 API 才能使用此构建器。

### Coroutines ARE light-weight

执行下面代码：

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
    repeat(100_000) { // launch a lot of coroutines
        launch {
            delay(1000L)
            print(".")
        }
    }
}
```

其创建了 100000 个协程，1 秒之后，每个协程都打印了一个 `.`，如果使用线程，大部分情况下将得到一个 OOM。

### Global coroutines are like daemon threads

下面代码在  GlobalScope 上启动了一个长期运行的协程，一秒钟打印两次"I'm sleeping"，然后在 delay 一段时间后它从 main 函数返回了。

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
//sampleStart
    GlobalScope.launch {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // just quit after delay
//sampleEnd    
}
```

运行代码，将会看到程序打印三行然后终止：

    I'm sleeping 0 ...
    I'm sleeping 1 ...
    I'm sleeping 2 ...

在 GlobalScope 中启动的协程不会使进程保持运行状态。它们就像守护程序线程。一旦主函数返回，它们也会立即停止运行。


## Cancellation and timeouts（取消与超时）

### Cancelling coroutine execution

在一个长时间运行的程序中，你可能需要细粒度地控制你的后台协程，比如用户可能关闭了启动了一个协程的界面，此时协程和其执行结构都不需要了，协程的操作也可以被取消了，launch 函数返回一个 Job 可以用来取消正在运行的协程。

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
//sampleStart
    val job = launch {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancel() // cancels the job
    job.join() // waits for job's completion 这里不加结果也是一样的
    println("main: Now I can quit.")
//sampleEnd    
}
```

它产生以下输出：

    I'm sleeping 0 ...
    I'm sleeping 1 ...
    I'm sleeping 2 ...
    main: I'm tired of waiting!
    main: Now I can quit.

一旦 main 调用 job.cancel()，我们不会从另一个协程看到任何输出，因为它被取消了，还有一个 Job 扩展函数 cancelAndJoin，它结合了取消和连接调用。

### Cancellation is cooperative

协程的取消是需要协作的，协程代码必须协作才能取消，kotlinx.coroutines中的所有挂起函数都是可取消的。它们会检查协程的 cancellation 状态并在取消时抛出CancellationException。然而如果一个协程正在计算任务中并且没有检查取消，那么其将无法被取消，就像下面代码展示的示例一样：

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
//sampleStart
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { // computation loop, just wastes CPU
            // print a message twice a second
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
//sampleEnd    
}
```

执行代码可以返现，即使调用了 cancelAndJoin，程序依然在打印信息。

### Making computation code cancellable

这里有两种方式让协程计算代码变得可以被取消，第一个是定义调用一个检测取消的 suspending function，出于这种目的， yield 方法是一个不错的选择；另一个方式是明确地检测取消状态，让我们来试试后一种。

把之前实例中的 `while (i < 5)` 替换为 `while (isActive)` 并且从新运行它：

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
//sampleStart
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive) { // cancellable computation loop
            // print a message twice a second
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
//sampleEnd    
}
```

正如你所见，现在循环被取消了，isActive 是一个扩展属性，可通过 CoroutineScope 对象在 coroutine 代码中使用。

### Closing resources with finally

在取消一个可以被取消的 suspending functions（处于暂停状态的） 时将会抛出 CancellationException ，可以用一种通常的方式来处理，比如：`try {...} finally {...} ` 表达式，当取消协程时，Kotlin使用函数正常执行其终结操作：

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
//sampleStart
    val job = launch {
        try {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            println("I'm running finally")
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
//sampleEnd    
}
```

join 和 cancelAndJoin都等待所有完成操作完成，所以上面的示例产生如下输出：

    I'm sleeping 0 ...
    I'm sleeping 1 ...
    I'm sleeping 2 ...
    main: I'm tired of waiting!
    I'm running finally
    main: Now I can quit.

### Run non-cancellable block

任何在前一个示例的 finally 块中使用挂起函数的尝试都会导致 CancellationException，因为运行此代码的协程已经取消了，通常，这并不是一个问题，因为所有表现良好的关闭操作（关闭文件，取消 job或关闭任何类型的通信通道）通常都是非阻塞并且不涉及其他 suspending functions，然而，在罕见的情况下，当你需要在取消的协程中暂停时，你可以使用withContext 函数和 NonCancellable 上下文将相应的代码包装在 `withContext(NonCancellable){...}`中，如下例所示：


```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
//sampleStart
    val job = launch {
        try {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            withContext(NonCancellable) {
                println("I'm running finally")
                //如果不使用 withContext(NonCancellable) {}，则下面两行代码不会执行
                delay(1000L)
                println("And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
//sampleEnd    
}
```

### Timeout