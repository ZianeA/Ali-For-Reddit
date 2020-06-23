package com.visualeap.aliforreddit.data.subreddit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubredditEntity(
    @PrimaryKey val id: String,
    val name: String, //TODO add unique constrain
    val iconUrl: String?,
    val primaryColor: String?,
    val keyColor: String?
)