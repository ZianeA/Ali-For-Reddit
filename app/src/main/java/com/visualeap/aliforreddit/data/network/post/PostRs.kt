package com.visualeap.aliforreddit.data.network.post

import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.data.repository.post.PostRemoteSource
import com.visualeap.aliforreddit.domain.model.Post
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRs @Inject constructor(private val redditService: RedditService) : PostRemoteSource {
    override fun getPosts(subredditName: String): Single<List<Post>> {
        return redditService.getPosts(subredditName)
            .map { postResponse ->
                postResponse.data.postHolders.map { postHolder ->
                    val it = postHolder.post
                    Post(
                        it.id,
                        it.authorName,
                        it.title,
                        it.text,
                        it.score,
                        it.commentCount
                    )
                }
            }
    }
}
