package main.advance.serializedaccess;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import rx.internal.util.unsafe.MpscLinkedQueue;

/**
 * Operator 并发原语：串行访问（serialized access）（二），queue-drain：http://blog.piasy.com/AdvancedRxJava/2016/05/13/operator-concurrency-primitives-2/
 *
 * @author Ztiany
 *         Date : 2016-12-06 22:52
 */
public class QueueDrainSample {

    /*
    在Operator 并发原语：串行访问（serialized access）（一）中，我介绍了串行访问的需求，它是为了让 RxJava 的一些方法以及操作符可以串行执行。
    我详细介绍了发射者循环（emitter-loop），并展示了如何利用它来实现串行访问的约定。我想再次强调一点，在大部分的单线程使用场景下，
    这种实现方式性能表现非常突出，因为 Java (JVM) 的 JIT 编译器会在检测到只有单线程使用时使用偏向锁和锁省略技术。然而如果异步执行占了主要部分，
    或者发射操作在另一个线程执行，由于其阻塞（blocking）的原理，发射者循环就会出现性能瓶颈。

    在本文中，我将介绍另一种非阻塞的串行实现方式，我称之为队列漏（queue-drain）。

    但是，有一个几乎任何人都会使用的操作符就是使用的队列漏：observeOn。它目前的实现，
    使用了 ValueQueueDrainOptimized 中介绍的优化方案，因为它对队列（SpscArrayQueue）的 offer 和 poll 的操作都只在单一的线程中（两者是不同的线程）
    （当然，特定的 backpressure 场景除外，我将在后续的文章中进行更多的分析）。
     */
    public static void main(String... args) {

    }


    class ValueQueueDrain<T> {
        final Queue<T> queue = new MpscLinkedQueue<>();     // (1)
        final AtomicInteger wip = new AtomicInteger();
        Consumer consumer;                                  // (2)

        public void drain(T value) {
            queue.offer(Objects.requireNonNull(value));     // (3)
            if (wip.getAndIncrement() == 0) {//
                do {
                    T v = queue.poll();            //poll() 不会返回 null（wip 的值一定不小于 queue 的元素个数）           // (4)
                    consumer.accept(v);                     // (5)
                } while (wip.decrementAndGet() != 0); // 2 1 0       // (6)
            }
        }
    }


    class BasicQueueDrain {
        final AtomicInteger wip = new AtomicInteger(); //wip:work-in-progress 的缩写，用来记录需要被执行的任务数量         // (1)

        public void drain() {
            // work preparation
            if (wip.getAndIncrement() == 0) {//如果原子操作返回0则进入下面的循环           // (2)
                do {
                    // work draining
                }
                while (wip.decrementAndGet() != 0);//每当一件任务被取出（漏出）并处理完毕，我们就对 wip 进行减一操作。如果减到了零，那我们就退出循环       // (3)
            }
        }
    }

}
