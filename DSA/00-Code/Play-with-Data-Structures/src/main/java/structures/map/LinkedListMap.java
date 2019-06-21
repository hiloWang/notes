package structures.map;

import java.util.Objects;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/13 17:57
 */
public class LinkedListMap<Key, Value> implements Map<Key, Value> {

    private Node dummyNode;
    private int size;

    public LinkedListMap() {
        dummyNode = new Node(null, null);
        size = 0;
    }

    private Node getNode(Key key) {
        Node cur = dummyNode.next;
        while (cur != null) {
            if (Objects.equals(cur.key, key)) {
                return cur;
            }
            cur = cur.next;
        }
        return null;
    }

    @Override
    public void add(Key key, Value value) {
        Node node = getNode(key);
        if (node == null) {
            dummyNode.next = new Node(key, value, dummyNode.next);
            size++;
        } else {
            node.value = value;
        }
    }

    @Override
    public Value remove(Key key) {
        Node cur = dummyNode;
        while (cur.next != null) {
            if (Objects.equals(key, cur.next.key)) {
                Node del = cur.next;
                cur.next = del.next;
                del.next = null;
                size--;
                return del.value;
            } else {
                cur = cur.next;
            }
        }
        return null;
    }

    @Override
    public Value get(Key key) {
        Node node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean contains(Key key) {
        return getNode(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void set(Key key, Value value) {
        Node node = getNode(key);
        if (node == null) {
            throw new NullPointerException("no this key");
        }
        node.value = value;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private class Node {

        private Key key;
        private Value value;
        private Node next;

        private Node(Key key, Value value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public Node(Key key, Value value) {
            this(key, value, null);
        }

        private Node() {
            this(null, null, null);
        }

        @Override
        public String toString() {
            return key + " = " + value;
        }

    }
}
