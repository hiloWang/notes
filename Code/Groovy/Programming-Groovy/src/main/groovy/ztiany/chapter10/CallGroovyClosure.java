package ztiany.chapter10;

/**
 * <br/>    功能描述：在Java中创建与传递Groovy闭包
 * <br/>    Email     : ztiany3@gmail.com
 *
 * @author Ztiany
 * @see
 * @since 1.0
 */
public class CallGroovyClosure {
    public static void main(String... args) {
        GroovyClosure aGroovyClass = new GroovyClosure();

        //调用无参闭包
        Object result = aGroovyClass.useClosure(new Object() {
            public String call() {
                return "You called from Groovy!";
            }
        });
        System.out.println("received : " + result);


        //调用有参数的闭包
        Object result2 =  aGroovyClass.passToClosure(4, new Object(){
            public String call(int value) {
                return "You called from Groovy! pass value is:"+value;
            }
        });
        System.out.println(result2);

        /*
        received : You called from Groovy!
        You called from Groovy! pass value is:4
         */
    }



}
