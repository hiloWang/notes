package chapter6.atomic;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2016-12-24 23:46
 *         Email: ztiany3@gmail.com
 */
public class AtomicTest {
    public static void main(String... args) {

        //原子基本类型
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicInteger atomicInteger = new AtomicInteger(100);
        AtomicLong atomicLong = new AtomicLong(100000);

        //原子更新数组
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(4);
        AtomicLongArray atomicLongArray = new AtomicLongArray(4);
        AtomicReferenceArray<String> stringAtomicReferenceArray = new AtomicReferenceArray<>(4);

        //原子更新引用类型
        AtomicReference<String> atomicReference = new AtomicReference<>(); //原子更新引用
        //AtomicReferenceFieldUpdater  //原子更新引用类型的字段
        AtomicMarkableReference<String> atomicMarkableReference = new AtomicMarkableReference<>("ddd", false);//原子更新带有标记的引用类型的字段

        //原子更新字段类
        //        AtomicIntegerFieldUpdater atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater()
        //        AtomicLongFieldUpdater atomicLongFieldUpdater = AtomicLongFieldUpdater.newUpdater();
        //        AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>("St", 1);//原子更新带有版本号的引用，该类将数组与引用关联
        //起来，可用于原子更新数据和数的版本号，解决CAS进行原子更新时的ABA问题。

    }
}
