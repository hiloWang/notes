# 同步集合

---
## 1 CopyOnWriteArrayList

Copy-On-Write是一种用于程序设计中的策略，其基本思想是，从多个线程共享同一个列表，当某个线程想要修改这个列表的元素时，会把列表的元素复制一份，然后进行修改，修改完成之后再将新的元素设置给这个列表，这是一种延时懒惰策略，这样做的好处我们可以对Copy-ON-Write容器进行并发的读而不需要加锁，因为当前容易不会添加移除任何元素，所以Copy-On-Write也是一种读写分离的思想，读和写不在同一个容器。

Java中提供的CopyOnWrite容器：

- CopyOnWriteArrayList
- CopyOnWriteArraySet

CopyOnWriteArrayList的add方法：

```java
    public boolean add(E e) {
            final ReentrantLock lock = this.lock;
            lock.lock();
            try {
                Object[] elements = getArray();
                int len = elements.length;
                Object[] newElements = Arrays.copyOf(elements, len + 1);
                newElements[len] = e;
                setArray(newElements);
                return true;
            } finally {
                lock.unlock();
            }
        }
    
        final void setArray(Object[] a) {
            array = a;
        }
```

从代码可以看出，add方法加了锁，而下面get方法则没有加锁：

```java
    public E get(int index) {
        return get(getArray(), index);
    }
```

通过这种写时拷贝的原理可以将读、写分离，使得并发场景下队列表的操作效率得到提高。但其没有一些问题，即在添加和删除元素时元素占用的内存空间翻了一倍，因此这是以空间换时间的策略。

---
## 2 ConcurrentHashMap

ConcurrentHashMap使用的是**锁分段技术**，加速容器里有多把锁，每把锁用于锁容器中的一部分数据，那么当多个线程读取容器中不同部分的数据时，就不会有锁竞争，从而可以提交并发效率，但是有些需要跨段的操作(size(),containsValue())就需要锁定整个容器而不是某一个数据段了，

另外还有并发容器：

- ConcurrentSkipListMap
- ConcurrentSkipListSet

---
## 3 ConcurrentLinkedQueue
