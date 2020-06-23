package com.visualeap.aliforreddit.presentation.login

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.authentication.AuthenticateUser
import com.visualeap.aliforreddit.domain.authentication.BuildAuthUrl
import com.visualeap.aliforreddit.presentation.common.di.FragmentScope
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.presentation.common.util.ResourceProvider
import io.reactivex.disposables.CompositeDisposable
import okhttp3.HttpUrl
import javax.inject.Inject

@FragmentScope
class LoginPresenter @Inject constructor(
    private val view: LoginView,
    private val buildAuthUrl: BuildAuthUrl,
    private val authenticateUser: AuthenticateUser,
    private val resourceProvider: ResourceProvider,
    private val schedulerProvider: SchedulerProvider
) {
    private val compositeDisposable = CompositeDisposable()
    private lateinit var authUrl: String

    fun start() {}

    fun onLogInClicked() {
        authUrl = buildAuthUrl.execute(
            resourceProvider.getString(R.string.client_id),
            resourceProvider.getString(R.string.redirect_url)
        )
        view.hideLoginPrompt()
        view.showLoginPage(authUrl)
    }

    fun onPageStarted(url: String) {
        if (isFinalRedirectUrl(url)) {
            view.hideLoginPage()

            //Authenticate user
            val disposable = authenticateUser.execute(
                resourceProvider.getString(R.string.redirect_url),
                url,
                authUrl
            )
                .applySchedulers(schedulerProvider)
                .subscribe({ view.reloadScreen() }, { /*TODO error*/ })

            compositeDisposable.add(disposable)
        }
    }

    fun stop() {
        compositeDisposable.dispose()
    }

    private fun isFinalRedirectUrl(url: String): Boolean {
        HttpUrl.parse(url)
            ?.let {
                return if (it.query() != null) {
                    url.contains(resourceProvider.getString(R.string.redirect_url))
                } else false
            }
            ?: return false
    }
}