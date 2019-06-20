package me.ztiany.generic

import java.util.*


/**
 *Kotlin泛型
 *
 *         1，泛型
 *         2，声明处型变
 *         3，类型投影
 *         4，星投影
 *         5，@UnsafeVariance
 *         6，泛型函数
 *         7，泛型约束与where子句
 *         8，Kotlin集合中的泛型
 *
 *         https://www.kotlincn.net/docs/reference/generics.html
 *         https://blog.kotliner.cn/2017/06/26/kotlin-generics/
 *         https://kymjs.com/code/2017/06/06/01/
 */

//1 泛型：与Java 类似，Kotlin 中的类也可以有类型参数
private class Box<T>(t: T) {
    var value = t
}

private val box1: Box<Int> = Box<Int>(1)
// 1 具有类型 Int，所以编译器知道我们说的是 Box<Int>。则可以省略掉返现说明
private val box2 = Box(1)


/**
 * 2 声明处型变——in和out：
 *
 *      一般原则是：当一个类 C 的类型参数 T 被声明为 out 时，它就只能出现在 C 的成员的输出-位置，
 *      但回报是 C<Base> 可以安全地作为 C<Derived>的超类。
 *
 *      简而言之，现在类 C 是在参数 T 上是协变的，或者说 T 是一个协变的类型参数。 可以认为 C 是 T 的生产者，而不是 T 的消费者。
 *
 *      out修饰符称为型变注解，并且由于它在类型参数声明处提供，所以我们讲声明处型变。
 *      这与 Java 的使用处型变相反，其类型用途通配符使得类型协变。
 *
 *      另外除了 out，Kotlin 又补充了一个型变注释：in。它使得一个类型参数逆变：
 *      只可以被消费而不可以被生产。逆变类的一个很好的例子是 Comparable：
 *
 *       in 和 out 两词是自解释的（因为它们已经在 C# 中成功使用很长时间了）
 *
 *       总结：out，输出，表示生产者；in，进入，表示消费者
 *
 *       可以看出Kotlin 抛弃了Java的extend和supuer，引用了生产者和消费者的概念
 */

private abstract class Source<out T> {
    abstract fun nextT(): T
}

//T extend String
private fun testSource(source: Source<String>) {
    val sourceAny: Source<Any> = source
}

private abstract class TComparable<in T> {
    abstract fun compareTo(other: T): Int
}

private fun testTComparable(x: TComparable<Number>) {
    // 1.0 拥有类型 Double，它是 Number 的子类型
    x.compareTo(1.0)
    // 因此，我们可以将 x 赋给类型为 Comparable <Double> 的变量
    val y: TComparable<Double> = x // OK！
}

/*
 * 3 类型投影
 *
 *  将类型参数 T 声明为 out 非常方便，并且能避免使用处子类型化的麻烦，但是有些类实际上不能限制为只返回 T！
 *
 *          比如Array，该类在 T 上既不能是协变的也不能是逆变的。这造成了一些不灵活性。
 *
 *               private  class Array<T>(val size: Int) {
 *                   abstract fun get(index: Int): T
 *                   abstract fun set(index: Int, value: T)
 *               }
 */

//这个函数应该将项目从一个数组复制到另一个数组。让我们尝试在实践中应用它
private fun copyArray1(from: Array<Any>, to: Array<Any>) {
    assert(from.size == to.size)
    for (i in from.indices) {
        to[i] = from[i]
    }
}

private fun testCopyArray1() {
    val ints: Array<Int> = arrayOf(1, 2, 3)
    val any = Array<Any>(3) { "" }
    //copyArray1(ints, any) // 错误：期望 (Array<Any>, Array<Any>)
}

//Array <T> 在 T 上是不型变的，因此 Array <Int> 和 Array <Any> 都不是另一个的子类型。为什么？
// 因为 copy 可能做坏事， 也就是说，例如它可能尝试写一个 String 到 from， 并且如果我们实际上传递一个 Int 的数组，
// 一段时间后将会抛出一个 ClassCastException 异常。

//那么，我们唯一要确保的是 copy() 不会做任何坏事。我们想阻止它写到 from，我们可以：

private fun copyArray2(from: Array<out Any>, to: Array<Any>) {
    assert(from.size == to.size)
    for (i in from.indices) {
        to[i] = from[i]
    }
    //from[3] = 3 //错误，无法使用，只能调用get类方法
}

private fun testCopyArray2() {
    val ints: Array<Int> = arrayOf(1, 2, 3)
    val any = Array<Any>(3) { "" }
    copyArray2(ints, any)
    //现在我们可以以这种形式调用了

    //这里发生的事情称为类型投影：我们说from不仅仅是一个数组，而是一个受限制的（投影的）数组：我们只可以调用返回类型为类型参数 T 的方法
    //如copyArray2方法中，这意味着我们只能调用 get()。这就是我们的使用处型变的用法，并且是对应于 Java 的 Array<? extends Object>、 但使用更简单些的方式。
}

//也可以使用 in 投影一个类型：
//Array<in String> 对应于 Java 的 Array<? super String>，也就是说，你可以传递一个 CharSequence 数组或一个 Object 数组给 fill() 函数。
private fun fill(dest: Array<in String>, value: String) {

}

/**
 * 星投影：
 *
 *          有时你想说，你对类型参数一无所知，但仍然希望以安全的方式使用它。 这里的安全方式是定义泛型类型的这种投影，
 *          该泛型类型的每个具体实例化将是该投影的子类型。
 *
 *          Kotlin 为此提供了所谓的星投影语法：
 *
 *             1，对于 Foo <out T>，其中 T 是一个具有上界 TUpper 的协变类型参数，Foo <*> 等价于 Foo <out TUpper>。
 *                  这意味着当 T 未知时，你可以安全地从 Foo <*> 读取 TUpper 的值。
 *             2，对于 Foo <in T>，其中 T 是一个逆变类型参数，Foo <*> 等价于 Foo <in Nothing>。 这意味着当 T 未知时，
 *                  没有什么可以以安全的方式写入 Foo <*>。
 *             3，对于 Foo <T>，其中 T 是一个具有上界 TUpper 的不型变类型参数，Foo<*> 对于读取值时等价于 Foo<out TUpper>
 *                   而对于写值时等价于 Foo<in Nothing>。
 *
 *          如果泛型类型具有多个类型参数，则每个类型参数都可以单独投影。 例如，如果类型被声明为 interface Function <in T, out U>，
 *          我们可以想象以下星投影：
 *
 *          Function<*, String> 表示 Function<in Nothing, String>；
 *          Function<Int, *> 表示 Function<Int, out Any?>；
 *          Function<*, *> 表示 Function<in Nothing, out Any?>。
 */


private interface Foo<T>

private class Bar : Foo<Foo<*>>


// 5  @UnsafeVariance注解，用于协变类型的内部方法参数上的注解

//考虑下面代码，add方法的不允许定义T类型参数
private class MyCollection<out T> {
    // ERROR!，为什么？因为 T 是协变的

    //fun add(t: T) {
    //}
}

//如果上面add编译通过，考虑下面代码，原本只能存入Int类型的集合现在可以存入浮点类型了
private fun test() {
    var myList: MyCollection<Number> = MyCollection<Int>()
    //myList.add(3.0)
}
//对于协变的类型，通常我们是不允许将泛型类型作为传入参数的类型的，
// 或者说，对于协变类型，我们通常是不允许其涉及泛型参数的部分被改变的。
//逆变的情形正好相反，即不可以将泛型参数作为方法的返回值。

//但实际上有些情况下，我们不得已需要在协变的情况下使用泛型参数类型作为方法参数的类型：
/*
            public interface Collection<out E> : Iterable<E> {
                ...
                public operator fun contains(element: @UnsafeVariance E): Boolean
                ...
            }
 */

private class MyCollection2<out T> {
    //为了让编译器放过一马，我们就可以用 @UnsafeVariance 来告诉编译器：“我知道我在干啥，保证不会出错，你不用担心”。
    fun add(t: @UnsafeVariance T) {
    }
}


/**
 * 6 泛型函数
 *      不仅类可以有类型参数。函数也可以有。类型参数要放在函数名称之前：
 */

private fun <T> singletonList(item: T): List<T> {
    return Collections.singletonList(item)
}

private fun <E> E.basicToString(): String {  // 扩展函数
    println(this.toString())
    return "abc"
}

//要调用泛型函数，在调用处函数名之后指定类型参数即可：
private fun testFunctionGeneric() {
    singletonList<String>("A")
    singletonList("A")//如果编译器可以推断出，实际泛型参数可以省略
    1.basicToString()
}


/*
 * 7  泛型约束
 *          能够替换给定类型参数的所有可能类型的集合可以由泛型约束限制。
 */

//上界：最常见的约束类型是与 Java 的 extends 关键字对应的 上界
//冒号之后指定的类型是上界：只有 Comparable<T> 的子类型可以替代 T
//翻译成java中的泛型就是：T extends Comparable<T>
private fun <T : Comparable<T>> sort(list: List<T>) {
}

private fun testSort() {
    sort(listOf(1, 2, 3)) // OK。Int 是 Comparable<Int> 的子类型
    // 错误：HashMap<Int, String> 不是 Comparable<HashMap<Int, String>> 的子类型
    // sort(listOf(HashMap<Int, String>()))
}

//默认的上界（如果没有声明）是 Any?。在尖括号中只能指定一个上界。 如果同一类型参数需要多个上界，我们需要一个单独的 where-子句：
private fun <T> cloneWhenGreater(list: List<T>, threshold: T): List<T> where T : Comparable<T>, T : Cloneable {
    return list.filter { it > threshold }
}


/**

8  Kotlin集合中的泛型

public interface Iterable<out T>

public interface ListIterator<out T> : Iterator<T>
public interface Collection<out E> : Iterable<E>
public interface MutableIterable<out T> : Iterable<T>

public interface List<out E> : Collection<E>
public interface Set<out E> : Collection<E>
public interface MutableCollection<E> : Collection<E>, MutableIterable<E>

public interface MutableList<E> : List<E>, MutableCollection<E>
public interface MutableSet<E> : Set<E>, MutableCollection<E>

 */