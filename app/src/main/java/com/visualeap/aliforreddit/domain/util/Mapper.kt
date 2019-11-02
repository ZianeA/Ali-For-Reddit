package com.visualeap.aliforreddit.domain.util

interface Mapper<T, R> {
    fun map(model: T): R
    fun mapReverse(model: R): T
}