package com.visualeap.aliforreddit.presentation.main.login

import android.util.Log
import com.visualeap.aliforreddit.presentation.di.FragmentScope
import com.visualeap.aliforreddit.domain.usecase.*
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import okhttp3.HttpUrl
import javax.inject.Inject
import javax.inject.Named

@FragmentScope
class LoginPresenter @Inject constructor(
    private val view: LoginView,
    private val generateAuthCode: GenerateAuthCode,
    private val buildAuthUrl: BuildAuthUrl,
    private val authenticateUser: AuthenticateUser,
    private val schedulerProvider: SchedulerProvider,
    @Named("redirectUrl") private val redirectUrl: String
) {

    private val tag = LoginPresenter::class.java.simpleName
    private val compositeDisposable = CompositeDisposable()

    fun start() {}

    fun onLogInClicked() {
        val authUrl = buildAuthUrl.execute(generateAuthCode.execute(Unit))
        view.hideLoginPrompt()
        view.showLoginPage(authUrl)
    }

    fun onPageStarted(url: String) {
        if (isFinalRedirectUrl(url)) {
            view.hideLoginPage()

            //Authenticate user
            val params = AuthenticateUser.Params(url, generateAuthCode.execute(Unit))

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

    private fun isFinalRedirectUrl(url: String): Boolean {
        HttpUrl.parse(url)
            ?.let {
                return if (it.query() != null) url.contains(redirectUrl) else false
            }
            ?: return false
    }
}