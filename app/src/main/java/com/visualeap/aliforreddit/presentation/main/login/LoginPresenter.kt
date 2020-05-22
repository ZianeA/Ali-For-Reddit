package com.visualeap.aliforreddit.presentation.main.login

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.di.FragmentScope
import com.visualeap.aliforreddit.domain.usecase.*
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.presentation.common.ResourceProvider
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
    private val resourceProvider: ResourceProvider,
    private val schedulerProvider: SchedulerProvider
) {

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
            val disposable = authenticateUser.execute(
                resourceProvider.getString(R.string.client_id),
                resourceProvider.getString(R.string.redirect_url),
                url,
                generateAuthCode.execute(Unit)
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