package chapter2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  Java里的原子操作
 *
 * @author Ztiany
 *         Date : 2016-12-13 22:32
 */
public class AtomicOperation {

    private int count;
    private AtomicInteger mAtomicInteger = new AtomicInteger(0);

    public static void main(String... args) {

        final AtomicOperation atomicOperation = new AtomicOperation();
        List<Thread> threads = new ArrayList<>(600);
        long start = System.currentTimeMillis();
        for (int j = 0; j < 100; j++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        atomicOperation.unsafeCount();
                        atomicOperation.safeCount();
                    }
                }
            };
            threads.add(thread);
        }

        threads.forEach(Thread::start);

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("非安全自加：" + atomicOperation.count);
        System.out.println("安全自加：" + atomicOperation.mAtomicInteger.get());
        System.out.println("耗时：" + (System.currentTimeMillis() - start));
    }

    private void unsafeCount() {
        count++;
    }

    /**
     * 使用cas实现线程安全计数器
     */
    private void safeCount() {
        for (; ; ) {
            int i = mAtomicInteger.get();
            boolean success = mAtomicInteger.compareAndSet(i, ++i);
            if (success) {
                break;
            }
        }
    }
}
