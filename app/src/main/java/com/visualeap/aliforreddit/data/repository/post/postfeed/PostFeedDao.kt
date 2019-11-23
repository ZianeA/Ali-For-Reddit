package com.visualeap.aliforreddit.data.repository.post.postfeed

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.visualeap.aliforreddit.data.repository.post.PostWithSubredditEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface PostFeedDao {
    @Insert
    fun add(postFeed: PostFeedEntity): Completable

    @Insert
    fun addAll(postFeed: List<PostFeedEntity>): Completable

    @Query("SELECT * FROM PostFeedEntity")
    fun getAll() : Single<List<PostFeedEntity>>

    @Query("SELECT * FROM PostFeedEntity pf INNER JOIN PostEntity p ON pf.postId=p.id WHERE pf.feedName=:feedName")
    fun getPostsForFeed(feedName: String): DataSource.Factory<Int, PostWithSubredditEntity>
}