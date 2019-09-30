package com.visualeap.aliforreddit.data.repository.post

import androidx.room.Embedded
import androidx.room.Relation
import com.visualeap.aliforreddit.data.cache.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity

data class PostWithRedditor(
    @Embedded
    val postEntity: PostEntity,
    @Relation(parentColumn = "authorName", entityColumn = "username")
    val redditorEntity: RedditorEntity,
    @Relation(parentColumn = "subredditId", entityColumn = "id")
    val subredditEntity: SubredditEntity
)