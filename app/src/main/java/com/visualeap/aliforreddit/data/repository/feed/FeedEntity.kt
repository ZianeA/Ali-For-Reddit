package com.visualeap.aliforreddit.data.repository.feed

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.visualeap.aliforreddit.domain.model.feed.SortType

@Entity
data class FeedEntity(@PrimaryKey @ColumnInfo(collate = ColumnInfo.NOCASE) val name: String)