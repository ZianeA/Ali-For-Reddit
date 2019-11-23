package com.visualeap.aliforreddit.data.repository.feed

import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(
    private val feedDao: FeedDao,
    private val schedulerProvider: SchedulerProvider
) {
    /*fun addDefaultFeeds(): Completable {
        return feedDao.getAll()
            .flatMapCompletable {
                if (it.isEmpty()) feedDao.addAll(DEFAULT_FEEDS) else Completable.complete()
            }
    }

    companion object {
        private val DEFAULT_FEEDS = listOf(
            FeedEntity("HOME"),
            FeedEntity("POPULAR"),
            FeedEntity("ALL")
        )
    }*/
}
