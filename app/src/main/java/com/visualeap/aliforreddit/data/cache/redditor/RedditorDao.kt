package com.visualeap.aliforreddit.data.cache.redditor

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface RedditorDao {
    @Query("SELECT * FROM RedditorEntity WHERE username = :username")
    fun getRedditor(username: String): Single<RedditorEntity>

    @Insert
    fun addRedditors(vararg redditors: RedditorEntity): Completable
}