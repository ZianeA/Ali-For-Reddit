package com.visualeap.aliforreddit.data.cache.redditor

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RedditorEntity(@PrimaryKey val username: String) {
}