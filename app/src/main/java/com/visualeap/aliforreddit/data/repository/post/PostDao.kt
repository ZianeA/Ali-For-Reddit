package com.visualeap.aliforreddit.data.repository.post

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.visualeap.aliforreddit.data.repository.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity

@Dao
interface PostDao {
    //LIMIT :requestedLoadSize OFFSET :position
//    @Query("SELECT * FROM PostEntity p INNER JOIN RedditorEntity r ON p.authorName = r.username INNER JOIN SubredditEntity s ON p.subredditName = s.id")
    @Query("SELECT * FROM PostEntity")
    fun getAll(): DataSource.Factory<Int, PostWithSubredditEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAll(
        subreddits: List<SubredditEntity>,
        posts: List<PostEntity>
    )

    // Since Posts can have the same author/redditor, inserting a PostEntity along with its RedditorEntity can result in a conflict if the redditor already exists in the database.
    // Since Posts can be from the same subreddit, inserting a PostEntity along with its SubredditEntity can result in a conflict if the subreddit is already cached.
    // One solution is to just replace, but this is also problematic see https://stackoverflow.com/questions/45677230/android-room-persistence-library-upsert
    // This is why I opted for the current solution.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(subreddit: SubredditEntity, post: PostEntity)
}