package com.bennyhuo.github.common

import com.bennyhuo.github.common.unused.AbsProperties
import org.junit.Test

class InfoProps: AbsProperties("info.properties"){
    var name: String by prop
    var email: String by prop
    var age: Int by prop
    var student: Boolean by prop
    var point: Float by prop
}

class TestProperties{

    @Test
    fun testProperties(){
        InfoProps().let {
            println(it.name)
            println(it.email)
            println(it.age)
            println(it.student)
            println(it.point)
            it.name = "kotlin"
            it.email = "admin@kotliner.cn"
            it.age = 8
            it.point = 3.0f
        }
    }

}
