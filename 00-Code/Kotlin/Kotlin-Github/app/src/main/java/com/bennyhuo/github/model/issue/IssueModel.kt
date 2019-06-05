package com.bennyhuo.github.model.issue

import com.bennyhuo.github.model.page.ListPage
import com.bennyhuo.github.network.entities.Issue
import com.bennyhuo.github.network.services.IssueService
import retrofit2.adapter.rxjava.GitHubPaging
import rx.Observable

/**
 * Created by benny on 9/24/17.
 */
class MyIssuePage : ListPage<Issue>() {
    override fun getData(page: Int): Observable<GitHubPaging<Issue>> {
        return IssueService.listIssuesOfAuthenticatedUser(page = page)
    }
}