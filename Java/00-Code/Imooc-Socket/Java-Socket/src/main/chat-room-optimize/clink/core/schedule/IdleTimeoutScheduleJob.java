package clink.core.schedule;

import java.util.concurrent.TimeUnit;

import clink.core.Connector;
import clink.core.ScheduleJob;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/12/9 20:11
 */
public class IdleTimeoutScheduleJob extends ScheduleJob {

    public IdleTimeoutScheduleJob(long timeIdle, TimeUnit timeUnit, Connector connector) {
        super(timeUnit.toMillis(timeIdle), connector);
    }

    @Override
    public void run() {
        // 最后的活跃时间
        long lastActiveTime = mConnector.getLastActiveTime();
        // 空闲超时时间值
        long idleTimeoutMilliseconds = this.mIdleTimeoutMilliseconds;
        //下一次调度的延迟时间：空闲超时：50；当前时间：100；最后活跃时间：80；当前就已消耗20，下一次调度就是30毫秒后
        long nextDelay = idleTimeoutMilliseconds - (System.currentTimeMillis() - lastActiveTime);
        if (nextDelay <= idleTimeoutMilliseconds) {
            // 调度下一次
            schedule(idleTimeoutMilliseconds);
            //执行这一次
            try {
                mConnector.fireIdleTimeoutEvent();
            } catch (Throwable throwable) {
                mConnector.fireExceptionCaught(throwable);
            }
        } else {
            // 激活时，如果当前判断未超时，则基于最后一次活跃时间进行二次调度
            schedule(nextDelay);
        }
    }

}
