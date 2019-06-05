package structures.arrays;

/**
 * 数组封装，数组在内存空间中是连续的
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.7.21 11:30
 */
public class IntArray {

    private int[] data;
    /*size指向下一个将要被添加元素的位置*/
    private int size;

    public IntArray() {
        this(10/*default*/);
    }

    public IntArray(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity can not less than 0");
        }
        data = new int[capacity];
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return data.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(int index, int e) {
        /*add 时，index = size是可以的*/
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("index out of bound");
        }

        /*如果已经满了，就需要扩容，扩容到原来的两倍*/
        if (size == getCapacity()) {
            resize(getCapacity() * 2);
        }

        //在指定位置添加元素，就把该位置已经之后所有的元素往后移
        for (int i = size - 1; i >= index; i--) {
            data[i + 1] = data[i];
        }

        //指定位置赋值
        data[index] = e;
        size++;
    }

    private void checkIndexBound(int index) {
        /*数组只能线性增加，所以检测size边界*/
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("index out of bound");
        }
    }

    public void addLast(int e) {
        add(size, e);
    }

    public void addFirst(int e) {
        add(0, e);
    }

    public int remove(int index) {
        checkIndexBound(index);

        int result = data[index];

        /*index位置以后的元素往左边移动*/
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        size--;

        /*size减少到一定程度，则缩容，为什么是少到 capacity/4 时才缩容，为了防止复杂度震荡（add和remove反复调用，导致每次都resize），即错开扩容与缩容的时机*/
        if (size <= getCapacity() / 4 && getCapacity() / 2 != 0) {
            resize(getCapacity() / 2);
        }

        return result;
    }

    public int removeFirst() {
        return remove(0);
    }

    public int removeLast() {
        return remove(size - 1);
    }

    public int find(int e) {
        for (int i = 0; i < size; i++) {
            if (data[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public boolean removeElement(int e) {
        int index = find(e);
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    public boolean contains(int e) {
        return find(e) != -1;
    }

    public int get(int index) {
        checkIndexBound(index);
        return data[index];
    }

    public void set(int index, int e) {
        checkIndexBound(index);
        data[index] = e;
    }

    /*调整容量*/
    private void resize(int newCapacity) {
        int[] old = data;
        data = new int[newCapacity];
        for (int i = 0; i < size; i++) {
            data[i] = old[i];
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[ ");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(" ]");
        return "IntArray{" +
                "data=" + sb +
                ", size=" + size +
                ", capacity=" + data.length +
                '}';
    }

}
