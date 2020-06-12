package com.visualeap.aliforreddit.data.repository.post.postfeed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.visualeap.aliforreddit.domain.model.feed.SortType
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface PostFeedDao {
    @Insert
    fun add(postFeed: PostFeedEntity): Completable

    @Insert
    fun addAll(postFeed: List<PostFeedEntity>)

    @Query("SELECT * FROM PostFeedEntity")
    fun getAll(): Single<List<PostFeedEntity>>

    @Query("SELECT COUNT(*) FROM PostFeedEntity WHERE feedName = :feed AND sortType = :sortType")
    fun countPostsByFeed(feed: String, sortType: SortType): Int
}