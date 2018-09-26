#  View的onMeasure一般写法

```java
      private Bitmap mBitmap;
    
        private void init() {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.meinv);
        }
    
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // 获取宽度测量规格中的mode和size
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            // 声明一个临时变量来存储计算出的测量值
            int  heightResult = 0;
            int widthResult = 0;
            
             /*
             * 如果父容器心里有数
             */
            if (widthMode == MeasureSpec.EXACTLY) {
                // 那么子view就用父容器给定尺寸
                widthResult = widthSize;
            }
            /*
             * 如果父容器不确定自身大小
             */
            else{
                // 那么子view可要自己看看自己需要多大了
                widthResult = mBitmap.getWidth()+getPaddingLeft()+getPaddingRight();
                /*
                   * 如果爹给儿子的是一个限制值
                 */
                if (widthMode == MeasureSpec.AT_MOST) {
                    // 那么儿子自己的需求就要跟爹的限制比比看谁小要谁
                    widthResult = Math.min(widthSize, widthResult);
                }
            }
    
            if (heightMode == MeasureSpec.EXACTLY) {
                heightResult = widthSize;
            }else{
                //考虑padding
                heightResult = mBitmap.getHeight()+getPaddingBottom()+getPaddingTop();
                if (heightMode == MeasureSpec.AT_MOST) {
                    heightResult = Math.min(widthSize, heightResult);
                }
            }
            // 设置测量尺寸
            setMeasuredDimension(widthResult, heightResult);
        }
```


流程就是：

1. 获取测量尺寸和模式,定义临时变量存储技术结果
2. 判断测量模式：
    - 模式是EXACTL的，就使用测量规格中的尺寸
    - 模式是UNSPECIFIED，使用自身计算的尺寸
    - 模式是AT_MOST的，使用自身计算的尺寸与规定尺寸中较小的一个

3.设置测量尺寸


## ViewGroup的onMeasure一般写法总结

由于ViewGroup的布局卡变万化，根本没有统一的模板，只能根据业务来定，一般大概流程就是：

1. 首先ViewGroup作为容器，有测量所有子view的职责，所以第一步是遍历测量所有的字view。一般我们会选用ViewGroup提供的`measureChildWithMargins`方法，这个方法已经考虑了子view需要的margin和ViewGroup自身需要的padding。
2. 遍历测量完所有子view，那么子view的宽高基本可以确定了，如果有特殊的需求，可以对子view进行多次测量。
3. 根据自身的布局特性和自view测量的结果(记得带上子view的margin)，来确定自身的size，比如在精确模式下使用自身的size、在atMost模式下，考虑自身margin和子view的margin和size来确认自身的size，一般情况下需要遵守基本的测量规则