package ztiany.chapter2

//----------
// 可选形参
//----------

/* Groovy可以把方法和构造器中的形参设置为可选的，想设置多少就可以设置多少，但是要求这些可选的形参必须放在参数列表的最后面
定义形参只需 为其设置一个默认值*/

def log(x, base = 10) {
    Math.log(x) / Math.log(base)
}

println log(1024)
println log(1024, 10)
println log(1024, 2)

//不仅如此，Groovy会把末尾的数据形参是做可选的，可以为最后一个参数提供0个或者多个值
def task(name, String[] details) {
    println "$name ,  $details"
}

task 'call', '123-456-7890'
task 'call', '123-456-7890', 'ee3-456-rwe2'
task 'call'

//--------------------
//使用多赋值
//--------------------
def splitName(fullName) {
    fullName?.split(' ')
}

def (firstName, secondName) = splitName("Zhan tianyou")//从splitName方法接受多个参数
println firstName + "  " + secondName

//还可以使用该特性来交换变量，无需创建中间变量来保存被交换值
def name1 = "Ztiany"
def name2 = "MeiNv"
println "$name1 and $name2"
(name1, name2) = [name2, name1]
println "$name1 and $name2"
//当 变量与值的数量不相等时，如果有多余的变量，Groovy会将它们设置为null，多余的值则会被抛弃，如果对于的变量是不能设置为null的基本类型，Groovy将会抛出一个异常
//在Groovy2中，只要可能int会被看作基本类型，而非Integer














