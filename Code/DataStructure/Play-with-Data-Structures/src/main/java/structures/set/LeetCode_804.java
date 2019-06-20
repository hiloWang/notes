package structures.set;

import java.util.TreeSet;

/**
 * https://leetcode-cn.com/problems/unique-morse-code-words/description/
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/13 16:19
 */
public class LeetCode_804 {

    private String[] codes = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};

    public int uniqueMorseRepresentations(String[] words) {
        TreeSet<String> treeSet = new TreeSet<>();
        for (String word : words) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                stringBuilder.append(codes[word.charAt(i) - 'a']);
            }
            treeSet.add(stringBuilder.toString());
        }
        int size = treeSet.size();
        System.out.println(size);
        return size;
    }

}
