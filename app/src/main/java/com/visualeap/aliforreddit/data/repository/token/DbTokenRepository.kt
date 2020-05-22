package com.visualeap.aliforreddit.data.repository.token

import com.visualeap.aliforreddit.data.cache.RedditDatabase
import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.NOT_SET_ROW_ID
import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.SINGLE_RECORD_ID
import com.visualeap.aliforreddit.data.repository.token.CurrentTokenEntity.*
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import java.lang.IllegalArgumentException
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DbTokenRepository @Inject constructor(
    private val db: RedditDatabase,
    private val tokenDao: TokenDao,
    private val userTokenDao: UserTokenDao,
    private val userlessTokenDao: UserlessTokenDao,
    private val currentTokenDao: CurrentTokenDao
) : TokenRepository {

    /**
     * @param token Given that there will be only one current token record, then the id value is ignored.
     */
    override fun setCurrentToken(token: Token): Completable {
        val tokenType = when (token) {
            is UserToken -> TokenType.USER
            is UserlessToken -> TokenType.USERLESS
            else -> throw IllegalArgumentException("Unknown token type")
        }
        val currentTokenEntity = CurrentTokenEntity(CURRENT_TOKEN_ID, token.id, tokenType)
        return currentTokenDao.setCurrentTokenEntity(currentTokenEntity)
    }

    override fun getCurrentToken(): Maybe<Token> {
        return currentTokenDao.getCurrentTokenEntity()
            .flatMapSingleElement {
                if (it.tokenType == TokenType.USER) {
                    userTokenDao.getUserToken(it.tokenId)
                } else {
                    userlessTokenDao.getUserlessToken()
                }
            }
    }

    override fun addUserToken(token: UserToken): Single<Int> {
        return Single.fromCallable {
            db.runInTransaction<Int> {
                val rowId = tokenDao.addTokenEntity(token.toTokenEntity(0)).toInt()
                userTokenDao.addUserTokenEntity(token.toUserTokenEntity(rowId))
                rowId
            }
        }
    }

    override fun updateUserToken(token: UserToken): Completable {
        return Completable.fromAction {
            db.runInTransaction {
                tokenDao.updateTokenEntity(token.toTokenEntity())
                userTokenDao.updateUserTokenEntity(token.toUserTokenEntity())
            }
        }
    }

    /**
     * @param token Given that there will be only one userless token record, then the id value is ignored.
     */
    override fun setUserlessToken(token: UserlessToken): Single<Int> {
        return userlessTokenDao.getUserlessTokenId()
            .defaultIfEmpty(-1)
            .flatMapSingle { id ->
                if (id == -1) {
                    // add new userless token
                    Single.fromCallable {
                        db.runInTransaction<Int> {
                            val rowId = tokenDao.addTokenEntity(token.toTokenEntity(0)).toInt()
                            userlessTokenDao.addUserlessTokenEntity(
                                token.toUserlessTokenEntity(rowId)
                            )
                            // Return new token id
                            rowId
                        }
                    }
                } else {
                    // update existing userless token
                    Single.fromCallable {
                        db.runInTransaction {
                            tokenDao.updateTokenEntity(token.toTokenEntity(id))
                            userlessTokenDao.updateUserlessTokenEntity(
                                token.toUserlessTokenEntity(id)
                            )
                        }
                        // Return existing token id
                        id
                    }
                }
            }
    }

    private fun UserToken.toTokenEntity(id: Int = this.id) = TokenEntity(id, accessToken, type)
    private fun UserToken.toUserTokenEntity(id: Int = this.id) = UserTokenEntity(id, refreshToken)
    private fun UserlessToken.toTokenEntity(id: Int = this.id) = TokenEntity(id, accessToken, type)
    private fun UserlessToken.toUserlessTokenEntity(id: Int = this.id) =
        UserlessTokenEntity(id, deviceId)

    companion object {
        private const val CURRENT_TOKEN_ID = 1
    }
}