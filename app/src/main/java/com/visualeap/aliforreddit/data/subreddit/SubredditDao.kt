package com.visualeap.aliforreddit.data.subreddit

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface SubredditDao {
    // TODO use upsert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAll(subreddits: List<SubredditEntity>): Completable

    // TODO use upsert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(subredditEntity: SubredditEntity): Completable

    @Update
    fun update(subreddit: SubredditEntity): Completable

    @Query("SELECT * FROM SubredditEntity WHERE id IN (:ids)")
    fun getByIds(ids: List<String>): Flowable<List<SubredditEntity>>

    @Query("SELECT s.* FROM SubredditEntity s INNER JOIN PostEntity p ON s.id = p.subredditId WHERE p.id = :postId")
    fun getByPost(postId: String): Observable<SubredditEntity>

    @Query("SELECT * FROM SubredditEntity")
    fun getAll(): Observable<List<SubredditEntity>>
}