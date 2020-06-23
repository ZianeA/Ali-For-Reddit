package com.visualeap.aliforreddit.domain.post

import com.visualeap.aliforreddit.domain.feed.SortType
import io.reactivex.Completable
import io.reactivex.Single

interface AfterKeyRepository {
    fun getAfterKey(feed: String, sortType: SortType): Single<AfterKey>
    fun setAfterKey(feed: String, sortType: SortType, afterKey: AfterKey): Completable
}