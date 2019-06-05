package structures.avl;

import structures.map.Map;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/18 23:30
 */
public class AVLMap<Key extends Comparable<Key>, Value> implements Map<Key, Value> {

    private AVLTree<Key, Value> mAVLTree = new AVLTree<>();

    @Override
    public void add(Key key, Value value) {
        mAVLTree.add(key, value);
    }

    @Override
    public Value remove(Key key) {
        return mAVLTree.remove(key);
    }

    @Override
    public Value get(Key key) {
        return mAVLTree.get(key);
    }

    @Override
    public boolean contains(Key key) {
        return mAVLTree.contains(key);
    }

    @Override
    public int size() {
        return mAVLTree.size();
    }

    @Override
    public void set(Key key, Value value) {
        mAVLTree.set(key, value);
    }

    @Override
    public boolean isEmpty() {
        return mAVLTree.isEmpty();
    }

}
