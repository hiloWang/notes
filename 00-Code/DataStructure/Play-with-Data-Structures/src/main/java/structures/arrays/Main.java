package structures.arrays;

/**
 * 动态数组
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.21 12:57
 */
public class Main {

    public static void main(String... args) {
        testIntArray();
        System.out.println("--------------------------------");
        testArray();
    }

    private static void testArray() {
        Array<String> stringArray = new Array<>();

        for (int i = 0; i < 20; i++) {
            stringArray.addLast(String.valueOf(i));
        }

        System.out.println(stringArray);

        for (int i = 0; i < 16; i++) {
            stringArray.removeFirst();
        }

        System.out.println(stringArray);
    }

    private static void testIntArray() {
        IntArray intArray = new IntArray();

        for (int i = 0; i < 20; i++) {
            intArray.addLast(i);
        }

        System.out.println(intArray);

        for (int i = 0; i < 16; i++) {
            intArray.removeFirst();
        }

        System.out.println(intArray);
    }

}
