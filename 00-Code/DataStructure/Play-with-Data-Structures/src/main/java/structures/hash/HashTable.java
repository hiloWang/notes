package structures.hash;

import java.util.TreeMap;

/**
 * 哈希表实现
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/20 18:00
 */
public class HashTable<Key, Value> {

    private TreeMap<Key, Value>[] hashTable;
    private int M;//素数
    private int size;

    private static final int UPPER_TOL = 10;
    private static final int LOWER_TOL = 2;
    private int capacity_index = 0;

    /**
     * 素数表
     */
    private static final int[] CAPACITY
            = {53, 97, 193, 389, 769, 1543, 3079, 6151, 12289, 24593,
            49157, 98317, 196613, 393241, 786433, 1572869, 3145739, 6291469,
            12582917, 25165843, 50331653, 100663319, 201326611, 402653189, 805306457, 1610612741};

    @SuppressWarnings("unchecked")
    public HashTable() {
        this.M = CAPACITY[capacity_index];
        size = 0;
        hashTable = new TreeMap[M];
        for (int i = 0; i < M; i++) {
            hashTable[i] = new TreeMap<>();
        }
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7FFFFFFF) % M;
    }

    public int getSize() {
        return size;
    }

    public void add(Key key, Value value) {
        int hash = hash(key);
        TreeMap<Key, Value> map = hashTable[hash];
        if (map.containsKey(key)) {
            map.put(key, value);
        } else {
            map.put(key, value);
            size++;
            if (size >= UPPER_TOL * M && (capacity_index + 1 < CAPACITY.length)) {
                resize(CAPACITY[++capacity_index]);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void resize(int newM) {
        TreeMap<Key, Value>[] newTable = new TreeMap[newM];
        for (int i = 0; i < newM; i++) {
            newTable[i] = new TreeMap<>();
        }
        int oldM = M;
        this.M = newM;

        for (int i = 0; i < oldM; i++) {
            TreeMap<Key, Value> map = this.hashTable[i];
            for (Key key : map.keySet()) {
                newTable[hash(key)].put(key, map.get(key));
            }
        }

        this.hashTable = newTable;
    }

    public Value remove(Key key) {
        int hash = hash(key);
        TreeMap<Key, Value> map = hashTable[hash];
        if (map.containsKey(key)) {
            Value remove = map.remove(key);
            size--;
            if (size < LOWER_TOL * M && (capacity_index - 1 >= 0)) {
                resize(CAPACITY[--capacity_index]);
            }
            return remove;
        }
        return null;
    }

    public void set(Key key, Value value) {
        int hash = hash(key);
        TreeMap<Key, Value> map = hashTable[hash];
        if (map.containsKey(key)) {
            map.put(key, value);
        } else {
            throw new IllegalArgumentException("can not find value by key");
        }
    }

    public boolean contains(Key key) {
        int hash = hash(key);
        TreeMap<Key, Value> map = hashTable[hash];
        return map.containsKey(key);
    }

    public Value get(Key key) {
        int hash = hash(key);
        TreeMap<Key, Value> map = hashTable[hash];
        return map.get(key);
    }

}

