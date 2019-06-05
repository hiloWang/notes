package me.ztiany.oop


/**
 * 定义接口：Kotlin 的接口与 Java 8 类似，既包含抽象方法的声明，也包含实现。
 *
 *  1，接口无法保存状态。它可以有属性但必须声明为抽象或提供访问器实现。
 *  2，一个类或者对象可以实现一个或多个接口。
 *  3，可以在接口中定义属性。在接口中声明的属性要么是抽象的，要么提供 访问器的实现。
 *      在接口中声明的属性不能有幕后字段（backing field），因此接口中声明的访问器不能引用它们。
 *
 */
private interface InterfaceSample {

    //抽象方法
    fun method()

    //有实现的方法
    fun method2() {
        println("method2 called")
    }

    // 抽象的
    val prop: Int

    //提供访问器的
    val propertyWithImplementation: String
        get() = "foo"
}


private interface InterfaceA {

    fun foo() {
        print("A")
    }

    fun bar()
}

private interface InterfaceB {

    fun foo() {
        print("B")
    }

    fun bar() {
        print("bar")
    }
}

private class ImplA : InterfaceA {

    //覆盖InterfaceA的bar方法
    override fun bar() {
        print("bar")
    }
}

private class ImplAB : InterfaceA, InterfaceB {

    //必须覆盖bar方法
    override fun bar() {
        super<InterfaceB>.bar()//调用InterfaceB的实现
    }

    override fun foo() {
        //调用AB的实现
        super<InterfaceA>.foo()
        super<InterfaceB>.foo()
    }
}


fun main(args: Array<String>) {

    val implA = ImplA()
    val implAB = ImplAB()

    implA.bar()
    implAB.foo()
}