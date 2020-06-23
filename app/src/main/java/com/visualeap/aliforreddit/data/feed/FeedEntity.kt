package com.visualeap.aliforreddit.data.feed

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FeedEntity(@PrimaryKey @ColumnInfo(collate = ColumnInfo.NOCASE) val name: String)