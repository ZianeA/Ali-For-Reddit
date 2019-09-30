package com.visualeap.aliforreddit.data.repository.post

import com.visualeap.aliforreddit.data.repository.Mapper
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.domain.model.Subreddit
import dagger.Reusable
import javax.inject.Inject

@Reusable
class PostResponseMapper @Inject constructor() :
    Mapper<PostResponse, List<@JvmSuppressWildcards Post>> {
    override fun mapReverse(model: List<Post>): PostResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun map(model: PostResponse): List<Post> {
        return model.data.postHolders.map { postHolder ->
            //TODO fix redditor and subreddit
            postHolder.post.run {
                Post(
                    id,
                    Redditor(authorName),
                    title,
                    text,
                    score,
                    commentCount,
                    Subreddit(subredditId, subredditName)
                )
            }
        }
    }
}