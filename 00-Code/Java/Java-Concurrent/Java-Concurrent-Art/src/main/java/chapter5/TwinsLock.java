package chapter5;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 设计一个同步组件，该同步组件同一个时刻允许两个线程获取同步状态，超过两个线程访问将会被阻塞。
 *
 * @author Ztiany
 *         Date : 2016-12-18 16:45
 */
public class TwinsLock implements Lock {

    ///////////////////////////////////////////////////////////////////////////
    // 测试
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String... args) {
        Lock lock = new TwinsLock();
        class Worker extends Thread {
            @Override
            public void run() {
                while (true) {
                    lock.lock();
                    try {
                        try {
                            Thread.sleep(1000);
                            System.out.print(Thread.currentThread().getName()+" -- ");
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            Worker worker = new Worker();
            worker.setDaemon(true);
            worker.start();
        }
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
                System.out.println();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // 实现
    ///////////////////////////////////////////////////////////////////////////

    private Sync mSync = new Sync(2);

    private static class Sync extends AbstractQueuedSynchronizer {
        private Sync(int count) {
            if (count <= 0) {
                throw new IllegalArgumentException();
            }
            setState(count);
        }

        /**
         * 返回值小于等于0表示没有获取到同步状态
         */
        @Override
        protected int tryAcquireShared(int arg) {//arg恒为1
            for (; ; ) {
                int current = getState();//获取当前的状态，默认为构造方法传入的count
                int newCount = current - arg;//每一个线程获取到同步则-1
                //状态小于0表示没有获取的同步状态，直接返回即可
                //如果count大于等于0，则表示获取成功，原子的设置同步状态后返回
                if (newCount < 0 || compareAndSetState(current, newCount)) {
                    return newCount;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            for (; ; ) {
                int current = getState();//获取状态
                int newCount = current + arg;//释放则状态+1
                if (compareAndSetState(current, newCount)) {//原子的设置状态
                    return true;
                }
            }
        }

        Condition newCondition() {
            return new ConditionObject();
        }
    }


    @Override
    public void lock() {
        mSync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        mSync.acquireSharedInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return mSync.releaseShared(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return mSync.tryAcquireSharedNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        mSync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return mSync.newCondition();
    }

}
