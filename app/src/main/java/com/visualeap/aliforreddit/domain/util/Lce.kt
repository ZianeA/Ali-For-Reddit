package com.visualeap.aliforreddit.domain.util

import io.reactivex.Observable
import kotlin.reflect.KClass

sealed class Lce<T>() {
    class Loading<T> : Lce<T>()
    data class Content<T>(val data: T) : Lce<T>()
    data class Error<T>(val error: Throwable) : Lce<T>()
}

fun <T> Observable<T>.toLce(): Observable<Lce<T>> {
    return this.map { Lce.Content<T>(it) as Lce<T> }
        .onErrorReturn { Lce.Error<T>(it) }
}