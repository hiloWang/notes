package structures.linked;

import java.util.Objects;

import structures.queue.Queue;


/**
 * 使用链表实现队列
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.24 23:35
 */
public class LinkedListQueue<E> implements Queue<E> {

    private Node head;//head 用于删除元素，当作队列头
    private Node tail;//tail 用于添加元素，当作队尾部
    private int size;

    public LinkedListQueue() {
        head = tail = null;
        size = 0;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void enqueue(E e) {
        if (head == null) {
            head = new Node(e);
            tail = head;
        } else {
            Node newTail = new Node(e);
            tail.next = newTail;
            tail = newTail;
        }
        size++;
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("LinkedQueue is empty");
        }

        Node remove = head;
        head = head.next;
        remove.next = null;

        if (head == null) {/*说明此时没有元素了，所以为节点也要置null*/
            tail = null;
        }

        size--;

        return remove.e;
    }

    @Override
    public E getFront() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("LinkedQueue is empty");
        }
        return head.e;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("LinkedListQueue: front ");

        Node cur = head;
        while (cur != null) {
            res.append(cur + "->");
            cur = cur.next;
        }
        res.append("NULL tail");
        return res.toString();
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
