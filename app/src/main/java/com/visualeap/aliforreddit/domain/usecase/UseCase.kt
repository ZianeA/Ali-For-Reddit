package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.core.util.applySchedulers
import com.visualeap.aliforreddit.core.util.scheduler.SchedulerProvider
import io.reactivex.Observable

abstract class UseCase<T, P>(private val schedulerProvider: SchedulerProvider) {

    fun execute(param: P): Observable<T> {
        return createObservable(param).applySchedulers(schedulerProvider)
    }

    protected abstract fun createObservable(params: P): Observable<T>
}