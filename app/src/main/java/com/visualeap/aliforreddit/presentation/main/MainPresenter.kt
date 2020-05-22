package com.visualeap.aliforreddit.presentation.main

import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.domain.repository.RedditorRepository
import com.visualeap.aliforreddit.domain.usecase.IsUserLoggedIn
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.presentation.di.ActivityScope
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@ActivityScope
class MainPresenter @Inject constructor(
    private val view: MainView,
    private val redditorRepository: RedditorRepository,
    private val isUserLoggedIn: IsUserLoggedIn,
    private val schedulerProvider: SchedulerProvider
) {
    private val disposables = CompositeDisposable()

    fun start(refresh: Boolean) {
        if(refresh.not()) return

        val disposable = isUserLoggedIn.execute()
            .flatMapMaybe {
                if (it) redditorRepository.getCurrentRedditor().toMaybe()
                else Maybe.empty()
            }
            .applySchedulers(schedulerProvider)
            .subscribe(
                { view.displayCurrentRedditor(it) },
                { /*onError*/ },
                { view.displayLoginPrompt() })

        disposables.add(disposable)
    }

    fun stop() {
        disposables.clear()
    }
}