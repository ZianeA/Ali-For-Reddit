package com.visualeap.aliforreddit.data.repository.token

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface CurrentTokenDao {
    @Query("SELECT * FROM CurrentTokenEntity")
    fun getCurrentTokenEntity(): Maybe<CurrentTokenEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setCurrentTokenEntity(currentTokenEntity: CurrentTokenEntity): Completable
}