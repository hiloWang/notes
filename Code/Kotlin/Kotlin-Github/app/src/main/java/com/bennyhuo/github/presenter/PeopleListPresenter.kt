package com.bennyhuo.github.presenter

import com.bennyhuo.github.model.page.ListPage
import com.bennyhuo.github.model.people.PeoplePage
import com.bennyhuo.github.model.people.PeoplePageParams
import com.bennyhuo.github.network.entities.User
import com.bennyhuo.github.view.common.CommonListPresenter
import com.bennyhuo.github.view.fragments.subfragments.PeopleListFragment

/**
 * Created by benny on 7/9/17.
 */
class PeopleListPresenter : CommonListPresenter<User, PeopleListFragment>(){

    override val listPage: ListPage<User> by lazy {
        PeoplePage(PeoplePageParams(view.type, view.login))
    }

}