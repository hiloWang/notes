package ztiany.chapter3

/*
动态类型语言中的类型是运行时推断的，方法和参数也是运行时检查的，通过这种能力，可以在运行时向类中注入行为
 */

//--------------------
//java中类型
//--------------------
class Engine implements Cloneable {}

class Car implements Cloneable {
    private int mYear
    private Engine mEngine

    @Override
    protected Object clone() {
        try {
            Car cloneCar = (Car) super.clone()
            cloneCar.mEngine = (Engine) this.mEngine.clone()
            cloneCar
        } catch (CloneNotSupportedException e) {//不会发生这种情况，这只是为了取悦编译器
            return null
        }
    }
}
/*
上面用Java实现的一个Clone方法实例说明：有些时候静态类型检查只会带来烦恼，而且还会降低效率
 */


//--------------------
//动态类型
//--------------------
/*
动态类型放宽了对类型的要求，使语言能够根据上下文判定类型，主要有以下两大优点：
1：可以在不知道方法具体细节的情况下编写对象上的调用语句，在运行期间，对象会动态的响应方法或消息，在静态类型语言中，可以使用多态在某种程度上实现这种动态行为，
然而大部分静态语言把继承和多态捆绑在一起，而真正的多态并不关注类型——把消息发送给一个对象，在运行期间，它会确定所要使用的相应实现
2：不必用大量的强制类型转换操作来取悦编译器
 */

//--------------------
//动态类型不等于弱类型
//--------------------
/*
在运行时，Groovy会把错误的类型转换毫不含糊的提示出来，把实际的验证推迟到运行时，我们得以在编码和编译时以及代码执行时修改程序的结构，JVM上的动态类型
语言说明，动态类型并不等于弱类型

强类型：Ruby/Groovy /Java/C#
弱类型：JavaScript/Perl/C/C++
动态：Ruby/Groovy/JavaScript/Perl
静态：Java/C#/C/C++

 */


