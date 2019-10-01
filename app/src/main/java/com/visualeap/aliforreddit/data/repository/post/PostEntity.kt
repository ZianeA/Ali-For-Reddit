package com.visualeap.aliforreddit.data.repository.post

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.visualeap.aliforreddit.data.repository.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity

//TODO change redditor parentColumns to id
@Entity(
    foreignKeys = [ForeignKey(
        entity = RedditorEntity::class,
        parentColumns = ["username"],
        childColumns = ["authorName"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = SubredditEntity::class,
        parentColumns = ["id"],
        childColumns = ["subredditId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class PostEntity(
    @PrimaryKey val id: String,
    val authorName: String,
    val title: String,
    val text: String,
    val score: Int,
    val commentCount: Int,
    val subredditId: String,
    val created: Long
)