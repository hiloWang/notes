package structures.stack;


import structures.arrays.Array;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.22 11:09
 */
public class ArrayStack<E> implements Stack<E> {

    private Array<E> mArray;

    public ArrayStack() {
        mArray = new Array<>();
    }

    ArrayStack(int capacity) {
        mArray = new Array<>(capacity);
    }

    @Override
    public void push(E e) {
        mArray.addLast(e);
    }

    @Override
    public E pop() {
        return mArray.removeLast();
    }

    @Override
    public E peek() {
        return mArray.getLast();
    }

    @Override
    public boolean isEmpty() {
        return mArray.isEmpty();
    }

    @Override
    public int size() {
        return mArray.getSize();
    }

    public int getCapacity() {
        return mArray.getCapacity();
    }
}
