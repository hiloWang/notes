package com.bennyhuo.github.model.account

import com.bennyhuo.github.network.entities.AuthorizationReq
import com.bennyhuo.github.network.entities.AuthorizationRsp
import com.bennyhuo.github.network.entities.User
import com.bennyhuo.github.network.services.AuthService
import com.bennyhuo.github.network.services.UserService
import com.bennyhuo.github.utils.fromJson
import com.bennyhuo.github.utils.pref
import com.google.gson.Gson
import retrofit2.HttpException
import rx.Observable

interface OnAccountStateChangeListener {
    fun onLogin(user: User)

    fun onLogout()
}

object AccountManager {
    var authId by pref(-1)
    var username by pref("")
    var passwd by pref("")
    var token by pref("")

    private var userJson by pref("")

    var currentUser: User? = null
        get() {
            if (field == null && userJson.isNotEmpty()) {
                field = Gson().fromJson<User>(userJson)
            }
            return field
        }
        set(value) {
            if(value == null){
                userJson = ""
            } else {
                userJson = Gson().toJson(value)
            }
            field = value
        }

    val onAccountStateChangeListeners = ArrayList<OnAccountStateChangeListener>()

    private fun notifyLogin(user: User) {
        onAccountStateChangeListeners.forEach {
            it.onLogin(user)
        }
    }

    private fun notifyLogout() {
        onAccountStateChangeListeners.forEach { it.onLogout() }
    }

    fun isLoggedIn(): Boolean = token.isNotEmpty()

    fun login() =
            AuthService.createAuthorization(AuthorizationReq())
                    .doOnNext {
                        if (it.token.isEmpty()) throw AccountException(it)
                    }
                    .retryWhen {
                        it.flatMap {
                            if (it is AccountException) {
                                AuthService.deleteAuthorization(it.authorizationRsp.id)
                            } else {
                                Observable.error(it)
                            }
                        }
                    }
                    .flatMap {
                        token = it.token
                        authId = it.id
                        UserService.getAuthenticatedUser()
                    }
                    .map {
                        currentUser = it
                        notifyLogin(it)
                    }

    fun logout() = AuthService.deleteAuthorization(authId)
            .doOnNext {
                if (it.isSuccessful) {
                    authId = -1
                    token = ""
                    currentUser = null
                    notifyLogout()
                } else {
                    throw HttpException(it)
                }
            }

    class AccountException(val authorizationRsp: AuthorizationRsp) : Exception("Already logged in.")
}