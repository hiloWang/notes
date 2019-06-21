package ztiany.chapter19


//--------------------
//括号的限制与变通的方案
//--------------------

class Total1 {
    def value = 0

    def clear() {
        value = 0
    }

    def add(number) {
        value += number
    }

    def total() {
        println "total is $value"
    }
}

def calc1 = new Total1()
calc1.with {
    clear()
    add 3
    add 5
    add 5
    total()
}

//上面clear()和 total()都需要括号，因为调用没有参数的方法时不加括号，Groovy会认为其是属性
//下面使用变通的方案，定义名称修改为getClear和getTotal，这样就可以通过clear和total来调用对应的方法了，这是之前学习过的Groovy的特性。

class Total2 {
    def value = 0

    def getClear() {
        value = 0
    }

    def add(number) {
        value += number
    }

    def getTotal() {
        println "total is $value"
    }
}

def calc2 = new Total2()
calc2.with {

    clear
    add 3
    add 5
    add 5
    total

}