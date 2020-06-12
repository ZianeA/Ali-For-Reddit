package com.visualeap.aliforreddit.data.repository.post.postfeed

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.visualeap.aliforreddit.data.repository.feed.FeedEntity
import com.visualeap.aliforreddit.data.repository.post.PostEntity
import com.visualeap.aliforreddit.data.repository.sort.SortTypeEntity
import com.visualeap.aliforreddit.domain.model.feed.SortType

@Entity(
    primaryKeys = ["postId", "feedName", "sortType"],
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