package com.bennyhuo.github.presenter

import com.bennyhuo.github.model.repo.RepoListPage
import com.bennyhuo.github.network.entities.Repository
import com.bennyhuo.github.view.common.CommonListPresenter
import com.bennyhuo.github.view.fragments.subfragments.RepoListFragment

class RepoListPresenter : CommonListPresenter<Repository, RepoListFragment>(){
    override val listPage by lazy {
        RepoListPage(view.user)
    }

}