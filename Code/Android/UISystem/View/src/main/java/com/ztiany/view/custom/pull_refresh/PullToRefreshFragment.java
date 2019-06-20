package com.ztiany.view.custom.pull_refresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.view.R;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-14 15:55
 */
public class PullToRefreshFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pull_to_refresh_sample, container, false);
    }
}
