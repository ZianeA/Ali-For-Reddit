package com.visualeap.aliforreddit.core.util

import com.visualeap.aliforreddit.core.util.scheduler.SchedulerProvider
import io.reactivex.Observable

fun <T> Observable<T>.applySchedulers(provider: SchedulerProvider): Observable<T> =
    subscribeOn(provider.io).observeOn(provider.ui)