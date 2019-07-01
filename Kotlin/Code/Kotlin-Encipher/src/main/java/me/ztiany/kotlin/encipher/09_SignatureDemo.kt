package me.ztiany.kotlin.encipher

import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

/**
 * 数字签名
 */
object SignatureDemo {

    //签名
    fun sign(input: String, privateKey: PrivateKey): String {
        //获取数字签名实例对象
        val signature = Signature.getInstance("SHA256withRSA")
        //初始化签名
        signature.initSign(privateKey)
        //设置数据源
        signature.update(input.toByteArray())
        //签名
        val sign = signature.sign()
        return Base64.encode(sign)
    }

    fun verify(input: String, publicKey: PublicKey, sign: String): Boolean {
        val signature = Signature.getInstance("SHA256withRSA")
        //初始化签名
        signature.initVerify(publicKey)
        //传入数据源
        signature.update(input.toByteArray())
        //校验签名信息
        val verify = signature.verify(Base64.decode(sign))
        return verify
    }
}

fun main() {
    val input = "name=iPhone8&price=7888"

    val privateKey = getPrivateKey(privateKeyStr)
    val publicKey = getPublicKey(publicKeyStr)

    val sign = SignatureDemo.sign(input, privateKey)
    println("sign=$sign")

    //********************校验********************//

    val verify = SignatureDemo.verify("name=iPhone8&price=8", publicKey, sign)
    println("校验=$verify")
}
