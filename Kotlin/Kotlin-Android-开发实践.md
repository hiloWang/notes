# Kotlin Android 开发实践

Kotlin 为 Android 开发提供了许多的特新，比如Anko库和extension插件，这些都能在很多程度上帮助我们高效开发，但是在我看来，更为重要的是Kotlin本身的一些语言特性，比如 **代理、扩展、高阶函数等等**。利用这些灵活的特性或许在Android App的架构上也能引发很多新的思考与实践。以下我在实践过程中的一些记录。

---
## 1  利用Kotlin特性

1. 如何与现有的Java库兼容，null类型处理
2. 熟悉Kotlin的基本语法
3. 掌握Kotlin的特性功能，是开放更加高效
   1. 委托
   2. 扩展

---
## 2 Kotlin extension

具体参考 [extension keep](https://github.com/Kotlin/KEEP/blob/master/proposals/android-extensions-entity-caching.md)

1. 在 Activity 和 Fragment 中直接使用 id 引用 view。
2. 在 1.1.4 版本发布后，支持在 ViewHolder、自定义 View、 甚至是自定义布局容器中直接使用 id 引用 view，只需要实现 LayoutContainer 接口，同时支持使用 `@ContainerOptions` 指定 View 的缓存容器。
3. 支持 Parcelable 注解：通过 Parcelize 自动生成 Parcleable 实现。

遇到的问题与解决方法：

- 当两个布局中的控件 id 同名时，如何解决冲突：1 重名的 View id；2 使用 as 别名解决冲突

上面所说的 2 和 3 需要配置开启实验性功能：

```groovy
androidExtensions {
    experimental = true
}
```

---
## 3 Anko 的集成与使用

### 3.1 Common库

- 快速的构建Dialog
- 使用intentFor方面的打开Activity
- 更加方便的Log库
- `applyRecursively()`方法遍历View树
- `0xff0000.opaque`颜色值方法
- 各种 Dimensions 的转换

### 3.2 协程

anko 基于 `kotlin协程` 和 `android协程库` 做了扩展，提供了下面两个函数：

- `asReference()`用于避免内存泄漏
- `bg{}`用于在异步线程执行任务

---
### 3.3 Anko Layouts

Anko Layouts 允许使用 DSL 的方式来创建布局，切使用起来非常方便，**同时还提供了带协程上下文的 onClick 扩展**

#### 基本功能

1. Layouts可扩展，只需要在ViewManager上添加对应View的扩展
2. 可以使用Gradle继承Anko所有的库，也可以单独继承Anko的Layouts库
3. 使用AnkoComponent可以支持布局的预览，当然这需要Anko的插件支持，且插件只支持AndroidStudio2.4以上
4. Layouts的Listeners支持使用协程
5. Listeners支持自定义coroutine context
6. 在内部类中，使用 `ctx` 就可以引用外部类的this引用(Activity中)
7. Anko Layouts的DSL支持include xml布局
8. Anko的插件有两个功能
    - DSL布局预览
    - xml布局转换为DSL布局
9. 在Activity中使用DSL布局，不需要再调用setContentView方法，框架自动完成

#### DSL 的原理与 DSLMarker 元注解

1. Anko库给 Activity 添加了很多的扩展方法
2. 在Fragment中需要先调用 `UI` 方法获取 AnkoContext 才能进行DSL布局编程
3. DSLMarker 用于解决嵌套的作用域引用混乱的问题，被 DSLMarker元注解标注的注解，用于在某些类或类型作标注，防止 implicit receiver(隐式接收器)：所有被 DSLMarker 标注的类的子类，子类间的嵌套 implicit receiver 是互斥的
4. 在 `verticalLayout` 中发现接收者是 `_LinearLayout`，要多定义一个子类这种方式？为了在内部给它的子 View 添加拓展，具体可以参考`_LinearLayout`源码

#### 注意事项

- Anko DSL 布局创建声明 View 的两种方式：
    - textView 创建普通的 TextView
    - themedTextView 创建带 theme 的 TexiView，原理，ContextWrapper 包装原有的Context
    - 布局预览要求：1 安装 anko 插件、2 使用 AnkoComponent
    - Anko DSL 布局 适用于硬编码布局的场景
- 让 Anko DSL 布局支持`自定义 View`，需要给 ViewManager 添加扩展方法
- 让 Anko DSL 布局支持`自定义布局`，一般参考源码即可，比如 `verticalLayout`

#### XML 布局与 DSL 布局对比

测试代码：

```kotlin
 thread {
            //避免类加载导致的性能损失，包装对比的公平性
     PerformanceFragmentUI().createView(AnkoContext.create(container!!.context, this))
            inflater.inflate(R.layout.activity_login, container, false)
            System.nanoTime()

            fun cost(tag: String, block: ()->Unit){
                System.gc()
                System.gc()
                val start = System.nanoTime()
                repeat(1) {
                    block()
                }
                val cost = System.nanoTime() - start
                logger.error("$tag: $cost")
            }

            cost("dsl") {
                PerformanceFragmentUI().createView(AnkoContext.create(container!!.context, this))
            }

            cost("xml") {
                inflater.inflate(R.layout.fragment_about, container, false)
            }
        }
```


对比项 | DSL 布局 | XML 布局
---|---|---
预览|需要编译、速度慢、功能少、不稳定 | 稳定易用，功能丰富
类型|类型安全|默认需要类型强转
耗时|1x|4x
第三方组件|需要添加扩展函数以支持|天然支持
适用场景|代码布局以方便服用的情况、性能苛刻的情况|能使用 xml 的地方当然使用 xml

---
### 3.4 Anko SQLite

数据库操作

---
### 3.5 Anko issue

- [Anko support theme](https://github.com/Kotlin/anko/issues/16)

---
## 4 代理 + 扩展的应用

- SharedPreference 扩展 + 代理：但是也存在弊端，如果使用字段名作为 key，那么如果字段名变更的话将导致之前的存储无效，这就可能不适用于 SharedPreference 了
- Properties 扩展：`属性代理、反射`，多个字段可以共享同一个代理
- Fragments 参数代理（从 arguments 中获取传参）
- [ObjectPropertyDelegate](https://github.com/enbandari/ObjectPropertyDelegate)

---
## 5 实践总结

### 在data类中使用可null类型

在处理服务器返回的数据时，对于不确定返回的数据类型，使用可 null 类型接收，然后在使用此数据时，可以有效的避免空指针。

### 使用 object 实现单例

object实现单例非常简便

### anko库中的协程部分，优雅的切换线程

```kotlin
async {
    val response = longTimeGet()
    uiThread {
        textView.text = response
    }
}
```

### anko库中的dsl构建布局

使用dsl构建布局或许也是一个不错的选择

### apply 方法

调用某对象的apply函数，在函数块内可以通过 this 指代该对象。返回值为该对象自己。

```kotlin
        val list = mutableListOf<String>()
        list.apply {
            add("A")
            add("B")
        }
```

### 自定义高阶函数

```kotlin
inline fun afterL(code: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        code()
    }
}

inline fun ignoreCarsh(code: () -> Unit){
     try {
        code()
     } catch (e : Exception) {
        e.printStackTrace()
     }
}
```

### 为对象添加扩展方法

- 比如为String添加判断非空的方法
- 比如为TextInputLayout添加直接获取text的方法


### 使用 sealed 类

Kotlin 的 sealed 类可以实现轻松的处理错误数据，常见应用：

- 使用 sealed 类表示有限的不同状态，然后使用 when 可以方便的处理有戏的状态，比如 NetworkResult
- 使用 sealed 类表示有限的不同处理者，然后使用 when 可以方便的分发数据到对应的处理者，比如 RecyclerView 中 不同类型 Item 的 ViewHolder

### 使用懒加载

使用懒加载 `xx by layz{}` 可以先声明变量，然后在第一使用此变量的时候才会去初始化改变量。

### 暴露不可变属性

在 ViewModel 中的 LiveData 往往是可变的，但是只想暴露不可变的 LiveData 给 UI，当然可以定义一个方法实现，但是使用 Kotlin 的属性更为方便。

```kotlin
class RequestAfterSalesViewModel{

    private val _source = MutableLiveData<Resource<RequestAfterSalesModel>>()

    //export to ui
    val source: LiveData<Resource<RequestAfterSalesModel>>
        get() = _source

}
```


### 使用内联函数 + reified

使用内联函数时，在泛型类似加上 reified，可以在方法体中直接通过泛型类型参数获取 class

```kotlin
inline fun <reified T> Gson.fromJson(json: String) = fromJson(json, T::class.java)
```

---

## 6 填坑记录

### 与Retrofit配合使用时存在的问题

Kotlin会给泛型加上通配符，比如`Map<String, RequestBody>`在Java中会被认为是`Map<String, ？ extend RequestBody>`，但是在Retrofit规定接口中的参数不能有通配符，所以使用Kollin接口定于API时就可能会遇到这个问题：[Parameter type must not include a type variable or wildcard](https://github.com/square/retrofit/issues/1805)

解决方案是加上注解`@JvmSuppressWildcards`：

```kotlin
@PUT("/api/v3/{storeId}/categories/{categoryId}")
fun update(@Path("storeId") storeId: Int, @Path("categoryId") categoryId: Int?, @Query("token") token: String, @Body category: Map<String, @JvmSuppressWildcards Any>): Observable<OperationResultUpdated>
```

------

###  RxJava2的combineLatest泛型丢失

```kotlin
    private var userNameObservable: Observable<CharSequence>? = null
    private var passwordObservable: Observable<CharSequence>? = null

    fun initialize() {
        userNameObservable = RxTextView.textChanges(username).skip(1)
            .debounce(500, TimeUnit.MILLISECONDS)
        passwordObservable = RxTextView.textChanges(password).skip(1)
            .debounce(500, TimeUnit.MILLISECONDS) 
      }

    //下面代码编译无法通过，泛型推导失败
    Observable.combineLatest(userNameObservable,
            passwordObservable, 
            { u: CharSequence, p: CharSequence -> u.isNotEmpty() && p.isNotEmpty() })
```

参考：[Observable.combineLatest type inference in kotlin](https://stackoverflow.com/questions/42725749/observable-combinelatest-type-inference-in-kotlin)

------

### Suppress 压制警告

- `@Suppress("UNCHECKED_CAST")`：没有检查的类型转换
- `@Suppress("DEPRECATION")`：废弃的API

------

###  反编译 Kotlin

Kotlin 反编译时，在 IDEA 上进行 decompile 可能造成 IDEA 卡死，而使用 AndroidStuidio 则不会。

------

### Gson 和 DataClass

DataClass 没有默认的构造函数，而 Gson 却可以通过 `Unsafe.allocateInstance()` 类绕过构造函数实例化对象，这样实例化出来的对象的字段都是没有被初始化的，基于这种情况，就可能存在问题：

```kotlin
//父类
abstract class PagingWrapper<T>{

    abstract fun getElements(): List<T>

    /*这里把 getElements 添加到 GitHubPaging中，是为了给上层使用*/
    val paging by lazy {
        GitHubPaging<T>().also { it.addAll(getElements()) }
    }
}

//基类
data class SearchRepositories(var total_count: Int,
                              var incomplete_results: Boolean,
                              var items: List<Repository>) : PagingWrapper<Repository>() {

    override fun getElements() = items

}
```

PagingWrapper 中 paging 是懒加载的，反编译之后会发现实现是这样的：

```kotlin
public abstract class PagingWrapper {

   @NotNull
   private final Lazy paging$delegate = LazyKt.lazy((Function0)(new Function0() {
      public Object invoke() {
         return this.invoke();
      }

      @NotNull
      public final GitHubPaging invoke() {
         GitHubPaging var1 = new GitHubPaging();
         var1.addAll((Collection)PagingWrapper.this.getElements());
         return var1;
      }
   }));

   @NotNull
   public abstract List getElements();

   @NotNull
   public final GitHubPaging getPaging() {
      Lazy var1 = this.paging$delegate;
      KProperty var3 = $$delegatedProperties[0];
      return (GitHubPaging)var1.getValue();
   }
}
```

layz 依赖于 paging$delegate 字段，但是如果是 Unsafe 通过 `Unsafe.allocateInstance()` 实例化 SearchRepositories 的话，paging$delegate 是没有被初始化的，它的值还是null，这样后面调用 paging 必然会抛出 NPE。