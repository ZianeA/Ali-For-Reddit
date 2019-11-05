package com.visualeap.aliforreddit.data.repository.post

interface KeyValueStore<T> {
    fun get(key: String, defaultValue: T): T
    fun put(key: String, value: T)
}
