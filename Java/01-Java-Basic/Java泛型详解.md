# 泛型

---
## 1 泛型(Generic)简介

泛型是java1.5推出的一个新特性，是一种安全机制，同时也使程序更具有扩展性。

- 把运行时的ClassCastException转移到编译时期，从而简化开发
- 避免了强制类型转换的麻烦
- 泛型程序意味着编写的代码可以被很多不同类型的对象所重用

所谓泛型，就是允许定义**类，接口，方法**时使用类型参数，这个类型参数将在声明变量，创建对象，调用方法时动态的指定(即传入实际的类型参数)


### 泛型中的相关术语

一个泛型类就是具有一个或多个类型变量的类，比如`List<T>`

```
    ArrayList<E> 类定义和ArrayList<Integer>类引用中涉及如下术语：
    ArrayList<E>可以成为一个泛型类型集合
    ArrayList<E>中的E称为类型变量或者类型参数
    ArrayList<Integer>称为参数化类型
    ArrayList<Integer>中的Integer称为E的实际类型参数
    ArrayList<Integer>中的<>念做type of
```

### 泛型类

定义员工对象和经理对象：

```java
     public class Employee {

        private String mName;
        private String mId;

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }
    }

    public class Manager extends Employee {
        private float bonus;

        public float getBonus() {
            return bonus;
        }

        public void setBonus(float bonus) {
            this.bonus = bonus;
        }

    }
```

定义`Pari<T>`泛型类：

```
    public class Pair<T> {

        private T mFirst;
        private T mSecond;
        void setFirst(T t) {
            mFirst = t;
        }
        public T getFirst() {
            return mFirst;
        }
        public void setSecond(T second) {
            mSecond = second;
        }
        public T getSecond() {
            return mSecond;
        }
    }
```

使用泛型类：

```
    Pair<Employee> pairEmployee = new Pair<>();
    pairEmployee.setFirst(new Employee());
    pairEmployee.setSecond(new Employee()
```

### 泛型方法

泛型同样可以定义在方法中，但是static修饰的方法或者变量无法使用类上泛型参数，因为类上的泛型参数只有在示例话对象时才能确定，而静态随着类的加载而加载。

```java
    class ArrayAlg{
       public static<T> T getMiddle(T... t){
           return a[t.length /2 ];
       }
    }
```

当需要调用泛型方法时，在方法名前面的尖括号中放入实际类型参数：

```
    String middle = ArrayAlg.<String>getMiddle("john","Q","public");
```

大多数情况下编译器可以推断出实际类型参数，所以上面方法可以简化为：

```
    String middle = ArrayAlg.getMiddle("john","Q","public");
```

---
## 2 泛型代码与虚拟机

### 泛型的擦除

**虚拟机没有泛型类型对象**所有的对象都属于普通类。无论何时定义个泛型类型，在运行时泛型类型都会被擦除。

JAVA语言中的泛型基本上完全是在编译器中实现的，用于编译器执行类型检查和类型推断，然后生成普通的非泛型的字节码 这种实现技术称之为**擦除**

```
         //泛型只在编译时期有效，用过反射可以跳过泛型
        Collection<String> v=newVector<String>();
        v.getClass().getMethod("add",Object.class).invoke(v, 8);
```

可以说Java中的泛型是伪泛型，或者说是编译期泛型，与之相对的C++中的泛型是真的运行时返现。

###  擦除导致的泛型不可变性

泛型中没有逻辑上的父子关系，如`List<Object>`并不是` List<String>`的父类。两者擦除之后都是List，所以形如下面的代码，编译器会报错：

```
    //不能同时存在这样的两个方法。
    void method(List<Object> numbers) {
    }
    void method(List<String> strings) {
    }
```
或者从另一种情况也可以说明Java为什么要禁用容器的协变性

```Java
List<String> strs = new ArrayList<String>();
List<Object> objs = strs; // ！！！即将来临的问题的原因就在这里。Java 禁止这样！
objs.add(1); // 这里我们把一个整数放入一个字符串列表
String s = strs.get(0); // ！！！ ClassCastException：无法将整数转换为字符串
```
从代码可以看出，如果允许泛型协变性，则会出现类型安全的问题，因此Java禁止这样的事情以保证运行时的安全。

**泛型的这种情况称为不可变性，与之对应的概念是协变、逆变**

- 协变：如果A是B的父类，并且A的容器比如（`List<A>`） 也是 B 的容器（`List<B>`）的父类，则称之为协变的（父子关系保持一致）
- 逆变：如果A是B的父类，但是A的容器 是B的容器的子类，则称之为逆变（放入容器就篡位了）
- 不可变：不论AB有什么关系，A的容器和B的容器都没有父子关系，称之为不可变

**Java 中数组是协变的，泛型是不可变的。**


### 翻译泛型表达式

Java编辑器会将泛型代码中的类型完全擦除，使其变成原始类型，编译器会在编译的字节码中插入类型转换的代码。

```
    List<String> stringList = new ArrayList<>();
    String str = stringList.get(0);
    //被编译后的代码应该是这样的
    List list = new ArrayList<>();
    String str = (String) list.get(0);
```

翻译泛型方法：

```
    class DateInterval extends Pair<Date> {
            @Override
            public void setSecond(Date second) {
                if (second.compareTo(getFirst()) >= 0) {
                    super.setSecond(second);
                }
            }
    }
```

这个类被擦除后变成：

```
    class DateInterval extends Pair {
          public void setSecond(Date second) {
          }
    }
```

但是存在另一个从Pair继承过来的方法`public void setSecond(Object scond)`，这显然不是同一个方法，Object与Data不是同一个类型的。

    DateInterval dateInterval = new DateInterval();
    Pair<Date> pair = dateInterval;
    pair.setSecond(new Date());

这里希望对setSecond的调用具有多态性，并调用最合适的方法(不考虑泛型，当然是调用的`setSecond(Object scond)`方法)，由于pair引用的是dateInterval，所有应该调用DateInterval的setSecond方法，但是问题在于类型擦除与多态发发生了冲突，这时编译器在DateInterval中插入一个桥方法：`public void setSecond(Object second){setSecond((Date)second)}`，这些都是编译器做的事。

总之：

- 虚拟机中没有泛型，只有普通类型和方法
- 所有的类型参数都用它们限定符替换
- 桥方法被合成来保证多态
- 为保持类型安全，必要时插入强制类型转换

### 泛型推断

编译器判断泛型方法的实际参数的过程称为类型推断，类型推断是相对于知觉推断的，其实现方法是一种非常复杂的过程。比如：当某个变量在整个参数类表中的所有参数和返回值中都被应用到，调用方法时这多处实际类型参数不一样 ，这时候取这多个参数的最大交集:

```
        public static void main(String... args) {
            Number add = add(1, 1.2);
        }

        static <T> T add(T a, T b) {

        }
```

---
## 3 泛型的约束与局限

### 基本类型不用作为类型参数

    Pair<int>是不合法的

### 运行时类型查询只适用于原始类型

```
        if (args instanceof List<String>) {//不合法

        }
```

### 不能创建参数化类型的数组

`Pair<String>[] pairs = new Pair<String>[];`是不合法的，为什么呢？，

```
            Pair<String>[] pairs = new Pair<String>[4];//假设合法
            Object[] objects = pairs;/数组具有的协变性
            //数组会记住它的元素类型，如果试图存入其他类型的元素，就会抛出一个ArrayStoreException异常
            objects[0] = "Hello";//运行时错误，component type is Pair
```

由于数组具有协变性，导致泛型数组的不安全性，不允许创建参数化类型的数组。

>扩展：在用到反射时，使用 `java.lang.reflect.Array` 类来创建数组
```
        //演示用Array类来创建数组
        public static <T> T[] newInstance(Class<T> componentType,int length){
            return (T[])Array.newInstance(componentType, length);
        }
```

### 不能实例化类型变量

下面的语句是非法的：

```
    public Pair(){
        first = new T[];//实例化T
        T.class//应用T的class
    }
```

### 不能抛出或捕获泛型类的实例

不合法

```
    public static <T extends Exception> void testCatchException() throws T {
            try {
            } catch (T e) {
            } finally {
            }
        }
```

可以抛出泛型异常

```
        public static <T extends Exception> void testThrowException() throws T {
            try {

            } catch (Exception e) {
                throw (T) e;
            } finally {

            }
        }
```

使用泛型可以消除对已检查异常的检查(参考自CoreJava)

```
        private static void testThrowAs(Exception e) {
            throwAs(e);
        }

        @SuppressWarnings("unchecked")
        public static <T extends Throwable> void throwAs(Throwable throwable) throws T {
            throw (T) throwable;
        }
```

---
## 4 类型限定

Java泛型系统允许在泛型上加入类型限定：

- `<T extends Comparable>`表示泛型上限
- `<T super Comparable>`表示泛型下限

### 4.1 类型上限

有时需要在类或方法上对类型变量加以约束，比如下面`<T extends Comparable>`表示类型T被限定为Comparable的子类。所以可以调用Comparable的compareTo方法。

```
          public <T extends Comparable> T min(T[] a) {
            if (a == null || a.length == 0) {
                return null;
            }
            T small = a[0];
            for (int i = 0; i < a.length; i++) {
                if (small.compareTo(a[i]) > 0) {
                    small = a[i];
                }
            }
            return small;
        }
```

下面类定义一个泛型类型，其必须是Number的子类，并且是实现了Serializable接口，使用这种语法时，类上限必须放在第一位，因为父类只有一个，接口可以有多个

```
    class GenericClass<T extends Number & Serializable> {

    }
```


---
## 5 类型通配符

固有的泛型类型系统使用起来并没有那么愉快，而使用类型统配符`?`(表示一个占位符)将更加灵活。

### 5.1 通配符上限

    List<? extends Employee> genList2 = new ArrayList<>();

表示任何的泛型List类型，它的类型参数必须是Employee的子类，比如`List<Manager>`,而不是`List<String>`,比较下面两个方法，printNumberList更加灵活。

```
        //只能传入Manager列表
        private static void printManagerList(List<Manager> managers) {
            for (Manager manager : managers) {
                System.out.println(manager.getName());
            }
        }
        //可以传入Employee及其子类型列表
        private static void printEmployeeList(List<? extends Employee> employees) {
            for (Employee employee : employees) {
                System.out.println(employee.getName());
            }
        }
```

看下面代码：

            List<? extends Employee> list;
            List<Manager> managerList = new ArrayList<>();
            //可以理解为List<? extends Employee>现在是List<Manager>的父类型
            list = managerList;
            //list.add(new Employee()); 编译错误
            managerList.add(new Manager());//ok

`List<Manager>`是`List<? extends Employee>`的子类型。

对`list.add(new Employee())`调用有一个编译错误，对于`List<? extends Employee>`的方法可以理解为：

```
    ? extends Employee get()
    void add( ? extends Employee )
```

这样不能调用add方法，编译器只知道需要某个Employee的子类型，但是不知道具体是什么类型，它拒绝传递任何特定的类型，毕竟`?`不能用来匹配。

**简而言之，带 extends 限定（上界）的通配符类型使得类型是协变的（covariant）。**


### 5.2 通配符下限

通配符还可以指定下限，比如`? super Manager`，这个通配符限制为Manager的所有超类，它与通配符上限刚好相反，可以为方法提供参数，但是不能有返回值。

`Pair<? super Manager>`有如下方法：

     void setFirst(? super Manager)//编译器不知道setFirst的确切类型，但是可以调用任意的Manger对象和Manager的子类对象，但不能是Employee。

     ? super Manager getFirst()//调用getFirst返回的对象不能保证确切的类型，只能返回一个Object

通配符下限能力：

#### 1 使得对象可以写入父类型的容器

```java
        class A {
        }
        class B extends A {
        }
        class C extends B {
        }
        //? super A表示只要是A和A的子类都可以被添加到容器中
        List<? super A> integers2 = new ArrayList<>();
        integers2.add(new C());
        integers2.add(new B());
        integers2.add(new A());
        Object object = integers2.get(0);
```
现在可以写这样的方法

```java
    private static <T> T copy(Collection<? super T> dest, Collection<T> src) {
        T last = null;
        for (T ele : src) {
            last = ele;
            dest.add(ele);
        }
        //返回一个T类型的变量
        return last;
    }
```

#### 2 使得父类型的比较方法可以应用于子类对象。

比如TreeSet的一个构造函数`TreeSet(Comparator<? super E> comparator)`这里的E限制comparator的实际类型参数只能是E或E的父类，那么以E的子类为实际类型参数的TreeSet都可以使用实际类型参数为E的比较器。这里可以把comparator看着一个消费者。

```java
     private static Comparator<Manager> mManagerComparator = new Comparator<Manager>() {
            @Override
            public int compare(Manager o1, Manager o2) {
                return 0;
            }
        };

        private static Comparator<Employee> sEmployeeComparator = new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return 0;
            }
        };
        //Comparator<Manager> mManagerComparator可用于比较Manager
        TreeSet<Manager> managers1 = new TreeSet<>(mManagerComparator);
        //Comparator<Employee> sEmployeeComparator也可以用于比较Manager
        TreeSet<Manager> managers2 = new TreeSet<>(sEmployeeComparator);
```

泛型下限产生的逆变性

```java
   private void contravariance() {
        //可以理解为List<? super String> 是 List<Object> 的一个超类。
        List<? super String> a = new ArrayList<>();
        List<Object> b = new ArrayList<>();
        //b可以赋值给a
        a = b;
        a.add("A");
        b.add(1);
       //想想也是正常的，反正调用a的get方法得到的是一个Object类型
   }
```

### 5.3 PECS

带有超类型限定的通配符可以向泛型对象写入，带有子类型限定的通配符可以从泛型读取对象，在EffectiveJava中描述为**PECS: producer-extends, costumer-super**。

为了获得最大限度的灵活性，要在表示 **生产者或者消费者** 的输入参数上使用通配符，使用的规则就是：生产者有上限、消费者有下限.上面Comparator则可以理解为消费者。

示例：
```java
     public static class Stack<E>{
            private E[] objects;
            private int size;
            public static final int DEFAULT_SIZE = 16;

            //作为生产者的src使用extends限定
            //它生成的肯定是E或E的子类
            public void pushAll(Iterable<? extends E> src){
                  for(E e:src){
                      push(e)
                  }
            }
            //作为消费者的det使用surper限定
            //它接收的肯定是E或E的子类
            public void popAll(Collection<？ surper E> det){
               while(!isEmpty){
                  det.add(pop());
               }
            }
    }
```

### 5.4 无限定通配符

为了表示各种泛型List的父类型，可以使用一个类型通配符**`?`**

使用`?`通配符可以引用其他各种参数化的类型，`?`通配符定义的变量主要用于引用

```java
        public static void printList(List<?> list){
            for(Object o : list){
                System.out.println(o);
            }
        }
```

现在这个方法可以接受所有的list子类，方法内部的类型永远s是Object

```java
    List<String> listOb=new ArrayList<String>();
    printList(listOb);
```

**可以调用与参数无关的方法，不可以调用与参数有关的方法**，即上面`List<String>`应用在printList中，printList中无法调用与参数String相关的方法，由于Object是所有类的超类，所以可以调用Object拥有的方法。

带有这种通配符的List仅仅用来表示各种泛型List的父类型，无法把一个对象加入到其中(null除外)，因为？表示的类型无法确定

下面编译错误

```
    List<?> genList = new ArrayList<>();
    genList.add(new Object());
```

### 5.5 泛型方法与类型通配符的区别

如果某个方法中一个形参a的类型或者返回值的类型依赖于另一个形参b的类型，则形参b的类型声明不应该使用通配符，因为形参a或者返回值依赖于形参b的类型。反之如果不依赖，则应该使用通配符

比如：

```java
        public static <T extends Number> void printList(List<T> list){
            for(Object o : list){
                System.out.println(o);
            }
        }
        public static void printList(List<? extends Number> list){
            for(Object o : list){
                System.out.println(o);
            }
        }
```
两个方法的即使作用是一样的，显然应该使用第二种通配符，定义泛型T完全是多余的

如果有需要可以同时使用通配符和泛型方法

```java
    public static <T> void copy(List<T> dest , List<? extends T> src){

    }
    public static <T , S extends T> void copy(List<T> dest , List<S> src){

    }
```
显然第第一种方式更加简洁。

---
## 6 泛型与反射

### 使用反射获取泛型信息

获取字段上的泛型信息

```java
     class Cons{

        private Map<String ,Integer> map;

        public  Cons(){

        }
    }

            Cons cons = new Cons();
            Class<? extends Cons> class1 = cons.getClass();
            try {
                Field field = class1.getDeclaredField("map");
                field.setAccessible(true);
                Type genericType = field.getGenericType();
                if(genericType instanceof ParameterizedType){
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;

                    Type rawType = parameterizedType.getRawType();
                    System.out.println(rawType);
                    System.out.println("---------");
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    for (Type type : actualTypeArguments) {
                        System.out.println(type);
                    }
                }
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }

    结果：
    interface java.util.Map
    ---------
    class java.lang.String
    class java.lang.Integer
```

### 获取方法上的泛型信息


```java
    public class GenecriDao<T> {
        // 通过方法获取泛型的原始参数类型 和 实际参数类型

        public static void main(String args[]) throws Exception {
            //
            /*
             * GenecriDao<ReflectPoint> dap=new GenecriDao<ReflectPoint>(); Method
             * method=GenecriDao.class.getMethod("findTest", ArrayList.class);
             * Type[] t=method.getGenericParameterTypes(); ParameterizedType
             * pt=(ParameterizedType)t[0];
             * System.out.println(pt.getActualTypeArguments()[0]);
             */

            Method method = GenecriDao.class.getMethod("findData", Vector.class);
            Type[] pt = method.getGenericParameterTypes();// 获取泛型参数类型

            for(Type type : pt){
                System.out.println("++++++++++++++++++++++++++");
                if(type instanceof ParameterizedType){
                    ParameterizedType p = (ParameterizedType)type;// 获取参数化类型
                    System.out.println("------------------");
                    System.out.println(p.getRawType());// 获取原始参数类型
                    System.out.println(Arrays.toString(p.getActualTypeArguments()));// 获取原始参数类型
                }else{

                    System.out.println(type);
                }
            }


        }

        public void findTest(ArrayList<T> al) {

        }

        public void findData(Vector<Date> v1) {

        }

        // 不知道具体增加的东西是什么 用泛型
        public void add(T t) {

        }

        public T findByID() {
            return null;
        }

        public void delete(T obj) {

        }

        public void upload(T obj) {

        }

        public Set<T> findByConditions(String where) {
            return null;
        }

        public T findByUserName(String name) {
            return null;
        }
    }

执行结果：
    ++++++++++++++++++++++++++
    ------------------
    class java.util.Vector
    [class java.util.Date]
```

### 获取类上的泛型信息

子类继承一个带类型参数的父类时，如果此时子类也无法确定父类上的类型参数的实际类型，可以在子类上继续声明此类型参数。

编译不通过

```java
    public class GenericT1<T> {
    }
    class GenericT_sub extends GenericT1<T>{
    }
```

编译通过

```java
    public class GenericT1<T> {
    }

    class GenericT_sub<T> extends GenericT1<T>{
    }
```

但是如果需要通过反射获取一个类上类型参数的实际类型参数，注意 `getGenericSuperclass` 方法，getGenericSuperclass 返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。不注意的话很有可能就犯错了，看下面demo。代码如下：

```java
    class Fa<T> {
    }

    class GenericT_sub<T> extends Fa<T> {
    }


    public class Main {

        public static void main(String[] args) {
            //创建一个Fa的子类，fa此时是Fa的子类，注意现在实际类型参数定义在Fa上
            Fa<String> fa = new Fa<String>(){};
            //所以通过getClass可以拿到父类的泛型类型
            getGenericType(fa.getClass());
            System.out.println("=========================");

            //创建一个Fa的子类
            //这里genericT_sub类型就是自己，它的父类没有指定泛型的实际参数类型
            //是它自己指定了，所以在这里拿不到实际参数类型String。
            GenericT_sub<String> genericT_sub = new GenericT_sub<String>();
            getGenericType(genericT_sub.getClass());
        }

        private static void getGenericType(Class clazz) {
            //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
            //Type 是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
            Type genericSuperclass = clazz.getGenericSuperclass();
            System.out.println(genericSuperclass);
            //如果指向参数化类型
            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
                //返回 Type 对象，表示声明此类型的类或接口。
                Type rawType = parameterizedType.getRawType();
                System.out.println(rawType);
                //返回表示此类型实际类型参数的 Type 对象的数组。
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                for(Type type : actualTypeArguments){
                    System.out.println(type);
                }
            }else{
                System.out.println("获取泛型失败");
            }
        }
    }

打印结果为：
    com.zty.genericdemo.Fa<java.lang.String>
    class com.zty.genericdemo.Fa
    class java.lang.String
    =========================
    com.zty.genericdemo.Fa<T>
    class com.zty.genericdemo.Fa
    T
```

由于 GenericT_sub 也无法确定 Fa 的实际类型参数，只能继续用T表示，那么在创建 GenericT_sub 的实例对象时，虽然确定了实际类型参数，但是无法通过反射其 Class 获取执行的实际类型参数，Java 泛型有这么一种规律：**位于声明一侧的，源码里写了什么到运行时就能看到什么；位于使用一侧的，源码里写什么到运行时都没了**。

---
## 7 引用

- [Java中的逆变与协变](http://www.cnblogs.com/en-heng/p/5041124.html)
- [Effective Java——泛型](http://72df4c66.fromwiz.com/share/s/1OTQNC2rEQoF2SSa6c0VIC9-2S4Ysi1Uak8T2eXrEU3YKL4C)


