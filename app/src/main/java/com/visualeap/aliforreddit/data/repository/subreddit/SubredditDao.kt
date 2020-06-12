package com.visualeap.aliforreddit.data.repository.subreddit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.visualeap.aliforreddit.domain.model.Subreddit
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface SubredditDao {
    // TODO use upsert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAll(subreddits: List<SubredditEntity>): Completable

    // TODO use upsert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(subredditEntity: SubredditEntity): Completable

    @Query("SELECT * FROM SubredditEntity WHERE id IN (:ids)")
    fun getByIds(ids: List<String>): Flowable<List<SubredditEntity>>

    @Query("SELECT * FROM SubredditEntity")
    fun getAll(): Observable<List<SubredditEntity>>
}