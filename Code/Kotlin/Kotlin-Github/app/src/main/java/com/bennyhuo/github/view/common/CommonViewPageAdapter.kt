package com.bennyhuo.github.view.common

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.PagerAdapter
import com.bennyhuo.github.utils.ViewPagerAdapterList
import com.bennyhuo.github.view.config.FragmentPage

class CommonViewPageAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    val fragmentPages = ViewPagerAdapterList<FragmentPage>(this)

    override fun getItem(position: Int): Fragment {
        return fragmentPages[position].fragment
    }

    override fun getCount(): Int {
        return fragmentPages.size
    }

    /*在这里，通过返回 POSITION_NONE 解决界面刷新问题*/
    override fun getItemPosition(fragment: Any): Int {
        /*
        problem
            0-all
            0-my 1-all
            0-my
       fix
            0-all
            0-my(POSITION_NONE remove) 1-all
            0-all
         */
        for ((index, page) in fragmentPages.withIndex()){
            if(fragment == page.fragment){
                return index
            }
        }
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentPages[position].title
    }

}