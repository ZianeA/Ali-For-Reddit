package com.visualeap.aliforreddit.data.repository.post

import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.Subreddit
import dagger.Reusable
import javax.inject.Inject

@Reusable
class PostWithSubredditEntityMapper @Inject constructor(
    private val subredditEntityMapper: Mapper<SubredditEntity, Subreddit>
) :
    Mapper<PostWithSubredditEntity, Post> {
    override fun mapReverse(model: Post): PostWithSubredditEntity {
        return model.run {
            PostWithSubredditEntity(
                PostEntity(
                    id,
                    authorName,
                    title,
                    text,
                    score,
                    commentCount,
                    subreddit.name,
                    created
                ),
                subredditEntityMapper.mapReverse(subreddit)
            )
        }
    }

    override fun map(model: PostWithSubredditEntity): Post {
        return model.run {
            Post(
                postEntity.id,
                postEntity.authorName,
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