package com.visualeap.aliforreddit.domain.util

// TODO remove
interface Mapper<T, R> {
    fun map(model: T): R
    fun mapReverse(model: R): T
}