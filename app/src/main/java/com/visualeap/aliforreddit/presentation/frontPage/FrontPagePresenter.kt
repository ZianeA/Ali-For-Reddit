package com.visualeap.aliforreddit.presentation.frontPage

import com.visualeap.aliforreddit.domain.repository.SubmissionRepository
import com.visualeap.aliforreddit.presentation.util.SchedulerProvider
import com.visualeap.aliforreddit.presentation.util.applySchedulers
import io.reactivex.disposables.CompositeDisposable

class FrontPagePresenter(
    private val view: FrontPageView,
    private val repository: SubmissionRepository,
    private val schedulerProvider: SchedulerProvider
) {

    private val disposables = CompositeDisposable()

    fun loadSubmissions() {
        val disposable = repository.getSubmissions()
            .applySchedulers(schedulerProvider)
            .subscribe { view.displaySubmissions(it) }

        disposables.add(disposable)
    }

    //TODO - call this on onPause
    fun stop() {
        disposables.clear()
    }
}