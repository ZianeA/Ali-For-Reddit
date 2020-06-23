package com.visualeap.aliforreddit.data.token

import androidx.room.*

@Dao
interface TokenDao {
    @Insert
    fun addTokenEntity(token: TokenEntity): Long

    @Update
    fun updateTokenEntity(token: TokenEntity)
}