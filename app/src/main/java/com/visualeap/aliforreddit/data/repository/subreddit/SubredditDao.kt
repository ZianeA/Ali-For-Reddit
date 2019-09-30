package com.visualeap.aliforreddit.data.repository.subreddit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.visualeap.aliforreddit.domain.model.Subreddit
import io.reactivex.Completable

@Dao
interface SubredditDao {
    @Insert
    fun addAll(vararg subreddits: SubredditEntity): Completable
}