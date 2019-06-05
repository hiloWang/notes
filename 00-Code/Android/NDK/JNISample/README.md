# JNI实例

- JNI回调方法
- 动态注册方法
- Java调用C
- C调用Java
- 错误处理

```java
public class JniBridge{
    //返回字符串
    public static native String stringFromC();
    //模拟登录
    public native int intFromC(int a, int b);
    //修改每个元素后返回
    public native int[] addArray(int origin[], int add);
    //c语言的冒泡排序
    public native void bubbleSort(int[] arr);
    //加密
    public native String encryption(String password);
    //让C调用Java
    public native void callJava(String message);
    //让C抛出异常
    public native void throwError(String message);
    //调用C动态方法注册
    public native String dynamicRegisterFromJni();
}    
```