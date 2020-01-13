package com.visualeap.aliforreddit.data.repository.feed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface FeedDao {
    @Insert
    fun addAll(feeds: List<FeedEntity>) : Completable

    @Insert
    fun add(feed: FeedEntity): Completable

    @Update
    fun update(feed: FeedEntity): Completable

    @Query("SELECT * FROM FeedEntity")
    fun getAll(): Single<List<FeedEntity>>

    @Query("SELECT * FROM FeedEntity f WHERE f.name=:name")
    fun getByName(name: String): Maybe<FeedEntity>
}