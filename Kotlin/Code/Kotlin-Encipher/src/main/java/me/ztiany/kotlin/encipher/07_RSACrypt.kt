package me.ztiany.kotlin.encipher

import java.io.ByteArrayOutputStream
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


/**
 * 非对称加密RSA加密和解密
 */
object RSACrypt {

    private const val TRANSFORMATION = "RSA"
    private const val ENCRYPT_MAX_SIZE = 117 //加密：每次最大加密长度（根据密码长度而定）
    private const val DECRYPT_MAX_SIZE = 128 //解密：每次最大加密长度（根据密码长度而定）

    /**
     * 私钥加密
     * @param input 原文
     * @param privateKey 私钥
     */
    fun encryptByPrivateKey(input: String, privateKey: PrivateKey): String {
        //********************非对称加密三部曲********************//
        val byteArray = input.toByteArray()

        //1.创建cipher对象
        val cipher = Cipher.getInstance(TRANSFORMATION)

        //2.初始化cipher
        cipher.init(Cipher.ENCRYPT_MODE, privateKey)

        //3.加密：分段加密
        var temp: ByteArray?
        var offset = 0 //当前偏移的位置

        val bos = ByteArrayOutputStream()

        while (byteArray.size - offset > 0) { //没加密完
            //每次最大加密117个字节
            if (byteArray.size - offset >= ENCRYPT_MAX_SIZE) {
                //剩余部分大于117
                //加密完整117
                temp = cipher.doFinal(byteArray, offset, ENCRYPT_MAX_SIZE)
                //重新计算偏移的位置
                offset += ENCRYPT_MAX_SIZE
            } else {
                //加密最后一块
                temp = cipher.doFinal(byteArray, offset, byteArray.size - offset)
                //重新计算偏移的位置
                offset = byteArray.size
            }
            //存储到临时缓冲区
            bos.write(temp)
        }
        bos.close()

        return Base64.encode(bos.toByteArray())
    }

    //公钥加密
    fun encryptByPublicKey(input: String, publicKey: PublicKey): String {
        //********************非对称加密三部曲********************//

        val byteArray = input.toByteArray()

        //1.创建cipher对象
        val cipher = Cipher.getInstance(TRANSFORMATION)
        //2.初始化cipher
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        //3.加密：分段加密
        //val encrypt = cipher.doFinal(input.toByteArray())

        var temp: ByteArray?
        var offset = 0 //当前偏移的位置

        val bos = ByteArrayOutputStream()

        while (byteArray.size - offset > 0) { //没加密完
            //每次最大加密117个字节
            if (byteArray.size - offset >= ENCRYPT_MAX_SIZE) {
                //剩余部分大于117
                //加密完整117
                temp = cipher.doFinal(byteArray, offset, ENCRYPT_MAX_SIZE)
                //重新计算偏移的位置
                offset += ENCRYPT_MAX_SIZE
            } else {
                //加密最后一块
                temp = cipher.doFinal(byteArray, offset, byteArray.size - offset)
                //重新计算偏移的位置
                offset = byteArray.size
            }
            //存储到临时缓冲区
            bos.write(temp)
        }
        bos.close()

        return Base64.encode(bos.toByteArray())
    }

    /**
     * 私钥解密
     * @param input 密文
     */
    fun decryptByPrivateKey(input: String, privateKey: PrivateKey): String {
        //********************非对称加密三部曲********************//
        val byteArray = Base64.decode(input)

        //1.创建cipher对象
        val cipher = Cipher.getInstance(TRANSFORMATION)
        //2.初始化cipher
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        //3.分段解密
        var temp: ByteArray?
        var offset = 0 //当前偏移的位置

        val bos = ByteArrayOutputStream()

        while (byteArray.size - offset > 0) { //没加密完
            //每次最大解密128个字节
            if (byteArray.size - offset >= DECRYPT_MAX_SIZE) {
                temp = cipher.doFinal(byteArray, offset, DECRYPT_MAX_SIZE)
                //重新计算偏移的位置
                offset += DECRYPT_MAX_SIZE
            } else {
                //加密最后一块
                temp = cipher.doFinal(byteArray, offset, byteArray.size - offset)
                //重新计算偏移的位置
                offset = byteArray.size
            }
            //存储到临时缓冲区
            bos.write(temp)
        }
        bos.close()

        return String(bos.toByteArray())
    }

    /**
     * 私钥解密
     * @param input 密文
     */
    fun decryptByPublicKey(input: String, publicKey: PublicKey): String {
        //********************非对称加密三部曲********************//

        val byteArray = Base64.decode(input)

        //1.创建cipher对象
        val cipher = Cipher.getInstance(TRANSFORMATION)
        //2.初始化cipher
        cipher.init(Cipher.DECRYPT_MODE, publicKey)
        //3.分段解密
        var temp: ByteArray?
        var offset = 0 //当前偏移的位置

        val bos = ByteArrayOutputStream()

        while (byteArray.size - offset > 0) { //没加密完
            //每次最大解密128个字节
            if (byteArray.size - offset >= DECRYPT_MAX_SIZE) {
                temp = cipher.doFinal(byteArray, offset, DECRYPT_MAX_SIZE)
                //重新计算偏移的位置
                offset += DECRYPT_MAX_SIZE
            } else {
                //加密最后一块
                temp = cipher.doFinal(byteArray, offset, byteArray.size - offset)
                //重新计算偏移的位置
                offset = byteArray.size
            }
            //存储到临时缓冲区
            bos.write(temp)
        }
        bos.close()

        return String(bos.toByteArray())
    }
}

/**如何生成秘钥对*/
private fun createSecretKey() {
    val generator = KeyPairGenerator.getInstance("RSA")//秘钥对生成器
    generator.initialize(1024)
    val keyPair = generator.genKeyPair()//生成秘钥对
    val publicKey = keyPair.public//公钥
    val privateKey = keyPair.private//私钥

    println("publicKey=" + Base64.encode(publicKey.encoded))
    println("privateKey=" + Base64.encode(privateKey.encoded))
}

//********************保存的秘钥对********************//
const val publicKeyStr =
    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC039jg7sotX4xr+LGdmTWs7TgRGTAiMAINpAX8B1r8qUbiyHpqp4ozlQhOI8ogMM+p1rcDWTvM+8Iwd9laClFUeVYaun+h4XUgIM5nQ1qmTVN3uf1lYZxzf2a8B0pHWxPYDwIyeHj2UEb3Cx5i5NG5cZ24depXP6jPKwyzTTJtEwIDAQAB"
const val privateKeyStr =
    "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALTf2ODuyi1fjGv4sZ2ZNaztOBEZMCIwAg2kBfwHWvypRuLIemqnijOVCE4jyiAwz6nWtwNZO8z7wjB32VoKUVR5Vhq6f6HhdSAgzmdDWqZNU3e5/WVhnHN/ZrwHSkdbE9gPAjJ4ePZQRvcLHmLk0blxnbh16lc/qM8rDLNNMm0TAgMBAAECgYAKlYrAZtjH3O5/pvblzQBaFSuRvJKXfY2xNKbw/5EwdctjG+4l7ZXlvNPWlruONK0COEFPXdpk/Vp4sZqzbSUjHcirJp4NifP+RuJAiAYzqkVT7kPykC9+id4JPsyLmKRt7bLc30vCtdFCADlYW0/vHHxMo5bENQb1ssmWSA9QgQJBAP50eLzPGQRhzeQqcJEDEK1xNqj3bJ2sL5nKi4BpHoORoqjnJkxXOsiunkh2vOLW1Hv/rRvuSv4BPQ61qmJwnNMCQQC1+QA6WuEchcnM/kDof0HAIFJQ6iWdavoLFldSW8Jt5xoWjJ/BBEs2KGnAGFtEPzjGIM5pthqONbUbQLwKW8bBAkB8yYncroPKTly2pMmHlEU9ieQQgSbXPHYrqdU4KFU6mNV4l8OEdNLzUA934iNH66tRFFZE+Fv2rYzQBe+FT0zZAkBR9I8RuRRhkC/Oz0PUclved7AbGRlPyHpMvAcf5Iuwi8DIHxVkDNcC0Tivd0jDd+XN9cCBA676FV43o/QMhkEBAkAVQiJlcrVNJHfG3/94VV3vs8iCwcFiMn14Rij7YqhkpdaY6rEM17Wttc/jowkkJ4bk7mmDJOHWyyPLYhJq4tiV"

fun getPrivateKey(privateKey: String): PrivateKey {
    //字符串转成秘钥对对象
    val kf = KeyFactory.getInstance("RSA")
    return kf.generatePrivate(PKCS8EncodedKeySpec(Base64.decode(privateKey)))
}

fun getPublicKey(publicKey: String): PublicKey {
    //字符串转成秘钥对对象
    val kf = KeyFactory.getInstance("RSA")
    return kf.generatePublic(X509EncodedKeySpec(Base64.decode(publicKey)))
}

fun main() {
    createSecretKey()

    val input = "我是超级英雄我是超级英雄我是超级英雄我是超级英雄我是超级英雄我是超级英雄我是超级英雄我是超级英雄我是超级英雄我是超级英雄我是超级英雄我是超级英雄我是超级英雄我是超级英雄"
    println(input.length)
    println("byte数组长度=" + input.toByteArray().size)


    //字符串转成秘钥对对象
    val kf = KeyFactory.getInstance("RSA")
    val privateKey = kf.generatePrivate(PKCS8EncodedKeySpec(Base64.decode(privateKeyStr)))
    val publicKey = kf.generatePublic(X509EncodedKeySpec(Base64.decode(publicKeyStr)))

    //私钥加密：加密不能超过117个字节
    val encrypt = RSACrypt.encryptByPrivateKey(input, privateKey)
    println("私钥加密=$encrypt\n")

    val encryptByPublicKey = RSACrypt.encryptByPublicKey(input, publicKey)
    println("公钥加密=$encryptByPublicKey\n")

    val decryptByPrivateKey = RSACrypt.decryptByPrivateKey(encryptByPublicKey, privateKey)
    println("私钥解密=$decryptByPrivateKey\n")

    val decryptByPublicKey = RSACrypt.decryptByPublicKey(encrypt, publicKey)
    println("公钥解密=$decryptByPublicKey\n")

}