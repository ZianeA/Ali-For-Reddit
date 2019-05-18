package com.visualeap.aliforreddit.domain.usecase.base

import com.visualeap.aliforreddit.core.util.applySchedulers
import com.visualeap.aliforreddit.core.util.scheduler.SchedulerProvider
import io.reactivex.Observable

abstract class ObservableUseCase<Results, Params>(private val schedulerProvider: SchedulerProvider) {

    fun execute(params: Params): Observable<Results> {
        return createObservable(params).applySchedulers(schedulerProvider)
    }

    protected abstract fun createObservable(params: Params): Observable<Results>
}