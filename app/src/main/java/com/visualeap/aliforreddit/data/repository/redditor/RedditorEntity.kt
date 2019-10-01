package com.visualeap.aliforreddit.data.repository.redditor

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RedditorEntity(@PrimaryKey val username: String) {
}