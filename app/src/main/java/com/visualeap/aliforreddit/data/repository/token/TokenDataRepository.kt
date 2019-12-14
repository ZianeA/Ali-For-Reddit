package com.visualeap.aliforreddit.data.repository.token

import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.NOT_SET_ROW_ID
import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.SINGLE_RECORD_ID
import com.visualeap.aliforreddit.data.repository.token.CurrentTokenEntity.*
import com.visualeap.aliforreddit.data.network.auth.AuthService
import com.visualeap.aliforreddit.domain.util.Mapper
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
    private val remoteSource: AuthService,
    private val localSource: TokenDao,
    private val tokenWithUserTokenEntityMapper: Mapper<TokenWithUserTokenEntity, UserToken>,
    private val tokenWithUserlessTokenEntityMapper: Mapper<TokenWithUserlessTokenEntity, UserlessToken>,
    @Named("redirectUrl") private val redirectUrl: String,
    @Named("basicAuth") private val basicAuth: String
) :
    TokenRepository {
    override fun setCurrentToken(token: Token): Completable {
        val tokenType = when (token) {
            is UserToken -> TokenType.USER
            is UserlessToken -> TokenType.USERLESS
            else -> throw IllegalArgumentException("Unknown token type")
        }
        val currentTokenEntity =
            CurrentTokenEntity(
                SINGLE_RECORD_ID,
                token.id,
                tokenType
            )
        return localSource.setCurrentTokenEntity(currentTokenEntity)
    }

    override fun getCurrentToken(): Maybe<Token> {
        return localSource.getCurrentTokenEntity()
            .flatMapSingleElement {
                if (it.tokenType == TokenType.USER) {
                    localSource.getTokenWithUserTokenEntity(it.tokenId)
                        .map(tokenWithUserTokenEntityMapper::map)
                } else {
                    Single.fromCallable { localSource.getTokenWithUserlessTokenEntity() }
                        .map(tokenWithUserlessTokenEntityMapper::map)
                }
            }
    }

    /**
     * Always @return a fresh user token.
     * Use [getCurrentToken] if you which to get the cached token instead.
     */
    override fun getUserToken(code: String): Single<UserToken> {
        return remoteSource.getUserToken(USER_TOKEN_GRANT_TYPE, code, redirectUrl, basicAuth)
            .map(::tokenResponseToUserToken)
            .map(tokenWithUserTokenEntityMapper::mapReverse)
            .flatMap { Single.fromCallable { localSource.addUserToken(it) } }
            .flatMap { rowId -> localSource.getTokenWithUserTokenEntity(rowId) }
            .map(tokenWithUserTokenEntityMapper::map)
    }

    override fun getUserlessToken(deviceId: String): Single<UserlessToken> {
        return remoteSource.getUserlessToken(USERLESS_TOKEN_GRANT_TYPE, deviceId, basicAuth)
            .map { tokenResponseToUserlessToken(it, deviceId) }
            .map(tokenWithUserlessTokenEntityMapper::mapReverse)
            .flatMap { Single.fromCallable { localSource.setUserlessToken(it) } }
            .flatMap { Single.fromCallable { localSource.getTokenWithUserlessTokenEntity() } }
            .map(tokenWithUserlessTokenEntityMapper::map)
    }

    override fun refreshUserToken(tokenId: Int, refreshToken: String): Single<UserToken> {
        return remoteSource.refreshUserToken(REFRESH_TOKEN_GRANT_TYPE, refreshToken, basicAuth)
            .map(::tokenResponseToUserToken)
            .map(tokenWithUserTokenEntityMapper::mapReverse)
            .flatMap {
                localSource.updateUserToken(it.tokenEntity, it.userTokenEntity)
                    .andThen(localSource.getTokenWithUserTokenEntity(it.tokenEntity.id))
            }.map(tokenWithUserTokenEntityMapper::map)
    }

    override fun refreshUserlessToken(deviceId: String) = getUserlessToken(deviceId)

    private fun tokenResponseToUserToken(tokenRespnose: TokenResponse): UserToken =
        tokenRespnose.run {
            UserToken(NOT_SET_ROW_ID, accessToken, type, refreshToken!!)
        }

    private fun tokenResponseToUserlessToken(
        tokenResponse: TokenResponse,
        deviceId: String
    ): UserlessToken = tokenResponse.run {
        UserlessToken(NOT_SET_ROW_ID, accessToken, type, deviceId)
    }

    companion object {
        private const val USER_TOKEN_GRANT_TYPE = "authorization_code"
        private const val REFRESH_TOKEN_GRANT_TYPE = "refresh_token"
        private const val USERLESS_TOKEN_GRANT_TYPE =
            "https://oauth.reddit.com/grants/installed_client"
    }
}