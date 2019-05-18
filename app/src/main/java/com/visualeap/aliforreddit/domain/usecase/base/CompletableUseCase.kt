package com.visualeap.aliforreddit.domain.usecase.base

import com.visualeap.aliforreddit.core.util.applySchedulers
import com.visualeap.aliforreddit.core.util.scheduler.SchedulerProvider
import io.reactivex.Completable

abstract class CompletableUseCase<Params>(private val schedulerProvider: SchedulerProvider) {

    fun execute(params: Params): Completable {
        return createObservable(params).applySchedulers(schedulerProvider)
    }

    protected abstract fun createObservable(params: Params): Completable
}