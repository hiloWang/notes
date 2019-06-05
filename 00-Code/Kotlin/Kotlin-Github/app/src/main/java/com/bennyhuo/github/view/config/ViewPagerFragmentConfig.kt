package com.bennyhuo.github.view.config

import android.support.v4.app.Fragment

interface ViewPagerFragmentConfig {
    fun getFragmentPagesLoggedIn(): List<FragmentPage>
    fun getFragmentPagesNotLoggedIn(): List<FragmentPage>
}

data class FragmentPage(val fragment: Fragment, val title: String)
