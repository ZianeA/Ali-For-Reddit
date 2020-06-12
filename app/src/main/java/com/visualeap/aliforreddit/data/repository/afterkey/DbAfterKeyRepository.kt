package com.visualeap.aliforreddit.data.repository.afterkey

import com.visualeap.aliforreddit.domain.model.AfterKey
import com.visualeap.aliforreddit.domain.model.feed.SortType
import com.visualeap.aliforreddit.domain.repository.AfterKeyRepository
import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Reusable
class DbAfterKeyRepository @Inject constructor(private val feedAfterKeyDao: FeedAfterKeyDao) : AfterKeyRepository {
    override fun setAfterKey(feed: String, sortType: SortType, afterKey: AfterKey): Completable {
        return Single.fromCallable {
            when (afterKey) {
                is AfterKey.Next -> afterKey.value
                AfterKey.End -> ""
                AfterKey.Empty -> throw IllegalArgumentException("Can't set after key to empty.")
            }
        }
            .flatMapCompletable { key -> feedAfterKeyDao.add(FeedAfterKeyEntity(feed, sortType, key)) }
    }

    override fun getAfterKey(feed: String, sortType: SortType): Single<AfterKey> {
        return Single.fromCallable<AfterKey> {
            val key = feedAfterKeyDao.get("FAKE_FEED", SortType.Best)

            when {
                key == null -> AfterKey.Empty
                key.isBlank() -> AfterKey.End
                else -> AfterKey.Next(key)
            }
        }
    }
}