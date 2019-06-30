package me.ztiany.kotlin.encipher

fun main() {

    //获取字符ascii编码
    val c = 'a'
    //字符转成十进制
    val value: Int = c.toInt()//97


    //获取字符串ascii
    val str = "I love you"
    //遍历获取每一个字符的ascii
    val array = str.toCharArray()

    val result = with(StringBuilder()) {
        for (ch in array) {
            val result = ch.toInt()
            append("$result ")
        }
        //返回结果
        toString()
    }

    println(result)
}

