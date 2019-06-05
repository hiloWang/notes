package me.ztiany.noarg

import java.lang.reflect.Type


inline fun <reified T> Storage.entity(key: String): T? {
    println(object :TypeFlag<T>(){}.type)
    return this.getEntity(key, T::class.java.componentType)
}

class AStorage : Storage {
    override fun <T : Any?> getEntity(key: String?, type: Type?): T? {
        return null
    }
}

fun main(args: Array<String>) {
    AStorage().entity<List<String>>("A")
    AStorage().entity<String>("A")
}