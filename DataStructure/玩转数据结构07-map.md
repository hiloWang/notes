# 映射（map）

Map，将键映射到值的对象。在 map 中可以使用匿名对象找到一个 ke y对应的 value。

---
## 1 映射的实现

定于 Map 的功能：

```java
public interface Map<Key, Value> {

    void add(Key key, Value value);

    Value remove(Key key);

    Value get(Key key);

    boolean contains(Key key);

    int size();

    void set(Key key, Value value);

    boolean isEmpty();

}
```

### 使用链表实现

```java
public class LinkedListMap<Key, Value> implements Map<Key, Value> {

    private Node dummyNode;
    private int size;

    public LinkedListMap() {
        dummyNode = new Node(null, null);
        size = 0;
    }

    private Node getNode(Key key) {
        Node cur = dummyNode.next;
        while (cur != null) {
            if (Objects.equals(cur.key, key)) {
                return cur;
            }
            cur = cur.next;
        }
        return null;
    }

    @Override
    public void add(Key key, Value value) {
        Node node = getNode(key);
        if (node == null) {
            dummyNode.next = new Node(key, value, dummyNode.next);
            size++;
        } else {
            node.value = value;
        }
    }

    @Override
    public Value remove(Key key) {
        Node cur = dummyNode;
        while (cur.next != null) {
            if (Objects.equals(key, cur.next.key)) {
                Node del = cur.next;
                cur.next = del.next;
                del.next = null;
                size--;
                return del.value;
            } else {
                cur = cur.next;
            }
        }
        return null;
    }

    @Override
    public Value get(Key key) {
        Node node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean contains(Key key) {
        return getNode(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void set(Key key, Value value) {
        Node node = getNode(key);
        if (node == null) {
            throw new NullPointerException("no this key");
        }
        node.value = value;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    private class Node {

        private Key key;
        private Value value;
        private Node next;

        private Node(Key key, Value value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public Node(Key key, Value value) {
            this(key, value, null);
        }

        private Node() {
            this(null, null, null);
        }

        @Override
        public String toString() {
            return key + " = " + value;
        }

    }
}
```

### 使用二分搜索树实现

```java
public class BSTMap<Key extends Comparable<Key>, Value> implements Map<Key, Value> {

    private Node root;
    private int size;

    @Override
    public void add(Key key, Value value) {
        root = addImproved(root, key, value);
    }

    /**
     * 向以node为根的二分搜索树中插入元素e，递归算法，返回插入新节点后二分搜索树的根。
     * 对于二分搜索树，递归的每一次深度都把源树的层级减一，直到最后的null位就是需要添加新节点的位置。这个递归算法的精髓在于定义了返回值。
     */
    private Node addImproved(Node node, Key key, Value value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        if (key.compareTo(node.key) < 0) {
            node.left = addImproved(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = addImproved(node.right, key, value);
        } else {// e.compareTo(node.e) == 0
            node.value = value;
        }

        return node;
    }

    @Override
    public Value remove(Key key) {
        Node node = getNode(root, key);
        if (node == null) {
            return null;
        }
        return remove(root, key).value;
    }

    /*删除指定元素的节点*/
    @SuppressWarnings("all")
    private Node remove(Node node, Key key) {
        if (node == null) {
            return null;
        }

        int compare = key.compareTo(node.key);

        if (compare < 0) {
            node.left = remove(node.left, key);
            return node;
        } else if (compare > 0) {
            node.right = remove(node.right, key);
            return node;
        } else {
            //compare == 0
            if (node.left == null) {/*要删除的节点只有右子树*/
                Node rightNode = node.right;
                node.right = null;
                size--;
                return rightNode;
            } else if (node.right == null) {/*要删除的节点只有左子树*/
                Node leftNode = node.left;
                node.left = null;
                size--;
                return leftNode;
            } else {/*要删除的节点既有左子树也有右子树*/
                //node.left != null && node.right != null
                //把node右子树中的最小值作为node 的替代
                Node successor = minimum(node.right);
                successor.right = removeMin(node.right);
                successor.left = node.left;
                node.left = node.right = null;
                return successor;
            }
        }

    }

    /* 递归算法，一直往左找，直到左子节点为null，就是该树的最小值*/
    private Node minimum(Node node) {
        if (node.left == null) {
            return node;
        }
        return minimum(node.left);
    }

    @SuppressWarnings("all")
    private Node removeMin(Node node) {
        if (node.left == null) {
            Node rightNode = node.right;
            node.right = null;
            size--;
            return rightNode;
        }
        node.left = removeMin(node.left);
        return node;
    }

    @Override
    public Value get(Key key) {
        Node node = getNode(root, key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean contains(Key key) {
        return getNode(root, key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void set(Key key, Value value) {
        Node node = getNode(root, key);
        if (node == null) {
            throw new NullPointerException("no this key");
        }
        node.value = value;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private Node getNode(Node node, Key key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) < 0) {
            return getNode(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            return getNode(node.right, key);
        } else {// e.compareTo(node.e) == 0
            return node;
        }
    }

    private class Node {

        Key key;
        Value value;
        Node left;
        Node right;

        Node(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }
}
```

---
## 2 性能对比


通过计算傲慢与偏见中的词汇量和单词出现的次数来测试性能。

```java
    public static void main(String... args) {
        System.out.println("LinkedListMap---------------------------------------------------------------");
        printWords("files/pride-and-prejudice.txt", "傲慢与偏见", new LinkedListMap<>());
        System.out.println("BSTMap---------------------------------------------------------------");
        printWords("files/pride-and-prejudice.txt", "傲慢与偏见", new BSTMap<>());
    }

    private static void printWords(String path, String name, Map<String, Integer> map) {
        long start = System.nanoTime();
        ArrayList<String> words = new ArrayList<>();
        FileOperation.readFile(path, words);

        for (String word : words) {
            if (map.contains(word)) {
                map.set(word, map.get(word) + 1);
            } else {
                map.add(word, 1);
            }
        }

        System.out.println(name + " 词汇量：" + map.size());
        System.out.println("pride 出现 " + map.get("pride") + "次");
        System.out.println("prejudice 出现 " + map.get("prejudice") + "次");

        long used = System.nanoTime() - start;
        System.out.println("用时 " + (used / 1000000000.0) + "s");
    }
```

结果：

    LinkedListMap---------------------------------------------------------------
    傲慢与偏见 词汇量：6530
    pride 出现 53次
    prejudice 出现 11次
    用时 10.389890681s
    BSTMap---------------------------------------------------------------
    傲慢与偏见 词汇量：6530
    pride 出现 53次
    prejudice 出现 11次
    用时 0.077937695s


具体的复杂度分析与 set 一致。

---
## 3 集合的有序性

- 使用二分搜索树实现的集合是有序的。
- 使用哈希表实现的集合是无序的。

---
## 4 集合与映射的关系

如果有了一个 Map 的底层实现，那么其可以直接用来实现 Set，这只是 Map 的一种特殊情况，即 Map 中的 Key 对应的 Value 始终是为 null 的。事实上 Java 类库中也是这么做的，比如 HashSet 内部就是使用的 HashMap 实现的。

---
## 5 [LeetCode 349](https://leetcode-cn.com/problems/intersection-of-two-arrays/description/) 和 [LeetCode 350](https://leetcode-cn.com/problems/intersection-of-two-arrays-ii/description/)

```java
public class LeetCode_349 {

    public int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> set = new HashSet<>();
        for (int i : nums1) {
            set.add(i);
        }

        List<Integer> list = new ArrayList<>();
        for (int i : nums2) {
            if (set.contains(i)) {
                list.add(i);
                set.remove(i);
            }
        }

        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

}


public class LeetCode_350 {

    public int[] intersect(int[] nums1, int[] nums2) {
        java.util.Map<Integer, Integer> map = new TreeMap<>();
        for (int i : nums1) {
            if (!map.containsKey(i)) {
                map.put(i, 1);
            }else {
                map.put(i, map.get(i) + 1);
            }
        }

        List<Integer> list = new ArrayList<>();

        for (int i : nums2) {
            if (map.containsKey(i)) {
                list.add(i);
                int value = map.get(i) - 1;
                if (value == 0) {
                    map.remove(i);
                }else {
                    map.put(i, value);
                }
            }
        }

        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

}
```