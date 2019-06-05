package me.ztiany.delegate

import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * 委托属性：有一些常见的属性类型，虽然我们可以在每次需要的时候手动实现它们，但是如果能够为大家把他们只实现一次并放入一个库会更好。例如包括：
 *
 *          延迟属性（lazy properties）: 其值只在首次访问时计算，
 *          可观察属性（observable properties）: 监听器会收到有关此属性变更的通知，
 *          把多个属性储存在一个映射（map）中，而不是每个存在单独的字段中。
 *
 *Kotlin 支持委托属性，语法是： val/var <属性名>: <类型> by <表达式>
 *
 *     在 by 后面的表达式是该委托， 因为属性对应的 get()（和 set()）会被委托给它的 getValue() 和 setValue() 方法。
 *     属性的委托不必实现任何的接口，但是需要提供一个 getValue() 函数（和 setValue()——对于 var 属性）
 */


private class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    /**
     * thisRef 表示属性所有者
     * property 表示属性字段，属性的反射成分
     * newValue 表示该属性将要被设置的新值
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: String) {
        println("$newValue has been assigned to '${property.name} in $thisRef.'")
    }
}

private class DelegationExample {
    var p: String by Delegate()
}

private fun testPropertyDelegation() {
    val delegationExample = DelegationExample()
    delegationExample.p = "Like"
    println(delegationExample.p)
}


//标准委托：Kotlin 标准库为几种有用的委托提供了工厂方法

/**
 *1，延迟属性 Lazy
 *
 * lazy() 是接受一个 lambda 并返回一个 Lazy <T> 实例的函数，返回的实例可以作为实现延迟属性的委托：
 * 第一次调用 get() 会执行已传递给 lazy() 的 lambda 表达式并记录结果， 后续调用 get() 只是返回记录的结果。
 *
 * 默认情况下，对于 lazy 属性的求值是同步锁的（synchronized）：该值只在一个线程中计算，并且所有线程 会看到相同的值。
 * 如果初始化委托的同步锁不是必需的，这样多个线程 可以同时执行，那么将 LazyThreadSafetyMode.PUBLICATION 作为参数传递给 lazy() 函数。
 * 而如果你确定初始化将总是发生在单个线程，那么你可以使用 LazyThreadSafetyMode.NONE 模式， 它不会有任何线程安全的保证和相关的开销。
 */

//默认是同步初始化
private val lazyValue: String by lazy {
    println("computed!")
    "Hello"
}

//无锁延迟初始化
private val lazyValueUnSafe: String by lazy(LazyThreadSafetyMode.PUBLICATION) {
    println("computed!")
    "Hello"
}


/**
 *2，可观察属性 Observable：
 *
 * Delegates.observable() 接受两个参数：初始值和修改时处理程序（handler）。 每当我们给属性赋值时会调用该处理程序（在赋值后执行）。
 * 它有三个参数：被赋值的属性、旧值和新值
 *
 * 如果你想能够截获一个赋值并“否决”它，就使用 vetoable() 取代 observable()。 在属性被赋新值生效之前会调用传递给 vetoable 的处理程序。
 */

private class User {

    var name: String by Delegates.observable("<no name>") { property, old, new ->
        println("$property.name now：$old -> $new")
    }

    var age: Int by Delegates.vetoable(0) { property, old, new ->
        println("$property.name now：$old -> $new")
        true
    }
}


/**
 * 3，把属性储存在映射中：
 *
 * 一个常见的用例是在一个映射（map）里存储属性的值，这时属性会以属性名从映射中获取属性的值。
 * 这经常出现在像解析 JSON 或者做其他“动态”事情的应用中。在这种情况下，你可以使用映射实例自身作为委托来实现委托属性。
 */
private class MapDelegate(val map: Map<String, Any?>) {
    val name: String by map
    val age: Int     by map
}


/**
 * 4，局部委托属性：可以将局部变量声明为委托属性。 例如可以使一个局部变量惰性初始化
 */
private fun localDelegate(computeFoo: () -> String) {

    val memoizedFoo by lazy(computeFoo)

    //memoizedFoo只会在第一次访问时求值
    if (!memoizedFoo.isEmpty()) {
        println("memoizedFoo is $memoizedFoo")
    }
}


/**
 *属性委托要求：
 *
 *  对于一个只读属性（即 val 声明的），委托必须提供一个名为 getValue 的函数，该函数接受以下参数：
 *
 *      thisRef —— 必须与属性所有者类型（对于扩展属性——指被扩展的类型）相同或者是它的超类型
 *      property —— 必须是类型 KProperty<*> 或其超类型
 *
 *  对于一个可变属性（即 var 声明的），委托必须额外提供一个名为 setValue 的函数，该函数接受以下参数：
 *
 *   thisRef —— 同 getValue()，
 *   property —— 同 getValue()，
 *   new value —— 必须和属性同类型或者是它的超类型。
 *
 *  getValue() 或/和 setValue() 函数可以通过委托类的成员函数提供或者由扩展函数提供。
 *  当你需要委托属性到原本未提供的这些函数的对象时后者会更便利。两函数都需要用 operator 关键字来进行标记。
 *
 *  Kotlin提供的接口ReadOnlyProperty和ReadWriteProperty中定义了这些操作符，分别对应只读属性和可读写属性。
 *      ReadOnlyProperty
 *      ReadWriteProperty
 */

private class ADelegate(property: ReadWriteProperty<ADelegationExample, String>) : ReadWriteProperty<ADelegationExample, String> by property

private val propertyA = object : ReadWriteProperty<ADelegationExample, String> {

    private var value = "abc"

    override fun setValue(thisRef: ADelegationExample, property: KProperty<*>, value: String) {
        this.value = value
    }

    override fun getValue(thisRef: ADelegationExample, property: KProperty<*>): String {
        return value
    }
}

private class ADelegationExample {
    var name: String by ADelegate(propertyA)
}


/**
 * 翻译规则：在每个委托属性的实现的背后，Kotlin 编译器都会生成辅助属性并委托给它。
 *                 例如，对于属性 prop，生成隐藏属性 prop$delegate，而访问器的代码只是简单地委托给这个附加属性：
 *
 *                  //代理属性
 *                 private class C {
 *                      var prop: Type by MyDelegate()
 *                 }
 *
 *                 //编译后
 *                 class C {
 *                      private val prop$delegate = MyDelegate()
 *                      var prop: Type
 *                      get() = prop$delegate.getValue(this, this::prop)
 *                      set(value: Type) = prop$delegate.setValue(this, this::prop, value)
 *                 }
 */


/**
 *
 * 提供委托：通过定义 provideDelegate 操作符，可以扩展创建属性实现所委托对象的逻辑。
 *                 如果 by 右侧所使用的对象将 provideDelegate 定义为成员或扩展函数，那么会调用该函数来创建属性委托实例。
 *
 *                  provideDelegate 的一个可能的使用场景是在创建属性时（而不仅在其 getter 或 setter 中）检查属性一致性。
 *                  provideDelegate 方法只影响辅助属性的创建，并不会影响为 getter 或 setter 生成的代码。
 */

//属性代理1
private class ResourceDelegate1 : ReadOnlyProperty<UI, String> {

    override fun getValue(thisRef: UI, property: KProperty<*>): String {
        return "ABC"
    }
}

//属性代理2
private class ResourceDelegate2 : ReadOnlyProperty<UI, String> {

    override fun getValue(thisRef: UI, property: KProperty<*>): String {
        return "DEF"
    }
}

//用于提供属性代理，属性代理由其内部的provideDelegate操作符提供。
private class ResourceLoader(val resourceId: Int) {

    operator fun provideDelegate(thisRef: UI, prop: KProperty<*>): ReadOnlyProperty<UI, String> {
        //检测属性
        checkProperty(thisRef, prop.name)
        // 创建委托
        return if (resourceId == 1) {
            ResourceDelegate1()
        } else {
            ResourceDelegate2()
        }
    }

    private fun checkProperty(thisRef: UI, name: String) {

    }
}

private class UI {

    fun bindResource(resourceId: Int): ResourceLoader {
        return ResourceLoader(resourceId)
    }

    //image和text由含有成员操作符函数provideDelegate的返回值代理
    val image by bindResource(1)
    val text by bindResource(2)
}


fun main(args: Array<String>) {
    //test property delegate
    testPropertyDelegation()

    //test Lazy
    println(lazyValue)

    //test Observable
    val user = User()
    user.name = "first"
    user.name = "second"
    user.age = 12
    user.age = 27

    // test map deleaget
    val mapDelegate = MapDelegate(mapOf(
            "name" to "John Doe",
            "age" to 25
    ))

    //test ADelegationExample
    val aDelegationExample = ADelegationExample()
    println("aDelegationExample.name = " + aDelegationExample.name)

    // test local delegate
    localDelegate { "abc" }

    //test provide Delegate
    val ui = UI()
    println(ui.text)
    println(ui.image)
}