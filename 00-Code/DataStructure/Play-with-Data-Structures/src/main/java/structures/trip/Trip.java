package structures.trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 字典树(前缀树)，同 LeetCode208 https://leetcode-cn.com/problems/implement-trie-prefix-tree/description/
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/17 12:28
 */
public class Trip {

    private Node root;
    private int size;

    public Trip() {
        root = new Node();
        size = 0;
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

        @Override
        public String toString() {
            return "Node{" +
                    "next=" + next +
                    ", isWord=" + isWord +
                    '}';
        }
    }

    public void add(String word) {
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
        //最后一个节点了，现在标识它是一个单词的结尾
        if (!cur.isWord) {
            cur.isWord = true;
            size++;
        }
    }

    @SuppressWarnings("all")
    public boolean contains(String word) {
        if (word == null) {
            return false;
        }
        int length = word.length();
        Node cur = root;
        for (int i = 0; i < length; i++) {
            char key = word.charAt(i);
            if (cur.next.get(key) == null) {
                return false;
            }
            cur = cur.next.get(key);
        }
        return cur.isWord;
    }

    /*判断Trip是否有以 prefix 为前缀的单词*/
    @SuppressWarnings("all")
    public boolean isPrefix(String prefix) {
        if (prefix == null) {
            return false;
        }
        int length = prefix.length();
        Node cur = root;
        for (int i = 0; i < length; i++) {
            char key = prefix.charAt(i);
            if (cur.next.get(key) == null) {
                return false;
            }
            cur = cur.next.get(key);
        }
        return true;
    }

    public int getSize() {
        return size;
    }

    //递归的方式删除单词
    public void removeOptimize(String word) {
        if (word == null) {
            return;
        }
        removeOptimize(root, word, 0);
    }

    private boolean removeOptimize(Node node, String word, int index) {

        /*到达最后的位置*/
        /*最后一个节点的删除条件，它没有节点了*/
        if (index == word.length() - 1) {
            size--;
            boolean nextIsEmpty = node.next.get(word.charAt(index)).next.isEmpty();
            if (nextIsEmpty) {
                return true;
            } else {
                node.isWord = false;
                return false;
            }
        }

        /*链路上的节点的删除条件：不是单词结尾并且没有子节点了*/
        char c = word.charAt(index);
        Node next = node.next.get(c);
        if (next != null) {
            boolean ret = removeOptimize(next, word, index + 1);
            if (ret) {
                node.next.remove(c);
            }
            return ret && node.next.isEmpty() && !node.isWord;
        } else {
            return false;
        }
    }

    //遍历的方式删除单词
    public void remove(String word) {
        if (word == null) {
            return;
        }
        int length = word.length();
        List<Node> parents = new ArrayList<>();
        Node cur = root;
        parents.add(cur);
        for (int i = 0; i < length; i++) {
            Node node = cur.next.get(word.charAt(i));
            if (node == null) {
                return;
            } else {
                parents.add(node);
                cur = node;
            }
        }
        size--;

        /*最后一个节点的删除条件，它没有节点了*/
        if (cur.next.isEmpty()) {
            int index = parents.indexOf(cur) - 1;
            parents.get(index).next.remove(word.charAt(index));
            parents.remove(cur);
        } else {
            /*否则改变标记*/
            cur.isWord = false;
            return;
        }

        /*链路上的节点的删除条件：不是单词结尾并且没有子节点了*/
        int size = parents.size();
        for (int i = size - 1; i > 0; i--) {
            Node node = parents.get(i);
            if (node.next.isEmpty() && !node.isWord) {
                parents.get(i - 1).next.remove(word.charAt(i - 1));
            } else {
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Trip{" +
                "root=" + root +
                ", size=" + size +
                '}';
    }

}
