package com.visualeap.aliforreddit.data.repository.feed

import com.visualeap.aliforreddit.domain.repository.FeedRepository
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import dagger.Reusable
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton

@Reusable
class DbFeedRepository @Inject constructor(private val feedDao: FeedDao) : FeedRepository {
    override fun addFeed(feedName: String): Completable {
        return feedDao.add(FeedEntity(feedName))
    }
}
