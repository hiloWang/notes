# 集合（set）

---
## 1 集合实现

集合中不能存储重复元素。

应用场景：

- 客户访问统计。
- 词汇量统计。

集合的实现方式：

- 二分搜索树天然支持 set 所有的操作。
- 使用链表实现。

### 使用二分搜索树实现 Set

```java
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
```

### 使用链表实现 Set

```java
public class LinkedListSet<E> implements Set<E> {

    private LinkedList<E> mLinkedList;

    public LinkedListSet() {
        mLinkedList = new LinkedList<>();
    }

    @Override
    public boolean isEmpty() {
        return mLinkedList.isEmpty();
    }

    @Override
    public int size() {
        return mLinkedList.getSize();
    }

    @Override
    public boolean contains(E e) {
        return mLinkedList.contains(e);
    }

    @Override
    public void add(E e) {
        if (!contains(e)) {
            mLinkedList.addFirst(e);
        }
    }

    @Override
    public void remove(E e) {
        mLinkedList.removeElement(e);
    }
}
```

---
## 2 性能对比

通过计算双城记和傲慢与偏见中的词汇量来测试性能。

```java
 public static void main(String... args) {
        System.out.println("bst---------------------------------------------------------------");
        printWords("files/a-tale-of-two-cities.txt", "双城记", new BSTSet<>());
        printWords("files/pride-and-prejudice.txt", "傲慢与偏见", new BSTSet<>());
        System.out.println("linked---------------------------------------------------------------");
        printWords("files/a-tale-of-two-cities.txt", "双城记", new LinkedSet<>());
        printWords("files/pride-and-prejudice.txt", "傲慢与偏见", new LinkedSet<>());
    }

    private static void printWords(String path, String name, Set<String> stringBST) {
        long start = System.nanoTime();
        ArrayList<String> words = new ArrayList<>();
        FileOperation.readFile(path, words);
        System.out.println(name + "单词数：" + words.size());
        for (String word : words) {
            stringBST.add(word);
        }
        System.out.println(name + "词汇量：" + stringBST.size());
        long  used = System.nanoTime() -start;
        System.out.println("用时" + (used / 1000000000.0) + "s");
    }
```

结果：

        bst---------------------------------------------------------------
        双城计单词数：141489
        双城计词汇量：9944
        用时0.10852926s
        傲慢与偏见单词数：125901
        傲慢与偏见词汇量：6530
        用时0.056738372s
        linked---------------------------------------------------------------
        双城计单词数：141489
        双城计词汇量：9944
        用时3.914412906s
        傲慢与偏见单词数：125901
        傲慢与偏见词汇量：6530
        用时2.347496207s

### 链表复杂度

- 查：`O(n)`
- 增（添加头节点本身是常数级的，但需要先查防止重复添加）：`O(n)`
- 删：`O(n)`

### BST 复杂度

- 查：`O(h)`，h 是树的高度
- 增：`O(h)`，h 是树的高度
- 删：`O(h)`，h 是树的高度

h 的复杂度具体是多少呢？对于 **满二叉树** 来说，树的高度与节点数的关系如下图所示：

层级 | h 层有多少个节点
---|---
0 层 | 1
1 层 | 2
2 层 | 4
3 层 | 8
4 层 | 16
h 层 | 2<sup>(h-1)</sup>

所以：第 (h-1) 层有 2<sup>(h -1)</sup> 个节点，h 层的树有 （2<sup>1</sup> +2<sup>2</sup> + ... + 2<sup>(h-1)</sup>） = （2<sup>h</sup> -1），由此推导出（n是节点数）：

- 根据节点数求高度：h = log<sub>2</sub>(n + 1)
- 复杂度为：O(log<sub>2</sub> n)，即 O(log n)


### log(n) 和 O(n) 复杂度对比

n | O(log n)  | O(n) | 差距
---|---|---|---
n = 16 | 4 | 16| 4 倍
n = 1024 | 10 | 1024| 100 倍
n = 100万 | 20 | 100万| 5 万倍

### BST 有局限性

但是 BST 有局限性，BST 中的数据如果是按照大小顺序依次添加的，那么其将退化成链表，复杂度明显提高，变为 O(n)。而平衡二叉树没有这个问题。

### 集合的不同实现的复杂度对比

操作 | LinkedListSet  | BTSSet | BTSSet平均复杂度 | BTSSet退化成链表后
---|---|---|---|---
add|O(n) | O(h)|O(log n) |O(n) 
contains|O(n) | O(h) |O(log n) | O(n)|O(n)
remove| O(n)|O(h) |O(log n) | O(n)

---
## 3 [LeetCode804](https://leetcode-cn.com/problems/unique-morse-code-words/description/)

唯一摩尔斯密码词

```java
class Solution {
    
    private String[] codes = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};

    public int uniqueMorseRepresentations(String[] words) {
        //这里使用java的TreeSet，TreeSet底层是平衡二叉树。
        TreeSet<String> treeSet = new TreeSet<>();
        for (String word : words) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                stringBuilder.append(codes[word.charAt(i) - 'a']);
            }
            treeSet.add(stringBuilder.toString());
        }
        int size = treeSet.size();
        System.out.println(size);
        return size;
    }
}
```

---
## 4 集合的有序性

- 使用二分搜索树实现的集合是有序的。
- 使用哈希表实现的集合是无序的。