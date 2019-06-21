package com.ztiany.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;


class ItemAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private final List<Item> mItems;

    ItemAdapter(Context context, List<Item> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Button button = new AppCompatButton(mContext);
        button.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new RecyclerView.ViewHolder(button) {
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Item item = mItems.get(position);
        Button button = (Button) holder.itemView;
        button.setText(item.mName);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppCompatActivity.class.isAssignableFrom(item.mClazz)) {
                    mContext.startActivity(new Intent(mContext, item.mClazz));
                } else {
                    mContext.startActivity(ContentActivity.getLaunchIntent(mContext, item.mName, item.mClazz));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }
}
