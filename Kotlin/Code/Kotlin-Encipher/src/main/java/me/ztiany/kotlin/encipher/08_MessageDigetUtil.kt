package me.ztiany.kotlin.encipher

import java.security.MessageDigest

/**
 * 消息摘要
 */
object MessageDigestUtil {

    fun md5(input:String): String {
        val digest = MessageDigest.getInstance("MD5")
        val result = digest.digest(input.toByteArray())
        return toHex(result)
    }

    fun sha1(input:String):String{
        val digest = MessageDigest.getInstance("SHA-1")
        val result = digest.digest(input.toByteArray())
        println("sha1加密后="+result.size)
        //转成16进制
        val toHex = toHex(result)
        println("sha1加密后转成16进制="+toHex.toByteArray().size)
        return toHex
    }

    fun sha256(input:String):String{
        val digest = MessageDigest.getInstance("SHA-256")
        val result = digest.digest(input.toByteArray())
        println("sha256加密后="+result.size)
        //转成16进制
        val toHex = toHex(result)
        println("sh256加密后转成16进制="+toHex.toByteArray().size)
        return toHex
    }

    //字节数组不方便输出，于是转成16进制
    private fun toHex(byteArray: ByteArray):String{
        return with(StringBuilder()){
            //转成16进制
            byteArray.forEach {
                val value = it
                val hex = value.toInt() and (0xFF)
                val hexStr = Integer.toHexString(hex)
                //println(hexStr)
                if(hexStr.length == 1){
                    append("0").append(hexStr)
                }else{
                    append(hexStr)
                }
            }
            toString()
        }
    }

}

fun main() {
    val input = "哈哈"

    //md5:16,32
    val md5 = MessageDigestUtil.md5(input)
    println(md5)
    println(md5.toByteArray().size)

    //32
    println("tomcat="+"827fb063bcac90d9a5ce0e9397041e92".toByteArray().size)

    val sha1 = MessageDigestUtil.sha1(input)
    println(sha1)

    val sha256 = MessageDigestUtil.sha256(input)
    println("sha256=$sha256")
}



