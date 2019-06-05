package com.bennyhuo.github.presenter

import com.bennyhuo.github.model.issue.MyIssuePage
import com.bennyhuo.github.network.entities.Issue
import com.bennyhuo.github.view.common.CommonListPresenter
import com.bennyhuo.github.view.fragments.subfragments.MyIssueListFragment

/**
 * Created by benny on 7/9/17.
 */
class MyIssuePresenter : CommonListPresenter<Issue, MyIssueListFragment>() {
    override val listPage = MyIssuePage()
}