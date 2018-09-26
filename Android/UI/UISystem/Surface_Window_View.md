
---
## Surface

Surface是一个对象，该对象拥有一群像素，这些像素可以组合层图像显示到屏幕上、手机屏幕上的每一个window（如对话框、全屏的activity、状态栏）都有唯一一个自己的surface，window将自己的内容（content）绘制到该surface中。Surface Flinger根据各个surface在Z轴上的顺序（Z-order）将它们渲染到最终的显示屏上。

一个surface通常有两个缓冲区以实现双缓冲绘制：当应用正在一个缓冲区中绘制自己下一个UI状态时，Surface Flinger可以将另一个缓冲区中的数据合成显示到屏幕上，而不用等待应用绘制完成。


---
## Window

官方文档对Window的解释：Abstract base class for a top-level window look and behavior policy. An instance of this class should be used as the top-level view added to the window manager. It provides standard UI policies such as a background, title area, default key processing, etc.翻译过来就是Window是一个抽象类，是所有视图的最顶层容器，视图的外观和行为都归他管，不论是背景显示，标题栏还是事件处理都是他管理的范畴

Windows是抽象的，就像我们在计算机中看到的各个软件窗口一样，它拥有**唯一一个用以绘制自己的内容的surface**、应用通过 Window Manager创建一个window，Window Manager 为每一个window创建一个surface，并把该surface传递给应用以便应用在上面绘制。应用可以在surface上任意进行绘制。对于Window Manager来说，surface就是一个不透明的矩形而已。


---
## View

View就是与用户交互的基本元素，属于一个Window中的某一个UI元素，而每个Window都有自己的View hierarchy，该view hierarchy提供了window所有的行为。当一个window需要重绘时（比如一个view 通过invalidate方法使自己失效了）就要进入到window的surface中去完成了。首先，该window的surface会被锁定，锁定的同时会返回一个canvas，该canvas可被用来在surface上绘制内容。该canvas会沿着view hierarchy遍历传递给每一个view，好让每个view绘制自己的UI部分。当这个过程完成时，surface将会被解锁和提交（posted），提交的目的是将刚刚绘制好的缓冲区交换到前台，然后让Surface Flinger利用该缓冲区的数据刷新window的显示。


---
##  SurfaceView

一个SurfaceView就是一个被特殊实现的View，它拥有自己专门的一个surface，以便让应用直接在里面绘制内容。该SurfaceView是独立于其所属window的view hierarchy的，view hierarchy中的view们共享window那一个surface。SurfaceView 的工作原理比你想的要简单——SurfaceView所做的全部就是要求Window Manager创建一个window，并告诉Window Manager所创建的window的Z轴顺序（Z-order），这个Z轴顺序可以帮助Window Manager决定将新建的window置于SurfaceView所属window的前面还是后面。然后，Window Manager会将新建的window放置到SurfaceView在所属window中的位置。如果新建window在SurfaceView所属window后面，SurfaceView会将它在所属window中占据的部分变透明，以便让后面的window显示出来。

---
## Bitmap

一个Bitmap只是一些像素数据的接口。Bitmap中的像素数据所占内存空间既可以在直接创建Bitmap时分配，也可以创建Bitmap后将它指向一块像素数据空间，比如，当把一个canvas对象链接（hook up）到一个surface进行绘制时，就会创建一个Bitmap指向surface当前正在绘制的缓冲区。

---
## 引用

- [Android易混概念辨析之Surface,Window,View,SurfaceView,Bitmap](http://www.jianshu.com/p/7897d97d17cc)
- [Android 5.0(Lollipop)中的SurfaceTexture，TextureView, SurfaceView和GLSurfaceView](https://blog.csdn.net/jinzhuojun/article/details/44062175)




