package com.visualeap.aliforreddit.data.sort

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.visualeap.aliforreddit.domain.feed.SortType

@Entity
data class SortTypeEntity(@PrimaryKey val name: SortType) {
    class SortTypeConverter {
        @TypeConverter
        fun fromSortTypeToString(sortType: SortType) = sortType.name

        @TypeConverter
        fun fromStringToSortType(sortType: String) = SortType.valueOf(sortType)
    }
}