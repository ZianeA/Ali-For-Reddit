package com.visualeap.aliforreddit.data.repository.token

import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.NOT_SET_ROW_ID
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TokenDataRepository @Inject constructor(
    private val remoteSource: TokenRemoteSource,
    private val localSource: TokenLocalSource
    /*@Named("redirectUrl") private val redirectUrl: String,
    @Named("basicAuth") private val basicAuth: String*/
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
            .map {
                UserToken(
                    NOT_SET_ROW_ID,
                    it.accessToken,
                    it.type,
                    it.refreshToken
                        ?: throw IllegalStateException("Refresh token cannot be null or empty")
                )
            }.flatMap {
                val rowId = localSource.saveUserToken(it)
                localSource.getUserToken(rowId)
            }
    }

    override fun getUserLessToken(deviceId: String): Single<UserlessToken> {
        return remoteSource.getUserlessToken(deviceId)
            .map { UserlessToken(NOT_SET_ROW_ID, it.accessToken, it.type, deviceId) }
            .flatMap {
                localSource.saveUserlessToken(it)
                localSource.getUserlessToken()
            }
    }

    override fun refreshUserToken(tokenId: Int, refreshToken: String): Single<UserToken> {
        return remoteSource.refreshUserToken(refreshToken)
            .map {
                UserToken(tokenId, it.accessToken, it.type, refreshToken)
            }.flatMap {
                val rowId = localSource.updateUserToken(it)
                localSource.getUserToken(rowId)
            }
    }

    override fun refreshUserLessToken(deviceId: String): Single<UserlessToken> {
        return remoteSource.getUserlessToken(deviceId)
            .map { UserlessToken(1, it.accessToken, it.type, deviceId) }
            .flatMap {
                localSource.updateUserlessToken(it)
                localSource.getUserlessToken()
            }
    }

    //TODO move this to RemoteDataSource impl
/*    companion object {
        private const val USER_TOKEN_GRANT_TYPE = "authorization_code"
        private const val REFRESH_TOKEN_GRANT_TYPE = "refresh_token"
        private const val USERLESS_TOKEN_GRANT_TYPE =
            "https://oauth.reddit.com/grants/installed_client"
    }*/
}