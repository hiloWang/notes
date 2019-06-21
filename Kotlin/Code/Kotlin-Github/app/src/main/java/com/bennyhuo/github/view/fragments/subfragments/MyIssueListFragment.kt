package com.bennyhuo.github.view.fragments.subfragments

import com.bennyhuo.github.network.entities.Issue
import com.bennyhuo.github.presenter.MyIssuePresenter
import com.bennyhuo.github.view.common.CommonListFragment

/**
 * Created by benny on 7/9/17.
 */
class MyIssueListFragment : CommonListFragment<Issue, MyIssuePresenter>() {
    companion object{
        const val REPOSITORY_NAME = "repository_name"
        const val OWNER_LOGIN = "owner_login"
    }

    override val adapter = IssueListAdapter()
}