package com.ztiany.systemui.uisapmle;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-07-19 10:05
 */
public class NormalFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new AppCompatButton(getContext());
        textView.setText("Normal");
        textView.setTextColor(Color.RED);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SystemUIWithFragmentActivity) getActivity()).showFullscreenFragment();
            }
        });
        return textView;
    }
}
