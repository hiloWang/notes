package me.ztiany.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Java 泛型
 *
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class JavaGeneric {

    //1 非协变性
    private void genericSample() {
        // Java
        List<String> strs = new ArrayList<>();
        //List<Object> objs = strs; // ！！！即将来临的问题的原因就在这里。Java 禁止这样！
        //objs.add(1); // 这里我们把一个整数放入一个字符串列表
        //String s = strs.get(0); // ！！！ ClassCastException：无法将整数转换为字符串

        List<Object> objs = new ArrayList<>();
        objs.add("ABC");
    }


    //2 泛型上限
    private interface CollectionA<E> {
        void addAll(Collection<E> items);
    }

    // Java
    private void copyAllA(CollectionA<Object> to, CollectionA<String> from) {
        // ！！！对于这种简单声明的 addAll 将不能编译：
        //Collection<String> 不是 Collection<Object> 的子类型
        //to.addAll(from);
    }

    private interface CollectionB<E> {
        //通配符类型参数 ? extends E 表示此方法接受 E 或者 E 的 一些子类型对象的集合，而不只是 E 自身。
        // 这意味着我们可以安全地从其中（该集合中的元素是 E 的子类的实例）读取 E，但不能写入
        // 因为我们不知道什么对象符合那个未知的 E 的子类型。

        //简而言之，带 extends 限定（上界）的通配符类型使得类型是协变的（covariant）。
        void addAll(CollectionB<? extends E> items);
    }

    // Java
    private void copyAllB(CollectionB<Object> to, CollectionB<String> from) {
        //该限制可以让Collection<String>表示为Collection<? extends Object>的子类型
        to.addAll(from);
    }

    private void addSample() {
        List<? extends Number> list;
        List<Integer> managerList = new ArrayList<>();
        list = managerList;
        //Java为了保护其类型一致，禁止向List<? extends Number>添加任意对象，null除外
        //list.add(new Float(2)); //编译错误
        managerList.add(new Integer(2));//ok
    }


    //3 泛型下限

    // 在 Java 中有 List<? super String> 是 List<Object> 的一个超类。这称为逆变性（contravariance）
    //并且对于 List <? super String> 你只能调用接受 String 作为参数的方法 （例如，你可以调用 add(String) 或者 set(int, String)），
    // 当然如果调用函数返回 List<T> 中的 T，你得到的并非一个 String 而是一个 Object。

    //泛型下限使得子类型可以加入父类型容器
    private void addToPareanContainer() {
        class A {
        }
        class B extends A {
        }
        class C extends B {

        }

        List<? super A> integers2 = new ArrayList<>();
        integers2.add(new C());
        integers2.add(new B());
        integers2.add(new A());
        Object object = integers2.get(0);
    }


    // public TreeSet(Comparator<? super E> comparator) {...}，TreeSet中就使用了通配符下限，这里的Comparator作为消费者

    //通配符 ? 表示它必须是Type本身，或是Type的父类
    private static <T> T copy(Collection<? super T> dest, Collection<T> src) {
        T last = null;
        for (T ele : src) {
            last = ele;
            dest.add(ele);
        }
        return last; //返回一个T类型的变量
    }

    private void contravariance() {
        //在 Java 中有 List<? super String> 是 List<Object> 的一个超类。
        List<? super String> a = new ArrayList<>();
        List<Object> b = new ArrayList<>();
        a = b;
        a.add("A");
        b.add(1);
    }


    //4 PECS原则

    //如果只能从集合中获取项目，那么使用 String 的集合， 并且从其中读取 Object 也没问题 。
    //反过来，如果只能向集合中放入 项目，就可以用 Object 集合并向其中放入 String：

    // Joshua Bloch 称那些你只能从中读取的对象为生产者，并称那些你只能写入的对象为消费者。
    // 他建议：“为了灵活性最大化，在表示生产者或消费者的输入参数上使用通配符类型”，并提出了以下助记符：
    // PECS： 代表生产者-Extens，消费者-Super（Producer-Extends, Consumer-Super）。


    //5 弊端
    //假设有一个泛型接口 Source<T>，该接口中不存在任何以 T 作为参数的方法，只是方法返回 T 类型值：
    private interface Source<T> {
        T nextT();
    }

    //那么，在 Source <Object> 类型的变量中存储 Source <String> 实例的引用是极为安全的——没有消费者-方法可以调用。
    // 但是 Java 并不知道这一点，并且仍然禁止这样操作：
    private void sourceSample1(Source<String> strs) {
        // ！！！在 Java 中不允许
        //Source<Object> objects = strs;
    }

    //为了修正这一点，我们必须声明对象的类型为 Source<? extends Object>，这是毫无意义的，
    //因为我们可以像以前一样在该对象上调用所有相同的方法，所以更复杂的类型并没有带来价值。但编译器并不知道。
    private void sourceSample2(Source<String> strs) {
        Source<? extends Object> objects = strs;
    }

}
