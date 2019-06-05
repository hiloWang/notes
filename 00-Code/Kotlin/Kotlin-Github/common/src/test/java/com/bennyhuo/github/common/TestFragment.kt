package com.bennyhuo.github.common

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-08-20 23:51
 */
class TestFragment : Fragment() {

    private val a by lazy {
        arguments?.getString("")
    }

    companion object {
        fun newInstance() {
            Bundle().apply {
                putString(TestFragment::a.name, "A")
            }
        }
    }

}