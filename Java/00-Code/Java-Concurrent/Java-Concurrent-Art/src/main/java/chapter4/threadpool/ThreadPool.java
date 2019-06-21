package chapter4.threadpool;


public interface ThreadPool<Job extends Runnable> {

    /**
     * 执行一个job
     *
     * @param job
     */
    void execute(Job job);

    /**
     * 关闭线程池
     */
    void shutdown();

    /**
     * 增加工作线程
     *
     * @param num
     */
    void addWorkers(int num);

    /**
     * 减少工作线程
     *
     * @param num
     */
    void removeWorker(int num);

    /**
     * 得到正在等待执行任务的数量
     */
    int getJobSize();

}
