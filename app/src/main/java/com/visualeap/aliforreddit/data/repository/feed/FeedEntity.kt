package com.visualeap.aliforreddit.data.repository.feed

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.visualeap.aliforreddit.domain.model.Feed.FeedType
import com.visualeap.aliforreddit.domain.model.Feed.SortBy

@Entity
data class FeedEntity(
    @PrimaryKey @ColumnInfo(collate = ColumnInfo.NOCASE) val name: String,
    val sortBy: SortBy,
    val afterKey: String?,
    val type: FeedType
) {
    class SortByTypeConverter {
        @TypeConverter
        fun fromSortByToString(sortBy: SortBy) = sortBy.name

        @TypeConverter
        fun fromStringToSortBy(sortBy: String) = SortBy.valueOf(sortBy)
    }

    class FeedTypeConverter {
        @TypeConverter
        fun fromFeedTypeToString(feedType: FeedType) = feedType.name

        @TypeConverter
        fun fromStringToFeedType(feedType: String) = FeedType.valueOf(feedType)
    }
}