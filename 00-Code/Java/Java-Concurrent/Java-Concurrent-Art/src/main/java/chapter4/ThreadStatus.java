package chapter4;

/**
 * 线程的状态
 */
public class ThreadStatus {

    public static void main(String[] args) {
        new Thread(new Sleep(), "sleep-1").start();
        new Thread(new Waiting(), "Waiting-1").start();
        new Thread(new Block(), "Block-1").start();
        new Thread(new Block(), "Block-2").start();
    }


    static class Sleep implements Runnable{
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Waiting implements Runnable{
        @Override
        public void run() {
            while (true) {
                synchronized (Waiting.class) {
                    try {
                        Waiting.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class Block implements Runnable{
        @Override
        public void run() {
            synchronized (Block.class) {
                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
