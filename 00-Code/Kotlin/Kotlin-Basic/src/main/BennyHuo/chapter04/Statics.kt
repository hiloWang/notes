package chapter04


fun main(args: Array<String>) {
    val latitude = Latitude.ofDouble(3.0)
    val latitude2 = Latitude.ofLatitude(latitude)
    println(Latitude.TAG)
}

class Latitude private constructor(val value: Double) {

    /**该类将会有一个单例的companion对象*/
    companion object {

        /**将会为 Latitude 生成 ofDoublei 静态方法*/
        @JvmStatic
        fun ofDouble(double: Double): Latitude {
            return Latitude(double)
        }

        fun ofLatitude(latitude: Latitude): Latitude {
            return Latitude(latitude.value)
        }

        @JvmField
        val TAG: String = "Latitude"
    }

}
