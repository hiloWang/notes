package me.ztiany.kotlin.encipher

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap


/**
 * 频度分析法破解凯撒加密算法
 */
class FrequencyAnalysis {

    //英文里出现次数最多的字符
    private val MAGIC_CHAR = 'e'
    /** 破解生成的最大文件数 */
    private val DE_MAX_FILE = 4


    fun printCharCount(path: String) {
        val data = file2String(path)
        val mapList = getMaxCountChar(data)
        for (entry in mapList) {
            //输出前几位的统计信息
            println("字符'" + entry.key + "'出现" + entry.value + "次")
        }
    }

    fun file2String(path: String): String {
        val reader = FileReader(File(path))
        val buffer = CharArray(1024)
        var len = reader.read(buffer)
        val sb = StringBuffer()
        while (len != -1) {
            sb.append(buffer, 0, len)
            len = reader.read(buffer)
        }
        return sb.toString()
    }


    ////统计String里出现最多的字符
    fun getMaxCountChar(data: String): SortedMap<Char, Int> {
        val map = HashMap<Char, Int>()
        val array = data.toCharArray()
        for (c in array) {
            if (!map.containsKey(c)) {
                map.put(c, 1)
            } else {
                val count = map[c]
                map.put(c, count!! + 1)
            }
        }

        return map.toSortedMap(compareByDescending {
            map[it]
        })

    }


    fun encryptFile(srcFile: String, destFile: String, key: Int) {
        val artile = file2String(srcFile)
        //加密文件
        val encryptData = CaesarCrypt().encrypt(artile, key)
        //保存加密后的文件
        string2File(encryptData, destFile)
    }

    fun string2File(data: String, path: String) {
        var writer: FileWriter? = null
        try {
            writer = FileWriter(File(path))
            writer.write(data)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (writer != null) {
                try {
                    writer.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

    }

    /**
     * 破解凯撒密码
     * @param input 数据源
     * *
     * @return 返回解密后的数据
     */
    fun decryptCaesarCode(input: String, destPath: String) {
        var deCount = 0//当前解密生成的备选文件数
        //获取出现频率最高的字符信息（出现次数越多越靠前）
        val mapList = getMaxCountChar(input)
        for ((key1, value) in mapList) {
            //限制解密文件备选数
            if (deCount >= DE_MAX_FILE) {
                break
            }

            //输出前几位的统计信息
            println("字符'" + key1 + "'出现" + value + "次")

            ++deCount
            //出现次数最高的字符跟MAGIC_CHAR的偏移量即为秘钥
            val key = key1 - MAGIC_CHAR
            println("猜测key = " + key + "， 解密生成第" + deCount + "个备选文件" + "\n")
            val decrypt = CaesarCrypt().decrypt(input, key)

            val fileName = "de_$deCount$destPath"
            string2File(decrypt, fileName)
        }
    }

}

fun main() {
    //打印出现次数最多的字符
    println("加密前：")
    FrequencyAnalysis().printCharCount("files/article.txt")

    val key = 3
    //加密文件
    FrequencyAnalysis().encryptFile("files/article.txt","files/article_en.txt", key)
    //统计密文出现次数最多的字符
    println("加密后：")
    FrequencyAnalysis().printCharCount("files/article_en.txt")

    //读取加密后的文件
    val article = FrequencyAnalysis().file2String("files/article_en.txt")
    //解密（会生成多个备选文件）
    FrequencyAnalysis().decryptCaesarCode(article, "files/article_de.txt")
}

