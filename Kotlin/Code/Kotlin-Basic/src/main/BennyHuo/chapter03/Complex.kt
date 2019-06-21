package chapter03

/**运算符重载*/
private class Complex(var real: Double, var imaginary: Double) {

    operator fun plus(other: Complex): Complex {
        return Complex(real + other.real, imaginary + other.imaginary)
    }

    operator fun plus(other: Int): Complex {
        return Complex(real + other, imaginary)
    }

    operator fun plus(other: Any): Int {
        return real.toInt()
    }

    operator fun invoke(): Double {
        return Math.hypot(real, imaginary)
    }

    override fun toString(): String {
        return "$real + ${imaginary}i"
    }
}

