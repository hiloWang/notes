package structures.set;

import structures.tree.BST;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/13 15:27
 */
public class BSTSet<E extends Comparable<E>> implements Set<E> {

    private BST<E> mBST;

    public BSTSet() {
        mBST = new BST<>();
    }

    @Override
    public boolean isEmpty() {
        return mBST.getSize() == 0;
    }

    @Override
    public int size() {
        return mBST.getSize();
    }

    @Override
    public boolean contains(E e) {
        return mBST.contains(e);
    }

    @Override
    public void add(E e) {
        mBST.add(e);
    }

    @Override
    public void remove(E e) {
        mBST.remove(e);
    }

}
