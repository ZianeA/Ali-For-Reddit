package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.core.util.applySchedulers
import com.visualeap.aliforreddit.core.util.scheduler.SchedulerProvider
import io.reactivex.Completable

abstract class CompletableUseCase<P>(private val schedulerProvider: SchedulerProvider) {

    fun execute(param: P): Completable {
        return createObservable(param).applySchedulers(schedulerProvider)
    }

    protected abstract fun createObservable(params: P): Completable
}