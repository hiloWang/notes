# String相关内容

- String相关API
- 重载的`+`操作符，JVM使用StringBuilder进行的优化
- String格式化输出
- 正则表达式(重要)
    - Pattern
    - Matcher
- String的intern方法
- StringTokenizer
- String内部实现：字符数组、字节数组。

---
## 1 格式化输出

语法为：`%[argument_index$][flags][width][.precision]conversion`

- width用来表示最小尺寸，默认情况下是左对齐的，通过"-"符号可以改变对其的方向
- 与width对应的是precision，用来表示最大的尺寸，width可以根所有的数据类型转换，而precision则不行，
 - precision应用于string，表示打印String时除此字符的最大数量
 - precision应用与浮点数时，表示小数部分要显示出来的位数，默认是6为位，按四舍五入运算

示例：

```java
    System.out.print(String.format("%-15s %5s %10s\n", "item", "Qty", "Price"));//-表示右对齐，15表示宽度
    System.out.print(String.format("%-15s %5s %10s\n", "-----", "-----", "-----"));
    System.out.print(String.format("%-15.15s %5d %10.2f\n", "Jacks Magic Be", 4, 4.25));// 15.15表示15个宽度，最多打印15个字符，%10.2f表示10个宽度，浮点保留两位
    System.out.print(String.format("%-15.15s %5d %10.2f\n", "Jacks Magic Be", 5, 2.2));


    //打印结果
    item              Qty      Price
    -----           -----      -----
    Jacks Magic Be      4       4.25
    Jacks Magic Be      5       2.20
```

---
**System.out.printf与String的Format转换符：**

符号|类型|示例|符号|类型|示例
--|--|--|--|--|--|--|---
d|十进制|432|s|字符串|hello
x|十六进制|9F|c|字符|a
o|八进制|032|b|布尔|true
f|浮点|15.9|h|散列码|42628b2
e|指数浮点|1.86e+01|tx或Tx|日期时间(T强制大写)|
g|通用浮点|——|%|百分号|%
a|十六进制浮点|0x1.fccdp3|n|与平台有关的行分隔符|——

格式化字符串可以使用参数索引，索引必须在 `%` 号后面，以`$`符号结束(索引从1开始)，`<`标志，指示前面格式说明中的参数将再次被使用

`System.out.printf("%1$s %2$tB %2$tY  ", "Date", new Date());` `1$`表示格式化第一次索引，后面`%2$tB %2$tY`使用的都是Date对象，只是格式化的方式不一样。`System.out.printf("%s %tB %<tY  ", "Date", new Date());`与前面语句输出一样。

