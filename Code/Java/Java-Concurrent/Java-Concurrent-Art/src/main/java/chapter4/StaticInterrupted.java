package chapter4;

/**
 *测试线程的静态方法interrupted
 */
public class StaticInterrupted {

    static Thread main;

    public static void main(String... args) {

        main = Thread.currentThread();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    main.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().isInterrupted());
                System.out.println(Thread.interrupted());
                System.out.println(Thread.interrupted());
            }
        };
        thread.start();
        thread.interrupt();
    }


}
