package me.ztiany.oop


/**
 * 密封类：密封类用来表示受限的类继承结构：当一个值为有限集中的 类型、而不能有任何其他类型时。
 * 在某种意义上，他们是枚举类的扩展：枚举类型的值集合 也是受限的，
 * 但每个枚举常量只存在一个实例，而密封类 的一个子类可以有可包含状态的多个实例。
 *
 * 要声明一个密封类，需要在类名前面添加 sealed 修饰符：
 *
 *1，虽然密封类也可以有子类，但是所有子类都必须在与密封类自身相同的文件中声明。
 *2，（在 Kotlin 1.1 之前， 该规则更加严格：子类必须嵌套在密封类声明的内部）。
 *3，扩展密封类子类的类（间接继承者）可以放在任何位置，而无需在 同一个文件中。
 *4，使用密封类的关键好处在于使用 when 表达式 的时候，如果能够 验证语句覆盖了所有情况，就不需要为该语句再添加一个 else 子句了
 */

private sealed class SealedExpr

private data class Const(val number: Double) : SealedExpr()
private data class Sum(val e1: SealedExpr, val e2: SealedExpr) : SealedExpr()
private object NotANumber : SealedExpr()

private fun eval(expr: SealedExpr): Double =
        when (expr) {
            is Const -> expr.number
            is Sum -> eval(expr.e1) + eval(expr.e2)
            NotANumber -> Double.NaN
            // 不再需要 `else` 子句，因为我们已经覆盖了所有的情况
        }
