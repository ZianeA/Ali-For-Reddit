package com.visualeap.aliforreddit.data.comment

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface CommentDao {
    @Query("SELECT * FROM Comment c WHERE c.postId=:postId")
    fun getByPostId(postId: String): Observable<List<CommentEntity>>

    @Query("SELECT * FROM Comment")
    fun getAll(): Observable<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(comments: List<CommentEntity>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(comment: CommentEntity): Completable

    @Query("DELETE FROM Comment")
    fun deleteAll(): Completable
}
