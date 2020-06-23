package com.visualeap.aliforreddit.data.post

import androidx.room.*
import com.visualeap.aliforreddit.domain.feed.SortType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity WHERE id = :id")
    fun getById(id: String): Observable<PostEntity>

    @Query("SELECT * FROM PostEntity")
    fun getAll(): Observable<List<PostEntity>>

    @Query(
        """
        SELECT * 
        FROM PostEntity
        INNER JOIN PostFeedEntity  
        ON PostEntity.id = postId 
        WHERE feedName = :feed AND sortType = :sortType AND rank >= :offset
        ORDER BY rank
        LIMIT :limit
        """
    )
    fun getByFeed(
        feed: String,
        sortType: SortType,
        offset: Int,
        limit: Int
    ): Flowable<List<PostEntity>>

    // TODO use upsert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(post: PostEntity): Completable

    // TODO use upsert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAll(posts: List<PostEntity>)

    @Update
    fun update(post: PostEntity): Completable

    @Query("DELETE FROM PostEntity")
    fun deleteAll(): Completable
}