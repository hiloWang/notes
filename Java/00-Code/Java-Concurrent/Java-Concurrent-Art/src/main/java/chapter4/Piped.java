package chapter4;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 *管道流用于两个线程通过流传输数据
 */
public class Piped {

    public static void main(String[] args) throws IOException {
        PipedWriter pipedWriter = new PipedWriter();
        PipedReader pipedReader = new PipedReader();

        pipedWriter.connect(pipedReader);
        new Thread(new Print(pipedReader)).start();

        int receive;
        try {
            while ((receive = System.in.read()) != -1) {
                pipedWriter.write(receive);
            }
        } finally {
            pipedWriter.close();
        }


    }


    public static class Print implements Runnable {

        private final PipedReader mPipedReader;

        public Print(PipedReader pipedReader) {
            mPipedReader = pipedReader;
        }

        @Override

        public void run() {
            int receive = 0;
            try {
                while ((receive = mPipedReader.read()) != -1) {
                    System.out.println((char) receive);
                }
            } catch (IOException e) {


            }
        }
    }

}
