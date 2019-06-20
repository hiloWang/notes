package structures.heap;


/**
 * 使用数组实现二叉堆，根节点从 0 开始存储。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/14 11:35
 */
public class ArrayMaxHeap<E extends Comparable<E>> {

    private Array<E> data;

    public ArrayMaxHeap(int capacity) {
        data = new Array<>(capacity);
    }

    public ArrayMaxHeap() {
        data = new Array<>();
    }

    public ArrayMaxHeap(E[] array) {
        data = new Array<>(array);
        //执行heapify，找到最后一个非叶子节点，以此进行siftDown操作
        for (int i = parent(size() - 1); i >= 0; i--) {
            siftDown(i);
        }
    }

    // 看堆中的最大元素
    public E findMax() {
        if (data.getSize() == 0) {
            throw new IllegalArgumentException("Can not findMax when heap is empty.");
        }
        return data.get(0);
    }

    public E extractMax() {
        E max = findMax();
        data.swap(0, size() - 1);//把最小值移动到根节点，把最大值移动到最后
        data.removeLast();//移除最大值
        siftDown(0);
        return max;
    }

    private void siftDown(int index) {
        while (leftChild(index) < size()/*循环条件，没有到最下层*/) {
            int target = leftChild(index);//先获取左节点
            if (target + 1 < size() && data.get(target).compareTo(data.get(target + 1)) < 0) {//找两个子节点中的最大值
                target++;//选择右节点
            }
            if (data.get(index).compareTo(data.get(target)) < 0/*父亲比孩子小则交换*/) {
                data.swap(index, target);
                index = target;
            } else {
                break;
            }
        }
    }

    public void add(E e) {
        //添加到末尾
        data.addLast(e);
        //调整树结构
        siftUp(size() - 1/*last index*/);
    }

    private void siftUp(int index) {
        while (index > 0 && (data.get(parent(index)).compareTo(data.get(index)) < 0)/*//父节点比自己还小*/) {
            int parent = parent(index);
            data.swap(parent, index);
            index = parent;
        }
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.getSize();
    }

    // 返回完全二叉树的数组表示中，一个索引所表示的元素的父亲节点的索引
    public int parent(int index) {
        if (index == 0) {
            throw new IllegalArgumentException("index-0 doesn't have parent.");
        }
        return (index - 1) / 2;
    }

    // 返回完全二叉树的数组表示中，一个索引所表示的元素的左孩子节点的索引
    public int leftChild(int index) {
        return (index * 2) + 1;
    }

    // 返回完全二叉树的数组表示中，一个索引所表示的元素的左孩子节点的索引
    public int rightChild(int index) {
        return (index * 2) + 2;
    }

    //获取最大的元素，然后插入新的元素
    public E replace(E newE) {
        E max = findMax();
        data.set(0, newE);
        siftDown(0);
        return max;
    }

}
