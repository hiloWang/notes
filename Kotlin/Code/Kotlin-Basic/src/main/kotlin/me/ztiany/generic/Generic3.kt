package me.ztiany.generic

import java.util.*
import kotlin.reflect.KClass


/** 泛型：变型和子类型化*/

/*添加out，使其可以型变*/
private class Producer<out T>

private fun testProducer() {
    val intProducer = Producer<Int>()
    val numProducer: Producer<Number>
    //因为具有了协变性，可以进行赋值
    numProducer = intProducer
}

private fun testList() {
    val listInt = listOf(1, 2, 3)
    val listNumber: List<Number>
    //Kotlin中的只读列表List是协变的：List<out E>
    listNumber = listInt
}

private open class Animal {
    fun feed() {}
}

private class Cat : Animal()

private class Herd1<out T : Animal>(vararg animals: T)

private class Herd2<out T : Animal>(val leadAnimal: T, vararg animals: T)

/*此时 T 不能被声明为 out，因为var默认生成了get/set, 即可变属性leadAnimal在out和in位置上都是用了T，而T只被声明为out*/
private class Herd3<T : Animal>(var leadAnimal: T, vararg animals: T)

/*此时 T 又能被声明为 out，因为位置规则只覆盖了类外部可见的API，即public/protected/internal的，私有方法的参数既不在in位置，也不在out位置，
* 变型规则只会防止外部使用者对类的误用，但不会对类自己起作用*/
private class Herd4<out T : Animal>(private var leadAnimal: T, vararg animals: T)


/*逆变性：Comparator这个接口只是为了消费T类型，这说明T只在in位置使用，因此它的声明之前添加了in关键字，现在Comparator可以用于比较任何类型*/
private val anyComparator = Comparator<Any> { e1: Any, e2: Any ->
    e1.hashCode() - e2.hashCode()
}

private fun testAnyComparator() {
    val strings = listOf("A", "D", "B")
    strings.sortedWith(anyComparator)
}

private fun <T> copyData1(source: MutableList<T>, destination: MutableList<T>) {
    for (item in source) {
        destination.add(item)
    }
}

private fun <T : R, R> copyData2(source: MutableList<T>, destination: MutableList<R>) {
    for (item in source) {
        destination.add(item)
    }
}

private fun <T> copyData3(source: MutableList<out T>, destination: MutableList<T>) {
    for (item in source) {
        destination.add(item)
    }
}

private fun <T> copyData4(source: MutableList<T>, destination: MutableList<in T>) {
    for (item in source) {
        destination.add(item)
    }
}

private fun testCopyData() {
    val ints: MutableList<Int> = mutableListOf(1, 2, 3)
    val anyItems: MutableList<Any> = mutableListOf<Any>()
    val numberItems: MutableList<Number> = mutableListOf<Number>()

    //copyData2(ints, anyItems)
    copyData3(ints, anyItems)
    copyData3(ints, numberItems)
    copyData4(ints, anyItems)
    copyData4(ints, numberItems)
}


private fun printFirst(list: List<*>) {
    if (list.isNotEmpty()) {
        println(list.first())
    }
}

private interface FieldValidator<in T> {
    fun validate(input: T): Boolean
}

private object DefaultStringValidator : FieldValidator<String> {
    override fun validate(input: String) = input.isNotEmpty()
}

private object DefaultIntValidator : FieldValidator<Int> {
    override fun validate(input: Int) = input >= 0
}

private object Validators {
    private val validators = mutableMapOf<KClass<*>, FieldValidator<*>>()

    fun <T : Any> registerValidator(kClass: KClass<T>, fieldValidator: FieldValidator<T>) {
        validators[kClass] = fieldValidator
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(kClass: KClass<T>): FieldValidator<T> =
            validators[kClass] as? FieldValidator<T>
                    ?: throw IllegalArgumentException("No validator for ${kClass.simpleName}")
}

private interface Function<in T, out U> {
    fun invoke(t: T): U
}

private fun map1(function: Function<*, String>) {

}

private fun map2(function: Function<Int, *>) {

}

private fun map3(function: Function<*, *>) {

}

private fun testStar(args: Array<String>) {
    Validators.registerValidator(String::class, DefaultStringValidator)
    Validators.registerValidator(Int::class, DefaultIntValidator)
    println(Validators[String::class].validate("Kotlin"))
    println(Validators[Int::class].validate(42))

    map1(object : Function<Nothing, String> {
        override fun invoke(t: Nothing): String {
            return ""
        }
    })

    map2(object : Function<Int, Date> {
        override fun invoke(t: Int): Date {
            return Date()
        }
    })

    map3(object : Function<Nothing, Number> {
        override fun invoke(t: Nothing): Number {
            return 3
        }
    })
}
