package com.visualeap.aliforreddit.data.cache.token

import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.NOT_SET_ROW_ID
import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.SINGLE_RECORD_ID
import com.visualeap.aliforreddit.data.repository.token.TokenLocalSource
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenLs @Inject constructor(private val tokenDao: TokenDao) : TokenLocalSource {
    override fun addUserToken(userToken: UserToken): Single<Int> {
        return Single.fromCallable {
            val holder = userToken.map(NOT_SET_ROW_ID)
            tokenDao.addUserToken(holder.tokenEntity, holder.userTokenEntity)
        }
    }

    override fun getUserToken(id: Int): Single<UserToken> {
        return tokenDao.getTokenEntity(id)
            .flatMap { tokenEntity ->
                tokenDao.getUserTokenEntity(id)
                    .map { entityToModel(tokenEntity, it) }
            }
    }

    override fun setUserlessToken(userlessToken: UserlessToken): Completable {
        return tokenDao.getAllUserlessTokenEntities()
            .flatMapCompletable {
                if (it.isEmpty()) {
                    Completable.fromCallable {
                        val holder = userlessToken.map(NOT_SET_ROW_ID)
                        tokenDao.addUserlessToken(
                            holder.tokenEntity,
                            holder.userlessTokenEntity
                        )
                    }
                } else {
                    Completable.fromCallable {
                        val holder = userlessToken.map(it.single().id)
                        tokenDao.updateUserlessToken(
                            holder.tokenEntity,
                            holder.userlessTokenEntity
                        )
                    }
                }
            }
    }

    override fun getUserlessToken(): Single<UserlessToken> {
        return tokenDao.getAllUserlessTokenEntities()
            .map { it.single() }
            .flatMap { userlessTokenEntity ->
                tokenDao.getTokenEntity(userlessTokenEntity.id)
                    .map { entityToModel(it, userlessTokenEntity) }
            }
    }

    override fun updateUserToken(userToken: UserToken): Completable {
        return Completable.fromCallable {
            val holder = userToken.map()
            tokenDao.updateUserToken(holder.tokenEntity, holder.userTokenEntity)
        }
    }

    override fun getCurrentToken(): Maybe<Token> {
        return tokenDao.getCurrentTokenEntity()
            .flatMapSingleElement { tokenEntity ->
                tokenDao.getUserTokenEntity(tokenEntity.id)
                    .map { entityToModel(tokenEntity, it) as Token }
                    .onErrorResumeNext { _ ->
                        tokenDao.getUserlessTokenEntity(tokenEntity.id)
                            .map { entityToModel(tokenEntity, it) }
                    }
            }
    }

    override fun setCurrentToken(token: Token): Completable {
        return tokenDao.addCurrentTokenEntity(CurrentTokenEntity(SINGLE_RECORD_ID, token.id))
    }

    //region mappers
    private fun UserToken.map(id: Int = this.id): UserTokenEntitiesHolder =
        UserTokenEntitiesHolder(
            TokenEntity(id, accessToken, type),
            UserTokenEntity(id, refreshToken)
        )

    private fun entityToModel(
        tokenEntity: TokenEntity,
        userTokenEntity: UserTokenEntity
    ): UserToken =
        UserToken(
            tokenEntity.id,
            tokenEntity.accessToken,
            tokenEntity.type,
            userTokenEntity.refreshToken
        )

    class UserTokenEntitiesHolder(
        val tokenEntity: TokenEntity,
        val userTokenEntity: UserTokenEntity
    )

    private fun UserlessToken.map(id: Int = this.id): UserlessTokenEntitiesHolder =
        UserlessTokenEntitiesHolder(
            TokenEntity(id, accessToken, type),
            UserlessTokenEntity(id, deviceId)
        )

    private fun entityToModel(
        tokenEntity: TokenEntity,
        userlessTokenEntity: UserlessTokenEntity
    ): UserlessToken = UserlessToken(
        tokenEntity.id,
        tokenEntity.accessToken,
        tokenEntity.type,
        userlessTokenEntity.deviceId
    )

    class UserlessTokenEntitiesHolder(
        val tokenEntity: TokenEntity,
        val userlessTokenEntity: UserlessTokenEntity
    )

    //endregion
}