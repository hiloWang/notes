package structures.hash;

/**
 * https://leetcode-cn.com/problems/first-unique-character-in-a-string/description/
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/20 15:20
 */
public class LeetCode_387 {

    /*s所包含的字符只可能是 a-z */
    public int firstUniqChar(String s) {
        int[] table = new int[26];
        int length = s.length();

        for (int i = 0; i < length; i++) {
            table[s.charAt(i) - 'a']++;
        }
        for (int i = 0; i < length; i++) {
            if (table[s.charAt(i) - 'a'] == 1) {
                return i;
            }
        }
        return -1;
    }

}
