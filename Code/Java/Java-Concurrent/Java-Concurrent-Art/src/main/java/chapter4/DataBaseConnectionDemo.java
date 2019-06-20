package chapter4;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟一个多线程链接数据库连接池
 */
public class DataBaseConnectionDemo {

    private static ConnectionPool mConnectionPool = new ConnectionPool();
    private static CountDownLatch mStart = new CountDownLatch(1);
    private static CountDownLatch mEnd;

    public static void main(String[] args) throws InterruptedException {

        int threadSize = 10;
        mConnectionPool.init(10);

        mEnd = new CountDownLatch(threadSize);

        AtomicInteger get = new AtomicInteger();
        AtomicInteger noGet = new AtomicInteger();
        int count = 20;
        for (int i = 0; i < threadSize; i++) {
            new Thread(new ConnectionRunner(count,get,noGet),"ConnectionRunner").start();
        }

        mStart.countDown();
        mEnd.await();

        System.out.println("total invoke =" + threadSize * count);
        System.out.println("get connection =" +  get.get());
        System.out.println("noGet connection =" +  noGet.get());
    }


    static class ConnectionRunner implements Runnable {

        int mCount;
        AtomicInteger mGet;
        AtomicInteger mNoGet;

        public ConnectionRunner(int count, AtomicInteger get, AtomicInteger noGet) {
            this.mCount = count;
            this.mGet = get;
            this.mNoGet = noGet;
        }

        @Override
        public void run() {
            try {
                mStart.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (mCount > 0) {
                try {
                    Connection connection = mConnectionPool.fetchConnection(1000);
                    System.out.println(connection);
                    if (connection != null) {
                        System.out.println("get connection");
                        try {
                            connection.createStatement();
                            connection.commit();
                        } finally {
                            mConnectionPool.releaseConnection(connection);
                            mGet.incrementAndGet();
                        }
                    } else {
                        System.out.println(" no get connection");
                        mNoGet.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mCount--;
                }
            }
            mEnd.countDown();
        }
    }


    public static class ConnectionPool {

        private LinkedList<Connection> mConnections = new LinkedList<Connection>();

        public void init(int connectionSize) {
            for (int i = 0; i < connectionSize; i++) {
                mConnections.add(ConnectionDriver.createConnection());
            }
        }

        public void releaseConnection(Connection connection) {
            if (connection != null) {
                synchronized (mConnections) {
                    mConnections.addLast(connection);
                    mConnections.notifyAll();
                }
            }
        }

        public Connection fetchConnection(long mills) throws InterruptedException {
            synchronized (mConnections) {
                if (mills == 0) {
                    while (mConnections.isEmpty()) {
                        mConnections.wait();
                    }
                    return mConnections.removeFirst();
                } else {
                    long future = mills + System.currentTimeMillis();
                    long remaining = mills;
                    while (mConnections.isEmpty() && remaining > 0) {
                        mConnections.wait(remaining);
                        remaining = future - System.currentTimeMillis();
                    }
                    Connection result = null;
                    if (!mConnections.isEmpty()) {
                        result = mConnections.removeFirst();
                    }
                    return result;
                }
            }
        }
    }


    public static class ConnectionDriver {
        public static Connection createConnection() {
            return new Connection();
        }
    }

    public static class Connection {

        public void createStatement() {

        }

        public void commit() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
