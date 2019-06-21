package utils;

/**
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class Utils {

    private Utils() {

    }

    private static final Object SIGN = new Object();

    public static void blockMain() {
        synchronized (SIGN) {
            try {
                SIGN.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
