package structures.linked;

import java.util.Objects;

/**
 * 链表实现：使用虚拟头节点版本。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.24 22:21
 */
public class LinkedList<E> {

    private Node dummyNode;//虚拟头节点，仅用于标识头节点，使用虚拟结点可以统一结点操作
    private int size;

    public LinkedList() {
        dummyNode = new Node(null, null);
        size = 0;
    }

    public void addFirst(E e) {
        add(0, e);
    }

    public void addLast(E e) {
        add(size, e);
    }

    public void add(int index, E e) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("index out of bound");
        }

        Node prev = dummyNode;//指向虚拟头节点
        //找到index之前的那个节点
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }

        Node newNode = new Node(e);
        newNode.next = prev.next;
        prev.next = newNode;
        size++;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("index out of bound");
        }

        Node prev = dummyNode;//指向虚拟头节点
        //找到index之前的那个节点
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }
        Node remove = prev.next;
        prev.next = remove.next;
        remove.next = null;
        size--;
        return remove.e;
    }

    public E removeFirst() {
        return remove(0);
    }

    public E removeLast() {
        return remove(size - 1);
    }

    public boolean removeElement(E e) {
        Node prev = dummyNode;
        while (prev.next != null) {

            if (Objects.equals(prev.next.e, e)) {

                Node remove = prev.next;
                prev.next = remove.next;
                remove.next = null;
                size--;

                return true;
            }

            prev = prev.next;
        }
        return false;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(E e) {
        Node cur = dummyNode.next;
        while (cur != null) {
            if (Objects.equals(cur.e, e)) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("index out of bound");
        }

        Node cur = dummyNode;//指向虚拟头节点
        //找到index那个节点
        for (int i = 0; i < index + 1; i++) {
            cur = cur.next;
        }
        return cur.e;
    }

    public E getFirst() {
        return get(0);
    }

    public E getLast() {
        return get(size - 1);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LinkedList：size = ").append(size);
        stringBuilder.append("; data = ");
        Node cur = dummyNode.next;
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
