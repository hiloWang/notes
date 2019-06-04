# Linux I/O

## 汇编程序的 Hello world

- 所有I/O操作最终都是在内核中做的。
- 我们用的C标准I/O库函数最终也是通过系统调用把I/O操作从用户空间传给内核，然后让内核去做I/O操作。

通过一个汇编程序理解 IO 的系统调用，具体参考[汇编程序的Hello world](https://akaedu.github.io/book/ch28s01.html)

## C 标准 I/O 库函数与 Unbuffered I/O 函数