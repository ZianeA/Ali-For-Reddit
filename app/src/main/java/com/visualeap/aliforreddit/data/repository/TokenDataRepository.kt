package com.visualeap.aliforreddit.data.repository

import com.visualeap.aliforreddit.data.network.AuthService
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TokenDataRepository @Inject constructor(
    private val authService: AuthService,
    @Named("redirectUrl") private val redirectUrl: String,
    @Named("basicAuth") private val basicAuth: String
) :
    TokenRepository {

    override fun getCurrentToken(): Maybe<Token> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserToken(code: String): Single<UserToken> {
        return authService.getUserToken(USER_TOKEN_GRANT_TYPE, code, redirectUrl, basicAuth)
    }

    override fun getUserLessToken(deviceId: String): Single<UserlessToken> {
        return authService.getUserLessToken(USERLESS_TOKEN_GRANT_TYPE, deviceId, basicAuth)
    }

    override fun refreshUserToken(refreshToken: String): Single<UserToken> {
        return authService.refreshUserToken(REFRESH_TOKEN_GRANT_TYPE, refreshToken)
    }

    override fun refreshUserLessToken(deviceId: String): Single<UserlessToken> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val USER_TOKEN_GRANT_TYPE = "authorization_code"
        private const val REFRESH_TOKEN_GRANT_TYPE = "refresh_token"
        private const val USERLESS_TOKEN_GRANT_TYPE =
            "https://oauth.reddit.com/grants/installed_client"
    }
}