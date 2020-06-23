package com.visualeap.aliforreddit.data.feed

import com.visualeap.aliforreddit.domain.feed.FeedRepository
import dagger.Reusable
import io.reactivex.Completable
import javax.inject.Inject

@Reusable
class FeedRoomRepository @Inject constructor(private val feedDao: FeedDao) :
    FeedRepository {
    override fun addFeed(feedName: String): Completable {
        return feedDao.add(FeedEntity(feedName))
    }
}
