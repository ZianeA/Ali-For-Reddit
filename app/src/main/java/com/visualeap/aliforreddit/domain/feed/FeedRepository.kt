package com.visualeap.aliforreddit.domain.feed

import io.reactivex.Completable

interface FeedRepository {
    fun addFeed(feedName: String): Completable
}