package structures.trip;

import java.util.Map;
import java.util.TreeMap;

/**
 * https://leetcode-cn.com/problems/map-sum-pairs/description/
 */
public class LeetCode_677 {

    private Node root;

    public LeetCode_677() {
        root = new Node();
    }

    private class Node {
        private Map<Character, Node> next;
        private int value;

        private Node(int value) {
            this.value = value;
            next = new TreeMap<>();
        }

        private Node() {
            this(0);
        }

    }

    @SuppressWarnings("all")
    public void insert(String key, int val) {
        if (key == null) {
            return;
        }
        int length = key.length();
        Node cur = root;
        for (int i = 0; i < length; i++) {
            char c = key.charAt(i);
            if (cur.next.get(c) == null) {
                cur.next.put(c, new Node());
            }
            cur = cur.next.get(c);
        }
        cur.value = val;
    }

    public int sum(String prefix) {
        if (prefix == null) {
            return 0;
        }
        int length = prefix.length();
        Node cur = root;
        //找到最后节点匹配前缀的那个节点
        for (int i = 0; i < length; i++) {
            Node node = cur.next.get(prefix.charAt(i));
            if (node == null) {
                return 0;
            }
            cur = node;
        }
        return sum(cur);
    }

    private int sum(Node cur) {
        if (cur.next.isEmpty()) {
            return cur.value;
        }

        int sum = cur.value;
        for (Character character : cur.next.keySet()) {
            sum += sum(cur.next.get(character));
        }
        return sum;
    }

}
