package com.visualeap.aliforreddit.domain.usecase.base

import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.Single

interface SingleUseCase<Results, Params> {
    fun execute(params: Params): Single<Results>
}