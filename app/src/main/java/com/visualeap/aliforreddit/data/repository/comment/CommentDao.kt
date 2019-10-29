package com.visualeap.aliforreddit.data.repository.comment

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CommentDao {
    @Query("SELECT * FROM CommentEntity c WHERE c.postId=:postId")
    fun getByPostId(postId: String): Single<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(comments: List<CommentEntity>): Completable
}
