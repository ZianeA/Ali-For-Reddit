package com.visualeap.aliforreddit.data.afterkey

import com.visualeap.aliforreddit.domain.post.AfterKey
import com.visualeap.aliforreddit.domain.feed.SortType
import com.visualeap.aliforreddit.domain.post.AfterKeyRepository
import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@Reusable
class AfterKeyRoomRepository @Inject constructor(private val feedAfterKeyDao: FeedAfterKeyDao) :
    AfterKeyRepository {
    companion object {
        private const val END = "End"
    }

    override fun setAfterKey(feed: String, sortType: SortType, afterKey: AfterKey): Completable {
        return Single.fromCallable {
            when (afterKey) {
                is AfterKey.Next -> afterKey.value
                AfterKey.End -> END
                AfterKey.Empty -> throw IllegalArgumentException("Can't set after key to empty.")
            }
        }
            .flatMapCompletable { key ->
                feedAfterKeyDao.add(FeedAfterKeyEntity(feed, sortType, key))
            }
    }

    override fun getAfterKey(feed: String, sortType: SortType): Single<AfterKey> {
        return Single.fromCallable<AfterKey> {
            when (val key = feedAfterKeyDao.get(feed, sortType)) {
                null -> AfterKey.Empty
                END -> AfterKey.End
                else -> AfterKey.Next(key)
            }
        }
    }
}