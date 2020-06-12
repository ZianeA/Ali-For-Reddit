package com.visualeap.aliforreddit.data.repository.sort

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.visualeap.aliforreddit.domain.model.feed.SortType

@Entity
data class SortTypeEntity(@PrimaryKey val name: SortType) {
    class SortTypeConverter {
        @TypeConverter
        fun fromSortTypeToString(sortType: SortType) = sortType.name

        @TypeConverter
        fun fromStringToSortType(sortType: String) = SortType.valueOf(sortType)
    }
}