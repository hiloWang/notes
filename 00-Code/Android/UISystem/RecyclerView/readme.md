# RecyclerView相关技术

## 1 使用ItemDecoration实现分组粘性索引

- 判断当前Item属于哪一个组？通过数据的分组的tag来判断，这个判断可以通过接口暴露，可以内部判断，但是通过接口暴露，可扩展性更强
- 通过接口获取哪些信息
  - 当前组的title(用于绘制title)
  - 当前position的item属于哪个组
  - 当前position的item是不是该组的第一个item(用于绘制该组的header)
  - 当前position的item在组中的位置
  - 当前组的长度(用于判断是不是改组最后一个item)
- 每个组的第一个item都需要绘制它的header
- 但是当一个组将要画出屏幕时，如何让header也跟着一起滑出屏幕？通过观察，可以发现要滑出屏幕的header的底部高度与该组最后一个item的底部高度是一致的。

## 2 ScrollView中嵌套RecyclerView

这样的需求还是经常有的，强大的RecyclerView本身就支持WrapContent，但是实践发现RecyclerView与ScrollView嵌套时，最好使用NestedScrollView
