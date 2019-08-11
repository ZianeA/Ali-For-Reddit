package com.visualeap.aliforreddit.data.cache.token

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.visualeap.aliforreddit.data.cache.RedditDatabase
import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.NOT_SET_ROW_ID
import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.SINGLE_RECORD_ID
import com.visualeap.aliforreddit.data.repository.token.TokenLocalSource
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import java.lang.IllegalStateException
import java.lang.UnsupportedOperationException

@Dao
abstract class TokenDao : TokenLocalSource {
    @Insert
    protected abstract fun saveUserTokenEntity(token: UserTokenEntity)

    @Insert
    protected abstract fun saveUserlessTokenEntity(token: UserlessTokenEntity)

    @Insert
    protected abstract fun saveTokenEntity(token: TokenEntity): Long

    @Transaction
    override fun saveUserToken(userToken: UserToken): Int {
        userToken.run {
            val rowId = saveTokenEntity(TokenEntity(NOT_SET_ROW_ID, accessToken, type)).toInt()
            saveUserTokenEntity(UserTokenEntity(rowId, refreshToken))
            return rowId
        }
    }

    @Query("SELECT * FROM UserlessTokenEntity")
    protected abstract fun getAllUserlessTokenEntities(): List<UserlessTokenEntity>

    @Transaction
    override fun saveUserlessToken(userlessToken: UserlessToken): Int {
        if (getAllUserlessTokenEntities().isNotEmpty()) throw UnsupportedOperationException(
            "Only one user-less token can be created/saved per app. " +
                    "Use updateUserlessToken if you wish to update the stored user-less token"
        )

        userlessToken.run {
            val rowId = saveTokenEntity(TokenEntity(NOT_SET_ROW_ID, accessToken, type)).toInt()
            saveUserlessTokenEntity(UserlessTokenEntity(rowId, deviceId))
            return rowId
        }
    }

    @Query("SELECT * FROM UserTokenEntity ut INNER JOIN TokenEntity t ON t.id = ut.id WHERE ut.id =:id")
    abstract override fun getUserToken(id: Int): Single<UserToken>

    @Query("SELECT * FROM UserlessTokenEntity ult INNER JOIN TokenEntity t ON t.id = ult.id")
    protected abstract fun getAllUserlessTokens(): Single<List<UserlessToken>>

    override fun getUserlessToken(): Single<UserlessToken> =
        getAllUserlessTokens().map {
            if (it.size > 1) throw IllegalStateException("There should only be a single user-less token")
            it.single()
        }

    @Update
    protected abstract fun updateUserTokenEntity(token: UserTokenEntity)

    @Update
    protected abstract fun updateUserlessTokenEntity(token: UserlessTokenEntity)

    @Update
    protected abstract fun updateTokenEntity(token: TokenEntity)

    @Transaction
    override fun updateUserToken(userToken: UserToken) {
        userToken.run {
            updateTokenEntity(TokenEntity(id, accessToken, type))
            updateUserTokenEntity(UserTokenEntity(id, refreshToken))
        }
    }

    @Transaction
    override fun updateUserlessToken(userlessToken: UserlessToken) {
        userlessToken.run {
            updateTokenEntity(TokenEntity(id, accessToken, type))
            updateUserlessTokenEntity(UserlessTokenEntity(id, deviceId))
        }
    }

    @Query("SELECT * FROM CurrentTokenEntity ct INNER JOIN TokenEntity t ON ct.tokenId = t.id INNER JOIN UserTokenEntity ut ON t.id = ut.id")
    abstract fun getCurrentUserToken(): Maybe<UserToken>

    @Query("SELECT * FROM CurrentTokenEntity ct INNER JOIN TokenEntity t ON ct.tokenId = t.id INNER JOIN UserlessTokenEntity ult ON t.id = ult.id")
    abstract fun getCurrentUserlessToken(): Maybe<UserlessToken>

    override fun getCurrentToken(): Maybe<Token> {
        return Maybe.concat(getCurrentUserlessToken(), getCurrentUserToken())
            .singleElement()
    }

    @Insert(onConflict = REPLACE)
    abstract fun insertCurrentTokenEntity(currentTokenEntity: CurrentTokenEntity): Completable

    override fun setCurrentToken(token: Token): Completable {
        return insertCurrentTokenEntity(CurrentTokenEntity(SINGLE_RECORD_ID, token.id))
    }
}