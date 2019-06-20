package com.bennyhuo.github.view.common

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.bennyhuo.github.view.MainActivity

abstract class CommonSinglePageFragment: Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //隐藏掉 actionBarController
        (activity as MainActivity).actionBarController.setupWithViewPager(null)
    }
}