package structures.queue;

/**
 * 环形队列
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.22 23:19
 */
public class LoopQueue<E> implements Queue<E> {

    private E[] data;
    private int front, tail;
    private int size;

    public LoopQueue() {
        this(10/*default*/);
    }

    @SuppressWarnings("unchecked")
    public LoopQueue(int capacity) {
        /*需要多一个空位，判断队列是否已满*/
        data = (E[]) new Object[capacity + 1];
        front = tail = size = 0;
    }

    public int getCapacity() {
        return data.length - 1;
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
        if ((tail + 1) % data.length == front/*队列已满*/) {
            resize(getCapacity() * 2);
        }
        data[tail] = e;
        tail = (tail + 1) % data.length;
        size++;
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("queue is empty");
        }

        E result = data[front];
        data[front] = null;
        size--;
        front = (front + 1) % data.length;

        /*缩容*/
        if (size <= getCapacity() / 4 && getCapacity() / 2 != 0) {
            resize(getCapacity() / 2);
        }

        return result;
    }

    @Override
    public E getFront() {
        return data[front];
    }

    private void resize(int newCapacity) {
        @SuppressWarnings("unchecked")
        E[] newData = (E[]) new Object[newCapacity + 1];
        /*调整队列*/
        for (int i = 0; i < size; i++) {
            newData[i] = data[(i + front) % data.length];
        }
        data = newData;
        front = 0;
        tail = size;
    }

    @Override
    public String toString(){
        StringBuilder res = new StringBuilder();
        res.append(String.format("Queue: size = %d , capacity = %d\n", size, getCapacity()));
        res.append("front [");
        for(int i = front; i != tail; i = (i + 1) % data.length){
            res.append(data[i]);
            if((i + 1) % data.length != tail)
                res.append(", ");
        }
        res.append("] tail");
        return res.toString();
    }

}
