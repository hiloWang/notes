package structures.avl;

import structures.set.Set;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/18 23:36
 */
public class AVLSet<Key extends Comparable<Key>> implements Set<Key> {

    private AVLTree<Key, Object> mAVLTree = new AVLTree<>();

    @Override
    public boolean isEmpty() {
        return mAVLTree.isEmpty();
    }

    @Override
    public int size() {
        return mAVLTree.size();
    }

    @Override
    public boolean contains(Key key) {
        return mAVLTree.contains(key);
    }

    @Override
    public void add(Key key) {
        mAVLTree.add(key, null);
    }

    @Override
    public void remove(Key key) {
        mAVLTree.remove(key);
    }

}
