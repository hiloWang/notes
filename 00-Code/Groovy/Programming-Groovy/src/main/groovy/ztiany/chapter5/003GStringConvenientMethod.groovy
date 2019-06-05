package ztiany.chapter5

//--------------------
//字符串便捷的方法
//--------------------

//String的execute方法可以帮我们创建一个process对象，他还有其他重载的操作符
str = "It is a rainy day in seattle"
println str
str -= "rainy"
println str
/*
打印结果：
It is a rainy day in seattle
It is a  day in seattle

 -=用于从原有的字符串中减去字符串，其他的操作符还有
 plus + ， multiply * , next ++ , replaceAll和tokenize等
 */

//还可以迭代String
for(str1 in 'held'.."helm"){
    print str1+" "
}//held hele helf helg helh heli helj helk hell helm  这里映射的是next操作符

println 'a'.next()

//索引String
str3 = "abcdefg"
println "str3[2..5] = ${str3[2..5]}"



//--------------------
//正则表达式
//--------------------
obj = ~"hello"//~用于方便的创建正则表达式
println obj.getClass().name//java.util.regex.Pattern

//单/双引号和正斜杠都可以创建字符串，而正斜杠不需要转义
//=~和==~是Groovy提供的操作符，
pattern = ~"(G|g)roovy"
text = 'Groovy is hip'

if (text =~ pattern) {
    println "match"
}else {
    println "no match"
}

if (text ==~ pattern) {
    println "match"
}else {
    println "no match"
}
/*
match
no match
=~  执行部分匹配
==~ 执行精确匹配
 */

/*
=~返回一个Matcher读写，java.util.regex.Matcher实例，
Matcher的boolean求值实现是至少有一个匹配，就会返回true，如果有多个匹配，则matcher会包含一个匹配的数组：
 */
matcher = "Groovy is groovy" =~ /(G|g)roovy/
println "size of matcher is ${matcher.size()}"
println "with elements ${matcher[0]} and ${matcher[1]}"

//可以使用replaceFirst方法或者replaceAll方法方便的替换匹配文本
str2 = 'groovy is groovy ,really groovy'
println str2
result = (str2 =~/groovy/).replaceAll("hip")
println result

/*
总结：
要从字符串创建一个模式，使用~操作符
要定义一个RegEx，使用正斜杠，向/[G|g]roovy/
要确定是否存在匹配，使用=~
对于精确匹配，使用==~
 */












