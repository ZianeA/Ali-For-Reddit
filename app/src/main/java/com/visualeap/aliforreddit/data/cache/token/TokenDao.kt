package com.visualeap.aliforreddit.data.cache.token

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.NOT_SET_ROW_ID
import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.SINGLE_RECORD_ID
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
abstract class TokenDao {
    @Insert
    protected abstract fun addUserTokenEntity(token: UserTokenEntity)

    @Insert
    protected abstract fun addUserlessTokenEntity(token: UserlessTokenEntity)

    @Insert
    protected abstract fun addTokenEntity(token: TokenEntity): Long

    @Transaction
    open fun addUserToken(tokenEntity: TokenEntity, userTokenEntity: UserTokenEntity): Int {
        val rowId = addTokenEntity(tokenEntity).toInt()
        addUserTokenEntity(userTokenEntity.copy(id = rowId))
        return rowId
    }

    @Query("SELECT * FROM UserlessTokenEntity")
    abstract fun getAllUserlessTokenEntities(): Single<List<UserlessTokenEntity>>

    @Transaction
    open fun addUserlessToken(tokenEntity: TokenEntity, userlessTokenEntity: UserlessTokenEntity) {
        val rowId = addTokenEntity(tokenEntity).toInt()
        addUserlessTokenEntity(userlessTokenEntity.copy(id = rowId))
    }

    @Query("SELECT * FROM UserTokenEntity u WHERE u.id =:id")
    abstract fun getUserTokenEntity(id: Int): Single<UserTokenEntity>

    @Query("SELECT * FROM UserlessTokenEntity u WHERE u.id =:id")
    abstract fun getUserlessTokenEntity(id: Int): Single<UserlessTokenEntity>

    @Query("SELECT * FROM TokenEntity t WHERE t.id =:id")
    abstract fun getTokenEntity(id: Int): Single<TokenEntity>

    @Update
    protected abstract fun updateUserTokenEntity(token: UserTokenEntity)

    @Update
    protected abstract fun updateUserlessTokenEntity(token: UserlessTokenEntity)

    @Update
    protected abstract fun updateTokenEntity(token: TokenEntity)

    @Transaction
    open fun updateUserToken(tokenEntity: TokenEntity, userTokenEntity: UserTokenEntity) {
        updateTokenEntity(tokenEntity)
        updateUserTokenEntity(userTokenEntity)
    }

    @Transaction
    open fun updateUserlessToken(
        tokenEntity: TokenEntity,
        userlessTokenEntity: UserlessTokenEntity
    ) {
        updateTokenEntity(tokenEntity)
        updateUserlessTokenEntity(userlessTokenEntity)
    }

    @Query("SELECT * FROM CurrentTokenEntity c INNER JOIN TokenEntity t ON c.tokenId = t.id")
    abstract fun getCurrentTokenEntity(): Maybe<TokenEntity>

    @Insert(onConflict = REPLACE)
    abstract fun addCurrentTokenEntity(currentTokenEntity: CurrentTokenEntity): Completable
}