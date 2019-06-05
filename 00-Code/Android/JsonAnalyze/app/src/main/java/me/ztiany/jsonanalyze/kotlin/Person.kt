package me.ztiany.jsonanalyze.kotlin

/**
 *
 *@author Ztiany
 *      Date : 2018-08-13 13:36
 */
const val PERSON_JSON = """{"name":"Ztiany","age":28,"address":"SZ"}"""

data class Person(val name: String, val age: Int, val address: String)