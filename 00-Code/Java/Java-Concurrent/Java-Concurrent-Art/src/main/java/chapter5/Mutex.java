package chapter5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *互斥锁，使用AbstractQueuedSynchronizer实现
 */
public class Mutex implements Lock {

    private static Mutex mutex = new Mutex();
    private static int value = 0;

    public static void main(String... args) {
        testUnSafe(false);
    }

    private static void testUnSafe(boolean safe) {
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            threadList.add(new Thread() {
                @Override
                public void run() {
                    for (int i1 = 0; i1 < 100; i1++) {
                        if (safe) {
                            safeAdd();
                        } else {
                            unSafeAdd();
                        }
                    }
                }
            });
        }
        threadList.forEach(Thread::start);
        threadList.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(value);
        value = 0;
    }


    private static void safeAdd() {
        mutex.lock();
        try {
            value++;
        } finally {
            mutex.unlock();
        }
    }

    private static void unSafeAdd() {
        value++;
    }


    private static final int OCCUPY_STATUS = 1;//锁状态
    private static final int LEISURE_STATUS = 0;//释放状态
    private final Sync mSync = new Sync();

    public boolean isLock() {
        return mSync.isHeldExclusively();
    }

    public boolean hasQueuedThreads() {
        return mSync.hasQueuedThreads();
    }

    @Override
    public void lock() {
        mSync.acquire(OCCUPY_STATUS);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        mSync.acquireInterruptibly(OCCUPY_STATUS);
    }

    @Override
    public boolean tryLock() {
        return mSync.tryAcquire(OCCUPY_STATUS);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return mSync.tryAcquireNanos(OCCUPY_STATUS, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        mSync.release(OCCUPY_STATUS);
    }

    @Override
    public Condition newCondition() {
        return mSync.newCondition();
    }


    private class Sync extends AbstractQueuedSynchronizer {

        /**
         * 是否处于占用状态,此方法被AbstractQueuedSynchronizer框架调用
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == OCCUPY_STATUS;
        }

        /**
         * 当状态为0的时候获取锁，此方法被AbstractQueuedSynchronizer框架调用
         */
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(LEISURE_STATUS, OCCUPY_STATUS)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /**
         * 释放锁，将状态设置为0，此方法被AbstractQueuedSynchronizer 框架调用
         */
        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        Condition newCondition() {
            return new ConditionObject();
        }
    }
}
