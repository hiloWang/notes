package com.bennyhuo.github.presenter

import com.bennyhuo.github.BuildConfig
import com.bennyhuo.github.model.account.AccountManager
import com.bennyhuo.github.view.LoginActivity
import com.bennyhuo.mvp.impl.BasePresenter

class LoginPresenter: BasePresenter<LoginActivity>()  {

    fun doLogin(name: String, passwd: String){
        AccountManager.username = name
        AccountManager.passwd = passwd
        view.onLoginStart()
        AccountManager.login()
                .subscribe({
                    view.onLoginSuccess()
                }, {
                    view.onLoginError(it)
                })
    }

    fun checkUserName(name: String): Boolean {
        return true
    }

    fun checkPasswd(passwd: String): Boolean {
        return true
    }

    override fun onResume() {
        super.onResume()
        if(BuildConfig.DEBUG){
            view.onDataInit(BuildConfig.testUserName, BuildConfig.testPassword)
        } else {
            view.onDataInit(AccountManager.username, AccountManager.passwd)
        }
    }
}