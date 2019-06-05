package com.bennyhuo.github.view.fragments

import android.os.Bundle
import com.bennyhuo.github.model.account.AccountManager
import com.bennyhuo.github.view.common.CommonViewPagerFragment
import com.bennyhuo.github.view.config.FragmentPage
import com.bennyhuo.github.view.fragments.subfragments.RepoListFragment
import com.bennyhuo.github.view.fragments.subfragments.RepoListFragmentBuilder

class RepoFragment: CommonViewPagerFragment() {
    override fun getFragmentPagesNotLoggedIn(): List<FragmentPage> {
        return listOf(FragmentPage(RepoListFragment(), "All"))
    }

    override fun getFragmentPagesLoggedIn(): List<FragmentPage> {
        return listOf(
                FragmentPage(RepoListFragment().apply { arguments = Bundle().apply { putParcelable(RepoListFragmentBuilder.OPTIONAL_USER, AccountManager.currentUser) } }, "My"),
                FragmentPage(RepoListFragment(), "All")
        )
    }

}