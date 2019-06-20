package me.ztiany.functions

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreeNode


/**
 *
 * 内联函数：
 *
 *      使用高阶函数会带来一些运行时的效率损失：每一个函数都是一个对象，并且会捕获一个闭包。
 *      即那些在函数体内会访问到的变量。 内存分配（对于函数对象和类）和虚拟调用会引入运行时间开销。
 *      但是在许多情况下通过内联化 lambda 表达式可以消除这类的开销。
 *
 *      1，inline 修饰符影响函数本身和传给它的 lambda 表达式：所有这些都将内联到调用处。
 *      2，内联可能导致生成的代码增加，但是如果我们使用得当（不内联大函数），它将在性能上有所提升，
 *           尤其是在循环中的“超多态（megamorphic）”调用处。
 */


//内联函数
private inline fun <T> lock(lock: Lock, body: () -> T): T {
    lock.lock()
    try {
        return body.invoke()
    } finally {
        lock.unlock()
    }
}

//非内联函数
private fun <T> noinlineLock(lock: Lock, body: () -> T): T {
    lock.lock()
    try {
        return body.invoke()
    } finally {
        lock.unlock()
    }
}

/*
 * 禁用内联：
 *
 *     1，如果传给一个内联函数的参数是 lambda 表达式，而且想要控制某些 lambda 参数不被内联，可以用 noinline 修饰符标记哪些不想被内联的函数
 *     2，可以内联的 lambda 表达式只能在内联函数内部调用或者作为可内联的参数传递， 但是 noinline 的可以以任何我们喜欢的方式操作：存储在字段中、传送它等等。
 *     3，需要注意的是，如果一个内联函数没有可内联的函数参数并且没有具体化的类型参数，编译器会产生一个警告，因为内联这样的函数很可能并无益处
 *     （如果你确认需要内联，则可以用 @Suppress("NOTHING_TO_INLINE") 注解关掉该警告）。
 */

private class NoinlineSub {
    var func: (() -> Unit)? = null
}

private inline fun noinlineSample(inlined: () -> Unit, /*使用noinline修饰*/noinline notInlined: () -> Unit) {
    val a = NoinlineSub()
    //a.func = inlined //由于inlined是内联lambda表达式，所以不能赋值给NoinlineSub的func字段。
    a.func = notInlined
    inlined.invoke()
    notInlined.invoke()
}


/**
 * 非局部返回：在 Kotlin 中，我们可以只使用一个正常的、非限定的 return 来退出一个命名或匿名函数。
 *                     这意味着要退出一个 lambda 表达式，我们必须使用一个标签，
 *                     并且 在 lambda 表达式内部禁止使用裸 return，因为 lambda 表达式不能使包含它的函数返回
 */
private fun inlineReturn1() {

    noinlineLock(ReentrantLock()) {
        //禁止使用裸 return，因为 lambda 表达式不能使包含它的函数返回：
        // 错误：不能使 `foo` 在此处返回
        //return
    }

    //内联函数可以直接从函数返回，因为运行时它将会被内联到该函数体内
    lock(ReentrantLock()) {
        return
    }
}

private fun inlineReturn2() {
    //但是如果 lambda 表达式传给的函数是内联的，该 return 也可以内联，所以它是允许的
    //这种返回（位于 lambda 表达式中，但退出包含它的函数）称为非局部返回
    fun hasZeros(ints: List<Int>): Boolean {
        //forEach是是一个内联扩展
        ints.forEach {
            if (it == 0) return true // 从hasZeros 返回
        }
        return false
    }
}

//名函数默认使用局部返回，fun 就近原则
private fun inlineReturn3(people: List<String>) {
    people.forEach(fun(s) {
        if (s == "Alice") return
        println("$s is not Alice")
    })
}

/**
 * crossinline：
 *
 *       一些内联函数可能调用传给它们的不是直接来自函数体、而是来自另一个执行上下文的lambda 表达式参数。
 *
 *       例如来自局部对象或嵌套函数。在这种情况下，该 lambda 表达式中 也不允许非局部控制流。
 *       为了标识这种情况，该 lambda 表达式参数需要 用 crossinline 修饰符标记
 */
private inline fun crossinlineSample(crossinline body: () -> Unit) {
    val f = object : Runnable {
        override fun run() = body()
    }
}


/**
 * 具体化的类型参数：
 *      有时候我们需要访问一个作为参数传给我们的一个类型，这时可以使用具体化的类型参数：`reified`，
 *      使用 reified 修饰代码类型参数的函数，在函数体内，可以像使用实际类型那样使用类型参数声明，因为它将会被内联。
 */
private fun <T> TreeNode.findParentOfType1(clazz: Class<T>): T? {
    //在这里我们向上遍历一棵树并且检查每个节点是不是特定的类型。 这都没有问题，但是调用处不是很优雅
    var p = parent
    while (p != null && !clazz.isInstance(p)) {
        p = p.parent
    }
    @Suppress("UNCHECKED_CAST")
    return p as T?
}

//内联函数支持具体化的类型参数，于是我们可以这样写：
//使用 reified 修饰符来限定类型参数，现在可以在函数内部访问它了， 几乎就像是一个普通的类一样。
// 由于函数是内联的，不需要反射，正常的操作符如 !is 和 as 现在都能用了
//普通的函数（未标记为内联函数的）不能有具体化参数。 不具有运行时表示的类型
private inline fun <reified T> TreeNode.findParentOfType2(): T? {
    var p = parent
    while (p != null && p !is T) {
        p = p.parent
    }
    return p as T?
}

private fun testfindParentOfType() {
    val treeNode: TreeNode? = null
    treeNode?.findParentOfType1(DefaultMutableTreeNode::class.java)
    treeNode?.findParentOfType2<TreeNode>()
}


/**
 * 内联属性：
 *
 *          inline 修饰符可用于没有幕后字段的属性的访问器。 可以标注独立的属性访问器
 *          在调用处，内联访问器如同内联函数一样内联。
 */

private class Pa {}

private class Pb {}

private fun getPa(): Pa {
    return Pa()
}

private class InlinePropertySub {

    val foo: Pa
        inline get() = getPa()
}