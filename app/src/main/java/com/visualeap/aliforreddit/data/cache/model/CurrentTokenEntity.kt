package com.visualeap.aliforreddit.data.cache.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = TokenEntity::class,
        parentColumns = ["id"],
        childColumns = ["tokenId"],
        onDelete = CASCADE,
        onUpdate = CASCADE
    )]
)
data class CurrentTokenEntity(@PrimaryKey(autoGenerate = true) val id: Int, val tokenId: Int)