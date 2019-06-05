package com.bennyhuo.github.view.fragments

import com.bennyhuo.github.view.common.CommonViewPagerFragment
import com.bennyhuo.github.view.config.FragmentPage
import com.bennyhuo.github.view.fragments.subfragments.MyIssueListFragment

/**
 * Created by benny on 7/16/17.
 */
class MyIssueFragment : CommonViewPagerFragment() {
    override fun getFragmentPagesNotLoggedIn()
            = listOf(
            FragmentPage(MyIssueListFragment(), "My")
    )

    override fun getFragmentPagesLoggedIn(): List<FragmentPage>
            = listOf(
            FragmentPage(MyIssueListFragment(), "My")
    )
}