package ztiany.chapter15

//--------------------
//使用Expando创建动态类
//--------------------


//就这么随意的创建了carA和carB
carA = new Expando()
carB = new Expando(year:2012, miles:0)
carA.year= 2012
carA.miles = 10
println carA.dump()
println carB.dump()
//不仅可以定义属性，还可以定义行为：

carC = new Expando(year:2012, miles:0, trun:{ println 'turning...'})
carC.drive = {
    miles += 10
    println "$miles miles driven"
}

carC.drive()
carC.trun()

//假设有一个文件，其中保存了汽车用的数据，如下所示：
/*
miles,year,make
42535,2003,Acura
43652,2006,Chevy
13655,2004,Honda
 */
//无需显示创建一个Car类，解析其文件即可
data = new File("CarInfo").readLines()

props = data[0].split(",")
data -= data[0]

def averageMilesDrivenPerYear ={
    mile.toLong() / (2008 - year.toLong() )
}

cars = data.collect{
    car = new Expando()
    it.split(",").eachWithIndex { String entry, int i ->
        car[props[i]] = entry
    }
    car.ampy = averageMilesDrivenPerYear
    car
}

props.each {
    name->
        print " $name "
}
println " Avg.MPY "
ampyMethod = 'ampy'
cars.each { car ->
    for (String property : props) {
        print " ${car[property]} "
    }
    println car."$ampyMethod"()
}








