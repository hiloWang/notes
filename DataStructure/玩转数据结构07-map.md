# 映射（map）

Map，将键映射到值的对象。在 map 中可以使用匿名对象找到一个 ke y对应的 value。

## 映射的实现

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
class Node {
    K key;
    V value;
    Node next;
}
```

### 使用二分搜索树实现

```java
class Node {
    K key;
    V value;
    Node left;
    Node right;
}
```