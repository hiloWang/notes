# BFC

BFC(Block formatting context)直译为 **块级格式化上下文**。它是一个独立的渲染区域，只有 `Block-level box` 参与， 它规定了内部的 Block-level Box 如何布局，并且与这个区域外部毫不相干。在 BFC 中，盒子从顶端开始垂直地一个接一个地排列，两个盒子之间的垂直的间隙是由它们的 margin 值所决定的。在一个 BFC 中，两个相邻的块级盒子的垂直外边距会产生折叠。

**哪些元素会生成 BFC**？

- 根元素 html
- float 属性不为 none 浮动框
- position 为 absolute 或 fixed
- display 为 `inline-block, table-cell, table-caption, flex, inline-flex`
- overflow 不为 visible 的块框。这就是为什么我们经常用 `overflow:hidden` 去清除内部浮动的原因
- 触发 IE 的 hasLayout 特性

**BFC 的布局规则**：

- 内部的 Box 会在垂直方向，一个接着一个地放置。
- BFC 的区域不会与 float box 重叠。
- 内部的 Box 垂直方向的距离由 margin 决定，属于同一个 BFC 的两个相邻 Box 的 Margin 会发生重叠。
- 计算 BFC 的高度时，浮动的元素也要参与计算。
- BFC 就是页面上的一个隔离器，容器里面的子元素不会影响到外面的元素。

具体参考 [深入理解 BFC和 Margin Collapse](https://www.w3cplus.com/css/understanding-bfc-and-margin-collapse.html)
