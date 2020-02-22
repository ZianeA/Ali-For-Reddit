package com.visualeap.aliforreddit.data.repository.post

//TODO remove
interface KeyValueStore<T> {
    fun get(key: String, defaultValue: T): T
    fun put(key: String, value: T)
}
