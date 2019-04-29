package com.visualeap.aliforreddit.presentation.util

import io.reactivex.Observable

fun <T> Observable<T>.applySchedulers(provider: SchedulerProvider): Observable<T> =
    subscribeOn(provider.io).observeOn(provider.ui)