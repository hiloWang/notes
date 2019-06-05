package com.bennyhuo.github.model.people

import com.bennyhuo.github.model.page.ListPage
import com.bennyhuo.github.network.entities.User
import com.bennyhuo.github.network.services.UserService
import retrofit2.adapter.rxjava.GitHubPaging
import rx.Observable

/**
 * Created by benny on 1/11/18.
 */
class PeoplePageParams(val type: String, val login: String?)

class PeoplePage(val params: PeoplePageParams) : ListPage<User>() {

    enum class Type {
        FOLLOWER, FOLLOWING, ALL
    }

    override fun getData(page: Int): Observable<GitHubPaging<User>> {
        return when (Type.valueOf(params.type)) {
            PeoplePage.Type.FOLLOWER -> UserService.followers(params.login!!, page = page)
            PeoplePage.Type.FOLLOWING -> UserService.following(params.login!!, page = page)
            PeoplePage.Type.ALL -> UserService.allUsers(data.since)
        }
    }

}
