package com.visualeap.aliforreddit.data.repository.post

import com.visualeap.aliforreddit.data.repository.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.repository.Mapper
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.domain.model.Subreddit
import dagger.Reusable
import javax.inject.Inject

@Reusable
class PostWithRedditorMapper @Inject constructor(
    private val redditorEntityMapper: Mapper<RedditorEntity, Redditor>,
    private val subredditEntityMapper: Mapper<SubredditEntity, Subreddit>
) :
    Mapper<PostWithRedditor, Post> {
    override fun mapReverse(model: Post): PostWithRedditor {
        return model.run {
            PostWithRedditor(
                PostEntity(
                    id,
                    author.username,
                    title,
                    text,
                    score,
                    commentCount,
                    subreddit.id,
                    created
                ),
                redditorEntityMapper.mapReverse(author),
                subredditEntityMapper.mapReverse(subreddit)
            )
        }
    }

    override fun map(model: PostWithRedditor): Post {
        return model.run {
            Post(
                postEntity.id,
                redditorEntityMapper.map(redditorEntity),
                postEntity.title,
                postEntity.text,
                postEntity.score,
                postEntity.commentCount,
                subredditEntityMapper.map(subredditEntity),
                postEntity.created
            )
        }
    }
}