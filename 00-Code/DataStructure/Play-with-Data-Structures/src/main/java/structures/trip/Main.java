package structures.trip;

import java.util.ArrayList;

import structures.set.BSTSet;
import structures.set.FileOperation;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/17 13:58
 */
public class Main {

    public static void main(String... args) {
        // testPerformance();
        testDelete();
        testDeleteOptimize();
    }


    private static void testDelete() {
        System.out.println("---------------------------------testDelete");
        Trip trip = new Trip();
        trip.add("deep");
        trip.add("append");
        trip.add("app");
        trip.add("hello");
        trip.add("world");
        trip.remove("deep");
        trip.remove("append");
        trip.remove("hello");
        System.out.println(trip.contains("deep"));
        System.out.println(trip.contains("append"));
        System.out.println(trip.contains("hello"));
        System.out.println(trip.contains("world"));
        System.out.println(trip);
    }


    private static void testDeleteOptimize() {
        System.out.println("---------------------------------testDeleteOptimize");
        Trip trip = new Trip();
        trip.add("deep");
        trip.add("append");
        trip.add("app");
        trip.add("hello");
        trip.add("world");
        trip.removeOptimize("deep");
        trip.removeOptimize("append");
        trip.removeOptimize("hello");
        System.out.println(trip.contains("deep"));
        System.out.println(trip.contains("append"));
        System.out.println(trip.contains("hello"));
        System.out.println(trip.contains("world"));
        System.out.println(trip);
    }

    private static void testPerformance() {
        System.out.println("set---------------------------------------------------------------");
        testBSTSet("files/pride-and-prejudice.txt", "傲慢与偏见");
        System.out.println("linked---------------------------------------------------------------");
        testTrip("files/pride-and-prejudice.txt", "傲慢与偏见");
    }

    private static void testTrip(String path, String name) {
        long start = System.nanoTime();
        ArrayList<String> words = new ArrayList<>();
        FileOperation.readFile(path, words);
        Trip trip = new Trip();
        System.out.println(name + "单词数：" + words.size());
        for (String word : words) {
            trip.add(word);
        }
        System.out.println(name + "词汇量：" + trip.getSize());
        for (String word : words) {
            trip.contains(word);
        }
        long used = System.nanoTime() - start;
        System.out.println("用时" + (used / 1000000000.0) + "s");
    }

    private static void testBSTSet(String path, String name) {
        long start = System.nanoTime();
        ArrayList<String> words = new ArrayList<>();
        FileOperation.readFile(path, words);
        BSTSet<String> bstSet = new BSTSet<>();
        System.out.println(name + "单词数：" + words.size());
        for (String word : words) {
            bstSet.add(word);
        }
        System.out.println(name + "词汇量：" + bstSet.size());
        for (String word : words) {
            bstSet.contains(word);
        }
        long used = System.nanoTime() - start;
        System.out.println("用时" + (used / 1000000000.0) + "s");
    }


}
