package chapter6.queue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class DelayedTask implements Delayed {

    private static AtomicLong sequencer = new AtomicLong(0);
    private long mTime;
    private long mSequencer;
    private String mName;

    /**
     * @param r
     * @param result
     * @param ns     毫秒
     * @param period period
     */
    public DelayedTask(String name, long ns) {
        mName = name;
        mTime = ns;
        mSequencer = sequencer.getAndIncrement();
    }

    public int compareTo(Delayed o) {
        if (o == this) {
            return 0;
        }
        if (o instanceof DelayedTask) {
            DelayedTask delayedTask = (DelayedTask) o;
            long diff = mTime - delayedTask.mTime;
            if (diff < 0) {
                return -1;
            } else if (diff > 0) {
                return 1;
            } else if (mSequencer < delayedTask.mSequencer) {
                return -1;
            } else {
                return 1;
            }
        }

        long d = (getDelay(TimeUnit.NANOSECONDS) - o
                .getDelay(TimeUnit.NANOSECONDS));
        return d == 0 ? 0 : ((d < 0 ? -1 : 1));
    }

    public long getDelay(TimeUnit unit) {
        long convert = unit.convert(mTime - System.nanoTime(), TimeUnit.NANOSECONDS);
        System.out.println(convert);
        return unit.convert(mTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    public void start() {
        System.err.println(mName);
    }
}
