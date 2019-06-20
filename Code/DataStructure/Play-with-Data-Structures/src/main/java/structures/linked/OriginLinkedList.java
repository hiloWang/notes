package structures.linked;

import java.util.Objects;

/**
 * 链表实现：不使用虚拟头节点版本。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.24 22:21
 */
public class OriginLinkedList<E> {

    private Node head;//头节点，这种方式在操作时，需要针对头节点进行特殊的判断
    private int size;

    public OriginLinkedList() {
        head = null;
        size = 0;
    }

    public void addFirst(E e) {
        head = new Node(e, head);
        /*
        等价于
            Node newHead = new Node(e);
            newHead.next = head;
            head = newHead;
        */
        size++;
    }

    public void addLast(E e) {
        add(size, e);
    }

    public void add(int index, E e) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("index out of bound");
        }

        if (head == null) {
            addFirst(e);
        } else {
            Node prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
            }
            Node node = new Node(e);
            node.next = prev.next;
            prev.next = node;
            size++;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("OriginLinkedList：size = ").append(size);
        stringBuilder.append("; data = ");
        Node cur = head;
        while (cur != null) {
            stringBuilder.append(cur.e).append(" -> ");
            cur = cur.next;
        }
        stringBuilder.append(" end ");
        return stringBuilder.toString();
    }


    private class Node {

        private E e;
        private Node next;

        private Node(E e, Node next) {
            this.e = e;
            this.next = next;
        }

        private Node(E e) {
            this(e, null);
        }

        private Node() {
            this(null, null);
        }

        @Override
        public String toString() {
            return Objects.toString(e);
        }

    }

}
