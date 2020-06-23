package com.visualeap.aliforreddit.data.feed

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface FeedDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(feed: FeedEntity): Completable

    @Update
    fun update(feed: FeedEntity): Completable

    @Query("SELECT * FROM FeedEntity")
    fun getAll(): Single<List<FeedEntity>>

    @Query("SELECT * FROM FeedEntity f WHERE f.name=:name")
    fun getByName(name: String): Maybe<FeedEntity>
}