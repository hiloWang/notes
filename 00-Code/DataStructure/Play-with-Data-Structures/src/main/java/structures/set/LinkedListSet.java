package structures.set;

import structures.linked.LinkedList;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/13 15:44
 */
public class LinkedListSet<E> implements Set<E> {

    private LinkedList<E> mLinkedList;

    public LinkedListSet() {
        mLinkedList = new LinkedList<>();
    }

    @Override
    public boolean isEmpty() {
        return mLinkedList.isEmpty();
    }

    @Override
    public int size() {
        return mLinkedList.getSize();
    }

    @Override
    public boolean contains(E e) {
        return mLinkedList.contains(e);
    }

    @Override
    public void add(E e) {
        if (!contains(e)) {
            mLinkedList.addFirst(e);
        }
    }

    @Override
    public void remove(E e) {
        mLinkedList.removeElement(e);
    }
}
