package ztiany.chapter6

//--------------------
//使用Map类
//--------------------
langs = [
        'c++':'Stroustrup',
        'java':'Gosling',
        'lisp':'McCarthy'
]
println langs.getClass().name//java.util.LinkedHashMap ，使用getClass，而不是直接访问class，因为Groovy会认为你是根据key取value
println langs."c++"//因为+的原因，加上“”
println "langs.java:"+langs.java
println "langs['java']:"+langs['java']

//定义map时，也可以不带引号，(因为key必须为String)
langs = [
        'c++':'Stroustrup',
        java:'Gosling',
        lisp:'McCarthy'
]

//--------------------
//在Map上迭代
//--------------------
langs.each {//一个参数的闭包
    Map.Entry entry->
        println entry.key+"   "+entry.value
}
langs.each {//两个参数的闭包
    key,value->
        println key+"   "+value
}

//--------------------
//Map的collect方法
//--------------------
println langs.collect {//返回一个新的List
    key,value->
        key.replaceAll("[+]","p")//此处返回的一个String
}

//--------------------
//find和findAll方法
//--------------------
println "===========find findAll ================"
println langs.find {//返回的是LinkedHashMap$Entry
    key,value->
        key.size() > 3
}
println langs.findAll {//返回一个新的map
    key,value->
        key.size()>2
}


//--------------------
//Map上的其他便捷方法
//--------------------
println "=========== Map上的其他便捷方法 ================"

println langs.any {//用于确定map中有任何元素满足条件
    key,value->
        (key == "java")
}
println langs.every {//用于确定map中所有元素满足条件
    key,value->
    key.size()>3
}

friends = [
        briang:"Brian Goetz",
        brians:"Brian Sletten",
        dividb:"Divid Block"

]

def map = friends.groupBy {//根据返回一个map
    it.value.split(' ')[0]
}//[Brian:[briang:Brian Goetz, brians:Brian Sletten], Divid:[dividb:Divid Block]]
println map

//最后：Groovy的map用于具名参数，以及使用Map实现接口