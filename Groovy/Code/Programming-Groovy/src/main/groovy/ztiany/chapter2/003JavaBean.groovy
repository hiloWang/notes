package ztiany.chapter2

//--------------------
//javaBean
//--------------------
class Car {
    private def miles = 0;
    final year;

    Car(year) {
        this.year = year
    }
    //禁止private字段被修改，所以我们手动加上此方法
    void setMiles(miles) {
        throw new IllegalAccessException("no allow")
    }

}

Car car = new Car(2008)
println(car)
//car.year = 2004;
//car.miles = 43;
//println(car.miles)




//--------------------
//灵活初始化与具名参数
//--------------------

class Robot {

    def type, height, width

    def access(location, weight, fragile) {
        println "received fragile? $fragile, weight:$weight, loc:$location"
    }
}

robot = new Robot(type: 'arm', width: 10, height: 40)//robot实例吧type，height，width等实参当作了名值对,要求类必须有一个无参的构造器
println "$robot.type , $robot.height, $robot.width"

//访问方法
//access方法有三个形参，如果第一个是map，则可以将这个映射中的键值对展开放在实参列表中
robot.access(x: 20, y: 20, z: 10, 50, true)//这里我们依次放入了映射、weight、fragile
robot.access(50, true, x: 20, y: 20, z: 10)//映射的传递可以往后移
//如果发生的实参的个数多余形参的个数，并且多出来的实参是名值对，那么groovy就会假设方法的第一个形参是一个Map，然后将所有的名值对组织到一起作为方法的第一个实参
// 尽管这种灵活性非常强大，但是可能给人带来困惑，所以谨慎的使用，如果想使用具名参数，那最好只接受一个Map形参，而不要混用不同的形参,如：

def access(Map location, weight, fragile) {
    println "received fragile? $fragile, weight:$weight, loc:$location"
}

access(x: 20, y: 20, z: 10, 50, true)//这时如果传入的不是两个对象+一个名值对就会报错。





