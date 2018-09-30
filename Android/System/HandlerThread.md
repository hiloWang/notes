# HandlerThread解析

HandlerThread继承自Thread，其内部直接封装了Looper创建，使用HandlerThread时。我们可以直接使用Handler，它的是实现如下：

```java
    public class HandlerThread extends Thread {
        
        int mPriority;
        int mTid = -1;
        Looper mLooper;

        public HandlerThread(String name) {
            super(name);
            mPriority = Process.THREAD_PRIORITY_DEFAULT;
        }

        public HandlerThread(String name, int priority) {
            super(name);
            mPriority = priority;
        }

        protected void onLooperPrepared() {
        }

        @Override
        public void run() {
            mTid = Process.myTid();
            Looper.prepare();
            synchronized (this) {
                mLooper = Looper.myLooper();
                notifyAll();
            }
            Process.setThreadPriority(mPriority);
            onLooperPrepared();
            Looper.loop();
            mTid = -1;
        }

        public Looper getLooper() {
            if (!isAlive()) {
                return null;
            }

            // If the thread has been started, wait until the looper has been created.
            synchronized (this) {
                while (isAlive() && mLooper == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            return mLooper;
        }

        public boolean quit() {
            Looper looper = getLooper();
            if (looper != null) {
                looper.quit();
                return true;
            }
            return false;
        }
    
        public boolean quitSafely() {
            Looper looper = getLooper();
            if (looper != null) {
                looper.quitSafely();
                return true;
            }
            return false;
        }

        public int getThreadId() {
            return mTid;
        }
    }
```

可以看到，他的run方法中已经帮我们实现了Looper的创建和启动，这样我们就可以直接在HandlerThread中使用Handler了，其次他还提供了quit和quitSafely方法，用于退出消息轮询。可见HandleThread是一个很方便的类。

一般HandlerThread使用方式如下：


```java
     private void initBackThread()
        {
            mCheckMsgThread = new HandlerThread("check-message-coming");
            mCheckMsgThread.start();
            mCheckMsgHandler = new Handler(mCheckMsgThread.getLooper())
            {
                @Override
                public void handleMessage(Message msg)
                {
                    checkForUpdate();
                    if (isUpdateInfo)
                    {
                        mCheckMsgHandler.sendEmptyMessageDelayed(MSG_UPDATE_INFO, 1000);
                    }
                }
            };
        }
```

这样HandlerThread比普通Thread实现Handler消息轮询更加安全，我们可以看一下HandlerThread的getLooper()，它是具有阻塞功能的，因为线程切换的不确定性，在使用Handler时，我们并不能保证轮询线程的Looper已经创建好了，而getLooper()内部会判断，如果Looper没有创建好，则创建Hander的线程会阻塞直到轮询线程的Looper创建完毕！