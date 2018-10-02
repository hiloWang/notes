# JavaType

![](index_files/JavaType.png)


- **Type** 是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型(Class)、参数化类型(ParameterizedType)、数组类型(GenericArrayType)、类型变量(TypeVariable)和基本类型(Class)。

- **GenericArrayType** 表示一种数组类型，其组件类型为参数化类型或类型变量。比如`T[] tArr `

- **ParameterizedType** 表示参数化类型，如 `Collection<String>`。

- **TypeVariable** 是各种类型变量的公共高级接口。比如:下面代码中的K、V、等类型

```
public class TypeVariableBean<K extends InputStream & Closeable, V> {
    // K 的上边界是 InputStream
    K key;
    // 没有指定的话 ，V 的 上边界 属于 Object
    V value;
    // 不属于TypeVariable
    V[] values;
    String str;
    List<K> kList;
}
```

- **WildcardType** 即通配符的类型，表示一个通配符类型表达式，如 `?、? extends Number` 或 `? super Integer`。

可以通过 `Class.getGenericSuperclass()` 返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。 


具体参考：
- [JavaType](#javatype)