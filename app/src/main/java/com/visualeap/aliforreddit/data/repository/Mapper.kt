package com.visualeap.aliforreddit.data.repository

interface Mapper<T, R> {
    fun map(model: T): R
    fun mapReverse(model: R): T
}