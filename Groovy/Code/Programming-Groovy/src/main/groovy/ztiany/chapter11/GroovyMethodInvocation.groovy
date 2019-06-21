package ztiany.chapter11

//--------------------
//测试Groovy对方法调用的处理
//--------------------




testInterceptedMethodCallOnPOJO()
testInerrceptable()
testInterceptedExistingMethodCalled()
testUnInerceptedExistingMethodCalled()
testPropertyThatIsClosure()
testMethodMissingCalledOnlyForNoExisting()
tetInvokedMethodCalledForOnlyNonExistent()

/*
------testInterceptedMethodCallOnPOJO-------
intercepted
------testInerrceptable-------
Interceptable
Interceptable
------testInterceptedExistingMethodCalled-------
intercepted
------testUnInerceptedExistingMethodCalled-------
existingMethod
------testPropertyThatIsClosure-------
closureProp called
------testMethodMissingCalledOnlyForNoExisting-------
existingMethod
methodMissing called
------tetInvokedMethodCalledForOnlyNonExistent-------
existingMethod
invokeMethod called
 */

void testInterceptedMethodCallOnPOJO() {
    println '------testInterceptedMethodCallOnPOJO-------'
    def val = new Integer(3)
    Integer.metaClass.toString = {
        -> "intercepted"
    }
    println val.toString()
}


void testInerrceptable() {

    println '------testInerrceptable-------'


    def obj = new AnInterceptable()

    println obj.existingMethod()
    println obj.nonExistMethod()

}


void testInterceptedExistingMethodCalled() {
    println '------testInterceptedExistingMethodCalled-------'

    AGroovyObject.metaClass.existingMethod2 = {
        ->
        "intercepted"
    }
    AGroovyObject aClass = new AGroovyObject()
    println aClass.existingMethod2()

}


void testUnInerceptedExistingMethodCalled() {
    println '------testUnInerceptedExistingMethodCalled-------'

    def obj = new AGroovyObject()
    println obj.existingMethod()
}


void testPropertyThatIsClosure() {
    println '------testPropertyThatIsClosure-------'

    def obj = new AGroovyObject()
    println obj.closureProp()
}


void testMethodMissingCalledOnlyForNoExisting() {
    println '------testMethodMissingCalledOnlyForNoExisting-------'

    def obj = new ClassWithInvokeAndMissingMethod()
    println obj.existingMethod()
    println obj.nonExistMehtod()
}

void tetInvokedMethodCalledForOnlyNonExistent() {
    println '------tetInvokedMethodCalledForOnlyNonExistent-------'
    def obj = new ClassWithInvokeOnly()
    println obj.existingMethod()
    println obj.nonExistMehtod()
}

//--------------------
//Class
//--------------------

class AnInterceptable implements GroovyInterceptable {

    def existingMethod() {
        "existingMethod"
    }

    def invokeMethod(String name, args) {
        "Interceptable "
    }

}

class AGroovyObject {
    def existingMethod() {
        "existingMethod"
    }

    def existingMethod2() {
        'existingMethod2'
    }

    def closureProp = {
        'closureProp called '
    }

}

class ClassWithInvokeAndMissingMethod {
    def existingMethod() {
        "existingMethod"
    }

    def invokeMethod(String name, args) {
        "invokeMethod called"
    }

    def methodMissing(String name, args) {
        'methodMissing called'
    }
}


class ClassWithInvokeOnly {
    def existingMethod() {
        "existingMethod"
    }

    def invokeMethod(String name, args) {
        'invokeMethod called'
    }
}


