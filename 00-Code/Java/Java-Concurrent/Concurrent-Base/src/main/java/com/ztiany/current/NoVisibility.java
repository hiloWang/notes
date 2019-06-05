package com.ztiany.current;

public class NoVisibility {

    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {

        @Override
        public void run() {
            while (!ready)
                Thread.yield();
            System.out.print(number);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ReaderThread().start();
        ready = true;
        number = 42;
    }

}
