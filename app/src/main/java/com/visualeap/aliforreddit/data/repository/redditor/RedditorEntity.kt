package com.visualeap.aliforreddit.data.repository.redditor

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RedditorEntity(
    @PrimaryKey val id: String,
    val username: String, //TODO add unique constrain
    val creationDate: Long,
    val linkKarma: Int,
    val commentKarma: Int,
    val iconUrl: String,
    val coins: Int
)