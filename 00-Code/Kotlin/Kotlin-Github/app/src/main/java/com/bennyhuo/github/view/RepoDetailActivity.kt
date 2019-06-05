package com.bennyhuo.github.view

import android.os.Bundle
import com.apollographql.apollo.rx.RxApollo
import com.bennyhuo.experimental.coroutines.awaitOrError
import com.bennyhuo.experimental.coroutines.launchUI
import com.bennyhuo.github.R
import com.bennyhuo.github.network.GraphQLService
import com.bennyhuo.github.network.apolloClient
import com.bennyhuo.github.network.entities.Repository
import com.bennyhuo.github.network.graphql.entities.RepositoryIssueCountQuery
import com.bennyhuo.github.network.services.ActivityService
import com.bennyhuo.github.network.services.RepositoryService
import com.bennyhuo.github.utils.*
import com.bennyhuo.github.view.common.BaseDetailSwipeFinishableActivity
import com.bennyhuo.tieguanyin.annotations.ActivityBuilder
import com.bennyhuo.tieguanyin.annotations.Required
import kotlinx.android.synthetic.main.activity_repo_details.*
import kotlinx.android.synthetic.main.app_bar_details.*
import retrofit2.Response
import rx.Subscriber

@ActivityBuilder
class RepoDetailActivity: BaseDetailSwipeFinishableActivity() {

    @Required
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_details)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initDetails()
        reloadDetails()
    }

    private fun initDetails(){
        avatarView.loadWithGlide(repository.owner.avatar_url, repository.owner.login.first())
        collapsingToolbar.title = repository.name

        descriptionView.markdownText = getString(R.string.repo_description_template,
                repository.owner.login,
                repository.owner.html_url,
                repository.name,
                repository.html_url,
                repository.owner.login,
                repository.owner.html_url,
                githubTimeToDate(repository.created_at).view())

        bodyView.text = repository.description

        detailContainer.alpha = 0f

        stars.checkEvent = { isChecked ->
            if(isChecked){
                ActivityService.unstar(repository.owner.login, repository.name)
                        .map { false }
            } else {
                ActivityService.star(repository.owner.login, repository.name)
                        .map { true }
            }.doOnNext { reloadDetails(true) }
        }

        watches.checkEvent = { isChecked ->
            if(isChecked){
                ActivityService.unwatch(repository.owner.login, repository.name)
                        .map { false }
            } else {
                ActivityService.watch(repository.owner.login, repository.name)
                        .map { true }
            }.doOnNext { reloadDetails(true) }
        }

        ActivityService.isStarred(repository.owner.login, repository.name)
                .onErrorReturn {
                    if(it is retrofit2.HttpException){
                        it.response() as Response<Any>
                    } else {
                        throw it
                    }
                }
                .subscribeIgnoreError {
                    stars.isChecked = it.isSuccessful
                }

//        ActivityService.isWatched(repository.owner.login, repository.name)
//                .subscribeIgnoreError {
//                    watches.isChecked = it.subscribed
//                }

//        launchUI {
//            try {
//                watches.isChecked = ActivityService.isWatchedDeferred(repository.owner.login, repository.name).await().subscribed
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }

//        launchUI {
//            val (subscriptionResponse, error) = ActivityService.isWatchedDeferred(repository.owner.login, repository.name).awaitOrError()
//            error?.printStackTrace() ?: run {
//                watches.isChecked = subscriptionResponse!!.subscribed
//            }
//        }

        launchUI {
            val (subscriptionResponse, error) = ActivityService.isWatchedDeferred(repository.owner.login, repository.name).awaitOrError()
            error?.printStackTrace() ?: run {
                watches.isChecked = subscriptionResponse.subscribed
            }
        }

    }

    private fun reloadDetails(forceNetwork: Boolean = false){
        RepositoryService.getRepository(repository.owner.login, repository.name, forceNetwork)
                .subscribe(object: Subscriber<Repository>(){
                    override fun onStart() {
                        super.onStart()
                        loadingView.animate().alpha(1f).start()
                    }

                    override fun onNext(t: Repository) {
                        repository = t

                        owner.content = repository.owner.login
                        stars.content = repository.stargazers_count.toString()
                        watches.content = repository.subscribers_count.toString()
                        forks.content = repository.forks_count.toString()
                        //issues.content = repository.open_issues_count.toString()

                        loadingView.animate().alpha(0f).start()
                        detailContainer.animate().alpha(1f).start()
                    }

                    override fun onCompleted() = Unit

                    override fun onError(e: Throwable) {
                        loadingView.animate().alpha(0f).start()
                        e.printStackTrace()
                    }

                })

        val watcher = apolloClient.query(RepositoryIssueCountQuery(repository.name, repository.owner.login)).watcher()
        RxApollo.from(watcher)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    it.data()?.let{
//                        issues.content = "open: ${it.repository()?.openIssues()?.totalCount()?: 0} closed: ${it.repository()?.closedIssues()?.totalCount()?: 0}"
//                    }
//                }




//        GraphQLService.repositoryIssueCount(repository.owner.login, repository.name)
//                .subscribeIgnoreError {
//                    data ->
//                    issues.content = "open: ${data.repository()?.openIssues()?.totalCount()?: 0} closed: ${data.repository()?.closedIssues()?.totalCount()?: 0}"
//                }

        launchUI {
            val (data, error) = GraphQLService.repositoryIssueCount3(repository.owner.login, repository.name).awaitOrError()
            error?.printStackTrace()?: kotlin.run {
                issues.content = "open: ${data.repository()?.openIssues()?.totalCount()?: 0} closed: ${data.repository()?.closedIssues()?.totalCount()?: 0}"
            }
        }

//        GraphQLService.repositoryIssueCount2(repository.owner.login, repository.name)
//                        .enqueue(object : ApolloCall.Callback<RepositoryIssueCountQuery.Data>(){
//                    override fun onFailure(e: ApolloException) {
//                        e.printStackTrace()
//                    }
//
//                    override fun onResponse(response: com.apollographql.apollo.api.Response<Data>) {
//                        runOnUiThread {
//                            response.data()?.let{
//                                issues.content = "open: ${it.repository()?.openIssues()?.totalCount()?: 0} closed: ${it.repository()?.closedIssues()?.totalCount()?: 0}"
//                            }
//                        }
//                    }
//
//                })

//        apolloClient.query(RepositoryIssueCountQuery(repository.name, repository.owner.login))
//                .enqueue(object : ApolloCall.Callback<RepositoryIssueCountQuery.Data>(){
//                    override fun onFailure(e: ApolloException) {
//                        e.printStackTrace()
//                    }
//
//                    override fun onResponse(response: com.apollographql.apollo.api.Response<Data>) {
//                        runOnUiThread {
//                            response.data()?.let{
//                                issues.content = "open: ${it.repository()?.openIssues()?.totalCount()?: 0} closed: ${it.repository()?.closedIssues()?.totalCount()?: 0}"
//                            }
//                        }
//                    }
//
//                })

    }

}