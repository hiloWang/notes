package com.bennyhuo.github.settings

import com.bennyhuo.github.AppContext
import com.bennyhuo.github.R
import com.bennyhuo.github.model.account.AccountManager
import com.bennyhuo.github.utils.pref

object Settings {
    var lastPage: Int
        get() = if(lastPageIdString.isEmpty()) 0 else AppContext.resources.getIdentifier(lastPageIdString, "id", AppContext.packageName)
        set(value) {
            lastPageIdString = AppContext.resources.getResourceEntryName(value)
        }

    val defaultPage
        get() = if(AccountManager.isLoggedIn()) defaultPageForUser else defaultPageForVisitor

    private var defaultPageForUser by pref(R.id.navRepos)

    private var defaultPageForVisitor by pref(R.id.navRepos)

    private var lastPageIdString by pref("")

    var themeMode by pref("DAY")
}