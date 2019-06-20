package ztiany.chapter3

//--------------------
//多方法
//--------------------

/*
动态类型和动态语言改变了对象响应方法调用的方式
 */


class Employee {
    public void raise(Number number) {
        System.out.println("Employee raise")
    }
}

class Executive extends Employee {

    void raise(Number number) {
        System.out.println("Executive raise Number")

    }


    void raise(BigDecimal bigDecimal) {
        System.out.println("Executive raise BigDecimal")
    }
}


def runRaise(Employee employee) {
    employee.raise(new BigDecimal(32))//如果是在Java中：运行时方法必须接受一个Number作为参数，这是由基类Employee规定的，这里编译器会把BigDecimal看作Number
}

//此段代码如果是在Java中运行的话，则打印结果应该是  System.out.println("Executive raise Number"),而此在Groovy中则是System.out.println("Executive raise BigDecimal")
runRaise(new Employee())
runRaise(new Executive())


public class UsingCollection {
    public static void main(String... args) {
        ArrayList<String> list = new ArrayList<>();
        Collection<String> col = list;
        list.add("one")
        list.add("two")
        list.add("three")

        list.remove(0);
        col.remove(0);//这里调用的是List方法，Groovy的动态与多方法能力很好的处理这种情况
        println(list.size());
        println(col.size());
    }
}
/*
Groovy会聪明的选择正确的实现，不仅基于目标参数，还基于搜提供的参数的实际类型，因为方法分派基于多个实体——目标加参数，所以这被称做多分派或者多方法
 */





//--------------------
//动态还是非动态
//--------------------
/*
鉴于Groovy是一门可选类型的动态类型语言，那我们是应该指明类型还是依赖动态类型呢？

一般可以选择省略类型，不过应该为形参或者变量选择表达性比较好的名字，但是在必要的时候应该指明类型，比如在Junit要求的测试方法中返回值必须为void，或者指明
类型有很大的好处时(ORM数据库)时应该指明类型

社区倾向于指明方法签名中的类型



 */



