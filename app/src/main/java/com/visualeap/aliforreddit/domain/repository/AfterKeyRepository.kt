package com.visualeap.aliforreddit.domain.repository

import com.visualeap.aliforreddit.domain.model.AfterKey
import com.visualeap.aliforreddit.domain.model.feed.SortType
import com.visualeap.aliforreddit.domain.util.Optional
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface AfterKeyRepository {
    fun getAfterKey(feed: String, sortType: SortType): Single<AfterKey>
    fun setAfterKey(feed: String, sortType: SortType, afterKey: AfterKey): Completable
}