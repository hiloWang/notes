package structures.queue;


import structures.arrays.Array;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.22 22:57
 */
public class ArrayQueue<E> implements Queue<E> {

    private Array<E> mArray;

    public ArrayQueue() {
        mArray = new Array<>();
    }

    ArrayQueue(int capacity) {
        mArray = new Array<>(capacity);
    }

    @Override
    public int getSize() {
        return mArray.getSize();
    }

    @Override
    public boolean isEmpty() {
        return mArray.isEmpty();
    }

    @Override
    public void enqueue(E e) {
        mArray.addLast(e);
    }

    /*时间复杂度O(n)*/
    @Override
    public E dequeue() {
        return mArray.removeFirst();
    }

    @Override
    public E getFront() {
        return mArray.getFirst();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Stack: ");
        res.append('[');
        for (int i = 0; i < mArray.getSize(); i++) {
            res.append(mArray.get(i));
            if (i != mArray.getSize() - 1)
                res.append(", ");
        }
        res.append("] top");
        return res.toString();
    }

}
