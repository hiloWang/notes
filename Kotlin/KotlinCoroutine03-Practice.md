# Kotlin 实践

---
## 在 Android 上使用协程

因为协程还是实验性功能，需要在 gradle 中开启协程：

```kotlin
//app/build.gradle
kotlin {
    experimental {
        coroutines "enable"
    }
}
```

然后添加相关依赖：

```kotlin
    // coroutines 核心
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:0.22.5"
    // coroutines for android
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:0.22.5"
```

coroutines-android 中有些什么东西呢？其实只有四个文件：

- HandlerContext
- AndroidExceptionPreHandler
- AndroidExceptionPreHandlerKt.class
- HandlerContextKt.class
- HandlerContext 为 Kotlin 协程提供了 Android平台的 Hander上下文， `UI` 是 HandlerContext 的一个全局实例，让协程运行在 Android 的 UI线程。

---
## 传统的异步编程如何重构为协程风格

使用 CompletableDeferred

```kotlin
interface Callback {
    fun onSuccess(result: String)
    fun onError(e: Throwable)
}

/*传统异步编程如果重构为协程*/
fun loadAsync(callback: Callback) {
    thread {
        try {
            Thread.sleep(1000)
            if (Math.random() > 0.5f) {
                callback.onSuccess("HelloWorld")
            } else {
                throw IllegalStateException("This is a Demonstration Error.")
            }
        } catch (e: Throwable) {
            callback.onError(e)
        }
    }
}

suspend fun load(): String {
    val completableDeferred = CompletableDeferred<String>()
    loadAsync(object : Callback {
        override fun onSuccess(result: String) {
            completableDeferred.complete(result)
        }

        override fun onError(e: Throwable) {
            completableDeferred.completeExceptionally(e)
        }
    })
    return completableDeferred.await()
}
```

---
## 以同步代码的风格 show 一个 Dialog

```kotlin
//在 UIContext 启动一个协程
fun launchUI(start: CoroutineStart = CoroutineStart.DEFAULT, parent: Job? = null, block: suspend CoroutineScope.() -> Unit) =
        launch(UI, start, parent, block)

//展示一个 Dialog，然后可以直接获取其结果，suspendCoroutine 用于暂停当前协程，等待新的协程执行结束
suspend fun Context.confirm(title: String, message: String = "") = suspendCoroutine<Boolean> { continuation ->
    alert {
        this.title = title
        this.message = message

        negativeButton("Cancel") {
            continuation.resume(false)
        }
        positiveButton("OK") {
            continuation.resume(true)
        }
        onCancelled {
            continuation.resume(false)
        }
    }.show()
}
```

使用：

```kotlin
launchUI {
  if(confirm("提示", "确认注销吗?")){
     toast("开启了")
  } else {
     toast("取消了")
}
```

---
## 序列与协程

序列可以避免类似 map、filter 等操作符创建过多的临时集合，协程 `kotlin.coroutines.experimental.SequenceBuilder` 中的 buildSequence 可以让序列与协程协同工作。buildSequence 接收一个 suspend 函数，其文档说明为：`Builds a [Sequence] lazily yielding values one by one.`即构建一个序列，惰性的一个接着一个地生产变量。

关于序列与协程可以参考 [build-sequence](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines.experimental/build-sequence.html)，主要的函数为：

- buildSequence
- yield
- yieldAll

下面是一个延迟计算斐波那契数列的列子：

```kotlin
/**
斐波那契数列公式：
    F0 =0
    F1=1
    Fn=F(n-1)+F(n-2);(n>=2)
 */
fun main(args: Array<String>) {
    //使用协程，可以实现懒计算和缓存之前的计算结果
    val fibonacci = buildSequence {
        yield(1) //yield 也是一个suspend函数，可以暂停 buildSequence，发送数据到序列的下游中
        var cur = 1
        var next = 1
        while (true) {
            yield(next) // next Fibonacci number，转让执行权
            println("------3")
            val tmp = cur + next
            cur = next
            next = tmp
        }
    }

    for (i in fibonacci) {
        println(i)
        if (i > 100) break //大于100就停止循环
    }
}
```

## 协程的使用姿势（[google-codelabs：kotlin-coroutines](https://codelabs.developers.google.com/codelabs/kotlin-coroutines/#0)）

在 Repository 层提供 LiveData，用于让上层监听数据加载的结果。

```kotlin
  val title: LiveData<String> by lazy<LiveData<String>>(NONE) {
        Transformations.map(titleDao.loadTitle()) { it?.title }
   }
```

Repository  提供加载数据的方法，然后通过协程来执行耗时方法：

```kotlin
//刷新标题
suspend fun refreshTitle() {
        withContext(Dispatchers.IO) {
            try {
                val result = network.fetchNewWelcome().await()
                titleDao.insertTitle(Title(result))
            } catch (error: FakeNetworkException) {
                throw TitleRefreshError(error)
            }
        }
}

//suspendCoroutine 可以让异步风格的代码转化为同步风格的代码
suspend fun <T> FakeNetworkCall<T>.await(): T {
    return suspendCoroutine { continuation ->
        addOnResultListener { result ->
            when (result) {
                is FakeNetworkSuccess<T> -> continuation.resume(result.data)
                is FakeNetworkError -> continuation.resumeWithException(result.error)
            }
        }
    }
}
```

在 ViewModel 中使用 Repository 

```kotlin
class MainViewModel(private val repository: TitleRepository) : ViewModel() {
    
    //用于关联生命周期
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        //取消协程的执行
        viewModelJob.cancel()
    }
    
    //直接暴露 Repository 层的 LiveData 给 UI
    val title = repository.title
    
    fun refreshTitle() {
        launchDataLoad {
            repository.refreshTitle()
        }
    }
    
    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return uiScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: TitleRefreshError) {
                _snackBar.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }
    
}
```

