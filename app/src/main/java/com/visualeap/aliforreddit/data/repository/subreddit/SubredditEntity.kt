package com.visualeap.aliforreddit.data.repository.subreddit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubredditEntity(@PrimaryKey val id: String, val name: String)