package ztiany.chapter18

import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor

/*
GroovyMockLibrary用于模拟较深的的依赖，也就是模拟被测方法内部创建的协作对象或者依赖对象，StubFor和MockFor是负责
一个功能的两个类。

StubFor和MockFor的优势像分类那样拦截方法调用，与分类不同的是不必为模拟单独创建类

存根和模拟并不能拦截对构造器的调用，对于下面测试方法创建的output.txt可以使用tearDown做清理工作



 */

class TestClassWithDependencyUsingMockLibrary extends GroovyTestCase {

    /*
StubFor帮助我们测试的是methodB是否创建了一个正常的FileWriter，并将期望的内容写入到这个实例中，不过其有局限性，没有测试当关闭文件时，这个方法
的表现是否正常，即使存根上要求了close方法，它也不会检查该方法是否是被调用了。所以存根只是简单的代理协作者并验证状态，要验证行为，必须使用模拟
     */

    void testMethodBUseStubFor() {
        def testObj = new ClassWithDependency()
        def fileMock = new StubFor(java.io.FileWriter)//首先用StubFor为创建存根，这里是java.io.FileWriter
        def text = ""
        fileMock.demand.write {//然后为write方法的存根创建要给闭包
            text = it.toString()
        }
        fileMock.demand.close {}//不会验证close方法是否会被调用

        fileMock.use {
            testObj.methodB(1)
        }
        assertEquals("the value is 1", text)
    }

    /*
    这里会测试失败，因为methodB没有调用close方法，而MockFor要求了close方法的调用
     而在这里测试.methodC(1)则会成功
    */
    void testMethodBUseMockFor() {
        def testObj = new ClassWithDependency()
        def fileMock = new MockFor(java.io.FileWriter)//首先用StubFor为创建存根，这里是java.io.FileWriter
        def text = ""
        fileMock.demand.write {//然后为write方法的存根创建要给闭包
            text = it.toString()
        }
        fileMock.demand.close {}//要求close方法
        fileMock.use {
            testObj.methodB(1)
//            testObj.methodC(1)
        }
        assertEquals("the value is 1", text)
    }

    /*
     * 使用了多个依赖实例的测试方法
     */
    void testUseFiles() {
        def testObj = new TwoFileUser()
        def testData = "multi files"
        def fileMock = new MockFor(FileWriter)

        fileMock.demand.write(){//第一次调用
            assertEquals( testData ,it)
        }

        fileMock.demand.write(){//第二次调用
            assertEquals( testData.size() ,it)
        }
        fileMock.demand.close(2..2){//要求close至少调用两次，至多调用两次

        }
        fileMock.use {
            testObj.useFiles(testData)
        }
    }


    /*
    MockFor可以模拟一个方法的调用的次序和次数，如果被测代码没有严格的满足要求，模拟将会抛出严格异常

使用MockFor.demand.method(1..3){...}表示某method最多应调用三次，最少一次

     */
    void testMothodOrderAndCount() {
        def fileMock = new MockFor(FileWriter)
        fileMock.demand.write(3..3){}//write要调用三次
        fileMock.demand.flush(){}//flush调用一次
        fileMock.demand.getEncoding{'whatever'}//调用一次getEncoding
        fileMock.demand.write{assertEquals('whatever',it.toString())}//又调用一次write
        fileMock.demand.close{}//调用一个close
        fileMock.use {
            new TwoFileUser().someWriter()
        }
    }



    //扫尾工作
    void tearDown() {
        new File("output1.txt").delete()
        new File("output2.txt").delete()
    }


}
