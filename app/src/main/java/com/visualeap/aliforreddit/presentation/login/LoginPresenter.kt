package com.visualeap.aliforreddit.presentation.login

import android.util.Log
import com.visualeap.aliforreddit.presentation.di.FragmentScope
import com.visualeap.aliforreddit.domain.usecase.*
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

//TODO isFinalRedirectUrl is not a use-case, It should be a method inside LoginPresenter
//  Rename GetAuthUrl to BuildAuthUrl, and GetUniqueString to GenerateAuthCode.
@FragmentScope
class LoginPresenter @Inject constructor(
    private val view: LoginView,
    private val getUniqueString: GetUniqueString,
    private val getAuthUrl: GetAuthUrl,
    private val isFinalRedirectUrl: IsFinalRedirectUrl,
    private val authenticateUser: AuthenticateUser,
    private val schedulerProvider: SchedulerProvider
) {

    private val tag = LoginPresenter::class.java.simpleName
    private val compositeDisposable = CompositeDisposable()

    fun start() {
        val authUrl = getAuthUrl.execute(getUniqueString.execute(Unit))
        view.showLoginPage(authUrl)
    }

    fun onPageStarted(url: String) {
        if (isFinalRedirectUrl.execute(url)) {
            view.hideLoginPage()

            //Authenticate user
            val params = AuthenticateUser.Params(url, getUniqueString.execute(Unit))

            val disposable = authenticateUser.execute(params)
                .applySchedulers(schedulerProvider)
                .subscribe({ /*TODO on sucess*/ },
                    { /*TODO error*/ Log.i(tag, it.message) })

            compositeDisposable.add(disposable)
        }
    }

    fun stop() {
        compositeDisposable.dispose()
    }
}