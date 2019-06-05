package com.ztiany.itemtouch.help;

import android.support.v7.widget.RecyclerView;

/**
 * RecyclerView的适配器需要实现的接口，用来回调Iten移动或者删除后的数据操作
 */
public interface ItemTouchHelperAdapter {

    /**
     * 当item移动时
     *
     * @param fromPosition 原始位置
     * @param toPosition   目标位置
     * @see RecyclerView#getAdapterPositionFor(RecyclerView.ViewHolder)
     * @see RecyclerView.ViewHolder#getAdapterPosition()
     */
    void onItemMove(int fromPosition, int toPosition);


    /**
     * 当item移动时
     *
     * @param position   删除的位置
     * @see RecyclerView#getAdapterPositionFor(RecyclerView.ViewHolder)
     * @see RecyclerView.ViewHolder#getAdapterPosition()
     */
    void onItemDismiss(int position);
}
