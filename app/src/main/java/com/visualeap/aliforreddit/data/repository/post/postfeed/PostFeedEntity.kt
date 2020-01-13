package com.visualeap.aliforreddit.data.repository.post.postfeed

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.visualeap.aliforreddit.data.repository.feed.FeedEntity
import com.visualeap.aliforreddit.data.repository.post.PostEntity

@Entity(
    primaryKeys = ["postId", "feedName"],
    foreignKeys = [ForeignKey(
        entity = PostEntity::class,
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
    )]
)
data class PostFeedEntity(
    val postId: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val feedName: String
)