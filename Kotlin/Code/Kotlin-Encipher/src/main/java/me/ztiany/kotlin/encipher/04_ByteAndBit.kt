package me.ztiany.kotlin.encipher


fun main(args: Array<String>) {
    val ch: Char = 'A'
    //获取字符ascii
    val ascii = ch.toInt()
    println(ascii)
    //二进制
    val binary = Integer.toBinaryString(ascii)
    println(binary)//1000001 ，8位，一个英文字符占1个字节，
}
