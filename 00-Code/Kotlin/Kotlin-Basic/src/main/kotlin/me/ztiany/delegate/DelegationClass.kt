package me.ztiany.delegate


/**类的委托：委托模式已经证明是实现继承的一个很好的替代方式， 而 Kotlin 可以零样板代码地原生支持它。 */

private interface MachineGun {
    fun shooting()
}

/** 飞机 */
private interface Plane {
    fun fly()
}


/** 战斗机 */
private class Fighter(plane: Plane, gun: MachineGun) : Plane by plane, MachineGun by gun

private fun testClassDelegation() {

    val gun = object : MachineGun {
        override fun shooting() {
            println("飞机扫射")
        }
    }

    val plane = object : Plane {
        override fun fly() {
            println("飞机起飞")
        }
    }

    //传入代理实现
    val fighter = Fighter(plane, gun)
    fighter.fly()
    fighter.shooting()
}

fun main(args: Array<String>) {
    testClassDelegation()
}