package clink.core;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/12/9 20:03
 */
public abstract class ScheduleJob implements Runnable {

    protected final long mIdleTimeoutMilliseconds;
    protected final Connector mConnector;

    private Scheduler mScheduler;
    private ScheduledFuture mScheduledFuture;

    protected ScheduleJob(long idleTimeoutMilliseconds, Connector connector) {
        mIdleTimeoutMilliseconds = idleTimeoutMilliseconds;
        mConnector = connector;
    }

    synchronized void schedule(Scheduler scheduler) {
        mScheduler = scheduler;
        schedule(mIdleTimeoutMilliseconds);
    }

    synchronized void unSchedule() {
        if (mScheduler != null) {
            mScheduler = null;
        }
        if (mScheduledFuture != null) {
            mScheduledFuture.cancel(true);
            mScheduledFuture = null;
        }
    }

    protected synchronized void schedule(long idleTimeoutMilliseconds) {
        if (mScheduler != null) {
            mScheduledFuture = mScheduler.schedule(this, idleTimeoutMilliseconds, TimeUnit.MILLISECONDS);
        }
    }

}
