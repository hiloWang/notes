package me.ztiany.oop


/**
 * 注解声明：
 *
 * 1，注解是将元数据附加到代码的方法。要声明注解，需要将 annotation 修饰符放在类的前面
 * 2，如果需要对类的主构造函数进行标注，则需要在构造函数声明中添加 constructor 关键字 ，并将注解添加到其前面
 * 3，如果需要将一个类指定为注解的参数，请使用 Kotlin 类 （KClass）。Kotlin 编译器会自动将其转换为 Java 类
 *4，Kotlin的注解可以用在语句上
 *
 *      支持的使用处目标的完整列表为：
 *
 *              file；
 *              property（具有此目标的注解对 Java 不可见）；
 *              field；
 *              get（属性 getter）；
 *              set（属性 setter）；
 *              receiver（扩展函数或属性的接收者参数）；
 *              param（构造函数参数）；
 *              setparam（属性 setter 参数）；
 *              delegate（为委托属性存储其委托实例的字段）。
 *
 * */
private annotation class Ann(val abc: Int)

