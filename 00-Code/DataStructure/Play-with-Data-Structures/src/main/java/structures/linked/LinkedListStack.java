package structures.linked;


import structures.stack.Stack;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.24 23:32
 */
public class LinkedListStack<E> implements Stack<E> {

    private LinkedList<E> mLinkedList;

    public LinkedListStack() {
        mLinkedList = new LinkedList<>();
    }

    @Override
    public void push(E e) {
        mLinkedList.addFirst(e);
    }

    @Override
    public E pop() {
        return mLinkedList.removeFirst();
    }

    @Override
    public E peek() {
        return mLinkedList.getFirst();
    }

    @Override
    public boolean isEmpty() {
        return mLinkedList.isEmpty();
    }

    @Override
    public int size() {
        return mLinkedList.getSize();
    }

}
