# Framework层概概述

任何控制类函数都有一个入口，c 程序的入口是一个 main 函数，java 程序的入口必须是某个类的静态 main 函数，这在安卓中也是一样的，一般一个 app 就是一个java程序，所以它也需要一个入口，显然不是Activity，Activity对象的实例化不由我们控制，我们只是在其生命周期的各回调函数中做一些对应的操作，而函数的真正入口在 ActivityThread 中。

Framework包括 **客户端**，**服务端**，**Linux驱动**三个部分

---
## 服务端

服务端包装最重要的两个服务AMS和WMS，AMS管理所有应用的Activity，WMS管理所有的窗口的叠放次序，隐藏或者显示窗口。

还有两个消息处理类：

**KeyQ类**，Wms内部类，继承与KeyInputQueue，KeyQ一旦创建，就立即开启一个线程，改线程会不断的读取用户的UI操作，比如按键，触摸屏，trackball，鼠标等，并把这些消息添加到一个消息队列QueueEvent中去

**InputDispatcherThread类**：该类的对象一旦创建，也会理启动一个线程，不断的从QueueEvent中取出事件，并进行一定的过滤，然后再将这些消息发送到当前活动的客户端程序中

---
## 客户端

客户端主要包括以下重要的类：

- ActivityThread，对应一个应用程序的主线程，一个单进程apk程序对应一个ActivityThread，程序入口时main函数，ActivityThread所在线程即UI线程

- Activity，可以认为Activity是应用层程序的最新族称单位，Activity对应一个窗口，但它本身不是View，而是视图层在客户端的管理者

- PhoneWindow，继承于Window，PhoneWindow内部包含一个DecorView类，

- Window类，该类提供了一组通用的窗口操作，窗口是一个抽象的概念，而wms真正管理的不是窗口，而是View或者ViewGroup，对PhoneWindow而言，就是内部的DecorView
- DecorView 继承与FragmeLayout，decoration的意思，即对Framelayout的修饰，如添加title之类的修饰，作为Activity所对应View树的根View

- ViewRoot，字面意思是View的根，然而它并不是View,可以理解为View的管理者，ViewRoot是一个Handler，其内部的W类用于接收WMS的调用，ViewRoot主要把WMS成的操作切换至UI线程，从而完成对View树的各种操作，同时ViewRoot也是View树遍历的起点

- W，W是ViewRoot的内部类，是一个Binder，用于和WMS通信，接受WMS的调用

- WindowManager 客户端申请创建一个窗口，而具体的窗口任务有WMS完成，WindowManager相当于一个部门经理，谁有什么需求通过他来告诉WMS，而不是直接和WMS交互


---
## Linux驱动

Linux和framework相关的主要包括两个部分，分别是**surfaceFlingger(SF)**,和Binder，每一个窗口对应一个Surface，SF驱动的主要作用是吧各个Surface显示在同一个屏幕上



---
## Android系统运行的大概介绍

### APK大概的运行过程

首先是从ActivityThread的main函数开始，创建H和Application，然后和AMS通信，构建Activity，构建View树，完成PhoneWindow的创建，接着是View树的创建，然后是ViewRoot的创建，接着是跟WMS的交互，把DecorView添加到WMS进行管理，从而让View显示出来

接下来是KeyQ和InputDispatcherThread类的工作，不断获取用户在屏幕上的操作，不断发送给当前活跃的Actvity进行处理，当然事件的传递是通过ViewRoot->DecorView-Activity->DecorView-ConentView.....


### 客户端的线程

- UI线程 即主线程ActivityThread所在线程，UI线程被宰main方法中就会创建自己的消息循环机制，只有UI线程能更新UI

- 另外Binder是运行在各自的线程池重点，所以ApplicationThread作为一个Binder用于和AMS通信，还有一个Activity构建了一个Viwe树后,ViewRoot的内部类W也是作为Binder和WMS进行通信，所有一般至少有三个线程

### 窗口相关的概念

安卓涉及窗口有以下几个概念，窗口，Window类，ViewRoot类以及W类

- 窗口(非Window类) ，这只是一个纯语义上的说法，即程序员看到的屏幕上某个独立的界面，比如一个Activity对应的的一个界面，比如一个对话框，一个Menu菜单，这些都可以称为窗口，从WMS角度来，窗口时接受事件的最小单位，WMS内部用特定的，类表示一个窗口，而给WMS添加一个窗口是调用其addView方法，所以说，从WMS来讲，所谓的添加窗口就是添加一个View对象，至于这个View是来自Activity还是用户自定义的都不需要关心，WMS接受到来自用户的消息后，首先要判断这个消息属于哪一个窗口，，即哪个View，然后通过IPC调用把这个消息发送到ViewRoot，所以WMS根本不关心也不知道Activity的存在，而窗口是一个抽象的概念，他本是并不是View。

- Window类，该类包含在android.view包中，是个给抽象的类，抽象了客户端窗口的基本操作，并且内部定义callback接口，Activity在attach方法中构建一个窗口对象，并把自己通过设置成为这个callback，能在View之前处理事件

- ViewRoot类，客户端申请创建窗口的客户端代理，用以和WMS交互，ViewRoot的内部W类用来完成这些工作，所以WMS管理的每一个窗口都对应一个RootView类

- W类 ，该类是ViewRoot类的一个内部类，也是一个Binder，用于和WMs交互

由于存在窗口的概念，又有window类，容易照成混淆，而实际上窗口从来都只是一个抽象的说法，WMS所管理的窗口与Window并没有什么关系。
