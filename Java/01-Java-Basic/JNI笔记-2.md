# JNI 笔记 2

---
## 5 引用类型

JNI 把 instance 和 array 类型的指针对外公布为 opaque reference，这种引用对于 JNI 来说是透明的，本地代码不需要通过指针操作 reference，不需要关心对象的内存布局，而是通过 JNI API。

关于 reference，有以下内容需要掌握：

- JNI支持三种类型的opaque reference：`local references`，`global references`，和`weak global references`。
- Local 和 Global 引用有不同的生命周期，`local references` 在 native method 执行完毕后被 JavaVM 自动释放，而 `global references`，和`weak global references` 在程序员主动释放前一直有效
- 各种引用都有使用范围：如 `local references` 只能在当前线程的 native method 中使用。
- 防止内存泄漏：在JNI中，不管是局部还是全局引用，在用完之后就可以进行手动的释放，以防止持续的内存占用，通过`DeleteXXXRef`类方法来告知虚拟机何时回收一个JNI变量


### 5.1 局部引用

大部分 JNI 函数都会创建局部引用，如 NewObject 创建一个实例，并返回一个指向该实例的 LocalRef。LocalRef 只在创建该对象的线程中有效，不要把 LocalRef 存到全局变量中供其他线程使用。

#### 不要缓存 LocalRef

LocalRef 只在本线程的 native method 中有效. 一但 native method 返回，LocalRef 就会被释放。不要缓存一个 LocalRef，并在下次进入该 JNI 方法时使用它，参考下面错误代码：

```c
jstring MyNewString(JNIEnv*env,jchar* chars,jint len){
    static jclass stringClass = NULL;
    ...
    if(stringClass == NULL){
        stringClass = (*env)->FindClass(env,"java/lang/String");
        if(stringClass == NULL){
            returnNULL;/*exception thrown */
        }
    }
}
```

上面代码中重复使用 `FindClass(env, "java/lang/String")` 的返回值，这种方式是错误的，因为 FindClass 返回的是一个 LocalRef，第一次调用 MyNewString 可以正常运行， 当第二次调用时将会产生错误，因为此时访问的 stringClass 是一个无效的位置。

>这里不要和第四节讨论的 `缓存 filedId 和 methodId` 搞混淆了，filedId 和 methodId 的实际类型是基本类型，所以不存在引用释放与否。


#### 主动释放 LocalRef

有两种方式让 LocalRef 无效：

- native method 返回（返回到Java层），JavaVM 会自动释放局部引用。
- 用 DeleteLocalRef 主动释放。

为什么要主动释放 LocalRef：LocalRef 的作用是阻止引用被 GC，但当你在本地代码中操作大量对象时，而 LocalRefTable 又是有限的，及时调用 DeleteLocalRef 可以释放 LocalRef 在 LocalRefTable 中所占位置并使对象及时得到回收。

下面情况下应该主动的释放 LocalRef：

1. 访问一个很大的 java 对象，使用完之后，还要进行复杂的耗时操作
2. 创建了大量的 LocalRef，占用了太多的内存，而且这些局部引用跟后面的操作没有关联性

参考下面代码：

```c
JNIEXPORT void JNICALL testLocalRef(JNIEnv *env, jobject jobj){
    int i = 0;
    for (; i < 5; i++){
        //创建Date对象
        jclass cls = (*env)->FindClass(env, "java/util/Date");
        jmethodID constructor_mid = (*env)->GetMethodID(env, cls, "<init>", "()V");
        jobject obj = (*env)->NewObject(env, cls, constructor_mid);
        //此处省略使用局部引用的代码...

        //不在使用就删除
        (*env)->DeleteLocalRef(env, obj);
        //...
    }
}
```

### 5.2 全局引用

释放 GlobalRef 前，可以在多个本地方法调用过程和多线程中使用 GlobalRef 所指向的对象。与 LocalRef 类似，GlobalRef 的作用是防止对象被 GC。GlobalRef 与 LocalRef 不同的是，LocalRef 一般自动创建(返回值为 jobject/jclass 等JNI函数)，而 GlobalRef 必须由程序员主动创建。创建方法为`NewGlobalRef`。

```c
//全局引用
jstring global_str;

//创建
JNIEXPORT void JNICALL createGlobalRef(JNIEnv *env, jobject jobj){
    jstring obj = (*env)->NewStringUTF(env, "jni development is powerful!");
    global_str = (*env)->NewGlobalRef(env, obj);
}

//获得
JNIEXPORT jstring JNICALL getGlobalRef(JNIEnv *env, jobject jobj){
    return global_str;
}

//释放
JNIEXPORT void JNICALL deleteGlobalRef(JNIEnv *env, jobject jobj){
    (*env)->DeleteGlobalRef(env, global_str);
}
```

GlobalRef 是可共享(可以跨多个线程访问，线程不安全)的，需要手动控制内存使用。参考下面 GlobalRef 示例:

```c
    jstring MyNewString(JNIEnv*env,jchar*chars,jint len){
        static jclass stringClass = NULL;
        ...
        if(stringClass==NULL){
            jclasslocalRefCls=(*env)->FindClass(env,"java/lang/String");
            if(localRefCls==NULL){
                returnNULL;/*exception thrown */
            }
            /*Create a global reference */
            stringClass=(*env)->NewGlobalRef(env,localRefCls);
            /*The local reference is no longer useful */
            (*env)->DeleteLocalRef(env,localRefCls);
            /*Is the global reference created successfully? */
            if(stringClass==NULL){
                return NULL;
            /*out of memory exception thrown */
            }
        }...
    }
```

### 5.3 弱全局引用

WeakGlobalRef 用 NewGlobalWeakRef 于 DeleteGlobalWeakRef 进行创建和删除，多个本地方法调用过程中和多线程上下文中使用的特性与 GlobalRef 相同，但该类型的引用不保证不被GC。弱全局引用在内存不足时会被释放。

### 5.4 引用比较

IsSameObject 用于测试两个引用是否引用相同的 Java 对象。

- 用`(*env)->IsSameObject(env, obj1, obj2)`方法比较相等性，如果相等，返回 JNI_TRUE, 否则返回 JNI_FALSE。
- 对于 WeakGlobalRef 来说，可以使用 `(*env)->IsSameObject(env, wobj, NULL)` 方式判断，因为对于一个 WeakGlobalRef 来说可能指向已经被 GC 的无效对象。如果返回 JNI_TRUE 表示 WeakGlobalRef 已经被回收, 否则返回 JNI_FALSE 表示 WeakGlobalRef 还活着。

### 5.5 管理引用

- 每个 JNI 引用都会引用表中的一个位置，JNI 程序员应该清楚程序某阶段中使用的引用数量。 对于 LocalRef，如果你没有使用 DeleteLocalRef 及时的清理不需要的引用时，在极端情况程序会崩溃（由于 LocalRefTable大小是固定的，所以每个方法所能申请的 LocalRef 是有限的）。
- 当不再使用 GlobalRef 所指对象时，应该及时调用 DeleteGlobalRef释放对象，否则，GC将不回收该对象。
- 对于 DeleteWeakGlobalRef 来说，不使用 WeakGlobalRef 时，也应该及时释放，即使 GC 会回收该对象内容。
- 本地方法绝对不能滥用 GlobalRef 和 WeakGlobalRef，因为此类型引用不会被自动回收。

---
## 6 错误处理

### 6.1 在本地代码中捕获和处理异常

异常是难以避免的，当 java 调用本地代码或从本地代码调用 java 时都可可能发生异常，JNI API 提供了一些方法用于处理本地代码发生的异常：

- Java 调用本地代码时可能发生异常，在 Java 层可以使用 `try catch`来捕获异常。
- 本地代码没有 Java 的异常机制，必须调用 Throw 或者 ThrowNew 函数发布一个异常，当本地方法退出时，Java 虚拟机就会收到本地代码抛出的异常。
- 本地代码仅仅能发布异常，异常不会中断本能地方法的控制流，只有当方法返回时，Java 虚拟机才会抛出异常，所以一般应该在其后面加上 return 让方法立即返回。

```c
//1：使用 Throw 发布异常
jclass class_EOF = (*env)->FindClass(env,"java/io/EOFException");
jmethodID id = (*env)->GetMethodID(env,class_EOF,"<init>", "()V");
jthrowable obj_exc = (*env)->NewObject(env,class_EOF,id);
if(JNI_TRUE){
    (*env)->Throw(env,obj_exc);
    return;
}

//2：使用 ThrowNew 发布异常
(*env)->ThrowNew(env,(*env)->FindClass(env,"java/io/EOFException"), "Unexpected end of file");

//3：本地代码可以检测异常释放发生，并且可以考虑清除异常
jthrowable jthrow =  (*env)->ExceptionOccurred(env);  当java层抛出异常时，则此方法返回抛出的异常，否则返回NULL，注意结束处理时调用 DeleteLocalRef 清理该引用。
jboolean jbool = (*env)->ExceptionCheck(env); 当java层抛出异常时，此方法返回TRUE
(*env)->ExceptionClear(env); 清除异常
```

示例：

```java
class CatchThrow {    
    private native void doit()throws IllegalArgumentException;  

    private void callback() throws NullPointerException {        
        throw new NullPointerException("CatchThrow.callback");    
    }

    public static void main(String args[]) {        
        CatchThrow c = new CatchThrow();        
        try {            
            c.doit();        
        } catch (Exception e) {            
            System.out.println("In Java:\n\t" + e);        
        }    
    }    
    
    static {        
        System.loadLibrary("CatchThrow");    
        }
}
```

本地代码如何处理异常：

```c
JNIEXPORT void JNICALLJava_CatchThrow_doit(JNIEnv *env, jobject obj){    
    jthrowable exc;    
    jclass cls = (*env)->GetObjectClass(env, obj);    
    jmethodID mid = (*env)->GetMethodID(env, cls, "callback", "()V");    
    if (mid == NULL) {        
        return;    
    }    
    (*env)->CallVoidMethod(env, obj, mid);    
    //ExceptionOccurred 检测试错参数了异常
    exc = (*env)->ExceptionOccurred(env);    
    if (exc) {        
        /* 我们不对这个异常做过多处理，而为其打印一个调试信息，然后重新抛出一个新的异常 */        
        jclass newExcCls;        
        (*env)->ExceptionDescribe(env);//调用ExceptionDescribe打印调用堆栈
        (*env)->ExceptionClear(env);//用ExceptionClear清空异常
        newExcCls = (*env)->FindClass(env, "java/lang/IllegalArgumentException");        
        if (newExcCls == NULL) {            
            /* Unable to find the exception class, give up. */            
            return;        
        }
        (*env)->ThrowNew(env, newExcCls, "thrown from C code");    
    }
}
```

最终的输出结果为：

```
java.lang.NullPointerException:
    at CatchThrow.callback(CatchThrow.java)
    at CatchThrow.doit(Native Method)
    at CatchThrow.main(CatchThrow.java)
In Java:
java.lang.IllegalArgumentException: thrown from C code
```

本地代码中的异常不会中断本能地方法的控制流，在JNI中产生的异常(通过调用ThrowNew)，这与 Java 语言中异常发生的行为不同，在Java中发生异常，VM 自动把控制权转向 `try/catch` 中匹配的异常类型处理块，VM 首先清空异常队列，然后执行异常处理块。相反，JNI 中必须显式地执行 VM 的处理方式。


### 6.2 检测异常是否发生

有两种方式检查是否有异常发生：

- 大多数 JNI 函数用不同的返回值（比如NULL）来标识表明当前线程是否有异常发生：
  
下面是一个典型的示例，用 NULL 返回值检测 GetFieldID 方法是否参数异常

```java
/* a class in the Java programming language */
public class Window {    
    long handle;    
    int length;    
    int width;    
    static native void initIDs();    
    static {        
        initIDs();    
        }
    }

/* C code that implements Window.initIDs */
jfieldID FID_Window_handle;
jfieldID FID_Window_length;
jfieldID FID_Window_width;

JNIEXPORT void JNICALLJava_Window_initIDs(JNIEnv *env, jclass classWindow){   

    FID_Window_handle = (*env)->GetFieldID(env, classWindow, "handle", "J");    
    if (FID_Window_handle == NULL) {  
         /* 重要的检测 ，虽然这些字段已经再 Windows.class 中存在，我们依然要检测，因为 VM 可能无法再申请足够的内存来存储一个 jfieldID*/        
         return; /* 异常已经发生了*/    
    }    
    FID_Window_length = (*env)->GetFieldID(env, classWindow, "length", "I");    
    if (FID_Window_length == NULL) {  
        /* 重要的检测 */       
        return; /* 异常已经发生了 */  
    }    
    FID_Window_width = (*env)->GetFieldID(env, classWindow, "width", "I");    
    /* no checks necessary; we are about to return anyway */
}
```

- 如果返回值不能表明是否有异常发生，需要用 JNI 提供的 ExceptionOccurred 检查但前线程是否有未处理异常（ExceptionCheck 也可以用来检测异常）

比如 CallIntMethod 返回 int 类型，这个返回值不能够表示异常是否已经产生（不管用什么值，它都可能是一个合法的返回值），下面是一个示例：

```java
public class Fraction {    
    // details such as constructors omitted    
    int over, under;    
    public int floor() {        
        return Math.floor((double)over/under);    
    }
}

/* Native code that calls Fraction.floor. Assume method ID   MID_Fraction_floor has been initialized elsewhere. */
void f(JNIEnv *env, jobject fraction){    
    jint floor = (*env)->CallIntMethod(env, fraction, MID_Fraction_floor);    
    /* important: check if an exception was raised */    
    if ((*env)->ExceptionCheck(env)) {        
        return;    
    }   
     ... 
    /* use floor */
}
```

### 6.3 处理异常

本地代码以两种方式处理一个预期的异常：

- 本地代码检测到异常后立即返回，然后在调用处（java层）处理该异常
- 本地代码可以用 ExceptionClear 清空异常，然后执行自己的异常处理逻辑

在 JNI 中调用任何子流程前，必须严格按着 `检查->处理->清除` 的逻辑处理异常。如果没有预先清空异常就调用一个 JNI 方法，行为不可预料。有一些函数可以在未清空异常前调用，但只局限于很少的几个，而且多是异常处理JNI函数。在异常发生后，及时释放资源很重要，比如 `ReleaseStringChars`。


### 6.4 工具方法与异常处理

可以将抛出异常的逻辑封装为一个工具方法：

```c
void JNU_ThrowByName(JNIEnv* env,const char* name,const char* msg){
    jclass cls=(*env)->FindClass(env,name);
    /*if cls is NULL, an exception has already been thrown */
    if(cls!=NULL){
        (*env)->ThrowNew(env,cls,msg);
    }
    /*free the local ref */
    (*env)->DeleteLocalRef(env,cls);
}
```

另外工具函数对异常的处理要很注意，一定是针对调用者的，即无累积异常行为，一般原则如下：

- 工具函数应该提供返回值告诉调用者释放发生异常，以简化调用者的处理
- 工具函数要注意处理LocalRef


---
## 7 JNI 与线程

本地代码可以被任意 Java 线程调用，所以，本地代码中同样存在着并发问题，JNI 规范规定本地代码有如下约束，我们必须遵守这些规范：

- JNIEnv 结构与线程绑定的，绝对不能在多线程中共享 JNIEnv 结构
- LocalRef 与本线程绑定的，绝对不能在多线程中共享 LocalRef

### 7.1 Monitor 锁

JNI 中没有 synchronized 这样的关键字来保证线程安全，但是提供了同等级别的 API 来获取和释放 Monitor 锁：

```
if((*env)->MonitorEnter(env,obj)!=JNI_OK){//进入监视器
    .../*error handling */
}

.../*synchronized block */

if ((*env)->ExceptionOccurred(env)) {    
    ... /* exception handling */    
    /* remember to call MonitorExit here */    
    if ((*env)->MonitorExit(env, obj) != JNI_OK) 
    ...;
}

if((*env)->MonitorExit(env,obj)!=JNI_OK){//退出监视器
    ...
    /*error handling */
};
```

注意：

- `MonitorEnter/MonitorExit` 一定要配对调用。不然将导致死锁。
- 当前线程不拥有监视器时调用 MonitorExit 会导致错误并导致引发 IllegalMonitorStateException。
- 上面的代码包含一对匹配的 MonitorEnter 和 MonitorExit 调用，但我们仍然需要检查可能的错误。例如，如果底层线程实现无法分配执行监视器操作所需的资源，则监视操作可能会失败。
- 未能调用 MonitorExit 很可能会导致死锁。通过将上面的C代码段与 Java 代码段进行比较，可以看到使用 Java 编程比使用 JNI 更容易。因此，最好用 Java 表达同步结构。例如，如果静态本地方法需要进入与其定义类关联的监视器，则应该定义一个 `static synchronized native` 方法，而不是在本地代码中处理同步逻辑。


### 7.2 JNI 中实现 Wait 和 Notify

为了同步需要，Java 提供了 `Object.wait、Object.notify、Object.notifyAll` 方法。JNI 不提供这些对应的 JNI 实现，因为它们不像 Entry and Exit 操作那样具有性能关键性，但可以通过回调这些 Java 方法实现 Wait 和 Notify。

```c
/* 预先获取的 method IDs */
static jmethodID MID_Object_wait;
static jmethodID MID_Object_notify;
static jmethodID MID_Object_notifyAll;

void JNU_MonitorWait(JNIEnv *env, jobject object, jlong timeout){    
    (*env)->CallVoidMethod(env, object, MID_Object_wait, timeout);
}

void JNU_MonitorNotify(JNIEnv *env, jobject object){    
    (*env)->CallVoidMethod(env, object, MID_Object_notify);
}

void JNU_MonitorNotifyAll(JNIEnv *env, jobject object){    
    (*env)->CallVoidMethod(env, object, MID_Object_notifyAll);
}
```

### 7.3 在任意上下文获取 JNIEnv 指针

JNIEnv 指针仅在其关联的线程中有效。这对于本地代码通常不是问题，因为它们从虚拟机接收 JNIEnv 指针作为第一个参数。然而，偶尔可能需要一个未直接从虚拟机调用的本地代码来获取对当前线程的 JNI Envinterface 指针。例如，本地代码可能属于操作系统调用的“callback”函数，在这种情况下，JNIEnv 指针可能不会作为参数。您可以通过调用 AttachCurrentThread 获取当前线程的 JNIEnv 指针调用接口的功能。另外一个本地创建的线程（比如 thread_create 创建的线程）如果需要获取 JNIEnv 也需要通过 AttachCurrentThread 。

```c
JavaVM *jvm; /* already set */
f(){    
    JNIEnv *env;    
    (*jvm)->AttachCurrentThread(jvm, (void **)&env, NULL);    .
    .. /* use env */
}
```

当前线程已经连接到虚拟机时，AttachCurrentThread 返回属于当前线程的 JNIEnvinterface 指针。有许多方法可以获取 JavaVM 指针：

- 通过在创建虚拟机时记录它
- 通过查询创建的虚拟机使用 JNI_GetCreatedJavaVMs 的机器
- 通过在常规本地代码中调用 JNI 函数 GetJavaVM
- 通过定义 JNI_OnLoad 处理程序。与JNIEnv指针不同，JavaVM 指针在多个线程中保持有效，因此可以将其缓存在全局变量中。

**AttachCurrentThread 方法一定要 DetachCurrentThread 配合调用，否则线程无法正常退出**。DetachCurrentThread 的作用是：从 VM 分离当前线程。该线程持有的所有 Java 监视器都将被释放。等待此线程死亡的所有 Java 线程都会收到通知。主线程（即创建Java VM的线程）无法与 VM 分离。相反，主线程必须调用 `JNI_DestroyJavaVM()`来卸载整个 VM。

### 7.4 匹配线程模型

假设要在多个线程中运行的本地代码访问全局资源。本地代码应该使用 JNI 函数 MonitorEnter 和 MonitorExit，还是在主机环境中使用本地线程同步原语（例如mutex_lockon Solaris）？类似地，如果本地代码需要创建一个新线程，它应该创建一个`java.lang.Thread` 对象并通过 JNI 执行 `Thread.start` 的回调，还是应该在宿主环境中使用本地线程创建原语（如作为Solaris上的thr_create）？答案是，如果 Java 虚拟机实现支持与本地代码所使用的线程模型匹配的线程模型，则这些方法都有效。线程模型规定了系统如何实现基本线程操作，例如调度，上下文系统调用中的切换，同步和阻塞。在主动线程模型中，操作系统管理所有必要的线程操作。另一方面，在用户线程模型中，应用程序代码实现了线程操作。例如，Solaris 上附带 JDK 和 Java 2 SDK 版本的“绿色线程”模型使用 ANSI C 函数 setjmp 和 longjmp 来实现上下文切换。

---
## 8 其他 JNI 功能

### 8.1 动态注册方法

在应用程序执行本地代码之前，它会经历两个步骤来加载包含本地代码实现的本地库，然后链接到本地代码实现：

1. `System.loadLibrarylocates` 定位并并加载指定的本地库。例如，System.loadLibrary（“foo”）可能会导致在Win32上加载`foo.dll`。
2. 虚拟机在其中一个加载的本地库中查找本地代码实现。例如，`Foo.g` 本地代码调用需要定位和链接本地函数 `Java_Foo_g`，它可能驻留在`infoo.dll`中。

不依靠虚拟机在已加载的本地库中搜索本地代码，JNI 程序员可以通过向类引用注册函数指针来手动链接本地方法，方法名称和方法描述符：

```c
/*
 * 动态注册的方法，Java可直接调用
 */
static jstring dynamicRegisterFromJni(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "动态注册调用成功");
}

//JNINativeMethod是一个结构体，这里初始化了一个JNINativeMethod数组，正是这个，可以动态调用任意 native 方法
JNINativeMethod nativeMethod[] = {{"dynamicRegisterFromJni", "()Ljava/lang/String;", (void *) dynamicRegisterFromJni}};

//此方法在jni库被加载时有JVM调用
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
    JNIEnv *env;
    if ((*jvm)->GetEnv(jvm, (void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }

    jclass clz = (*env)->FindClass(env, "com/ztiany/jni/sample/JniBridge");

    (*env)->RegisterNatives(env, clz, nativeMethod, sizeof(nativeMethod) / sizeof(nativeMethod[0]));

    return JNI_VERSION_1_4;
}
```

RegisterNatives函数可用于多种用途：

- 注册大量本地方法实现有时更方便，更有效，而不是让虚拟机懒惰地链接这些条目。
- 可以在方法上多次调用 RegisterNatives，从而允许在运行时更新本地方法实现。
- 当本地应用程序嵌入虚拟机实现并需要与本地应用程序中定义的本地方法实现链接时，RegisterNatives 特别有用。虚拟机无法自动找到此本地方法实现，因为它仅在本地库中搜索，而不是在应用程序本身中搜索。

---
### 8.2 编写国际化的 JNI 代码

必须特别注意编写适用于多种语言环境的代码。 JNI 使程序员可以完全访问 Java 平台的国际化功能。以字符串转换作为示例，因为文件名和消息可能在许多语言环境中包含非ASCII字符。

Java 虚拟机使用 Unicode格式的字符串。虽然某些 native 平台（如Windows NT）也提供 Unicode 支持，但大多数用特定于语言环境的编码表示字符串。除非 UTF-8 碰巧是平台上的本地编码，否则不要使用 GetStringUTFChars 和 GetStringUTFRegion 函数来转换 jstrings 和特定于语言环境的字符串。在表示名称和描述符（例如参数要传递给JNI函数的GetMethodID）时，UTF-8 字符串很有用，但不适合表示特定于语言环境的字符串，例如文件名。

#### 根据 Native Strings 创建 jstring

使用 `String(byte [] bytes)` 构造函数将本地字符串转换为 jstring。以下实用程序函数从特定于语言环境的本地 C 字符串创建 jstring：

```c
jstring JNU_NewStringNative(JNIEnv *env, const char *str) {
    jstring result;
    jbyteArray bytes = 0;
    jclass Class_java_lang_String //(*env)->FindClass(env, "java/lang/String");
    jmethodID MID_String_init //(*env)->GetMethodID(env, Class_java_lang_String, "<init>", "([B)V");
    int len;
    //EnsureLocalCapacity 确保在当前线程中至少可以创建给定数量的本地引用。成功时返回0;否则返回一个负数并抛出一个OutOfMemoryError。
    if ((*env)->EnsureLocalCapacity(env, 2) < 0) {
        return NULL; /* out of memory error */
    }
    len = strlen(str);
    bytes = (*env)->NewByteArray(env, len);
    if (bytes != NULL) {
        (*env)->SetByteArrayRegion(env, bytes, 0, len, (jbyte *) str);
        result = (*env)->NewObject(env, Class_java_lang_String, MID_String_init, bytes);
        (*env)->DeleteLocalRef(env, bytes);
        return result;
    }
    /* else fall through */
    return NULL;
}
```

该函数创建一个字节数组，将本地 C 字符串复制到字节数组中，最后调用 `String(byte [] bytes)`构造函数来创建生成的 jstring 对象，Class_java_lang_String 是对 `java.lang.String` 类的全局引用，和 MID_String_init 是字符串构造函数的方法ID。因为这是一个实用程序函数，所以需要确保删除对临时创建的字节数组的本地引用来存储字符。

#### jstrings 转换为 Native Strings

使用 `String.getBytes` 方法将 jstring 转换为适当的本地编码。以下实用程序函数将 jstring 转换为特定于语言环境的本地 C 字符串：

```c
char *JNU_GetStringNativeChars(JNIEnv *env, jstring jstr) {
    jbyteArray bytes = 0;
    jthrowable exc;
    char *result = 0;

    jclass Class_java_lang_String = (*env)->FindClass(env, "java/lang/String");
    jmethodID MID_String_getBytes = (*env)->GetMethodID(env, Class_java_lang_String, "getBytes", "()[B");

    if ((*env)->EnsureLocalCapacity(env, 2) < 0) {
        return 0; /* out of memory error */
    }
    bytes = (*env)->CallObjectMethod(env, jstr, MID_String_getBytes);
    exc = (*env)->ExceptionOccurred(env);
    if (!exc) {
        jint len = (*env)->GetArrayLength(env, bytes);
        result = (char *) malloc(len + 1);
        if (result == 0) {
            JNU_ThrowByName(env, "java/lang/OutOfMemoryError", 0);
            (*env)->DeleteLocalRef(env, bytes);
            return 0;
        }
        (*env)->GetByteArrayRegion(env, bytes, 0, len, (jbyte *) result);
        result[len] = 0; /* NULL-terminate */
    } else {
        (*env)->DeleteLocalRef(env, exc);
    }
    (*env)->DeleteLocalRef(env, bytes);
    return result;
}
```

---
### 8.3 C++ 与 JNI

1. 1. 为了兼容 C 与 C++ 的混编，头文件中的函数声明要使用 `extern "C" {}` 括起来，因为 C++ 编译器会混编方法名。具体参考 [extern C](https://github.com/hokein/Wiki/wiki/extern-%22C%22)
2. c++ 对 JNI 函数的方法比 c 要简单，如`env->NewStringUTF(str)`即可实现 NewStringUTF 函数的调用。

---
### 8.4 本地库的加载和卸载处理

加载和卸载处理程序允许本地库导出两个函数：一个在 `System.loadLibrary` 加载本地库时调用，另一个在虚拟机卸载本地库时调用。此功能已添加到Java 2 SDK 1.2版中

#### JNI_OnLoad

当System.loadLibrary加载本地库时，虚拟机将在本地库中搜索以下导出条目：

```
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved);
```

可以在 JNI_Onload 的实现中调用任何 JNI 函数。 JNI_OnLoad 处理程序的典型用法是缓存 JavaVM 指针，类引用或方法和字段ID，如以下示例所示：

```c
JavaVM *cached_jvm;
jclass Class_C;
jmethodID MID_C_g;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
    JNIEnv *env;
    jclass cls;
    cached_jvm = jvm;  /* cache the JavaVM pointer */    
    if ((*jvm)->GetEnv(jvm, (void **) &env, JNI_VERSION_1_2)) { 
        return JNI_ERR; /* JNI version not supported */   
    }
    cls = (*env)->FindClass(env, "C");
    if (cls == NULL) { 
        return JNI_ERR;
    }    
    /* Use weak global ref to allow C class to be unloaded */    
    Class_C = (*env)->NewWeakGlobalRef(env, cls);
    if (Class_C == NULL) { 
        return JNI_ERR;
    }    /* Compute and cache the method ID */    
    MID_C_g = (*env)->GetMethodID(env, cls, "g", "()V");
    if (MID_C_g == NULL) { 
        return JNI_ERR;
    }
    //返回支持的JVM的最低版本
    return JNI_VERSION_1_2;
}
```

JNI_OnLoad 函数首先将 JavaVM 指针缓存在全局变量 cached_jvm 中。然后通过调用 GetEnv 获取 JNIEnv 指针。它最终加载 C 类，缓存类引用，并计算 `C.g` 的方法ID。当出错时，JNI_OnLoad 函数返回 JNI_ERR，否则返回本地库所需的 JNIEnv 版本JNI_VERSION_1_2。我们将 C 类缓存在弱全局引用而不是全局引用中。

给定一个缓存的 JavaVM 接口指针，实现一个允许本地代码获取当前线程的 JNIEnvinterface 指针的实用工具函数是简单的。

```c
JNIEnv *JNU_GetEnv() {
    JNIEnv *env;
    (*cached_jvm)->GetEnv(cached_jvm, (void **) &env, JNI_VERSION_1_2);
    return env;
}
```

#### JNI_OnUnload

虚拟机在卸载 JNI 本地库时调用 JNI_OnUnload 处理程序。然而这不够精确。虚拟机何时确定可以卸载本地库？哪个线程运行 JNI_OnUnload 处理程序？卸载本地库的规则如下：


- 虚拟机将每个本地库与发出 `System.loadLibrary` 调用的类C的类加载器L 相关联。
- 虚拟机调用 JNI_OnUnload 处理程序并在确定类加载器 L 不再是活动对象后卸载本地库。因为类加载器引用它定义的所有类，这意味着C也可以被卸载。
- JNI_OnUnload 处理程序在终结器中运行，并由 `java.lang.System.runFinalization` 同步调用或者由虚拟机异步调用。


以下是 JNI_OnUnload 处理程序的定义，该处理程序清除上面 JNI_OnLoad 方法分配的资源：

```c
JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *jvm, void *reserved) {
    JNIEnv *env;
    if ((*jvm)->GetEnv(jvm, (void **) &env, JNI_VERSION_1_2)) { 
        return; 
    }
    (*env)->DeleteWeakGlobalRef(env, Class_C);
    return;
}
```

JNI_OnUnload 函数删除对 JNI_OnLoad 处理程序中创建的 C 类的弱全局引用。我们不需要删除方法 ID MID_C_g，因为虚拟机在卸载其定义的 class C时自动回收表示 C 的方法 ID 所需的资源。为什么我们将 C 类缓存在弱全局引用而不是普通的全局引用中呢？因为全局引用会使 C 保持活跃状态，这反过来会使 C 的类加载器保持活动状态。鉴于本地库与 C 的类加载器 L 相关联，将不会卸载本地库并且不会调用 JNI_OnUnload，JNI_OnUnload 处理程序在终结器中运行。而 JNI_OnLoad 处理程序在启动 `System.loadLibrary` 调用的线程中运行。因为 JNI_OnUnload 在未知的线程上下文中运行，为了避免可能的死锁，应该避免在 JNI_OnUnload 中进行复杂的同步和锁定操作。 JNI_OnUnload 处理程序通常执行简单的任务，例如释放本地库分配的资源。当加载库的类加载器和该类加载器定义的所有类不再存在时，JNI_OnUnload 处理程序将被运行。 JNI_OnUnload 处理程序不得以任何方式使用这些类。在上面的 JNI_OnUnload 定义中，不能执行任何假定 Class_C 仍引用有效类的操作。示例中的 DeleteWeakGlobalRef 调用为其自身的弱全局引用释放内存，但不以任何方式操纵引用的类C，总之，在编写 JNI_OnUnload 处理时应该小心。避免可能引入死锁的复杂锁定操作。记住，在调用 JNI_OnUnload 处理程序时已卸载类。

---
### 8.5 反射支持

反射通常是指在运行时操作语言级构造。例如，反射允许你在运行时发现任意类对象的名称以及类中定义的字段和方法的集合。 Java 编程语言级别通过`java.lang.reflect`包以及`java.lang.Object`和`java.lang.Class`类中的一些方法提供了反射支持。虽然你总是可以调用相应的 Java API 来执行反射操作，但 JNI 提供了以下功能，使本机代码的频繁反射操作更加高效和方便：

- GetSuperclass 返回给定类引用的超类。
- IsAssignableFrom 检查当期望另一个类是否为这个类的子类
- GetObjectClass 返回给定 jobject 引用的类。
- IsInstanceOf 检查jobject引用是否是给定类的实例。
- FromReflectedField 和 ToReflectedField 允许本机代码在字段ID 和 `java.lang.reflect.Field` 对象之间进行转换。
- FromReflectedMethod 和 ToReflectedMethod 允许本机代码在方法ID，`java.lang.reflect.Method`对象和`java.lang.reflect.Constructor`对象之间进行转换。

---
## 9 TODO

- 利用现有的 Native Libraries
- JNI 中的陷阱
- JNI 设计概述