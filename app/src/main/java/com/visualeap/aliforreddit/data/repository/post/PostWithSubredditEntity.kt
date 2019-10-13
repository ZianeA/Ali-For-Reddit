package com.visualeap.aliforreddit.data.repository.post

import androidx.room.Embedded
import androidx.room.Relation
import com.visualeap.aliforreddit.data.repository.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity

data class PostWithSubredditEntity(
    @Embedded
    val postEntity: PostEntity,
    @Relation(parentColumn = "subredditName", entityColumn = "name")
    val subredditEntity: SubredditEntity
)