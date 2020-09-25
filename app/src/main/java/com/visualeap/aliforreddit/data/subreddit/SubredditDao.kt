package com.visualeap.aliforreddit.data.subreddit

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface SubredditDao {
    // TODO use upsert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAll(subreddits: List<Subreddit>): Completable

    // TODO use upsert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(subreddit: Subreddit): Completable

    @Update
    fun update(subreddit: Subreddit): Completable

    @Query("SELECT * FROM Subreddit WHERE id IN (:ids)")
    fun getByIds(ids: List<String>): Flowable<List<Subreddit>>

    @Query("SELECT s.* FROM Subreddit s INNER JOIN Post p ON s.id = p.subredditId WHERE p.id = :postId")
    fun getByPost(postId: String): Observable<Subreddit>

    @Query("SELECT * FROM Subreddit")
    fun getAll(): Observable<List<Subreddit>>
}