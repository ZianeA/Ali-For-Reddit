package com.visualeap.aliforreddit.data.redditor

import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.domain.redditor.Redditor
import com.visualeap.aliforreddit.domain.redditor.RedditorRepository
import com.visualeap.aliforreddit.domain.util.Mapper
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditorRoomRepository @Inject constructor(
    private val redditService: RedditService,
    private val redditorResponseMapper: Mapper<RedditorResponse, Redditor>
) :
    RedditorRepository {
    override fun getCurrentRedditor(): Single<Redditor> {
        return redditService.getCurrentRedditor()
            .map(redditorResponseMapper::map)
    }
}
