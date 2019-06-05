package structures.map;

import java.util.ArrayList;

import structures.set.FileOperation;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/13 23:13
 */
public class Main {

    public static void main(String... args) {
        System.out.println("LinkedListMap---------------------------------------------------------------");
        printWords("files/pride-and-prejudice.txt", "傲慢与偏见", new LinkedListMap<>());
        System.out.println("BSTMap---------------------------------------------------------------");
        printWords("files/pride-and-prejudice.txt", "傲慢与偏见", new BSTMap<>());
    }

    private static void printWords(String path, String name, Map<String, Integer> map) {
        long start = System.nanoTime();
        ArrayList<String> words = new ArrayList<>();
        FileOperation.readFile(path, words);

        for (String word : words) {
            if (map.contains(word)) {
                map.set(word, map.get(word) + 1);
            } else {
                map.add(word, 1);
            }
        }

        System.out.println(name + " 单词量：" + map.size());
        System.out.println("pride 出现 " + map.get("pride") + "次");
        System.out.println("prejudice 出现 " + map.get("prejudice") + "次");

        long used = System.nanoTime() - start;
        System.out.println("用时 " + (used / 1000000000.0) + "s");
    }


}
