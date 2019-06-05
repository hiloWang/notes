package com.bennyhuo.github.utils

/**
 * Created by benny on 7/9/17.
 */
fun Int.toKilo(): String{
    return if(this > 700){
        "${(Math.round(this / 100f) / 10f)}k"
    }else{
        "$this"
    }
}