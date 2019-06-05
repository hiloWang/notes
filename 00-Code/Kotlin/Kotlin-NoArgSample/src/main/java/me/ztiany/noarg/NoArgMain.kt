package me.ztiany.noarg

import com.google.gson.Gson
import java.util.*

const val json1 = "{\"age\":20,\"name\":\"ztiany\"}"
const val json2 = "{\"age\":20}"

annotation class NoArg

@NoArg
data class DataA(
    val name: String,
    val age: Int
)

@NoArg
data class DataB(
    var name: String = "DataB",
    val age: Int
) {
    //初始化逻辑
    init {
        name = "DataB"
    }
}

data class DataC(
    val name: String = "DataC",
    val age: Int = 0
)

data class DataD(
    val name: String = "DataD",
    val age: Int
)

fun main(args: Array<String>) {
    val gson = Gson()

    println(gson.fromJson(json1, DataA::class.java))
    println(gson.fromJson(json1, DataB::class.java))
    println(gson.fromJson(json1, DataC::class.java))
    println(gson.fromJson(json1, DataD::class.java))

    println(gson.fromJson(json2, DataA::class.java))
    println(gson.fromJson(json2, DataB::class.java))
    println(gson.fromJson(json2, DataC::class.java))
    println(gson.fromJson(json2, DataD::class.java))

    println(DataB::class.java.newInstance())
    println(Arrays.toString(DataB::class.java.constructors))
    println(DataD::class.java.newInstance())
}

