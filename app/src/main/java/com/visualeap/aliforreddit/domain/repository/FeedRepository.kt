package com.visualeap.aliforreddit.domain.repository

import io.reactivex.Completable

interface FeedRepository {
    fun addFeed(feedName: String): Completable
}