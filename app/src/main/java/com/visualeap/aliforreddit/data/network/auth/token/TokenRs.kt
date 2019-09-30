package com.visualeap.aliforreddit.data.network.auth.token

import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.NOT_SET_ROW_ID
import com.visualeap.aliforreddit.data.network.auth.AuthService
import com.visualeap.aliforreddit.data.repository.token.TokenRemoteSource
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import dagger.Reusable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named
import kotlin.IllegalArgumentException

@Reusable
class TokenRs @Inject constructor(
    private val authService: AuthService,
    @Named("redirectUrl") private val redirectUrl: String,
    @Named("basicAuth") private val basicAuth: String
) : TokenRemoteSource {
    override fun refreshUserToken(refreshToken: String): Single<UserToken> {
        return authService.refreshUserToken(REFRESH_TOKEN_GRANT_TYPE, refreshToken)
            .map { it.map(refreshToken) }
    }

    override fun getUserlessToken(deviceId: String): Single<UserlessToken> {
        return authService.getUserlessToken(USERLESS_TOKEN_GRANT_TYPE, deviceId, basicAuth)
            .map { UserlessToken(NOT_SET_ROW_ID, it.accessToken, it.type, deviceId) }
    }

    override fun getUserToken(code: String): Single<UserToken> {
        return authService.getUserToken(USER_TOKEN_GRANT_TYPE, code, redirectUrl, basicAuth)
            .map { it.map() }
    }

    private fun TokenResponse.map(
        refreshToken: String = this.refreshToken
            ?: throw IllegalArgumentException("Refresh token cannot be null")
    ): UserToken =
        UserToken(NOT_SET_ROW_ID, accessToken, type, refreshToken)

    companion object {
        private const val USER_TOKEN_GRANT_TYPE = "authorization_code"
        private const val REFRESH_TOKEN_GRANT_TYPE = "refresh_token"
        private const val USERLESS_TOKEN_GRANT_TYPE =
            "https://oauth.reddit.com/grants/installed_client"
    }
}