package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.core.util.applySchedulers
import com.visualeap.aliforreddit.core.util.scheduler.SchedulerProvider
import io.reactivex.Single

abstract class SingleUseCase<T, P>(private val schedulerProvider: SchedulerProvider) {

    fun execute(param: P): Single<T> {
        return createObservable(param).applySchedulers(schedulerProvider)
    }

    protected abstract fun createObservable(params: P): Single<T>
}