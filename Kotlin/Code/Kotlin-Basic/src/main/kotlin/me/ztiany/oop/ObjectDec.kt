package me.ztiany.oop

import java.util.*


/**
 * 对象声明：下面程序则为对象声明，可作为对单例模式应用。对象声明不能在局部作用域（即直接嵌套在函数内部），
 * 但是它们可以嵌套到其他对象声明或非内部类中。
 */
private interface DataProvider

private object DataProviderManager {

    fun registerDataProvider(provider: DataProvider) {

    }

    val allDataProviders: Collection<DataProvider>
        get() = Collections.emptyList()
}
