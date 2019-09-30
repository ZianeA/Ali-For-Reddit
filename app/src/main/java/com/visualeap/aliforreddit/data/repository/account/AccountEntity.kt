package com.visualeap.aliforreddit.data.repository.account

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.visualeap.aliforreddit.data.cache.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.cache.token.TokenEntity

@Entity(
    foreignKeys = [ForeignKey(
        entity = RedditorEntity::class,
        parentColumns = ["username"],
        childColumns = ["redditorUsername"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = TokenEntity::class,
        parentColumns = ["id"],
        childColumns = ["tokenId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Int, val redditorUsername: String,
    val tokenId: Int
)
