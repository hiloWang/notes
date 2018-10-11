# ConcureentLinkedQueue

在并发编程中，有时候需要使用线程安全的队列，如果要实现一个线程安全的队列，由两种方法:

- 一种是使用阻塞算法。
- 另一中是使用非阻塞算法，非阻塞算法可以通过非阻塞的循环CAS方式来实现。

## 1 ConcureentLinkedQueue的结构






