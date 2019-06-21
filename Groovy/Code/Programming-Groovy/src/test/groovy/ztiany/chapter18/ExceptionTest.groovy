package ztiany.chapter18

class ExceptionTest extends GroovyTestCase {

    def divide(a, b) {
        a / b
    }

    //Java的方式
    void testDivideJava() {
        try {
            divide(2, 0)
            fail "Exception ArithmeticException"
        }
        catch (ArithmeticException e) {
            assert true
        }
    }
    //Groovy的方式，使用shouldFail方法
    void testDivideGroovy() {
        shouldFail ArithmeticException, {
            divide(2, 0)
        }
    }

}