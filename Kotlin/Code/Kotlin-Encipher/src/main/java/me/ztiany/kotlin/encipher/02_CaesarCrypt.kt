package me.ztiany.kotlin.encipher

class CaesarCrypt {

    /**
     * 加密
     * @param input 原文
     * @param key 秘钥
     */
    fun encrypt(input: String, key: Int): String {
        val charArray = input.toCharArray()
        return with(StringBuffer()) {
            charArray.forEach {
                //遍历每一个字符，对ascii偏移
                val c = it
                //获取支付ascii
                var ascii = c.toInt()

                //移动
                ascii += key
                //转成字符
                val result = ascii.toChar()
                append(result)
            }
            //返回结果
            toString()
        }
    }

    /**
     * 解密
     * @param input 加密的密文
     */
    fun decrypt(input: String, key: Int): String {
        val charArray = input.toCharArray()
        return with(StringBuffer()) {
            charArray.forEach {
                //遍历每一个字符，对ascii偏移
                val c = it
                //获取支付ascii
                var ascii = c.toInt()

                //反方向移动
                ascii -= key
                //转成字符
                val result = ascii.toChar()
                append(result)
            }
            //返回结果
            toString()
        }
    }
}

fun main() {
    val command = "I love you"
    val key = 1

    val encrypt = CaesarCrypt().encrypt(command, key)
    println("凯撒加密=$encrypt")
    val decrypt = CaesarCrypt().decrypt(encrypt, key)
    println("凯撒解密=$decrypt")

}
