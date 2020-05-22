package com.visualeap.aliforreddit.data.repository.token

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.NOT_SET_ROW_ID
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface TokenDao {
    @Insert
    fun addTokenEntity(token: TokenEntity): Long

    @Update
    fun updateTokenEntity(token: TokenEntity)
}