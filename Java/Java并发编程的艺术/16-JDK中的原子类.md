# 原子操作类

在jdk1.5中，提供了用于原子操作的工具类，这些原子操作类提供了一种简单，性能高效、线程安全地更新一个变量的方式。

原子基本类型

```java
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicInteger atomicInteger = new AtomicInteger(100);
        AtomicLong atomicLong = new AtomicLong(100000);
```

原子更新数组

```java
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(4);
        AtomicLongArray atomicLongArray = new AtomicLongArray(4);
        AtomicReferenceArray<String> stringAtomicReferenceArray = new AtomicReferenceArray<>(4);
```

原子更新引用类型

```java
        AtomicReference<String> atomicReference = new AtomicReference<>(); //原子更新引用
        AtomicReferenceFieldUpdater  //原子更新引用类型的字段
        AtomicMarkableReference<String> atomicMarkableReference = new AtomicMarkableReference<>("ddd", false);//原子更新带有标记的引用类型的字段
```

原子更新字段类

```java
        AtomicIntegerFieldUpdater atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater()
        AtomicLongFieldUpdater atomicLongFieldUpdater = AtomicLongFieldUpdater.newUpdater();
        //原子更新带有版本号的引用，该类将数组与引用关联起来，可用于原子更新数据和数的版本号，解决CAS进行原子更新时的ABA问题。
        AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>("St", 1);
```