package chapter4.threadpool;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;


public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {

    private static final int MIN_WORKER_SIZE = 1;
    private static final int NORMAL_WORKER_SIZE = 5;
    private static final int MAX_WORKER_SIZE = 10;
    private int mCurrentWorkerSize = NORMAL_WORKER_SIZE;
    private AtomicLong mAtomicLong = new AtomicLong();

    private LinkedList<Job> mJobs = new LinkedList<>();
    private LinkedList<Worker> mWorkers = new LinkedList<Worker>();


    public DefaultThreadPool() {
        initWorker(mCurrentWorkerSize);
    }

    public DefaultThreadPool(int currentWorkerSize) {
        mCurrentWorkerSize = currentWorkerSize > MAX_WORKER_SIZE ? MAX_WORKER_SIZE
                : mCurrentWorkerSize < MIN_WORKER_SIZE ? MIN_WORKER_SIZE : currentWorkerSize;
        initWorker(mCurrentWorkerSize);
    }

    @Override
    public void execute(Job job) {
        if (job == null) {
            return;
        }
        synchronized (mJobs) {
            mJobs.addLast(job);
            mJobs.notifyAll();
        }
    }

    @Override
    public void shutdown() {
        for (Worker worker : mWorkers) {
            worker.shutDown();
        }
    }

    @Override
    public void addWorkers(int num) {
        synchronized (mWorkers) {
            if (num + mCurrentWorkerSize > MAX_WORKER_SIZE) {
                num = MAX_WORKER_SIZE - mCurrentWorkerSize;
            }
            initWorker(num);
            mCurrentWorkerSize += num;
        }
    }

    private void initWorker(int num) {
        for (int i = 0; i < num; i++) {
            Worker worker = new Worker();
            Thread thread = new Thread(worker, "worker Thread" + mAtomicLong.incrementAndGet());
            mWorkers.addLast(worker);
            thread.start();
        }
    }

    @Override
    public void removeWorker(int num) {
        if (num > mCurrentWorkerSize) {
            throw new IllegalArgumentException("num > mCurrentWorkerSize");
        }
        synchronized (mWorkers) {
            int index = 0;
            while (index < num) {
                // index num size
                // 0         4       5
                // 1         4        4
                // 2        4        3
                // 3        4        2
                Worker worker = mWorkers.get(0);
                if (mWorkers.remove(worker)) {
                    worker.shutDown();
                    index++;
                }
            }
            mCurrentWorkerSize -= num;
        }
    }

    @Override
    public int getJobSize() {
        return mJobs.size();
    }


    /**
     * 工作者
     */
    private class Worker implements Runnable {
        private volatile boolean mIsRunning = true;

        @Override
        public void run() {
            while (mIsRunning) {
                Job job = null;
                synchronized (mJobs) {
                    while (mJobs.isEmpty()) {
                        try {
                            mJobs.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            //感知到其他线程的终止操作，立即返回
                            e.printStackTrace();
                            return;
                        }
                    }
                    job = mJobs.removeFirst();
                    if (job != null) {
                        try {
                            job.run();
                        } catch (Exception e) {

                        }
                    }
                }

            }
        }

        private void shutDown() {
            mIsRunning = false;
        }

    }
}
