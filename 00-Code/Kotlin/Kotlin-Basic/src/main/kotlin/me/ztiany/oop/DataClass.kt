package me.ztiany.oop

/**
 *数据类
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 17.5.30 16:22
 */

/**
 * 数据类：创建一些只保存数据的类。在这些类中，一些标准函数往往是从 数据机械推导而来的。在 Kotlin 中，这叫做 数据类 并标记为 data
 *
 * 编译器自动从主构造函数中声明的所有属性导出以下成员：
 *
 *      equals()/hashCode() 对
 *      toString() 格式是 "UserModel(name=John, age=42)"
 *      componentN() 函数 按声明顺序对应于所有属性
 *      copy() 函数
 *
 *如果这些函数中的任何一个在类体中显式定义或继承自其基类型，则不会生成该函数
 *
 *为了确保生成的代码的一致性和有意义的行为，数据类必须满足以下要求：
 *
 *      主构造函数需要至少有一个参数；
 *      主构造函数的所有参数需要标记为 val 或 var；
 *      数据类不能是抽象、开放、密封或者内部的；
 *      数据类只能实现接口(在1.1之前)。
 *
 * 在 JVM 中，如果生成的类需要含有一个无参的构造函数，则所有的属性必须指定默认值
 */
private data class UserModel(val name: String = "XO", val age: Int = 19)

private class UserEntity(val name: String, val age: Int)

private fun testUserModel() {

    //数据类与非数据类的区别
    val userModel = UserModel()
    val userEntity = UserEntity("XY", 10)
    println(userModel)
    println(userEntity)

    //kotlin提供的copy方法
    val newUserModel = userModel.copy(name = "XY")//copy方法
    println(newUserModel)

    //Kotlin提供的标准数据类
    var pair = Pair("Ztiany", 29)//两个一组
    var triple = Triple(1, 2, 3)//三个一组
}

