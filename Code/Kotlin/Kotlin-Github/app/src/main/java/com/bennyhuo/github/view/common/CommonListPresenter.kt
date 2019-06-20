package com.bennyhuo.github.view.common

import com.bennyhuo.github.model.page.ListPage
import com.bennyhuo.mvp.impl.BasePresenter
import rx.Subscription

abstract class CommonListPresenter<DataType, out View : CommonListFragment<DataType, CommonListPresenter<DataType, View>>> : BasePresenter<View>() {

    abstract val listPage: ListPage<DataType>

    private var firstInView = true
    private val subscriptionList = ArrayList<Subscription>()

    fun initData() {
        listPage.loadFromFirst()
                .subscribe({
                    if (it.isEmpty()) view.onDataInitWithNothing() else view.onDataInit(it)
                }, {
                    view.onDataInitWithError(it.message ?: it.toString())
                }).let(subscriptionList::add)
    }

    fun refreshData() {
        listPage.loadFromFirst()
                .subscribe(
                        { if (it.isEmpty()) view.onDataInitWithNothing() else view.onDataRefresh(it) },
                        { view.onDataRefreshWithError(it.message ?: it.toString())}
                ).let(subscriptionList::add)
    }

    fun loadMore() {
        listPage.loadMore()
                .subscribe(
                        { view.onMoreDataLoaded(it) },
                        { view.onMoreDataLoadedWithError(it.message ?: it.toString()) }
                ).let(subscriptionList::add)
    }

    override fun onResume() {
        super.onResume()
        if (!firstInView) {
            refreshData()
        }
        firstInView = false
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptionList.forEach(Subscription::unsubscribe)
        subscriptionList.clear()
    }
}