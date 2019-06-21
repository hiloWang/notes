package structures.heap;

import structures.queue.Queue;

/**
 * 使用最大堆实现优先队列。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/14 16:04
 */
public class PriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private ArrayMaxHeap<E> mMaxHeap = new ArrayMaxHeap<>();

    @Override
    public int getSize() {
        return mMaxHeap.size();
    }

    @Override
    public boolean isEmpty() {
        return mMaxHeap.isEmpty();
    }

    @Override
    public void enqueue(E e) {
        mMaxHeap.add(e);
    }

    @Override
    public E dequeue() {
        return mMaxHeap.extractMax();
    }

    @Override
    public E getFront() {
        return mMaxHeap.findMax();
    }
}
