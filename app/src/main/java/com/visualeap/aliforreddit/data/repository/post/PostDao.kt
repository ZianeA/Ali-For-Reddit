package com.visualeap.aliforreddit.data.repository.post

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.visualeap.aliforreddit.data.cache.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity

@Dao
interface PostDao {
    //LIMIT :requestedLoadSize OFFSET :position
//    @Query("SELECT * FROM PostEntity p INNER JOIN RedditorEntity r ON p.authorName = r.username INNER JOIN SubredditEntity s ON p.subredditId = s.id")
    @Query("SELECT * FROM PostEntity")
    fun getAll(): DataSource.Factory<Int, PostWithRedditor>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAll(
        redditors: List<RedditorEntity>,
        subreddits: List<SubredditEntity>,
        posts: List<PostEntity>
    )

    // Since Posts can have the same author/redditor, inserting a PostEntity along with its RedditorEntity can result in a conflict if the redditor already exists in the database.
    // Since Posts can be from the same subreddit, inserting a PostEntity along with its SubredditEntity can result in a conflict if the subreddit is already cached.
    // One solution is to just replace, but this is also problematic see https://stackoverflow.com/questions/45677230/android-room-persistence-library-upsert
    // This is why I opted for the current solution.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(redditor: RedditorEntity, subreddit: SubredditEntity, post: PostEntity)

//    @Insert
//    protected abstract fun addRedditorEntities(vararg redditors: Redditor)
//
//    @Insert
//    protected abstract fun addSubredditEntities(vararg subreddits: Subreddit)
//
//    @Transaction
//    fun addAll(
//        posts: List<PostEntity>,
//        redditors: List<RedditorEntity>,
//        subreddits: List<Subreddit>
//    ) {
//        AddPostEntities(*posts.toTypedArray())
//    }
}