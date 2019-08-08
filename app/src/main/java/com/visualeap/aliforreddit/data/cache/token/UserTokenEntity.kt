package com.visualeap.aliforreddit.data.cache.token

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = TokenEntity::class,
        parentColumns = ["id"],
        childColumns = ["id"],
        onDelete = CASCADE,
        onUpdate = CASCADE
    )]
)
data class UserTokenEntity(@PrimaryKey val id: Int, val refreshToken: String)