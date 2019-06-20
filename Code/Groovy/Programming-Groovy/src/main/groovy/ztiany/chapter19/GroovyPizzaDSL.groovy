def large = 'large'
def thin = 'thin'
def visa = 'visa'
def Olives = 'Olives'
def Onions = 'Onions'
def Bell_Pepper = "Bell_Pepper"

orderInfo = [:]//要在闭包中使用，不能使用def定义

def methodMissing(String name, args) {
    orderInfo[name] = args
}

def acceptOrder(closure) {
    println "accept order"
    closure.delegate = this
    closure.call()
    println "Validation and processing performed here for order received:"
    orderInfo.each {
        key, value ->
            println "$key -> ${value.join(' , ')}"
    }
}

