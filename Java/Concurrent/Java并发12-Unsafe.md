# Unusafe

Unsafe 类存在于 sun.misc 包中，其内部方法操作可以像 C 的指针一样直接操作内存，可以说 juc 中的底层操作都依赖于 Unsafe。该类有如下特定：

- 该类是非安全的，Unsafe 拥有着类似于 C 的指针操作
- 不要直接调用其 getInsance() 方法，因为应用程序没有权限获取该示例，使用应该通过反射获取其内部的静态实例
- Java 官方不建议直接使用的Unsafe类，Oracle 正在计划从 Java 9 中去掉Unsafe类，所以除非你有足够的理由，不要使用该类
- Java 中 CAS 操作的执行依赖于 Unsafe 类的方法
- Unsafe 类中的所有方法都是 native 修饰的，它们直接调用操作系统底层的 API
- Unsafe 有内部的很多方法在某些情况下可以是有用的，比如 `allocateInstance` 可以绕过 Class 的构造方法来创建实例


## 示例

allocateInstance 可以绕过 Class 的构造方法来创建实例：

```java
public class UnsafeSample {

    private Unsafe unsafe;

    UnsafeSample() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
            System.out.println(unsafe);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void run() throws InstantiationException {
        Person person = (Person) unsafe.allocateInstance(Person.class);
        System.out.println(person);
    }

    public static void main(String... args) throws InstantiationException {
        UnsafeSample unsafeSample = new UnsafeSample();
        unsafeSample.run();
    }

}
```


## 参考

- [Java并发编程-无锁CAS与Unsafe类及其并发包Atomic](http://blog.csdn.net/javazejian/article/details/72772470)
