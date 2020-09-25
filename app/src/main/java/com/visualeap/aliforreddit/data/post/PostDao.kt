package com.visualeap.aliforreddit.data.post

import androidx.room.*
import com.visualeap.aliforreddit.domain.feed.SortType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface PostDao {
    @Query("SELECT * FROM Post WHERE id = :id")
    fun getById(id: String): Observable<Post>

    @Query("SELECT * FROM Post")
    fun getAll(): Observable<List<Post>>

    @Query(
        """
        SELECT * 
        FROM Post
        INNER JOIN PostFeedEntity  
        ON Post.id = postId 
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
    ): Flowable<List<Post>>

    // TODO use upsert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(post: Post): Completable

    // TODO use upsert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAll(posts: List<Post>)

    @Update
    fun update(post: Post): Completable

    @Query("DELETE FROM Post")
    fun deleteAll(): Completable
}