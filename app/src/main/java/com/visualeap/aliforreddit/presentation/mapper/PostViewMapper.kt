package com.visualeap.aliforreddit.presentation.mapper

import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.presentation.model.PostView
import com.visualeap.aliforreddit.presentation.model.SubredditView
import com.visualeap.aliforreddit.presentation.util.formatCount
import com.visualeap.aliforreddit.presentation.util.formatTimestamp
import dagger.Reusable
import javax.inject.Inject

@Reusable
class PostViewMapper @Inject constructor(private val subredditViewMapper: Mapper<SubredditView, Subreddit>) :
    Mapper<PostView, Post> {
    override fun map(model: PostView): Post {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapReverse(model: Post): PostView = model.run {
        /*PostView(
            id,
            "$USER_PREFIX$authorName",
            title,
            text,
            formatCount(score),
            formatCount(commentCount),
            subredditViewMapper.mapReverse(subredditId),
            formatTimestamp(created)
        )*/

        TODO("Not implemented")
    }

    companion object{
        private const val USER_PREFIX = "u/"
    }
}