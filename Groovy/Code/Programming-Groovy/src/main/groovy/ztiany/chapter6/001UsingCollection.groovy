package ztiany.chapter6



//--------------------
//使用List
//--------------------
list = [1,2,3,4,5,6,7,8,9]
println list
println list.dump()
println list.getClass().name//java.util.ArrayList
println list[0]// 1
println list[-1]//倒着取 9
println list[1..3]//[2, 3, 4]
println list[-2..0]//倒着取 [8, 7, 6, 5, 4, 3, 2, 1]

//查看list[1..3]返回的类型是什么
subList = list[1..4]
println subList.getClass().name
println subList.dump()//<java.util.ArrayList@f0c03 elementData=[2, 3, 4, 5] size=4 modCount=1> 可见返回的是一个新的子对象

//--------------------
//迭代List
//--------------------
/*
迭代器：
外部迭代器：比如for循环，可以控制迭代
内部迭代器：在支持闭包的语言中很流行，不能控制迭代，但更容易使用
 */
list.each {
    print it+" "
}
println ""
list.reverseEach {//反向迭代
    print it+" "
}
list.eachWithIndex { int entry, int i ->
    println "entry is $entry , index is $i"
}

//对闭包中的元素求和
total = 0
list.each {
    total += it
}
println total
//把集合中的每个元素变成原来的两倍
doubleList = []
list.each {
    doubleList << 2 * it
}
println doubleList


//--------------------
//使用List的Collect方法
//--------------------
//collect用于返回一个集合结果
println list.collect( ){it*2}//[2, 4, 6, 8, 10, 12, 14, 16, 18] , 返回的是一个List

//--------------------
//使用查找方法
//--------------------
println list.find(){//返回第一个满足条件的元素
    it >5
}
println list.findAll(){//返回一个集合，满足条件的所有元素
    it > 6
}
println list.findIndexOf {
    it > 2
}


//--------------------
//List上的其他便捷方法
//--------------------

list1 = ["Programming" , "in"  , ' Groovy']
def count  = 0
list1.each {
    count += it.size()
}
println count
println list1.collect{it.size()}.sum()//collect{it.size()} 返回每个元素的count， sum用于求和
println list1.inject(0){//inject接收要给初始值carryOver，把carryOver和每一个元素作为闭包的参数，并获取闭包返回的值，下次调用闭包时，传入此值
    carryOver, element ->
        carryOver + element.size()
}
//join 用于连接每个元素
println list1.join("")
println list1.join("----")

// flatten用于拉平一个list
list2 = [list1, ' aaaaaaaa', 'bbbbbb']
println list2.flatten()//[Programming, in,  Groovy,  aaaaaaaa, bbbbbb]

// 使用 minus()方法(映射到的是-操作符)
println list1 - ['in']//返回的是一个新的String
//得到一个反序的副本
println list.reverse()




// 展开操作符：

// 展开操作符(*.)通常被集合对象调用其所有的对象使用。等价于遍历调用,并将结果收集在一个集合中
println list.size()
println list1*.size()//[11, 2, 7] 因为*的影响，即作用于列表的每一个元素，变为展开操作符， 相当于下面代码

println list1.collect{
    it.size()
}


println list*.equals("")//[false, false, false, false, false, false, false, false, false]


//*的应用
def printWord(a, b, c) {
    println "$a $b $c"
}
printWord(//必须保证参数个数一致
        *list1
)











