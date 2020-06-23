package com.visualeap.aliforreddit.data.post

import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.data.subreddit.SubredditResponse
import com.visualeap.aliforreddit.domain.post.Post
import com.visualeap.aliforreddit.domain.subreddit.Subreddit
import dagger.Reusable
import javax.inject.Inject

object PostResponseMapper {
    fun map(response: PostResponse): List<Post> {
        return response.data.postHolders.map { postHolder ->
            postHolder.post.run {
                Post(id, authorName, title, text, score, commentCount, subredditId, created)
            }
        }
    }
}