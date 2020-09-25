package com.visualeap.aliforreddit.data.post.postfeed

import androidx.room.Entity
import androidx.room.ForeignKey
import com.visualeap.aliforreddit.data.feed.FeedEntity
import com.visualeap.aliforreddit.data.post.Post
import com.visualeap.aliforreddit.data.sort.SortTypeEntity
import com.visualeap.aliforreddit.domain.feed.SortType

@Entity(
    primaryKeys = ["postId", "feedName", "sortType"],
    foreignKeys = [ForeignKey(
        entity = Post::class,
        parentColumns = ["id"],
        childColumns = ["postId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = FeedEntity::class,
        parentColumns = ["name"],
        childColumns = ["feedName"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = SortTypeEntity::class,
        parentColumns = ["name"],
        childColumns = ["sortType"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class PostFeedEntity(
    val postId: String,
    val feedName: String,
    val sortType: SortType,
    val rank: Int
)