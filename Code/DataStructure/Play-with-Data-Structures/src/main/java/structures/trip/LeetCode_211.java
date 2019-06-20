package structures.trip;

import java.util.Map;
import java.util.TreeMap;

/**
 * https://leetcode-cn.com/problems/add-and-search-word-data-structure-design/description/
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/17 14:26
 */
public class LeetCode_211 {

    private Node root;

    public LeetCode_211() {
        root = new Node();
    }

    private class Node {
        private Map<Character, Node> next;
        private boolean isWord;

        private Node(boolean isWord) {
            this.isWord = isWord;
            next = new TreeMap<>();
        }

        private Node() {
            this(false);
        }
    }

    @SuppressWarnings("all")
    public void addWord(String word) {
        if (word == null) {
            return;
        }
        int length = word.length();
        Node cur = root;
        for (int i = 0; i < length; i++) {
            char key = word.charAt(i);
            if (cur.next.get(key) == null) {
                cur.next.put(key, new Node());
            }
            cur = cur.next.get(key);
        }
        if (!cur.isWord) {
            cur.isWord = true;
        }
    }

    /*判断node为根节点的Trip中是否包含wold，wold可以带有模式匹配，字符 . 可以匹配任意单词*/
    @SuppressWarnings("all")
    public boolean search(String word) {
        return match(root, word, 0);
    }

    private boolean match(Node node, String word, int index) {
        int length = word.length();
        if (index == length) {
            return node.isWord;
        }
        char c = word.charAt(index);
        if (c == '.') {//. 可以匹配任何字符
            for (Character character : node.next.keySet()) {
                if (match(node.next.get(character), word, index + 1)) {
                    return true;
                }
            }
            return false;
        } else {
            Node next = node.next.get(c);
            return next != null && match(next, word, index + 1);
        }
    }

}
