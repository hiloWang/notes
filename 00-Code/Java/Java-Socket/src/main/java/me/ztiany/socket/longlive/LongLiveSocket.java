package me.ztiany.socket.longlive;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/27 16:08
 */
final class LongLiveSocket {

    private static final long RETRY_INTERVAL_MILLIS = 3 * 1000;
    private static final long HEART_BEAT_INTERVAL_MILLIS = 5 * 1000;
    private static final long HEART_BEAT_TIMEOUT_MILLIS = 2 * 1000;

    private final String mHost;
    private final int mPort;
    private final DataCallback mDataCallback;
    private final ErrorCallback mErrorCallback;
    private ScheduledExecutorService mWriterExecutor;
    private ScheduledExecutorService mTimeoutListenerExecutor;
    private Future<?> mHeartbeatTimeoutTaskFuture;
    private Future<?> mHeartbeatFuture;

    private final Object mLock = new Object();
    private Socket mSocket;  // guarded by mLock
    private boolean mClosed; // guarded by mLock

    LongLiveSocket(String host, int port, DataCallback dataCallback, ErrorCallback errorCallback) {
        mHost = host;
        mPort = port;
        mDataCallback = dataCallback;
        mErrorCallback = errorCallback;
        mWriterExecutor = Executors.newSingleThreadScheduledExecutor();
        mWriterExecutor.execute(this::initSocket);
        mTimeoutListenerExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    private void initSocket() {
        while (true) {

            //如果用户已经关闭，就不启动了
            if (closed()) {
                return;
            }

            try {
                Socket socket = new Socket(mHost, mPort);
                //在我们启动过程中，用户可能调用 close，而启动过程需要初始化一系列对象和任务
                //不希望在中途被用户打断，如果在启动过程中保持原子操作，启动完毕后，再响应用户的关闭操作
                synchronized (mLock) {
                    if (mClosed) {
                        //静默关闭
                        silentlyClose(socket);
                        return;
                    }
                    mSocket = socket;
                    startReader();
                    startHeartbeat();
                }

                //初始化成功，跳出
                break;

            } catch (IOException e) {
                System.out.println("init socket error: " + e);
                /*如果用户已经关闭了或者不要求重试，则跳出*/
                if (closed() || !mErrorCallback.onError()) {
                    break;
                }
                //走到这里说明用户希望重试，则延迟一段事件后进行重试
                try {
                    TimeUnit.MILLISECONDS.sleep(RETRY_INTERVAL_MILLIS);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }

    /*心跳任务*/
    private Runnable mHeartbeatTask = new Runnable() {

        //我们使用长度为 0 的数据作为 heart beat
        private byte[] mHeartBeat = new byte[0];

        @Override
        public void run() {
            write(mHeartBeat, new WritingCallback() {
                @Override
                public void onSuccess() {
                    // 每隔 HEART_BEAT_INTERVAL_MILLIS 发送一次
                    mHeartbeatFuture = mWriterExecutor.schedule(mHeartbeatTask, HEART_BEAT_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
                    //如果 HEART_BEAT_TIMEOUT_MILLIS 后没有收到响应，则说明心跳超时了
                    System.out.println("start heartbeat timeout listener");
                    mHeartbeatTimeoutTaskFuture = mTimeoutListenerExecutor.schedule(mHeartBeatTimeoutTask, HEART_BEAT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
                }

                @Override
                public void onFail(byte[] data, int offset, int len) {
                    //no op
                }
            });
        }
    };

    /*心跳失败处理任务*/
    private final Runnable mHeartBeatTimeoutTask = () -> {
        System.out.println("mHeartBeatTimeoutTask heartbeat timeout");
        closeSocket();
    };

    /*开始心跳*/
    private void startHeartbeat() {
        mWriterExecutor.execute(mHeartbeatTask);
    }

    /*开始度服务器数据*/
    private void startReader() {
        new Thread(mReaderTask).start();
    }

    private Runnable mReaderTask = new Runnable() {
        @Override
        public void run() {
            System.out.println("start reader......");
            DataInputStream dataInputStream = null;
            try {
                //这里，如何确定服务器写完了一段数据是一个需要考虑的问题
                //为简单起见，假设msg不超过1024字节
                byte[] buffer = new byte[1024];
                InputStream inputStream = mSocket.getInputStream();
                dataInputStream = new DataInputStream(inputStream);

                while (true) {
                    int nbyte = dataInputStream.readInt();
                    System.out.println("read nbyte = " + nbyte);

                    if (nbyte == 0) {
                        System.out.println("readResponse: heart beat received");
                        cancelTimeoutListener();
                        continue;
                    }

                    if (nbyte > buffer.length) {
                        throw new IllegalStateException("Receive message with len " + nbyte + " which exceeds limit " + buffer.length);
                    }

                    if (readn(dataInputStream, buffer, nbyte) != 0) {
                        // Socket might be closed twice but it does no harm
                        silentlyClose(mSocket);
                        // Socket will be re-connected by writer-thread if you want
                        break;
                    }

                    mDataCallback.onData(buffer, 0, nbyte);
                }

            } catch (IOException e) {
                System.err.println("Reader error: " + e);
            } finally {
                Utils.close(dataInputStream);
            }
        }

        private int readn(InputStream in, byte[] buffer, int n) throws IOException {
            int offset = 0;
            while (n > 0) {
                int readBytes = in.read(buffer, offset, n);
                if (readBytes < 0) {
                    // EoF
                    break;
                }
                n -= readBytes;
                offset += readBytes;
            }
            return n;
        }

    };


    public void write(byte[] data, WritingCallback callback) {
        write(data, 0, data.length, callback);
    }

    public void write(byte[] data, int offset, int len, WritingCallback callback) {
        mWriterExecutor.execute(() -> {
            Socket socket = getSocket();

            if (socket == null) {
                // initSocket 失败而客户说不需要重连，但客户又叫我们给他发送数据
                throw new IllegalStateException("Socket not initialized");
            }

            try {
                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream out = new DataOutputStream(outputStream);
                //写数据的长度，再写数据
                out.writeInt(len);
                out.write(data, offset, len);
                callback.onSuccess();
            } catch (IOException e) {
                System.out.println("write error: " + e);
                closeSocket();
                callback.onFail(data, offset, len);
                if (!closed() && mErrorCallback.onError()) {
                    //重连
                    initSocket();
                }
            }

        });
    }

    private void silentlyClose(Socket socket) {
        Utils.close(socket);
    }

    public void close() {
        synchronized (mLock) {
            mClosed = true;
            // 关闭 socket，从而使得阻塞在 socket 上的线程返回
            closeSocketLocked();
        }
        mWriterExecutor.shutdownNow();
        mTimeoutListenerExecutor.shutdownNow();
    }

    private void closeSocket() {
        synchronized (mLock) {
            closeSocketLocked();
        }
    }

    private void closeSocketLocked() {
        if (mSocket == null) {
            return;
        }
        silentlyClose(mSocket);
        mSocket = null;
        cancelHeartbeat();
        cancelTimeoutListener();
    }

    private boolean closed() {
        synchronized (mLock) {
            return mClosed;
        }
    }

    private Socket getSocket() {
        synchronized (mLock) {
            return mSocket;
        }
    }

    private void cancelTimeoutListener() {
        if (mHeartbeatTimeoutTaskFuture != null) {
            mHeartbeatTimeoutTaskFuture.cancel(true);
        }
    }

    private void cancelHeartbeat() {
        if (mHeartbeatFuture != null) {
            mHeartbeatFuture.cancel(true);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Callbacks
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 错误回调
     */
    public interface ErrorCallback {
        /**
         * 如果需要重连，返回 true
         */
        boolean onError();
    }

    /**
     * 读数据回调
     */
    public interface DataCallback {
        void onData(byte[] data, int offset, int len);
    }

    /**
     * 写数据回调
     */
    public interface WritingCallback {
        void onSuccess();

        void onFail(byte[] data, int offset, int len);
    }


}
