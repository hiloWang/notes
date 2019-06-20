package me.ztiany.oop


/**
 *伴生对象：类内部的对象声明可以用 companion 关键字标记
 *
 * 1，伴生对象的成员可通过只使用类名作为限定符来调用
 * 2，可以省略伴生对象的名称，在这种情况下将使用名称 Companion
 * 3，即使伴生对象的成员看起来像其他语言的静态成员，在运行时他们仍然是真实对象的实例成员，而且还可以实现接口
 * 4，在 JVM 平台，如果使用 @JvmStatic 注解，你可以将伴生对象的成员生成为真正的 静态方法和字段
 * 5，每个类，只能有一个伴生对象
 */
private class CompanionClass1 {
    companion object {
        val x: Int = 100
    }
}

private class CompanionClass2 {
    companion object Factory {
        fun create(): CompanionClass2 = CompanionClass2()
    }
}

private fun testCompanion() {
    CompanionClass1.x
    CompanionClass2.create()
}
