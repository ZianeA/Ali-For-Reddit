package com.visualeap.aliforreddit.data.repository.afterkey

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.visualeap.aliforreddit.data.repository.feed.FeedEntity
import com.visualeap.aliforreddit.data.repository.sort.SortTypeEntity
import com.visualeap.aliforreddit.domain.model.feed.SortType

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