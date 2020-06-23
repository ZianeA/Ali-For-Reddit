package com.visualeap.aliforreddit.data.token

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TokenEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val accessToken: String,
    val type: String
)