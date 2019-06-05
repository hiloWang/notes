package chapter4;

/**
 * 线程超时获取述
 */
public class TimeWaiting {

    private Object mObject = null;

    public static void main(String[] args) throws InterruptedException {
        System.out.println(new TimeWaiting().get(5000));
    }

    public synchronized Object get(long time) throws InterruptedException {
        long future = System.currentTimeMillis() + time;
        long remaining = time;
        while (mObject == null && remaining > 0) {
            wait(remaining);
            remaining = future - System.currentTimeMillis();
        }
        return mObject;
    }
}
