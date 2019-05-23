package com.visualeap.aliforreddit.domain.util

import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

fun <T> Observable<T>.applySchedulers(provider: SchedulerProvider): Observable<T> =
    subscribeOn(provider.worker).observeOn(provider.main)

fun <T> Single<T>.applySchedulers(provider: SchedulerProvider): Single<T> =
    subscribeOn(provider.worker).observeOn(provider.main)

fun Completable.applySchedulers(provider: SchedulerProvider): Completable =
    subscribeOn(provider.worker).observeOn(provider.main)