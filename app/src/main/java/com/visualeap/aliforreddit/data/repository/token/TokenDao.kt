package com.visualeap.aliforreddit.data.repository.token

import androidx.room.*

@Dao
interface TokenDao {
    @Insert
    fun addTokenEntity(token: TokenEntity): Long

    @Update
    fun updateTokenEntity(token: TokenEntity)
}