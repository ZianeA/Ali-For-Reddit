package com.visualeap.aliforreddit.presentation.frontPage.container

import com.visualeap.aliforreddit.domain.authentication.IsUserLoggedIn
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.presentation.common.di.FragmentScope
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@FragmentScope
class FrontPageContainerPresenter @Inject constructor(
    private val view: FrontPageContainerView,
    private val isUserLoggedIn: IsUserLoggedIn,
    private val schedulerProvider: SchedulerProvider
) {
    private val disposables = CompositeDisposable()

    fun start() {
        val disposable = isUserLoggedIn.execute()
            .applySchedulers(schedulerProvider)
            .subscribe(
                { if (it) view.showHomeScreen() else view.showLoginScreen() },
                { /*onError*/ })

        disposables.add(disposable)
    }

    fun stop() {
        disposables.clear()
    }
}