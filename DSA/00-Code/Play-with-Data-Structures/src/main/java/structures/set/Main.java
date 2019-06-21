package structures.set;

import java.util.ArrayList;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/13 15:33
 */
public class Main {

    public static void main(String... args) {
        System.out.println("bst---------------------------------------------------------------");
        printWords("files/a-tale-of-two-cities.txt", "双城记", new BSTSet<>());
        printWords("files/pride-and-prejudice.txt", "傲慢与偏见", new BSTSet<>());
        System.out.println("linked---------------------------------------------------------------");
        printWords("files/a-tale-of-two-cities.txt", "双城记", new LinkedListSet<>());
        printWords("files/pride-and-prejudice.txt", "傲慢与偏见", new LinkedListSet<>());
    }

    private static void printWords(String path, String name, Set<String> stringBST) {
        long start = System.nanoTime();
        ArrayList<String> words = new ArrayList<>();
        FileOperation.readFile(path, words);
        System.out.println(name + "单词数：" + words.size());
        for (String word : words) {
            stringBST.add(word);
        }
        System.out.println(name + "词汇量：" + stringBST.size());
        long  used = System.nanoTime() -start;
        System.out.println("用时" + (used / 1000000000.0) + "s");
    }

}
