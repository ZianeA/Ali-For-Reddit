package com.visualeap.aliforreddit.data.token

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface CurrentTokenDao {
    @Query("SELECT * FROM CurrentTokenEntity")
    fun getCurrentTokenEntity(): Single<CurrentTokenEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setCurrentTokenEntity(currentTokenEntity: CurrentTokenEntity): Completable
}