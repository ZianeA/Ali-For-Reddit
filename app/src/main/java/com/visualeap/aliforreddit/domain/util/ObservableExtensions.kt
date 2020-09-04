package com.visualeap.aliforreddit.domain.util

import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.*
import io.reactivex.disposables.Disposable

fun <T> Observable<T>.applySchedulers(provider: SchedulerProvider): Observable<T> =
    subscribeOn(provider.io).observeOn(provider.ui)

fun <T> Flowable<T>.applySchedulers(provider: SchedulerProvider): Flowable<T> =
    subscribeOn(provider.io).observeOn(provider.ui)

fun <T> Single<T>.applySchedulers(provider: SchedulerProvider): Single<T> =
    subscribeOn(provider.io).observeOn(provider.ui)

fun Completable.applySchedulers(provider: SchedulerProvider): Completable =
    subscribeOn(provider.io).observeOn(provider.ui)

fun <T> Maybe<T>.applySchedulers(provider: SchedulerProvider): Maybe<T> =
    subscribeOn(provider.io).observeOn(provider.ui)

fun <T> Observable<T>.autoReplay(connection: (t: Disposable) -> Unit): Observable<T> =
    replay(1).autoConnect(1, connection)

fun <T> Flowable<T>.autoReplay(connection: (t: Disposable) -> Unit): Flowable<T> =
    replay(1).autoConnect(1, connection)