package main.advance.serializedaccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import rx.internal.util.unsafe.MpscLinkedQueue;

/**
 * Operator 并发原语：串行访问（serialized access）（一），emitter-loop：http://blog.piasy.com/AdvancedRxJava/2016/05/06/operator-concurrency-primitives/
 *
 * @author Ztiany
 *         Date : 2016-12-06 21:48
 */
public class EmitterLoopSample {

    /*

    RxJava 库中最重要的要求就是 Observer/Subscriber 的 onNext，onError 以及 onCompleted 方法需要是串行调用的
    （译者注：一个事件流，它的事件当然需要是串行发生的，不可能同一个流的多个事件同时发生）

    这样的串行访问控制在遇到并发情况时将发挥重要作用。一个典型的例子是使用者利用 merge，zip 或者 combineLatest 操作符把多个数据流合并为一个数据流。

    问题：
        1    如果不保证串行调用会有什么样的问题？

        在编写 RxJava 操作符时需要理解的最重要的算法，就是如何把并发的事件发射转化为串行的事件发射。在本文中，我介绍了一种叫做发射者循环（emitter-loop）的方式，
        这种方式和其他方式相比，具有不错的性能表现。由于这种方式使用了锁进行同步，以及偏向锁移除时的巨大开销，
        我建议只在异步运行比例低于 50% 的场景下使用这种方法。然而，如果异步运行比例高于 50%，
        或者可以确定一定会涉及到多线程（例如 observeOn），那就还有一种性能可能更好的方式，我称之为队列漏，我将在下一篇中进行讲解。
     */

    public static void main(String... args) {
        testValueEmitterLoop();
    }

    private static void testValueEmitterLoop() {
        final ValueEmitterLoop<String> valueEmitterLoop = new ValueEmitterLoop<>();

        class Task implements Runnable {
            private String value;
            private final Random RANDOM = new Random();

            private Task(String value) {
                this.value = value;
            }

            @Override
            public void run() {
                try {
                    Thread.sleep(RANDOM.nextInt(100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                valueEmitterLoop.emit(value);
            }
        }

        valueEmitterLoop.consumer = (Consumer<String>) s -> {
            System.out.println("ValueEmitterLoop test-->: thread:" + Thread.currentThread() + "  value:" + s);
        };

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 100; i++) {
            executorService.submit(new Task(String.valueOf(i)));
        }

    }


    /**
     * 使用具备线程安全性的数据结构来作为事件队列，对事件队列的访问就无需加锁
     *
     * @param <T>
     */
    private static class ValueEmitterLoop<T> {

        Queue<T> queue = new MpscLinkedQueue<>();//安全队列    // (1)
        boolean emitting;//是否正在发射数据
        Consumer<? super T> consumer;//消费者                // (2)


        public void emit(T value) {
            Objects.requireNonNull(value);
            queue.offer(value);//加入非空数据                      // (3)

            synchronized (this) {
                if (emitting) {//已经有线程在发射就返回
                    return;                          // (4)
                }
                emitting = true;//没有线程发射才会走到这里，改变标记
            }
            for (; ; ) {
                T v = queue.poll();//取出数据                  // (5)
                if (v != null) {
                    consumer.accept(v);//消费数据              // (6)
                } else {
                    synchronized (this) {
                        if (queue.isEmpty()) {//队列为空才退出队列       // (7)
                            emitting = false;
                            return;
                        }
                    }
                }
            }
        }
    }


    class ValueListEmitterLoop1<T> {
        List<T> queue;                           // (1)
        boolean emitting;
        Consumer<? super T> consumer;

        @SuppressWarnings("all")
        public void emit(T value) {
            synchronized (this) {
                if (emitting) {
                    List<T> q = queue;
                    if (q == null) {
                        q = new ArrayList<>();   // (2)
                        queue = q;
                    }
                    q.add(value);
                    return;
                }
                emitting = true;
            }
            consumer.accept(value);              // (3)
            for (; ; ) {
                List<T> q;
                synchronized (this) {           // (4)
                    q = queue;
                    if (q == null) {            // (5)
                        emitting = false;
                        return;
                    }
                    queue = null;               // (6)
                }
                q.forEach(consumer);            // (7)
            }
        }
    }


    class ValueListEmitterLoop2<T> {
        List<T> queue;                           // (1)
        boolean emitting;
        Consumer<? super T> consumer;

        @SuppressWarnings("all")
        public void emit(T value) {
            synchronized (this) {
                if (emitting) {
                    List<T> q = queue;
                    if (q == null) {
                        q = new ArrayList<>();   // (2)
                        queue = q;
                    }
                    q.add(value);
                    return;
                }
                emitting = true;
            }
            boolean skipFinal = false;             // (1)
            try {
                consumer.accept(value);            // (5)
                for (; ; ) {
                    List<T> q;
                    synchronized (this) {
                        q = queue;
                        if (q == null) {
                            emitting = false;
                            skipFinal = true;      // (2)
                            return;
                        }
                        queue = null;
                    }
                    q.forEach(consumer);           // (6)
                }
            } finally {
                if (!skipFinal) {                  // (3)
                    synchronized (this) {
                        emitting = false;          // (4)
                    }
                }
            }
        }
    }


    private class EmitterLoopSerializer {
        boolean emitting;
        boolean missed;

        public void emit() {
            synchronized (this) {           // (1)
                if (emitting) {
                    missed = true;          // (2)
                    return;
                }
                emitting = true;            // (3)
            }
            for (; ; ) {
                // do all emission work     // (4)
                synchronized (this) {       // (5)
                    if (!missed) {          // (6)
                        emitting = false;
                        return;
                    }
                    missed = false;         // (7)
                }
            }
        }
    }


}
