package com.visualeap.aliforreddit.domain.usecase.base

import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.Observable

interface ObservableUseCase<Results, Params> {
    fun execute(params: Params): Observable<Results>
}