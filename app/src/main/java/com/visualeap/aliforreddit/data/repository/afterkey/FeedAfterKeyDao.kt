package com.visualeap.aliforreddit.data.repository.afterkey

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.visualeap.aliforreddit.domain.model.feed.SortType
import com.visualeap.aliforreddit.domain.util.Optional
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface FeedAfterKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(afterKey: FeedAfterKeyEntity): Completable

    @Query("SELECT afterKey FROM FeedAfterKeyEntity WHERE feedName = :feed AND sortType = :sortType")
    fun get(feed: String, sortType: SortType): String?
}