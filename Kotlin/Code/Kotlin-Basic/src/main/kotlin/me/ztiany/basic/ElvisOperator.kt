package me.ztiany.basic

/**
 *Elvis操作符
 *
 * @author ztiany
 *          Email: ztiany3@gmail.com
 */

class User {
    fun save() {}
}

/*
* 对空值的处理
* */
fun testElvis(input: String?, user: User?) {
    val a: Int?

    if (input == null) {
        a = -1
    } else {
        a = input?.length
    }

    if (user == null) {
        val newOne = User()
        newOne.save()
    } else {
        user.save()
    }
}

/**
 * Elvis操作符 ?: 简化对空值的处理
 */
fun testElvis2(input: String?, user: User?) {
    val b = input?.length ?: -1
    //?:符号会在符号左边为空的情况才会进行下面的处理，不为空则不会有任何操作。
    user?.save() ?: User().save()
}