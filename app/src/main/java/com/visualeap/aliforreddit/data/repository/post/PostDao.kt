package com.visualeap.aliforreddit.data.repository.post

import androidx.room.*
import com.visualeap.aliforreddit.data.repository.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity
import com.visualeap.aliforreddit.domain.model.feed.Feed
import com.visualeap.aliforreddit.domain.model.feed.SortType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface PostDao {
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

    @Query("DELETE FROM PostEntity")
    fun deleteAll(): Completable
}