package ztiany.chapter2

//--------------------
//Boolean求值
//--------------------
//Groovy中的布尔求职与Java不同，根据上下文，Groovy会把表达式计算为布尔值，java代码中的if语句必须要求表达式的结果是个布尔值，而Groovy不会
//Groovy会尝试推断，比如在需要boolean的地方放一个对象引用，Groovy会尝试检查引用是否为null，它将null视作false，将非null视作true，如：

str1 = "hello"
str2 = null
if (str1) {
    println str1
}
if (str2) {//不会被打印
    println str2
}

//注意，当面所说的判断true/false的条件并不全面，对于集合来说，Groovy判断false的条件是 集合是否为空，只有当集合不为null，并且含有至少一个元素时才会计算为true

list0 = null
list1 = []
list2 = [1, 2, 3]

def printNotNull(list) {
    if (list) {
        println list
    }
}
printNotNull list0
printNotNull list1
printNotNull list2

//下面是一个被特殊对待的类型列表
/*
类型              |                   为真条件
Boolean         | 值为true
Collection      |       集合不为空
Character       |   值不为0
CharSequence    |   长度大于0
Enumeration |   Has More Elements 为true
Iterator        |       hasNext 返回true
Number      |       Double值不为0
Map         |       该映射不为空
Matcher     |       至少又要给匹配
Object[]        |       长度大于0
其他类型        |       引用不为null

 */





















