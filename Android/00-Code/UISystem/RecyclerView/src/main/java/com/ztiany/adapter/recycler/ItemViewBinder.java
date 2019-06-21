package com.ztiany.adapter.recycler;

import android.support.v7.widget.RecyclerView;

/**
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2017-09-13 15:33
 */
public abstract class ItemViewBinder<T, VH extends RecyclerView.ViewHolder> extends me.drakeet.multitype.ItemViewBinder<T, VH> {

    @SuppressWarnings("unchecked")
    protected final MultiTypeAdapter getDataManager() {
        return (MultiTypeAdapter) getAdapter();
    }
}
