package com.visualeap.aliforreddit.data.post

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.visualeap.aliforreddit.data.subreddit.Subreddit

@Entity(
    foreignKeys = [ForeignKey(
        entity = Subreddit::class,
        parentColumns = ["id"],
        childColumns = ["subredditId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class Post(
    @PrimaryKey val id: String,
    val authorName: String,
    val title: String,
    val text: String,
    val url: String,
    val score: Int,
    val commentCount: Int,
    val subredditId: String,
    val created: Long
)