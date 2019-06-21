package ztiany.chapter18

/**
 * <br/>   Description：
 * <br/>    Email: ztiany3@gmail.com
 *
 * @author Ztiany
 *      Date : 2016-10-22 00:04
 */
class TestCodeWithHeavierDependenciesUsingOverriding extends GroovyTestCase {

    void testMyMethod() {
        def testObj = new CodeWithHeavierDependenciesExt()
        testObj.myMethod()
        assertEquals 43, testObj.result
    }


    class CodeWithHeavierDependenciesExt extends CodeWithHeavierDependencies {
        def result

        //通过将非确定性行为变为确定性行为，就能够针对其进行断言了
        int sameAcion() {
            24
        }

        //通过覆写方法，捕获打印结果
        def println(text) {
            result = text
        }
    }
}
