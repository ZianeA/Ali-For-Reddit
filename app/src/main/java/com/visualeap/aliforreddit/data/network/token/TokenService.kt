package com.visualeap.aliforreddit.data.network.token

import com.visualeap.aliforreddit.data.network.AuthService
import com.visualeap.aliforreddit.data.repository.token.TokenRemoteSource
import dagger.Reusable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

@Reusable
class TokenService @Inject constructor(
    private val authService: AuthService,
    @Named("redirectUrl") private val redirectUrl: String,
    @Named("basicAuth") private val basicAuth: String
) : TokenRemoteSource {
    override fun refreshUserToken(refreshToken: String): Single<TokenResponse> {
        return authService.refreshUserToken(REFRESH_TOKEN_GRANT_TYPE, refreshToken)
    }

    override fun getUserlessToken(deviceId: String): Single<TokenResponse> {
        return authService.getUserlessToken(USERLESS_TOKEN_GRANT_TYPE, deviceId, basicAuth)
    }

    override fun getUserToken(code: String): Single<TokenResponse> {
        return authService.getUserToken(USER_TOKEN_GRANT_TYPE, code, redirectUrl, basicAuth)
    }

    companion object {
        private const val USER_TOKEN_GRANT_TYPE = "authorization_code"
        private const val REFRESH_TOKEN_GRANT_TYPE = "refresh_token"
        private const val USERLESS_TOKEN_GRANT_TYPE =
            "https://oauth.reddit.com/grants/installed_client"
    }
}