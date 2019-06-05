package com.ztiany.itemtouch.help;

/**
 *RecyclerView的ViewHolder需要实现此接口，当item的状态变化时的回调
 */
public interface ItemTouchHelperViewHolder {

    /**
     * 当Item被选中时
     */
    void onItemSelected();


    /**
     * 当Item被释放时
     */
    void onItemClear();
}
