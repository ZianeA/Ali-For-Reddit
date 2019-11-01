package com.visualeap.aliforreddit.data.repository.token

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.NOT_SET_ROW_ID
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
abstract class TokenDao {
    @Insert
    protected abstract fun addUserTokenEntity(token: UserTokenEntity)

    @Insert(onConflict = REPLACE)
    protected abstract fun setUserlessTokenEntity(token: UserlessTokenEntity)

    @Insert
    protected abstract fun addTokenEntity(token: TokenEntity): Long

    @Query("SELECT * FROM UserlessTokenEntity")
    protected abstract fun getUserlessTokenEntity(): UserlessTokenEntity?

    @Transaction
    @Query("SELECT * FROM TokenEntity t WHERE t.id =:id")
    protected abstract fun getTokenWithUserlessTokenEntity(id: Int): TokenWithUserlessTokenEntity

    @Update
    protected abstract fun updateUserTokenEntity(token: UserTokenEntity)

    @Update
    protected abstract fun updateTokenEntity(token: TokenEntity)

    @Transaction
    open fun addUserToken(token: TokenWithUserTokenEntity): Int {
        val rowId = addTokenEntity(token.tokenEntity.copy(id = NOT_SET_ROW_ID)).toInt()
        addUserTokenEntity(token.userTokenEntity.copy(id = rowId))
        return rowId
    }

    @Transaction
    open fun setUserlessToken(token: TokenWithUserlessTokenEntity) {
        val userlessTokenId = getUserlessTokenEntity()?.id
        if (userlessTokenId == null) {
            val rowId = addTokenEntity(token.tokenEntity.copy(id = NOT_SET_ROW_ID)).toInt()
            setUserlessTokenEntity(token.userlessTokenEntity.copy(id = rowId))
        } else {
            updateTokenEntity(token.tokenEntity.copy(id = userlessTokenId))
            setUserlessTokenEntity(token.userlessTokenEntity.copy(id = userlessTokenId))
        }
    }

    @Transaction
    open fun getTokenWithUserlessTokenEntity(): TokenWithUserlessTokenEntity {
        val userlessTokenId = getUserlessTokenEntity()?.id ?: -1
        return getTokenWithUserlessTokenEntity(userlessTokenId)
    }

    @Transaction
    @Query("SELECT * FROM TokenEntity t WHERE t.id =:id")
    abstract fun getTokenWithUserTokenEntity(id: Int): Single<TokenWithUserTokenEntity>

    @Update
    abstract fun updateUserToken(
        tokenEntity: TokenEntity,
        userTokenEntity: UserTokenEntity
    ): Completable

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM CurrentTokenEntity")
    abstract fun getCurrentTokenEntity(): Maybe<CurrentTokenEntity>

    @Insert(onConflict = REPLACE)
    abstract fun setCurrentTokenEntity(currentTokenEntity: CurrentTokenEntity): Completable
}