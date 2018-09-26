# Draw 流程分析

---
## 1 Draw流程

绘制是View树遍历流程的最后一个，前面说的测量和布局只是确定View的大小和位置，如果不对view进行绘制，那么界面上依然不会有任何图形显示出来，draw也是从ViewRoot中的performTraversals发起的。然后会view的draw相关方法，但是并不是每个View都需要执行绘制，在执行绘制的过程中，只会重绘需要绘制的View。

draw方法的流程为：

```
    ViewRoot调用DecorView的draw方法：ViewRoot-->DecorView.draw(canvas)
    
    DecorView的draw方法调用自己的dispatchDraw(Canvas canvas)方法，然后在此方法中会调用子view的
    draw(Canvas canvas, ViewGroup parent, long drawtime)方法，此方法会调用单个参数的draw(Canvas canvas)方法。
```

---
## 2 draw方法

view有两个重载的draw方法，分别是：

```
    draw(Canvas canvas, ViewGroup parent, long drawtime)
    draw(Canvas canvas)
```

`draw(Canvas canvas, ViewGroup parent, long drawtime)`方法由父view调用，此方法比较重要的，在这里会判断View的一些内部标识，还会对canvas做一些调整，如绘制区域与绘图坐标系的调整，不一定会调用view的 `draw(Canvas canvas)`方法，如果不调用则绘制的是view的缓存。具体可以查看相关方法的源码。

```java
     public void draw(Canvas canvas) {
           ......
           //通过内部标识，判断View的行为
            final int privateFlags = mPrivateFlags;
            final boolean dirtyOpaque = (privateFlags & DIRTY_MASK) == DIRTY_OPAQUE &&
                    (mAttachInfo == null || !mAttachInfo.mIgnoreDirtyState);
            mPrivateFlags = (privateFlags & ~DIRTY_MASK) | DRAWN;
    
            /*
             *draw的步骤
             *
             *      1. 画背景
             *      2. 如果需要, 为显示渐变框做一些准备操作
             *      3. 画内容(onDraw)
             *      4. 画子view
             *      5. 如果需要, 画一些渐变效果
             *      6. 画装饰内容，如滚动条
             */
    
            // Step 1, draw the background, if needed
            int saveCount;
    
            if (!dirtyOpaque) {
                final Drawable background = mBGDrawable;
                if (background != null) {
                    final int scrollX = mScrollX;
                    final int scrollY = mScrollY;
    
                    if (mBackgroundSizeChanged) {
                        background.setBounds(0, 0,  mRight - mLeft, mBottom - mTop);
                        mBackgroundSizeChanged = false;
                    }
    
                    if ((scrollX | scrollY) == 0) {
                        background.draw(canvas);
                    } else {
                        canvas.translate(scrollX, scrollY);
                        background.draw(canvas);
                        canvas.translate(-scrollX, -scrollY);
                    }
                }
            }
    
            // skip step 2 & 5 if possible (common case)
            final int viewFlags = mViewFlags;
            boolean horizontalEdges = (viewFlags & FADING_EDGE_HORIZONTAL) != 0;
            boolean verticalEdges = (viewFlags & FADING_EDGE_VERTICAL) != 0;
            //如果条件不成立，跳过2-5步
            if (!verticalEdges && !horizontalEdges) {
                // Step 3,画内容
                if (!dirtyOpaque) onDraw(canvas);
    
                // Step 4,画孩子
                dispatchDraw(canvas);
    
                // Step 6, 画装饰（滚动条）
                onDrawScrollBars(canvas);
    
                // we're done...
                return;
            }
    
           ......
    
            // Step 6, draw decorations (scrollbars)
            onDrawScrollBars(canvas);
        }
```

**dispatchDraw&drawChild**

```java
    protected void dispatchDraw(Canvas canvas) {
        ......
    
        for (int i = 0; i < count; i++) {
            final View child = children[i];
            if ((child.mViewFlags & VISIBILITY_MASK) == VISIBLE || child.getAnimation() != null) {
                more |= drawChild(canvas, child, drawingTime);
            }
        }
        ......
    }
                               
    
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
            return child.draw(canvas, this, drawingTime);
    }
```

可以看到draw的过程分为：

1. 如果设置了，画背景
2. 如果需要, 为显示渐变框做一些准备操作
3. 调用onDraw画内容
4. 调用dispatchDraw画子view
5. 如果需要, 画渐变框
6. 画装饰内容，如前景与滚动条

- onDraw是每个view需要实现的，否则View默认只能显示背景，而实现onDraw就是为了画出View的内容，而ViewGroup一般不需要实现onDraw,因为它仅仅是作为View的容器没有需要绘制东西，
- dispatchDraw用来遍历ViewGrop的所有子view，执行draw方法


---
## 3 onDraw中如何绘制

在系统源码中onDraw是个空实现方法，仅仅提供了一个Canvas画板，到底如何来画View的内容呢？

如果需要熟练的绘制出各种效果的View，我们需要掌握很多知识：

- Canvas的是使用 绘制-变化-图层操作等等
- Paint 画笔
- Path 路径
- Bitmap Canvas是画布，但是我们需要画纸，Bitmap就是画纸
- ColorMatrix和Matrix的熟练运用
- PathMeasure

---
## 4 View的缓存优化

在Android的显示机制中，View的软件渲染都是基于bitmap图片进行的处理。并且刷新机制中只要是与脏数据区有交集的视图都将重绘，所以在View的设计中就有一个cache的概念存在，这个cache无疑就是一个bitmap对象。也就是说在绘制流程中View不一定会被重新绘制，有可能绘制的只是View的缓存。这个在后面的绘图中深入学习。