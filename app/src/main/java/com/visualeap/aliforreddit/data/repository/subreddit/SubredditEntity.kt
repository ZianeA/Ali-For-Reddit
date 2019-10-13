package com.visualeap.aliforreddit.data.repository.subreddit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubredditEntity(
    @PrimaryKey val name: String, val id: String,
    val iconUrl: String?,
    val primaryColor: String?,
    val keyColor: String?
)