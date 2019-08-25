package com.visualeap.aliforreddit.data.repository.token

import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.NOT_SET_ROW_ID
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenDataRepository @Inject constructor(
    private val remoteSource: TokenRemoteSource,
    private val localSource: TokenLocalSource
) :
    TokenRepository {
    override fun setCurrentToken(token: Token): Completable {
        return localSource.setCurrentToken(token)
    }

    override fun getCurrentToken(): Maybe<Token> {
        return localSource.getCurrentToken()
    }

    override fun getUserToken(code: String): Single<UserToken> {
        return remoteSource.getUserToken(code)
            .flatMap {
                localSource.addUserToken(it)
                    .flatMap { rowId -> localSource.getUserToken(rowId) }
            }
    }

    override fun getUserlessToken(deviceId: String): Single<UserlessToken> {
        return remoteSource.getUserlessToken(deviceId)
            .flatMap {
                localSource.setUserlessToken(it)
                localSource.getUserlessToken()
            }
    }

    override fun refreshUserToken(tokenId: Int, refreshToken: String): Single<UserToken> {
        return remoteSource.refreshUserToken(refreshToken)
            .map {
                UserToken(tokenId, it.accessToken, it.type, refreshToken)
            }.flatMap {
                localSource.updateUserToken(it)
                localSource.getUserToken(it.id)
            }
    }

    override fun refreshUserlessToken(deviceId: String): Single<UserlessToken> {
        return remoteSource.getUserlessToken(deviceId)
            .flatMap {
                localSource.setUserlessToken(it)
                localSource.getUserlessToken()
            }
    }
}