package com.visualeap.aliforreddit.data.afterkey

import androidx.room.Entity
import androidx.room.ForeignKey
import com.visualeap.aliforreddit.data.feed.FeedEntity
import com.visualeap.aliforreddit.data.sort.SortTypeEntity
import com.visualeap.aliforreddit.domain.feed.SortType

@Entity(
    primaryKeys = ["feedName", "sortType"],
    foreignKeys = [ForeignKey(
        entity = FeedEntity::class,
        parentColumns = ["name"],
        childColumns = ["feedName"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = SortTypeEntity::class,
        parentColumns = ["name"],
        childColumns = ["sortType"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class FeedAfterKeyEntity(
    val feedName: String,
    val sortType: SortType,
    val afterKey: String
)